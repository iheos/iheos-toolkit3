package gov.nist.hit.ds.httpSoap.components.parsers;

import gov.nist.hit.ds.httpSoap.datatypes.SoapMessage;
import gov.nist.hit.ds.utilities.xml.Parse;
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;
import org.apache.axiom.om.OMElement;

/**
 * Created by bmajur on 8/28/14.
 */
public class SoapMessageParser {
    String xmlText;

    public SoapMessageParser(String xmlText) {
        this.xmlText = xmlText;
    }

    public SoapMessage parse() {
        try {
            OMElement ele = Parse.parse_xml_string(xmlText);
            SoapMessage soapMessage = new SoapMessage();
            soapMessage.setRoot(ele);
            soapMessage.setHeader(((OMElement) ele.getChildrenWithLocalName("Header").next()));
            soapMessage.setBody(((OMElement) ele.getChildrenWithLocalName("Body").next()));
            return soapMessage;
        } catch (Exception e) {
            throw new ToolkitRuntimeException("Cannot parse SOAP Message: " + e.getMessage());
        }
    }
}
