package gov.nist.toolkit.axis2SoapClient.validation;

import org.apache.axis2.addressing.EndpointReference;

/**
 * This is a strategy object that we use to customize the SOAP client behavior.
 */

public class RequestParams {

	public EndpointReference to;
	public EndpointReference replyTo;
	public boolean async;
	public String action;
	public boolean MTOM;
	
	public boolean requestSpecificSSLCredential;
	public String keystorePath;
	public String keystorePass;
	public String truststorePath;
	public String truststorePass;
}
