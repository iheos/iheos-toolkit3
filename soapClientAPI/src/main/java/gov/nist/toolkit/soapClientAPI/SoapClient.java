package gov.nist.toolkit.soapClientAPI;

import org.apache.axiom.soap.SOAPEnvelope;

/**
 * This is an interface for a generic soap transaction.
 * 
 * Each request is configured through
 * 
 * Each method defined in this interface must have 2 parameters : 
 * <br> - a SOAPEnvelope (from the axiom library) : this contains the envelope of the message to be sent
 * (headers + body).
 * <br >- a RequestConfig which is a java.util.Properties object :
 * this object allows to configure some aspects of the request/exchange pattern.
 * 
 * Note : the RequestConfig can be used to tell the implementation what header
 * to generate. However, any header defined in the envelope should take
 * precedence over RequestConfig params.
 * For example, if a <ReplyTo> header is present in the envelope, the REPLY_TO param of the RequestConfig
 * should be ignored.
 * 
 * See RequestConfig.java for details on the available options. 
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
