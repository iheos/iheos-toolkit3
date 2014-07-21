package gov.nist.hit.ds.simSupport.simExample

import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.annotations.SimComponentInject
import gov.nist.hit.ds.simSupport.annotations.SimComponentOutput
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.soapSupport.SoapFaultException
import org.apache.axiom.om.OMElement

/**
 * Parse SOAP message and make parts available through the SoapMessage object.
 * @author bmajur
 *
 */
public class SoapMessageParser extends ValComponentBase {
    OMElement xml;
    XmlMessage xmlMessage;
    OMElement header = null;
    OMElement body = null;
    OMElement root;
    int partCount;

    @SimComponentInject
    public SoapMessageParser setXML(XmlMessage xmlMessage) {
        this.xmlMessage = xmlMessage;
        return this;
    }

    @SimComponentOutput
    public SoapMessage getSoapMessage() {
        return new SoapMessage().
                setHeader(header).
                setBody(body).
                setRoot(root).
                setPartCount(partCount);
    }

    @Override
    public void run() throws SoapFaultException, RepositoryException {
        parse(xmlMessage.getXml());

        runValidationEngine();
    }

    public void parse(OMElement xml) {
        root = xml;
        if (root != null) {
            Iterator<?> partsIterator = root.getChildElements();

            partCount = 0;
            if (partsIterator.hasNext()) {
                partCount++;
                header = (OMElement) partsIterator.next();
                if (partsIterator.hasNext()) {
                    partCount++;
                    body = (OMElement) partsIterator.next();
                    partCount += countParts(partsIterator);
                }
            }
        }
    }

    int countParts(Iterator<?> it) {
        int cnt = 0;
        while (it.hasNext()) {
            it.next();
            cnt++;
        }
        return cnt;
    }

    @Override
    public boolean showOutputInLogs() {
        return false;
    }

}
