package gov.nist.toolkit.axis2SoapClient.validation;

import gov.nist.toolkit.soapClientAPI.RequestConfig;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates the parameters passed as properties.
 * Generates a strategy object that will be use by the axis2 client
 * @author gerardin
 *
 *
 *TODO should be split in 2. RequestParamValidator and ConfigValidator.
 */

public class RequestValidator {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private ConfigurationContext axisEngine;
	
	public RequestValidator(ConfigurationContext configurationContext) {
		axisEngine = configurationContext;
	}

	
	public RequestParams validate(RequestConfig conf) throws AxisFault {
		RequestParams params = new RequestParams();
		
		//TODO We could check we have only valid parameters, their numbers, etc...
		
		String transport = conf.getProperty("MODE");
		if (transport.compareToIgnoreCase("async") == 0){
			checkModuleEngaged("addressing");
			params.async = true;
			
			String replyTo = conf.getProperty("REPLY_TO");
			checkValidReplyTo();
			params.replyTo = new EndpointReference(replyTo);
		}
		else{
			checkNoReplyTo(conf);
		}
		
		
		params.MTOM = checkExistenceAndValue(conf, "MTOM", "true");
		
		
		String action = conf.getProperty("ACTION");
		params.action = action;
		
		String to = conf.getProperty("TO");
		params.to = new EndpointReference(to);
		
		if(checkExistenceAndValue(conf, "REQUEST_SPECIFIC_SSL_CREDENTIAL", "true")){

			try {
				String protocol = new URL(params.to.getAddress()).getProtocol();
				if(! protocol.equals("https")){
					throw new AxisFault(params.to.getAddress() + " is invalid. The protocol should have been https");
				}
			} catch (MalformedURLException e) {
				//should never happen
			}

			params.requestSpecificSSLCredential = true;
			params.keystorePath = conf.getProperty("KEYSTORE_PATH");
			params.keystorePass = conf.getProperty("KEYSTORE_PASS");
			params.truststorePath = conf.getProperty("TRUSTSTORE_PATH");
			params.truststorePass = conf.getProperty("TRUSTSTORE_PASS");
		}
		
		return params;
		
	}

	private void checkNoReplyTo(RequestConfig conf) throws AxisFault {
		String replyTo = conf.getProperty("REPLY_TO");
		if(replyTo != null){
			throw new AxisFault("not an async request. Should not contain a 'reply to' parameter");
		}
	}
	
	private boolean checkExistenceAndValue(RequestConfig conf, String property, String value){
		String v = conf.getProperty(property);
		if(v!= null && v.compareToIgnoreCase("true") == 0){
			return true;
		}
		return false;
	}


	//TODO we can check that the transport listener is configured properly in axis
	private void checkValidReplyTo() {
	}


	/*
	 * Check if a specific module is deployed
	 */
	private void checkModuleEngaged(String string) throws AxisFault {
		
	}

}
