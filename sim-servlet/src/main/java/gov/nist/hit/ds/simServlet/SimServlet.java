package gov.nist.hit.ds.simServlet;

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.dsSims.msgModels.RegistryResponse;
import gov.nist.hit.ds.dsSims.msgs.RegistryResponseGenerator;
import gov.nist.hit.ds.eventLog.Fault;
import gov.nist.hit.ds.httpSoap.parsers.HttpSoapParser;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder;
import gov.nist.hit.ds.simSupport.simulator.SimHandle;
import gov.nist.hit.ds.simSupport.simulator.SimSystemConfig;
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner;
import gov.nist.hit.ds.simSupport.utilities.SimSupport;
import gov.nist.hit.ds.simSupport.utilities.SimUtils;
import gov.nist.hit.ds.soapSupport.FaultCode;
import gov.nist.hit.ds.soapSupport.core.*;
import gov.nist.hit.ds.utilities.html.HttpMessageContent;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Servlet to service simulator input transactions.
 * @author bill
 *
 */
public class SimServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static Logger logger = Logger.getLogger(SimServlet.class);
    ServletConfig config;
    String simRepositoryName = "Sim";
    ActorTransactionTypeFactory factory;
    String errorSimId = "Errors";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.config = config;
        init();
    }

    public void init() {
        logger.info("SimServlet initializing...");
        BasicConfigurator.configure();

        SimSupport.initialize();
        factory = new ActorTransactionTypeFactory();
        factory.clear();
        factory.loadFromResource("actorTransactions.xml");
        SimSystemConfig.setRepoName(simRepositoryName);
        logger.info("SimServlet initialized");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        Endpoint endpoint = new Endpoint("http://host:port" + request.getRequestURI());
        logger.debug("Post to " + endpoint);
        List<String> options = parseOptionsFromURI(request.getRequestURI());
        String header = headersAsString(request);
        byte[] body;
        try {
            body = Io.getBytesFromInputStream(request.getInputStream());
        } catch (Exception e) {
            logger.error("Cannot read input message body: " + e.getMessage());
            return;
        }

        logger.debug("Running transaction");
        SimHandle simHandle = runPost(parseSimIdFromURI(request.getRequestURI()), header, body, options, response);
        response.setContentType("application/soap+xml");
        try {
            response.getWriter().print(simHandle.getEvent().getInOut().getRespBody());
        } catch (IOException e) {
                // give up - cannot handle this
        }
    }

    protected SimHandle runPost(SimId simId, String header, byte[] body, List<String> options, HttpServletResponse response) {
        SimHandle simHandle = runPost2(simId, new HttpMessageContent(header, body), options, response);
        Fault fault = simHandle.getEvent().getFault();
        if (fault == null) {
            OMElement responseEle = new RegistryResponseGenerator(new RegistryResponse()).toXml();
            String responseBody = new SoapResponseGenerator(simHandle.getSoapEnvironment(), responseEle).getEnvelopeAsString();
            simHandle.getEvent().getInOut().setRespBody(responseBody.getBytes());
        } else {
            logger.debug("Sending Fault");
            SoapEnvironment soapEnvironment = simHandle.getSoapEnvironment();
            OMElement faultEle = new SoapFaultGenerator(soapEnvironment, fault).getXML();
            String responseBody = new SoapResponseGenerator(simHandle.getSoapEnvironment(), faultEle).getEnvelopeAsString();
            simHandle.getEvent().getInOut().setRespBody(responseBody.getBytes());
        }
        SimUtils.close(simHandle);
        return simHandle;
    }

    // Surface errors
    //    invalid endpoint
    //    sim does not exist
    protected SimHandle runPost2(SimId simId, HttpMessageContent content, List<String> options, HttpServletResponse response) {
        byte[] soapEnvelopeBytes = new HttpSoapParser(content).getSoapEnvelope();
        SoapHeader soapHeader = new SoapHeaderParser(new String(soapEnvelopeBytes)).parse();
        String to = soapHeader.getTo();
        SoapEnvironment soapEnvironment = buildSoapEnvironment(soapHeader, response);
        if (to == null || to.equals(""))
            return sendFault(simId, "No To endpoint found in SOAP header", FaultCode.Sender, soapEnvironment, content);
        EndpointBuilder endpointBuilder = new EndpointBuilder().parse(to);
        SimId toSimId = endpointBuilder.getSimId();
        if (toSimId == null)
            return sendFault(new SimId(errorSimId), "Could not parse To endpoint found in SOAP header. Endpoint is [" + to + "]", FaultCode.Sender, soapEnvironment, content);
        if (!toSimId.getId().equals(simId.getId())) {
            // SimId mismatch between URI and SOAP Header To field
            String msg = "SimId mismatch: URI has " + simId.getId() + " and To Header has " + toSimId.getId();
            logger.debug(msg);
            // This is going into a special simulator log for collecting addressing errors
            return sendFault(new SimId(errorSimId), msg, FaultCode.Sender, soapEnvironment, content);
        }
        SimHandle simHandle;
        simHandle = SimUtils.open(simId);
        if (simHandle == null) {
            // Sim does not exist
            return sendFault(new SimId(errorSimId), "Simulator with ID [" + simId.getId() + "] does not exist.", FaultCode.Sender, soapEnvironment, content);
        }
        simHandle.setSoapEnvironment(soapEnvironment);
        simHandle.setEndpointBuilder(endpointBuilder);
        TransactionType transactionType = factory.getTransactionTypeFromRequestAction(soapHeader.getAction());
        if (transactionType == null) {
            simHandle.getEvent().setFault(new Fault("Unknown wsa:Action [" + soapHeader.getAction() + "]", FaultCode.Sender.toString(), "Unknown", ""));
            logRequest(simHandle, content);
            return simHandle;
        }
        simHandle.setOptions(options);
        simHandle.setTransactionType(transactionType);
        logRequest(simHandle, content);

        if (simHandle.hasOption("fault")) {
            simHandle.getEvent().setFault(new Fault("Forced Fault", FaultCode.Sender.toString(), "Unknown", ""));
            logRequest(simHandle, content);
            return simHandle;
        }

        runTransaction(simHandle);
        return simHandle;
    }

    SimHandle sendFault(SimId simId, String faultMsg, FaultCode code, SoapEnvironment soapEnvironment, HttpMessageContent content) {
        SimHandle simHandle = SimUtils.create(simId);
        simHandle.setSoapEnvironment(soapEnvironment);
        simHandle.getEvent().setFault(new Fault(faultMsg, code.toString(), "Unknown", ""));
        logRequest(simHandle, content);
        return simHandle;
    }

    void logRequest(SimHandle simHandle, HttpMessageContent content) {
        simHandle.getEvent().getInOut().setReqHdr(content.getHeader());
        simHandle.getEvent().getInOut().setReqBody(content.getBody());
    }

    private void runTransaction(SimHandle simHandle) {
        try {
           new TransactionRunner(simHandle).prun();
        } catch (Exception e) {
            logger.debug("runTransaction caught exception");
            simHandle.getEvent().setFault(new Fault(ExceptionUtil.exception_details(e), FaultCode.Receiver.toString(), "Unknown",
                    ""));
        } catch (Throwable e) {
            logger.debug("runTransaction caught throwable");
            simHandle.getEvent().setFault(new Fault(ExceptionUtil.exception_details(e), FaultCode.Receiver.toString(), "Unknown",
               ""     ));
        }
    }

    // xdstools3/sim/simid/option1/option2
    List<String> parseOptionsFromURI(String uri) {
        String[] parts = uri.split("/");
        return Arrays.asList(parts).subList(3, parts.length-1);
    }

    SimId parseSimIdFromURI(String uri) {
        String[] parts = uri.split("/");
        if (parts.length < 3) return new SimId(errorSimId);
        return new SimId(parts[2]);
    }


    SoapEnvironment buildSoapEnvironment(SoapHeader soapHeader, HttpServletResponse response) {
        SoapEnvironment soapEnvironment = new SoapEnvironment();
        soapEnvironment.setRequestAction(soapHeader.getAction());
        TransactionType transactionType = factory.getTransactionTypeFromRequestAction(soapEnvironment.getRequestAction());
        if (transactionType == null) {
            soapEnvironment.setResponseAction(null);
        } else {
            soapEnvironment.setResponseAction(transactionType.responseAction);
        }
        soapEnvironment.setMessageId(soapHeader.getMessageId());
        soapEnvironment.setTo(soapHeader.getTo());
        soapEnvironment.setResponse(response);
        return soapEnvironment;
    }

    String headersAsString(HttpServletRequest request) {
        StringBuilder buf = new StringBuilder();

        Enumeration e = request.getHeaderNames();
        while (e.hasMoreElements()) {
            String hdrName = (String) e.nextElement();
            StringBuilder line = new StringBuilder();
            line.append(hdrName).append(": ");
            Enumeration e1 = request.getHeaders(hdrName);
            boolean first = true;
            while(e1.hasMoreElements()) {
                String value = (String) e1.nextElement();
                if (!first) line.append("; ");
                line.append(value);
                first = false;
            }
            if (buf.length() != 0) buf.append("\r\n");
            buf.append(line);
        }
        return buf.toString();
    }

//    // TODO - add support for FaultTo
//    void sendSoapFault(SoapEnvironment soapEnv, FaultCode faultCode, String reason) {
//        Event event = (Event) soapEnv.getEvent();
//        reason = reason.replace('<', '(').replace('>', ')');
//        sendSoapFault(
//                soapEnv,
//                new SoapFaultException(
//                        event,      // No error recorder established to allow capture to logs
//                        faultCode,
//                        new ErrorContext(reason)));
//        try {
//            if (event != null)
//                event.getFault().add(getFaultSent());
//        } catch (RepositoryException e) {
//            logger.error(e);
//        }
//    }
//
//    void sendSoapFault(SoapEnvironment soapEnv, SoapFaultException sfe) {
//
//        try {
//            faultSent = new SoapFault(soapEnv, sfe).send();
//        }  catch (Exception ex) {
//            logger.error("Error sending SOAPFault", ex);
//        }
//    }


//    void returnEventResponse(HttpEnvironment httpEnv,
//                             SoapEnvironment soapEnv) {
//        try {
//            Event event = (Event) httpEnv.getEventLog();
//            String responseBodyString;
//            try {
//                responseBodyString = new String(event.getInOutMessages().getRespBody());
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//                responseBodyString = "<None/>";
//            }
//            SoapResponseGenerator soapGen = new SoapResponseGenerator(soapEnv, Parse.parse_xml_string(responseBodyString));
//            String responseString = soapGen.send();
////			event.getInOutMessages().putResponse(responseString);
//        } catch (Exception e) {
//            logger.error("Error generating response: \n" + ExceptionUtil.exception_details(e));
////			sendSoapFault(
////					soapEnv,
////					FaultCode.Receiver,
////					"Error generating response: \n" + ExceptionUtil.exception_details(e));
////		}
//        }
//    }

//	public boolean handleSimulatorInputTransaction(HttpServletRequest request,
//			SoapEnvironment soapEnv, SimEndpoint simEndpoint, Endpoint endpoint, Event event) {
//
//		soapEnv.getHttpEnvironment().setEventLog(event);
//		soapEnv.setEndpoint(endpoint);
//		request.setAttribute("Event", event); // SimServletFilter needs this to log response as it goes out on the wire
//
////		logger.error("Debug time");
////		sendSoapFault(soapEnv, FaultCode.Receiver, "DEBUG DEBUG DEBUG: " + "Hello World!");
////		if (1 == 1)
////			return true;
//
//		// Make the input message available to the SimChain
//		try {
//			logRequest(request, event, simEndpoint.getActor(), simEndpoint.getTransaction());
//		} catch (Exception e) {
//			logger.error("Internal error logging request message", e);
//			sendSoapFault(soapEnv, FaultCode.Receiver, "Internal error logging request message: " + e.getMessage());
//			return true;
//		}
//
//
//		// Find the correct SimChain and run it.  The SimChain selected by a combination of
//		// Actor and Transaction codes.  These codes came from the endpoint used to contact this
//		// Servlet.
//		try {
//			new ActorSimFactory().run(simEndpoint.getActor(), simEndpoint.getTransaction(), simEndpoint.getSimId().getId(), soapEnv, event);
//		} catch (SoapFaultException sfe) {
//			logger.info("SOAPFault: " + sfe.getMessage());
//			sendSoapFault(soapEnv, sfe);
//			return true;
//		} catch (Exception e) {
//			logger.error("SOAPFault: " + ExceptionUtil.exception_details(e));
//			sendSoapFault(
//					soapEnv,
//					new SoapFaultException(
//							null,
//							FaultCode.Receiver,
//							"Problem with SimChain definition for Actor (" + simEndpoint.getActor()
//							+ ") and Transaction (" + simEndpoint.getTransaction() + ") " +
//							ExceptionUtil.exception_details(e)
//							)
//					);
//			return true;
//		} catch (Throwable e) {
//			logger.error("SOAPFault: " + ExceptionUtil.exception_details(e));
//			sendSoapFault(
//					soapEnv,
//					new SoapFaultException(
//							null,
//							FaultCode.Receiver,
//							"Problem with SimChain definition for Actor (" + simEndpoint.getActor()
//							+ ") and Transaction (" + simEndpoint.getTransaction() + ") " +
//							ExceptionUtil.exception_details(e)
//							)
//					);
//			return true;
//		}
//		return false;
//	}

//	private Event buildEvent(SoapEnvironment soapEnv, SimEndpoint simEndpoint) {
//		Event event = null;
//		try {
//			event = new EventFactory().buildEvent(simEndpoint.getSimId(), ActorTypeFactory.find(simEndpoint.getActor()).getShortName(), simEndpoint.getTransaction());
//			//			SimDb db = new SimDb(simEndpoint.getSimId());
//			//			SimId simId = simEndpoint.getSimId();
//			//			RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
//			//			Repository repos = fact.createNamedRepository(
//			//					"Event_Repository",
//			//					"Event Repository",
//			//					new SimpleType("simEventRepository"),               // repository type
//			//					ActorType.findActor(simEndpoint.getActor()).getShortName() + "-" + simId    // repository name
//			//					);
//			//			Asset eventAsset = repos.createAsset(
//			//					db.nowAsFilenameBase(),
//			//					simEndpoint.getTransaction() + " Event",
//			//					new SimpleType("simEvent"));
//			//			event = new Event(eventAsset);
//		} catch (Exception e) {
//			logger.error("Internal error initializing simulator environment", e);
//			sendSoapFault(soapEnv, FaultCode.Receiver, "Internal error initializing simulator environment: " + e.getMessage());
//		}
//		return event;
//	}

//	/**
//	 * Log the incoming request in the SimDb.
//	 * @param request - HttpRequest
//	 * @param db - SimDb section created for this event
//	 * @param actor - actor parsed from endpoint
//	 * @param transaction - transaction parsed from endpoint
//	 * @return reference to this event in SimDb
//	 * @throws FileNotFoundException
//	 * @throws IOException
//	 * @throws HttpHeaderParseException
//	 * @throws ParseException
//	 * @throws RepositoryException
//	 */
//	void logRequest(HttpServletRequest request, Event event, String actor, String transaction)
//			throws FileNotFoundException, IOException, HttpHeaderParseException, ParseException, RepositoryException {
//		StringBuffer buf = new StringBuffer();
//		Map<String, String> headers = new HashMap<String, String>();
//
//		buf.append(request.getMethod() + " " + request.getRequestURI() + " " + request.getProtocol() + "\r\n");
//		for (@SuppressWarnings("unchecked")
//		Enumeration<String> en=request.getHeaderNames(); en.hasMoreElements(); ) {
//			String name = en.nextElement();
//			String value = request.getHeader(name);
//			if (name.equals("Transfer-Encoding"))
//				continue;  // log will not include transfer encoding so don't include this
//			headers.put(name.toLowerCase(), value);
//			buf.append(name).append(": ").append(value).append("\r\n");
//		}
//
//		buf.append("\r\n");
//
//		// Log the request header and body in the SimDb event
//		if (event!=null) {
//			event.getInOutMessages().putRequestHeader(buf.toString());
//			event.getInOutMessages().putRequestBody(Io.getBytesFromInputStream(request.getInputStream()));
//		}
//	}


//	/**
//	 * This is only used by unit tests to determine if a soap fault was returned.
//	 * It should never be used in production since the servlet is multi-threaded and
//	 * this feature is not.
//	 * @return
//	 */
//	public String getFaultSent() { return faultSent; }


}
