package gov.nist.hit.ds.axis2SoapClientFacade.impl;

import gov.nist.hit.ds.axis2SoapClientFacade.api.Axis2SoapClientFacade;
import gov.nist.hit.ds.axis2SoapClientFacade.api.RequestConfig;
import gov.nist.hit.ds.axis2SoapClientFacade.validation.RequestParams;
import gov.nist.hit.ds.axis2SoapClientFacade.validation.RequestValidator;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Axis2ClientFacadeOperationClientImpl implements Axis2SoapClientFacade {

	private ServiceClient sc;
	private OperationClient oc;
	private RequestValidator validator;
	
	private final AxisService ANONYMOUS_SERVICE = null;
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	public  Axis2ClientFacadeOperationClientImpl(ConfigurationContext configurationContext) throws AxisFault{
		sc = new ServiceClient(configurationContext, ANONYMOUS_SERVICE);
		oc = sc.createClient(ServiceClient.ANON_OUT_IN_OP);	
		validator = new RequestValidator(configurationContext);
	}
	
	public SOAPEnvelope runRequestSynchronously(RequestConfig conf, SOAPEnvelope envelope) throws AxisFault{
		//validate the properties
		RequestParams params = validator.validate(conf);
		
		//build the request for an operation client
		OperationClientRequestBuilder builder = new OperationClientRequestBuilder();
		MessageContext outMsgCtx = builder.buildRequest(params,envelope);
		oc.addMessageContext(outMsgCtx);
		
		oc.execute(true);

		//return the response envelope
		MessageContext inMsgtCtx = oc.getMessageContext("In");
		SOAPEnvelope response = inMsgtCtx.getEnvelope();
		
		sc.cleanupTransport();
		
		return response;
	}
}
