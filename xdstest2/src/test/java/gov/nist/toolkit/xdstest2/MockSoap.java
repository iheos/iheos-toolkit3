package gov.nist.toolkit.xdstest2;

import gov.nist.hit.ds.xdsException.EnvironmentNotSelectedException;
import gov.nist.hit.ds.xdsException.LoadKeystoreException;
import gov.nist.hit.ds.xdsException.XdsFormatException;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.soapAPI.axis2.SoapInterface;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

/**
 * Created by bmajur on 3/6/14.
 */
public class MockSoap implements SoapInterface {
    @Override
    public OMElement getInHeader() {
        return null;
    }

    @Override
    public OMElement getOutHeader() {
        return null;
    }

    @Override
    public void addHeader(OMElement header) {

    }

    @Override
    public void addSecHeader(OMElement header) {

    }

    @Override
    public void clearHeaders() {

    }

    @Override
    public void setAsync(boolean async) {

    }

    @Override
    public void soapCallWithWSSEC() throws XdsInternalException, AxisFault, EnvironmentNotSelectedException, LoadKeystoreException {

    }

    @Override
    public OMElement soapCall() throws LoadKeystoreException, XdsInternalException, AxisFault, XdsFormatException,
            EnvironmentNotSelectedException {
        return null;
    }

    @Override
    public OMElement getResult() {
        return null;
    }

    @Override
    public OMElement soapCall(OMElement body, String endpoint, boolean mtom, boolean addressing, boolean soap12, String action, String expected_return_action) throws XdsInternalException, AxisFault, XdsFormatException, EnvironmentNotSelectedException, LoadKeystoreException {
        return null;
    }

    @Override
    public String getExpectedReturnAction() {
        return null;
    }

    @Override
    public void setExpectedReturnAction(String expectedReturnAction) {

    }

    @Override
    public boolean isMtom() {
        return false;
    }

    @Override
    public void setMtom(boolean mtom) {

    }

    @Override
    public boolean isAddressing() {
        return false;
    }

    @Override
    public void setAddressing(boolean addressing) {

    }

    @Override
    public boolean isSoap12() {
        return false;
    }

    @Override
    public void setSoap12(boolean soap12) {

    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void setUseSaml(boolean use) {

    }
}
