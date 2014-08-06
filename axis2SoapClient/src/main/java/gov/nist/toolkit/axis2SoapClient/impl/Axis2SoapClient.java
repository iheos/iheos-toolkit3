package gov.nist.toolkit.axis2SoapClient.impl;

import gov.nist.toolkit.axis2SoapClient.validation.RequestParams;
import gov.nist.toolkit.axis2SoapClient.validation.RequestValidator;
import gov.nist.toolkit.soapClientAPI.RequestConfig;
import gov.nist.toolkit.soapClientAPI.SoapClient;
import gov.nist.toolkit.soapClientAPI.SoapClientFault;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Axis2SoapClient implements SoapClient {

	private ServiceClient sc;
	private OperationClient oc;
	private RequestValidator validator;

	private final AxisService ANONYMOUS_SERVICE = null;

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public Axis2SoapClient(ConfigurationContext configurationContext) throws AxisFault {
		sc = new ServiceClient(configurationContext, ANONYMOUS_SERVICE);
		oc = sc.createClient(ServiceClient.ANON_OUT_IN_OP);
		validator = new RequestValidator(configurationContext);
	}

	public SOAPEnvelope runBlockingRequest(RequestConfig conf, SOAPEnvelope envelope) throws SoapClientFault {

		try {
			// validate the properties
			RequestParams params = validator.validate(conf);

			// build the request for an operation client
			OperationClientRequestBuilder builder = new OperationClientRequestBuilder();
			MessageContext outMsgCtx = builder.buildRequest(params, envelope);
			oc.addMessageContext(outMsgCtx);

			oc.execute(true);

			// return the response envelope
			MessageContext inMsgtCtx = oc.getMessageContext("In");
			inMsgtCtx.getAxisMessage().getSoapHeaders();

			SOAPEnvelope response = inMsgtCtx.getEnvelope();

			// TODO check seems to be necessary to process MTOM responses
			response.build();

			sc.cleanupTransport();
			return response;
			 
		} catch (AxisFault e) {
			throw new SoapClientFault(e);
		}

	}
}
