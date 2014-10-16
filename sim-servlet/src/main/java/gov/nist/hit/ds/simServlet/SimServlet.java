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
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
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
//        BasicConfigurator.configure();

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
        logger.debug("Request options are " + options);
        String header = headersAsString(request);
        byte[] body;
        try {
            body = Io.getBytesFromInputStream(request.getInputStream());
        } catch (Exception e) {
            logger.error("Cannot read input message body: " + e.getMessage());
            return;
        }

        logger.debug("Running transaction");

        SimHandle simHandle;
        try {
            simHandle = runPost(parseSimIdFromURI(request.getRequestURI()), header, body, options, response);
        } catch (Throwable t) {
            logger.fatal("runPost threw exception " + ExceptionUtil.exception_details(t));
            return;
        }
        response.setContentType("application/soap+xml");
        try {
            OutputStream os = response.getOutputStream();
            os.write(simHandle.getEvent().getInOut().getRespBody());
            os.flush();
        } catch (IOException e) {
                // give up - cannot simHandle this
        }
    }

    protected SimHandle runPost(SimId simId, String header, byte[] body, List<String> options, HttpServletResponse response) {
        SimHandle simHandle;
        simHandle = runPost2(simId, new HttpMessageContent(header, body), options, response);
        Fault fault = simHandle.getEvent().getFault();
        if (fault == null) {
            logger.debug("No Fault - sending response");
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
        byte[] soapEnvelopeBytes;
        SoapHeader soapHeader;
        String to;
        SoapEnvironment soapEnvironment;

        try {
            soapEnvelopeBytes = new HttpSoapParser(content).getSoapEnvelope();
            soapHeader = new SoapHeaderParser(new String(soapEnvelopeBytes)).parse();
            to = soapHeader.getTo();
        } catch (Throwable t) {
            soapEnvironment = buildSoapEnvironment(response);
            return sendFault(simId, "Internal Error parsing SOAP Header\n" + ExceptionUtil.exception_details(t), FaultCode.Receiver, soapEnvironment, content);
        }
        soapEnvironment = buildSoapEnvironment(soapHeader, response);
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
            logger.debug("Sim " + simId.getId() + " does not exist");
            // Sim does not exist
            return sendFault(new SimId(errorSimId), "Simulator with ID " + simId.getId() + " does not exist.", FaultCode.Sender, soapEnvironment, content);
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
        return sendFault(simHandle, faultMsg, code, soapEnvironment, content);
    }

    SimHandle sendFault(SimHandle simHandle, String faultMsg, FaultCode code, SoapEnvironment soapEnvironment, HttpMessageContent content) {
        simHandle.setSoapEnvironment(soapEnvironment);
        simHandle.getEvent().setFault(new Fault(faultMsg, code.toString(), "Unknown", ""));
        logRequest(simHandle, content);
        logger.debug("Fault built");
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
        return Arrays.asList(parts).subList(5, parts.length-1);
    }

    SimId parseSimIdFromURI(String uri) {
        String[] parts = uri.split("/");
        logger.debug("Parsing " + uri + " to get simId");
        if (parts.length < 4) {
            logger.info("Badly formed uri - cannot extract simId from " + uri);
            return new SimId(errorSimId);
        }
        logger.debug("simId is " + parts[3]);
        return new SimId(parts[3]);
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

    // for error handling - SOAP header not availble.  Build SOAP environment from defaults
    SoapEnvironment buildSoapEnvironment(HttpServletResponse response) {
        SoapEnvironment soapEnvironment = new SoapEnvironment();
        soapEnvironment.setRequestAction("Unknown");
        TransactionType transactionType = factory.getTransactionTypeFromRequestAction(soapEnvironment.getRequestAction());
        if (transactionType == null) {
            soapEnvironment.setResponseAction(null);
        } else {
            soapEnvironment.setResponseAction(transactionType.responseAction);
        }
        soapEnvironment.setMessageId("Unknown");
        soapEnvironment.setTo("Unknown");
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

}
