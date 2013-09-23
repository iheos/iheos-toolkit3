package gov.nist.toolkit.wsseTool.api;

import gov.nist.toolkit.wsseTool.BaseTest;
import gov.nist.toolkit.wsseTool.api.config.KeystoreAccess;
import gov.nist.toolkit.wsseTool.api.config.Context;
import gov.nist.toolkit.wsseTool.api.config.ContextFactory;
import gov.nist.toolkit.wsseTool.api.exceptions.GenerationException;
import gov.nist.toolkit.wsseTool.generation.opensaml.OpenSamlWsseSecurityGenerator;
import gov.nist.toolkit.wsseTool.util.MyXmlUtils;

import java.security.KeyStoreException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class BuildTest extends BaseTest {

	private static final Logger log = LoggerFactory.getLogger(BuildTest.class);
	
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
		context.getParams().put("homeCommunityId", "urn:oid:2.2");
	}

	@Test
	public void buildTest() throws GenerationException {
		
		Document xml = new OpenSamlWsseSecurityGenerator().generateWsseHeader(context);
		
		log.debug("header to validate : \n {}", MyXmlUtils.DomToString(xml) );
	}
}
