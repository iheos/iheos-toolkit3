package gov.nist.hit.ds.soapSupport.core

import javax.servlet.http.HttpServletResponse
/**
 * Keep track of SOAP/HTTP environment so response can be
 * properly formatted.
 * 
 * @author bill
 *
 */
public class SoapEnvironment {
	String requestAction
	String responseAction
	String messageId
    String to
    HttpServletResponse response
}
