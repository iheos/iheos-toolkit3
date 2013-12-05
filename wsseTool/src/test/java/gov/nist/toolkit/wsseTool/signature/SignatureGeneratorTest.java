package gov.nist.toolkit.wsseTool.signature;

import gov.nist.hit.ds.wsseTool.api.config.KeystoreAccess;
import gov.nist.hit.ds.wsseTool.namespace.dom.NwhinNamespace;
import gov.nist.hit.ds.wsseTool.signature.generation.KeyInfoGenerator;
import gov.nist.hit.ds.wsseTool.signature.generation.SignatureGenerator;
import gov.nist.hit.ds.wsseTool.util.LogOutputStream;
import gov.nist.hit.ds.wsseTool.util.MyXmlUtils;
import gov.nist.toolkit.wsseTool.BaseTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class SignatureGeneratorTest extends BaseTest  {

	KeystoreAccess keystore;
	String signatureFile = "src/test/resources/signature/test_output/signature.xml";
	String keyInfoFile = "src/test/resources/signature/test_output/keyinfo.xml";
	
	private static Logger log = LoggerFactory.getLogger("");

	@Before
	public void loadKeystore() throws KeyStoreException {
		String store = "src/test/resources/keystore/keystore";
		String sPass = "changeit";
		String alias = "hit-testing.nist.gov";
		String kPass = "changeit";
		keystore = new KeystoreAccess(store, sPass, alias, kPass);
	}

	/*
	 * Make sure we can sign an element. We use an empty element
	 */
	@Test
	public void testSignature() throws GeneralSecurityException, ParserConfigurationException,
			MarshalException, XMLSignatureException, TransformerException, IOException {
		
		KeyPair keyPair = keystore.keyPair;
		XMLSignature signature = SignatureGenerator.generateSignature("", keyPair.getPublic());

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();
		DOMSignContext dsc = new DOMSignContext(keyPair.getPrivate(), doc);
		dsc.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, NwhinNamespace.DS.prefix());
		signature.sign(dsc);

		
		MyXmlUtils.DomToStream(doc, new LogOutputStream(log));

	}

	/*
	 * Make sure we create and add a keyinfo to an empty document
	 */
	@Test
	public void testCreateKeyInfo() throws GeneralSecurityException, ParserConfigurationException,
			MarshalException, XMLSignatureException, FileNotFoundException, TransformerException {
		
		Certificate cert = keystore.certificate;
		PublicKey publicKey = keystore.publicKey;
		KeyInfo keyinfo = KeyInfoGenerator.generatePublicKeyKeyInfo(publicKey);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();

		//need to switch representation to be compatible with java.security interfaces
		DOMStructure d = new DOMStructure(doc);
		
		DOMCryptoContext context = new DOMSignContext(publicKey, doc);
		context.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, NwhinNamespace.DS.prefix());
		keyinfo.marshal(d, context);

		MyXmlUtils.DomToStream(doc, new LogOutputStream(log));
	}
	
	/*
	 * Make sure we can add a key info to an existing element
	 */
	@Test
	public void testAddKeyInfoToElement() throws GeneralSecurityException, ParserConfigurationException,
			MarshalException, XMLSignatureException, TransformerException, SAXException, IOException, XPathExpressionException {
		
		org.w3c.dom.Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse( new InputSource( new FileReader( "src/test/resources/signature/wsseHeaderTemplate.xml" ) ) );
	
	//add keyInfo to subjectConfirmationData
	Certificate cert = keystore.certificate;
	PublicKey publicKey = keystore.publicKey;
	KeyInfo keyinfo = KeyInfoGenerator.generatePublicKeyKeyInfo(publicKey);
	
	 org.w3c.dom.Node subjectConfirmationData = doc.getElementsByTagName("saml2:SubjectConfirmationData").item(0);
	
	//need to switch representation to be compatible with java.security interfaces
	DOMStructure d = new DOMStructure(subjectConfirmationData);
	
	keyinfo.marshal(d, null);
	
	MyXmlUtils.DomToStream(doc, new LogOutputStream(log));
	}
}
