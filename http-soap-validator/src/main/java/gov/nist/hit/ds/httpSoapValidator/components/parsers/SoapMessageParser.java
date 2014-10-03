package gov.nist.hit.ds.httpSoapValidator.components.parsers;

import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.utilities.xml.Parse;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;
import org.apache.axiom.om.OMElement;

/**
 * Created by bmajur on 8/28/14.
 */
public class SoapMessageParser {
    String xmlText;

    public SoapMessageParser(String xmlText) {
        this.xmlText = xmlText;
    }

    SoapMessage parse() {
        try {
            OMElement ele = Parse.parse_xml_string(xmlText);
            SoapMessage soapMessage = new SoapMessage();
            soapMessage.setRoot(ele);
            soapMessage.setHeader(((OMElement) ele.getChildrenWithLocalName("Header").next()));
            soapMessage.setHeader(((OMElement) ele.getChildrenWithLocalName("Body").next()));
            return soapMessage;
        } catch (Exception e) {
            throw new ToolkitRuntimeException("Cannot parse SOAP Message: " + e.getMessage());
        }
    }
}
