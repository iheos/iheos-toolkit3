package gov.nist.toolkit.wsseTool.validation;

import static org.junit.Assert.*
import gov.nist.hit.ds.wsseTool.api.config.Context
import gov.nist.hit.ds.wsseTool.api.config.ContextFactory
import gov.nist.hit.ds.wsseTool.api.config.KeystoreAccess
import gov.nist.hit.ds.wsseTool.generation.opensaml.OpenSamlWsseSecurityGenerator
import gov.nist.hit.ds.wsseTool.util.MyXmlUtils
import gov.nist.hit.ds.wsseTool.validation.WsseHeaderValidator;
import gov.nist.toolkit.wsseTool.BaseTest

import java.security.KeyStoreException

import org.junit.Before
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document


class ValidationTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(ValidationTest.class);

	Context context;

	@Before
	public void loadKeystore() throws KeyStoreException {
		String store = "src/test/resources/keystore/keystore";
		String sPass = "changeit";
		String alias = "hit-testing.nist.gov";
		String kPass = "changeit";
		context = ContextFactory.getInstance();
		context.setKeystore(new KeystoreAccess(store,sPass,alias,kPass));
		context.getParams().put("patientId", "D123401^^^&1.1&ISO");
		context.setParam('homeCommunityId', "urn:oid:1.1");
		context.setParam('To', "http://endpoint1.hostname1.nist.gov");
	}

	@Test void runBatch(){
		Document xml = new OpenSamlWsseSecurityGenerator().generateWsseHeader(context);
		WsseHeaderValidator val = new WsseHeaderValidator();
		val.validate(xml.getDocumentElement(),context);
		}
	

	@Test void runMessage1(){
def file = "sets/connect4RequestSecHeader.xml";
		WsseHeaderValidator val = new WsseHeaderValidator();
		val.validate(MyXmlUtils.getDocumentWithResourcePath(file).getDocumentElement(),context);
		}
		
}
