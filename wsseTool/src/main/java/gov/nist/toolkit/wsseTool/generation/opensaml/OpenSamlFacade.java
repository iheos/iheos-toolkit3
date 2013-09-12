package gov.nist.toolkit.wsseTool.generation.opensaml;

import gov.nist.toolkit.wsseTool.api.exceptions.GenerationException;
import gov.nist.toolkit.wsseTool.parsing.opensaml.OpenSamlSecurityHeader;
import gov.nist.toolkit.wsseTool.util.PrettyPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Facade to the opensaml library.
 * 
 * Encapsulates the opensaml code and provides some convenience  methods.
 * All saml objects should be obtain via this class.
 * 
 * @author gerardin
 *
 */
public class OpenSamlFacade {

	private DocumentBuilder builder;
	private SecureRandomIdentifierGenerator generator;
	
	private static final Logger log = LoggerFactory.getLogger(OpenSamlFacade.class);
	
	public OpenSamlFacade() {
		try {
			//load the opensaml library
			org.opensaml.DefaultBootstrap.bootstrap();
			log.debug("saml bootstrap completed");
			
			//get a dom parser
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			builder = factory.newDocumentBuilder();
			
			//get an id generator
			generator = new SecureRandomIdentifierGenerator();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * <u>Slightly</u> easier way to create objects using OpenSAML's builder
	 * system.
	 */
	// cast to SAMLObjectBuilder<T> is caller's choice
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T create(Class<T> cls, QName qname) {
		return (T) ((XMLObjectBuilder) Configuration.getBuilderFactory().getBuilder(qname)).buildObject(qname);
	}

	/**
	 * Helper method to add an XMLObject as a child of a DOM Element.
	 */
	public static Element marshallAndAddToElement(XMLObject object, Element parent) throws IOException, MarshallingException,
			TransformerException {
		Marshaller out = Configuration.getMarshallerFactory().getMarshaller(object);
		return out.marshall(object, parent);
	}

	/**
	 * Helper method to get an XMLObject as a DOM Document.
	 */
	public Document marshallAsNewDOMDocument(XMLObject object) throws IOException, MarshallingException, TransformerException {
		Document document = builder.newDocument();
		Marshaller out = Configuration.getMarshallerFactory().getMarshaller(object);
		out.marshall(object, document);
		return document;
	}

	/**
	 * Helper method to pretty-print any XML object to a file.
	 */
	public void printToFile(XMLObject object, String filename) throws IOException, MarshallingException,
			TransformerException {
		Document document = marshallAsNewDOMDocument(object);

		String result = PrettyPrinter.prettyPrint(document);
		if (filename != null) {
			PrintWriter writer = new PrintWriter(new FileWriter(filename));
			writer.println(result);
			writer.close();
		} else
			System.out.println(result);
	}

	/**
	 * Helper method to read an XML object from a DOM element.
	 */
	public XMLObject fromElement(Element element) throws IOException, UnmarshallingException, SAXException {
		return Configuration.getUnmarshallerFactory().getUnmarshaller(element).unmarshall(element);
	}

	/**
	 * Helper method to read an XML object from a file.
	 */
	public XMLObject readFromFile(String filename) throws IOException, UnmarshallingException, SAXException {
		return fromElement(builder.parse(filename).getDocumentElement());
	}
	
	/**
	 * Generate a conform ID.
	 * @return
	 */
	public String generateID(){
		return generator.generateIdentifier();
	}
	
	/**
	 * Generate a object representation of the xml content of @templateFile
	 */
	public OpenSamlSecurityHeader createSecurityFromTemplate(String templateFile) throws GenerationException{
		return new OpenSamlSecurityHeader(this, templateFile);
	}
}
