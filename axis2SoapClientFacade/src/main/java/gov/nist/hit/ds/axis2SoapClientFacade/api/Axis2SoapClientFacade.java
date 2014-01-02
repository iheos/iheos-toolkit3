package gov.nist.hit.ds.axis2SoapClientFacade.api;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.AxisFault;

/**
 * The intent of this class is to provide a higher level interface to the axis2
 * client api. It is half-way between a higher facade modeling a completely
 * generic soap transaction and the low-level axis2 client api. In the future,
 * this interface will probably implements such a generic API.
 * 
 * Each request is configured through a RequestConfig which is a
 * java.util.Properties object.
 */

public interface Axis2SoapClientFacade {

	/**
	 * Execute a synchronous soap call(axis2 will start a new thread to handle the
	 * request and wait for it to return). 
	 * @param conf : an object representing the options for sending the request.
	 * @param envelope : an axis2 representation of the soap envelope.
	 * @return a representation of the response in the axis2 object model
	 * @throws AxisFault : axis2 exception if sth went wrong.
	 */
	public SOAPEnvelope runRequestSynchronously(RequestConfig conf, SOAPEnvelope envelope) throws AxisFault;
}
