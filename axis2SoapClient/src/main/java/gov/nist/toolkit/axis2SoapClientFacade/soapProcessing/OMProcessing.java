package gov.nist.toolkit.axis2SoapClientFacade.soapProcessing;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXBuilder;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;

public class OMProcessing {

	public static String toString(OMElement elt){
		Writer w = new StringWriter();
		try {
			elt.serialize(w);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
		return elt.toString();
	}
	
	public static OMElement buildAxiomFromReader(Reader r) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {

		// First, create the parser
		XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(r);
		// create an OM builder, the most recommend approach is to use the
		// factory, but here we simply create it
		StAXOMBuilder builder = new StAXOMBuilder(parser);
		// get the root element (in this case the envelope)
		OMElement doc = builder.getDocumentElement();
		
		return doc;
	}
	
	public static OMElement buildAxiomFromAString(String xmlString) throws XMLStreamException {
		ByteArrayInputStream xmlStream = new ByteArrayInputStream(
				xmlString.getBytes());
		// create a builder. Since you want the XML as a plain XML, you can just
		// use
		// the plain OMBuilder
		StAXBuilder builder = new StAXOMBuilder(xmlStream);
		// return the root element.
		OMElement doc = builder.getDocumentElement();

		return doc;

	}
	
	public static SOAPEnvelope getSoapRepresentation(OMElement xml){
		SOAPFactory soapFactory = OMAbstractFactory.getSOAP12Factory();
		return soapFactory.getDefaultEnvelope();
	}
	
}
