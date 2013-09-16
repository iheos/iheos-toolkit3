package gov.nist.toolkit.wsseTool.signature;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nist.toolkit.wsseTool.BaseTest;
import gov.nist.toolkit.wsseTool.api.config.KeystoreAccess;
import gov.nist.toolkit.wsseTool.api.config.SecurityContext;
import gov.nist.toolkit.wsseTool.api.config.SecurityContextFactory;
import gov.nist.toolkit.wsseTool.api.exceptions.GenerationException;
import gov.nist.toolkit.wsseTool.generation.opensaml.OpenSamlFacade;
import gov.nist.toolkit.wsseTool.generation.opensaml.OpenSamlWsseSecurityGenerator;
import gov.nist.toolkit.wsseTool.parsing.opensaml.OpenSamlSecurityHeader;
import gov.nist.toolkit.wsseTool.signature.api.Verifier;
import gov.nist.toolkit.wsseTool.util.MyXmlUtils;

import java.io.IOException;
import java.security.KeyException;
import java.security.KeyStoreException;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class VerifierTest extends BaseTest {
	
		SecurityContext context;
		
		private static Logger log = LoggerFactory.getLogger("");

		@Before
		public void loadKeystore() throws KeyStoreException {
			String store = "src/test/resources/keystore/keystore";
			String sPass = "changeit";
			String alias = "hit-testing.nist.gov";
			String kPass = "changeit";
			context = SecurityContextFactory.getInstance();
			context.setKeystore(new KeystoreAccess(store,sPass,alias,kPass));
			context.getParams().put("patientId", "D123401^^^&1.1&ISO");
		}

		/*
		 * Make sure we can sign an element. We use an empty element
		 */
		@Test
		public void testVerifySignature() throws GenerationException, KeyException, MarshalException, XMLSignatureException{
			OpenSamlWsseSecurityGenerator wsse = new OpenSamlWsseSecurityGenerator();
			Document doc = wsse .generateWsseHeader(context);
			
			log.debug("header to validate : \n {}", MyXmlUtils.DomToString(doc) );
			
			OpenSamlFacade saml = new OpenSamlFacade();
			
			OpenSamlSecurityHeader header = new OpenSamlSecurityHeader(saml, doc.getDocumentElement());
			
			Boolean isValid = new Verifier().verifyTimestampSignatureWithDOM(header);
			
			assertTrue(isValid);
		}
		
		/*
		 * Make sure we can sign an element. We use an empty element
		 */
		@Test
		public void testVerifySignatureWithOpenSaml() throws GenerationException, KeyException, MarshalException, XMLSignatureException{
			OpenSamlWsseSecurityGenerator wsse = new OpenSamlWsseSecurityGenerator();
			Document doc = wsse .generateWsseHeader(context);
			
			log.debug("header to validate : \n {}", MyXmlUtils.DomToString(doc) );
			
			OpenSamlFacade saml = new OpenSamlFacade();
			
			OpenSamlSecurityHeader header = new OpenSamlSecurityHeader(saml, doc.getDocumentElement());
			
			Boolean isValid = new Verifier().verifyTimestampSignature(header);
			
			assertTrue(isValid);
		}
		

		@Test
		public void testVerifyTimestampSignatureGood() throws GenerationException, KeyException, MarshalException, XMLSignatureException, SAXException, IOException, ParserConfigurationException{
			
			Document doc = MyXmlUtils.getDocumentAsResource("signature/validHeader.xml");
			
			OpenSamlFacade saml = new OpenSamlFacade();
			
			OpenSamlSecurityHeader header = new OpenSamlSecurityHeader(saml, doc.getDocumentElement());
			
			Boolean isValid = new Verifier().verifyTimestampSignature(header);
			
			assertTrue(isValid);
		}
		
		@Test
		public void testVerifyTimestampSignatureBad() throws GenerationException, KeyException, MarshalException, XMLSignatureException, SAXException, IOException, ParserConfigurationException{
			
			Document doc = MyXmlUtils.getDocumentAsResource("signature/invalidHeaderTimestampTampered.xml");
			
			OpenSamlFacade saml = new OpenSamlFacade();
			
			OpenSamlSecurityHeader header = new OpenSamlSecurityHeader(saml, doc.getDocumentElement());
			
			Boolean isValid = new Verifier().verifyTimestampSignature(header);
			
			assertFalse(isValid);
		}
		
		//should pass because we only check the timestamp validity
		@Test
		public void testVerifyTimestampSignatureGoodButDocumentBad() throws GenerationException, KeyException, MarshalException, XMLSignatureException, SAXException, IOException, ParserConfigurationException{
			
			Document doc = MyXmlUtils.getDocumentAsResource("signature/invalidHeaderSamlAssertionTampered.xml");
			
			OpenSamlFacade saml = new OpenSamlFacade();
			
			OpenSamlSecurityHeader header = new OpenSamlSecurityHeader(saml, doc.getDocumentElement());
			
			Boolean isValid = new Verifier().verifyTimestampSignature(header);
			
			assertTrue(isValid);
		}

		@Test
		public void testVerifySamlAssertionSignatureBad() throws GenerationException, KeyException, MarshalException, XMLSignatureException, SAXException, IOException, ParserConfigurationException{
			
			Document doc = MyXmlUtils.getDocumentAsResource("signature/invalidHeaderSamlAssertionTampered.xml");
			
			OpenSamlFacade saml = new OpenSamlFacade();
			
			OpenSamlSecurityHeader header = new OpenSamlSecurityHeader(saml, doc.getDocumentElement());
			
			Boolean isValid = new Verifier().verifySamlAssertionSignature(header);
			
			assertFalse(isValid);
		}
}
