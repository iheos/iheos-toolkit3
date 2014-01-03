package gov.nist.toolkit.soapClientAPI;

import org.apache.axiom.soap.SOAPEnvelope;

/**
 * It is interface for a generic soap transaction.
 * 
 * Each request is configured through a RequestConfig which is a
 * java.util.Properties object.
 */

public interface SoapClient {

	/**
	 * Execute a blocking soap call.
	 * 
	 * @param conf
	 *            : an object representing the options for sending the request.
	 * @param envelope
	 *            : an axiom representation of the soap envelope. Headers
	 *            present present in the envelope will be sent as such.
	 * @return a representation of the response in the axiom object model.
	 * @throws SoapClientFault
	 *             : general exception if sth went wrong.
	 */
	public SOAPEnvelope runBlockingRequest(RequestConfig conf, SOAPEnvelope envelope) throws SoapClientFault;

}
