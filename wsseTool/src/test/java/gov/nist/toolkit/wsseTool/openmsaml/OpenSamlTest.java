package gov.nist.toolkit.wsseTool.openmsaml;

import gov.nist.hit.ds.wsseTool.util.MyXmlUtils;
import gov.nist.toolkit.wsseTool.BaseTest;

import java.io.InputStream;

import org.junit.Test;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.ws.soap.soap11.Header;
import org.opensaml.ws.wssecurity.Security;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.ValidatorSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OpenSamlTest extends BaseTest  {

	// Static block to load the opensaml library
	static {
		try {
			org.opensaml.DefaultBootstrap.bootstrap();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void buildTest() {

		// Get the builder factory
		XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

		// Get the assertion builder based on the assertion element name
		@SuppressWarnings("unchecked")
		SAMLObjectBuilder<Assertion> builder = (SAMLObjectBuilder<Assertion>) builderFactory
				.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);

		// Create the assertion
		Assertion assertion = builder.buildObject();

	}

	@Test
	public void buildAndMarshallTest() throws MarshallingException {

		// Get the builder factory
		XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

		// Get the subject builder based on the subject element name
		SubjectBuilder builder = (SubjectBuilder) builderFactory.getBuilder(Subject.DEFAULT_ELEMENT_NAME);

		// Create the subject
		Subject subject = builder.buildObject();

		// Get the marshaller factory
		MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();

		// Get the Subject marshaller
		Marshaller marshaller = marshallerFactory.getMarshaller(subject);

		// Marshall the Subject
		Element subjectElement = marshaller.marshall(subject);

		MyXmlUtils.DomToStream(subjectElement, System.out);

	}

	@Test
	public void unmarshallBuildAndMarshallAgainTest() throws MarshallingException, XMLParserException,
			UnmarshallingException {
		String inCommonMDFile = "templates/basic_template_unsigned.xml";

		// Get parser pool manager
		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);

		// Parse metadata file
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(inCommonMDFile);
		Document inCommonMDDoc = ppMgr.parse(in);
		Element metadataRoot = inCommonMDDoc.getDocumentElement();

		// Get apropriate unmarshaller
		UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
		Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);

		// Unmarshall using the document root element, an EntitiesDescriptor in
		// this case
		Security security = (Security) unmarshaller.unmarshall(metadataRoot);

		// Get the marshaller factory
		MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();

		// Get the Subject marshaller
		Marshaller marshaller = marshallerFactory.getMarshaller(security);

		// Marshall the Subject
		Element subjectElement = marshaller.marshall(security);

	}
	
	@Test
	public void validate() throws XMLParserException, UnmarshallingException, ValidationException {
		String inCommonMDFile = "validCorrectedFullMessage.xml";
		
		// Get parser pool manager
		BasicParserPool ppMgr = new BasicParserPool();
		ppMgr.setNamespaceAware(true);

		// Parse metadata file
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(inCommonMDFile);
		Document inCommonMDDoc = ppMgr.parse(in);
		Element metadataRoot = inCommonMDDoc.getDocumentElement();

		// Get apropriate unmarshaller
		UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
	//	Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(metadataRoot);

		// Unmarshall using the document root element, an EntitiesDescriptor in
		// this case
	//	Header header = (Header) unmarshaller.unmarshall(metadataRoot);
	//	Security security = (Security) header.getOrderedChildren().get(0);
		
		Element sec = (Element) metadataRoot.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security").item(0);
		Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(sec);
		Security security = (Security) unmarshaller.unmarshall(sec);
			
		
		ValidatorSuite val = org.opensaml.Configuration.getValidatorSuite("saml2-metadata-schema-validator");
		ValidatorSuite val2 = org.opensaml.Configuration.getValidatorSuite("saml2-metadata-spec-validator");
		ValidatorSuite val3 = org.opensaml.Configuration.getValidatorSuite("saml2-core-schema-validator");
		ValidatorSuite val4 = org.opensaml.Configuration.getValidatorSuite("saml2-core-spec-validator");
		ValidatorSuite val5 = org.opensaml.Configuration.getValidatorSuite("signature-schema-validator");
		ValidatorSuite val6 = org.opensaml.Configuration.getValidatorSuite("encryption-schema-validator");

		
		
		val.validate(security);
		val2.validate(security);
		val3.validate(security);
		val4.validate(security);
		val5.validate(security);
		val6.validate(security);
	}
}
