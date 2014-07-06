package gov.nist.hit.ds.httpSoapValidator;

import static org.junit.Assert.*;
import gov.nist.hit.ds.httpSoapValidator.components.parsers.SoapMessageParser;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.utilities.xml.Parse;
import gov.nist.hit.ds.xdsException.XMLParserException;
import gov.nist.hit.ds.xmlValidator.XmlMessage;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMElement;
import org.junit.Test;

public class SoapMessageParserTest {

	@Test
	public void parseTest() throws XMLParserException, FactoryConfigurationError, SoapFaultException, RepositoryException {
		OMElement envelope = Parse.parse_xml_file("src/test/resources/goodSubmission.xml");
		XmlHolder xml = new XmlHolder();
		xml.xml = envelope;
		SoapMessageParser soapMessageParser = new SoapMessageParser();
		soapMessageParser.setXML(xml);
		soapMessageParser.run(null);
		SoapMessage soapMessage = soapMessageParser.getSoapMessage();
		assertTrue(soapMessage != null);
		assertTrue(soapMessage.getHeader() != null);
		assertTrue(soapMessage.getBody() != null);
	}

	class XmlHolder implements XmlMessage {
		OMElement xml;
		
		@Override
		public OMElement getXml() {
			return xml;
		}
		
	}

}
