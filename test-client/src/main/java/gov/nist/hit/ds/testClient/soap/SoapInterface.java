package gov.nist.hit.ds.testClient.soap;

import gov.nist.hit.ds.xdsExceptions.EnvironmentNotSelectedException;
import gov.nist.hit.ds.xdsExceptions.LoadKeystoreException;
import gov.nist.hit.ds.xdsExceptions.XdsFormatException;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

public interface SoapInterface {

	public abstract OMElement getInHeader();

	public abstract OMElement getOutHeader();

	public abstract void addHeader(OMElement header);

	public abstract void addSecHeader(OMElement header);

	public abstract void clearHeaders();

	public abstract void setAsync(boolean async);

	/**
	 * Low level invocation of soap call.  The basic ServiceClient is inadequate to gain this level
	 * of control, OperationClient must be used.  Reference:
	 *    http://today.java.net/pub/a/today/2006/12/13/invoking-web-services-using-apache-axis2.html#working-with
	 * This does not yet handle async.
	 * @param bodyBytes
	 * @return
	 * @throws gov.nist.hit.ds.xdsExceptions.XdsException
	 * @throws org.apache.axis2.AxisFault
	 * @throws gov.nist.hit.ds.xdsExceptions.EnvironmentNotSelectedException
	 */
	public abstract void soapCallWithWSSEC() throws XdsInternalException,
			AxisFault, EnvironmentNotSelectedException, LoadKeystoreException;

	public abstract OMElement soapCall() throws LoadKeystoreException, XdsInternalException,
			AxisFault, XdsFormatException, EnvironmentNotSelectedException;

	public abstract OMElement getResult();

	public abstract OMElement soapCall(OMElement body, String endpoint,
									   boolean mtom, boolean addressing, boolean soap12, String action,
									   String expected_return_action) throws XdsInternalException,
			AxisFault, XdsFormatException, EnvironmentNotSelectedException, LoadKeystoreException;

	public abstract String getExpectedReturnAction();

	public abstract void setExpectedReturnAction(String expectedReturnAction);

	public abstract boolean isMtom();

	public abstract void setMtom(boolean mtom);

	public abstract boolean isAddressing();

	public abstract void setAddressing(boolean addressing);

	public abstract boolean isSoap12();

	public abstract void setSoap12(boolean soap12);

	public abstract boolean isAsync();

	public abstract void setUseSaml(boolean use);

}
