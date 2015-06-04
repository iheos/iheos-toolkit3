package gov.nist.hit.ds.simServlet.servlet
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.dsSims.eb.msgs.RegistryResponseGenerator
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.httpSoap.parsers.HttpSoapParser
import gov.nist.hit.ds.simServlet.WrapMtom
import gov.nist.hit.ds.simServlet.rest.TransactionReportBuilder
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.config.TransactionSimConfigElement
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.simSupport.validationEngine.ValidatorWithError
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.core.*
import gov.nist.hit.ds.utilities.html.HttpMessageContent
import gov.nist.hit.ds.utilities.io.Io
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.axiom.om.OMElement
import org.apache.log4j.Logger

import javax.servlet.ServletConfig
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static groovyx.net.http.ContentType.XML
/**
 * Servlet to service simulator input transactions.
 * @author bill
 *
 */
// TODO: Send SIMPLE SOAP to PnR endpoint to test soap fault format
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
//        SimSystemConfig.setRepoName(simRepositoryName);
        logger.info("SimServlet initialized");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        EndpointValue endpoint = new EndpointValue("http://host:port" + request.getRequestURI());
        logger.info("\n\n\n===============================================================================\nPost received to ${endpoint}\n\n\n");
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
            simHandle = runPost(endpoint.simIdentifier(), header, body, options, response);
        } catch (Throwable t) {
            logger.fatal("runPost threw exception " + ExceptionUtil.exception_details(t));
            return;
        }
        try {
            String contentType = simHandle.getEvent().getInOut().getRespContentType();
            response.setContentType(contentType);
            request.setAttribute("contentType", contentType);
            PrintWriter out = response.getWriter();
            out.write(new String(simHandle.getEvent().getInOut().getRespBody()));
            out.flush();
        } catch (IOException e) {
            logger.error("Error writing response - " + ExceptionUtil.exception_details(e));
        }

        callback(simHandle, endpoint)
        logger.info("\n===============================================================================");
    }

    def callback(SimHandle simHandle, EndpointValue endpoint) {
        TransactionSimConfigElement transactionElement = simHandle.actorSimConfig.get(endpoint.trans)
        logger.debug("transactionSimConfigElement is ${transactionElement}" )
        assert transactionElement
        String callbackURI = transactionElement.getCallBack()
        if (callbackURI) {
            assert callbackURI.startsWith("http")
            String payload = new TransactionReportBuilder().build(simHandle);
            logger.info("Callback to ${callbackURI} with payload ${payload}");
            def http = new HTTPBuilder( callbackURI )
            http.request(Method.POST, XML) { request ->
                requestContentType = XML
                body = payload

                response.success = { resp ->
                    logger.info "POST Success: ${resp.statusLine}"
                }
                response.failure = { resp ->
                    logger.error("POST Failure: ${resp.statusLine}")
                }
            }
        }
    }

    SimHandle runPost(SimIdentifier simIdent, String header, byte[] body, List<String> options, HttpServletResponse response) {
        SimHandle simHandle;
        simHandle = runPost2(simIdent, new HttpMessageContent(header, body), options, response);
        Fault fault = simHandle.getEvent().getFault();
        if (fault == null) {
            logger.debug("No Fault - sending response");
            simHandle.getEvent().flushValidators();
            OMElement responseEle = new RegistryResponseGenerator(simHandle).toXml();
            String responseBody = new SoapResponseGenerator(simHandle.getSoapEnvironment(), responseEle).getEnvelopeAsString();
            buildSimpleOrMtom(simHandle, responseBody);
        } else {
            logger.debug("Sending Fault");
            SoapEnvironment soapEnvironment = simHandle.getSoapEnvironment();
            OMElement faultEle = new SoapFaultGenerator(soapEnvironment, fault).getXML();
            String responseBody = new SoapResponseGenerator(simHandle.getSoapEnvironment(), faultEle).getEnvelopeAsString();
            buildSimpleOrMtom(simHandle, responseBody);
        }
        SimUtils.close(simHandle);
        return simHandle;
    }

    // Surface errors
    //    invalid endpoint
    //    sim does not exist
    protected SimHandle runPost2(SimIdentifier simIdent, HttpMessageContent content, List<String> options, HttpServletResponse response) {
        byte[] soapEnvelopeBytes;
        SoapHeader soapHeader;
        String to;
        SoapEnvironment soapEnvironment;
        HttpSoapParser soapParser;

        soapParser = new HttpSoapParser(content);
        try {
            soapEnvelopeBytes = soapParser.getSoapEnvelope();
            soapHeader = new SoapHeaderParser(new String(soapEnvelopeBytes)).parse();
            to = soapHeader.getTo();
        } catch (Throwable t) {
            soapEnvironment = buildSoapEnvironment(response);
            return sendFault(simIdent, "Internal Error parsing SOAP Header\n" + ExceptionUtil.exception_details(t), FaultCode.Receiver, soapEnvironment, content, soapParser.isMultiPart());
        }
        soapEnvironment = buildSoapEnvironment(soapHeader, response);
        if (to == null || to.equals(""))
            return sendFault(simIdent, "No To endpoint found in SOAP header", FaultCode.Sender, soapEnvironment, content, soapParser.isMultiPart());
        EndpointBuilder endpointBuilder = new EndpointBuilder().parse(to);
        SimId toSimId = endpointBuilder.getSimId();
        if (toSimId == null)
            return sendFault(new SimIdentifier(SimUtils.defaultRepoName, errorSimId), "Could not parse To endpoint found in SOAP header. Endpoint is [" + to + "]", FaultCode.Sender, soapEnvironment, content, soapParser.isMultiPart());
        if (!toSimId.getId().equals(simIdent.simId.getId())) {
            // SimId mismatch between URI and SOAP Header To field
            String msg = "SimId mismatch: URI has " + simIdent.simId.getId() + " and To Header has " + toSimId.getId();
            logger.debug(msg);
            // This is going into a special simulator log for collecting addressing errors
            return sendFault(new SimIdentifier(SimUtils.defaultRepoName, errorSimId), msg, FaultCode.Sender, soapEnvironment, content, soapParser.isMultiPart());
        }

        SimHandle simHandle;


        // Endpoint says to return a specific error
        String errorToReturn = new SimHandle().getOptionFromMap("error", options);
        boolean hasErrorToReturn;
        if (errorToReturn != null && !errorToReturn.equals("")) {
            logger.info("Should return forced error - " + errorToReturn);
            simHandle = SimUtils.open(simIdent);
            hasErrorToReturn = true;
        } else {
            simHandle = SimUtils.open(simIdent);
            hasErrorToReturn = false;
        }

        if (simHandle == null) {
            if (options.contains("autocreate")) {
                logger.info("Auto Creating sim " + simId.getId());
                simHandle = autoCreateSim(endpointBuilder);
            } else {
                logger.debug("Sim " + simIdent.simId.getId() + " does not exist");
                // Sim does not exist
                return sendFault(new SimIdentifier(simIdent.repoName, errorSimId), "Simulator with ID " + simIdent.simId.getId() + " does not exist.", FaultCode.Sender, soapEnvironment, content, soapParser.isMultiPart());
            }
        }
        simHandle.setSoapEnvironment(soapEnvironment);
        simHandle.setRequestIsMultipart(soapParser.isMultiPart());
        simHandle.setEndpointBuilder(endpointBuilder);
//        TransactionType transactionType = factory.getTransactionTypeFromRequestAction(soapHeader.getAction());
        logger.debug("actorType is ${simHandle.actorSimConfig.actorType}")
        TransactionType transactionType = simHandle.actorSimConfig.actorType.getTransactionTypeFromRequestAction(soapHeader.getAction(), false)
        if (transactionType == null) {
            simHandle.getEvent().setFault(new Fault("Unknown wsa:Action [" + soapHeader.getAction() + "]", FaultCode.Sender.toString(), "Unknown", ""));
            logRequest(simHandle, content);
            return simHandle;
        }
        simHandle.setOptions(options);
        simHandle.setTransactionType(transactionType);
        logRequest(simHandle, content);

        if (simHandle.getOption("fault") != null) {
            simHandle.getEvent().setFault(new Fault("Forced Fault", FaultCode.Sender.toString(), "Unknown", ""));
            logRequest(simHandle, content);
            return simHandle;
        }

        if (simHandle.getRequestIsMultipart() != simHandle.getTransactionType().multiPart) {
            if (simHandle.getTransactionType().multiPart)
                return sendFault(simHandle, "This transaction [" + simHandle.getTransactionType().getName() + "] requires MTOM", FaultCode.Sender, soapEnvironment, content);
            else
                return sendFault(simHandle, "This transaction  [" + simHandle.getTransactionType().getName() + "] requires SIMPLE SOAP", FaultCode.Sender, soapEnvironment, content);
        }

        if (hasErrorToReturn) {
            logger.info("Forcing error");
            new ValidatorWithError().error(simHandle, errorToReturn, "message");
        } else {
            runTransaction(simHandle);
        }
        return simHandle;
    }

    void buildSimpleOrMtom(SimHandle simHandle, String responseBody) {
        String contentTypeHeader;
        if (simHandle.getRequestIsMultipart()) {
            logger.debug("Sending MTOM response");
            StringBuilder bodyBuffer = new StringBuilder();
            contentTypeHeader = new WrapMtom().wrap(responseBody, bodyBuffer);
            logger.debug("response content type is " + contentTypeHeader);
            simHandle.getEvent().getInOut().setRespBody(bodyBuffer.toString().getBytes());
        } else {
            logger.debug("Sending SIMPLE response");
            contentTypeHeader = "application/soap+xml";
            simHandle.getEvent().getInOut().setRespBody(responseBody.getBytes());
        }
        simHandle.getEvent().getInOut().setRespContentType(contentTypeHeader);
        simHandle.getEvent().getInOut().setRespHdr("content-type: " + contentTypeHeader);
    }

    static SimHandle sendFault(SimIdentifier simIdent, String faultMsg, FaultCode code, SoapEnvironment soapEnvironment, HttpMessageContent content, boolean multiPart) {
        SimHandle simHandle = SimUtils.create(simIdent);
        simHandle.setRequestIsMultipart(multiPart);
        return sendFault(simHandle, faultMsg, code, soapEnvironment, content);
    }

    static SimHandle sendFault(SimHandle simHandle, String faultMsg, FaultCode code, SoapEnvironment soapEnvironment, HttpMessageContent content) {
        simHandle.setSoapEnvironment(soapEnvironment);
        simHandle.getEvent().setFault(new Fault(faultMsg, code.toString(), "Unknown", ""));
        logRequest(simHandle, content);
        logger.debug("Fault built");
        return simHandle;
    }

    static void logRequest(SimHandle simHandle, HttpMessageContent content) {
        simHandle.getEvent().getInOut().setReqHdr(content.getHeader());
        simHandle.getEvent().getInOut().setReqBody(content.getBody());
    }

    static SimHandle autoCreateSim(EndpointBuilder builder) {
        return SimUtils.create(builder.getActorCode(), builder.simIdentifier);
    }

    static private void runTransaction(SimHandle simHandle) {
        try {
            def runner = new TransactionRunner(simHandle)
            runner.validateRequest()
            runner.acceptRequest();
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
    static List<String> parseOptionsFromURI(String uri) {
        String[] parts = uri.split("/");
        return Arrays.asList(parts).subList(6, parts.length);
    }

    // this is not the full uri, just the part following the port/
    static SimIdentifier parseSimIdentFromURI(String uri) {
        return new EndpointValue(uri).simIdentifier()
//        String[] parts = uri.split("/");
//        logger.debug("Parsing " + uri + " to get simId");
//        if (parts.length < 4) {
//            logger.info("Badly formed uri - cannot extract simId from " + uri);
//            return new SimId(errorSimId);
//        }
//        logger.debug("simId is " + parts[3]);
//        return new SimId(parts[3]);
    }

    static String parseUserFromURI(String uri) {
        new EndpointValue(uri).user
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
