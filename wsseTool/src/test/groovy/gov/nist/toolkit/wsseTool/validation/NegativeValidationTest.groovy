package gov.nist.toolkit.wsseTool.validation;

import static org.junit.Assert.*
import gov.nist.toolkit.wsseTool.BaseTest;
import gov.nist.toolkit.wsseTool.api.WsseHeaderValidator
import gov.nist.toolkit.wsseTool.api.config.KeystoreAccess
import gov.nist.toolkit.wsseTool.api.config.Context
import gov.nist.toolkit.wsseTool.api.config.ContextFactory
import gov.nist.toolkit.wsseTool.api.exceptions.ValidationException
import gov.nist.toolkit.wsseTool.generation.opensaml.OpenSamlWsseSecurityGenerator
import gov.nist.toolkit.wsseTool.util.MyXmlUtils

import java.security.KeyStoreException

import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element


class NegativeValidationTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(NegativeValidationTest.class);

	Context context;

	@Before
	public void loadKeystore() throws KeyStoreException {
		String store = "src/test/resources/keystore/keystore";
		String sPass = "changeit";
		String alias = "hit-testing.nist.gov";
		String kPass = "changeit";
		context = ContextFactory.getInstance();
		context.setKeystore(new KeystoreAccess(store,sPass,alias,kPass));
		context.setParam('homeCommunityId', "1.1");
		context.setParam('To', "http://endpoint1.hostname1.nist.gov");
		context.getParams().put("patientId", "D123401^^^&1.1&ISO");
		
	}
	
	
	//TODO check what to do when fatal error occurs! Right now we just exit and provide what have been already logged.
	// @Test(expected=ValidationException) void runBatchSecurity(){
		@Test void runBatchSecurity(){
		def file = "validation/noIssuer.xml";
		WsseHeaderValidator val = new WsseHeaderValidator();
		val.validate(MyXmlUtils.getDocumentWithResourcePath(file).getDocumentElement(), context);
		}
	
	@Test void badSubj(){
		def file = "bad/invalidSubjectConfirmationSchema.xml";
		WsseHeaderValidator val = new WsseHeaderValidator();
		Element xml = MyXmlUtils.getDocumentWithResourcePath(file).getDocumentElement();
		Element sec = xml.getElementsByTagNameNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security").item(0);
		val.validate(sec,context);
		}
	
	@Test void gracefullyWarnForProblemInContext(){
		Document xml = new OpenSamlWsseSecurityGenerator().generateWsseHeader(context);
		WsseHeaderValidator val = new WsseHeaderValidator();
		Context context = ContextFactory.getInstance(); //no info inside
		val.validate(xml.getDocumentElement(),context);
		
	}
	
}
