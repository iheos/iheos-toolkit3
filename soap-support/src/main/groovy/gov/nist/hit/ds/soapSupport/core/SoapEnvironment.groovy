package gov.nist.hit.ds.soapSupport.core

import javax.servlet.http.HttpServletResponse
/**
 * Keep track of SOAP/HTTP environment so response can be
 * properly formatted.
 * 
 * TODO:  response content type needs to be set
 * @author bill
 *
 */
public class SoapEnvironment {
	boolean multipart
	String requestAction
	String responseAction
	String messageId
    String to
//	Endpoint endpoint
    HttpServletResponse response
}
