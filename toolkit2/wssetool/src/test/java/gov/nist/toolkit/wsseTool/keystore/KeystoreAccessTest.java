package gov.nist.toolkit.wsseTool.keystore;

import static org.junit.Assert.assertNotNull;

import gov.nist.hit.ds.wsseTool.api.config.KeystoreAccess;
import gov.nist.toolkit.wsseTool.BaseTest;

import java.security.KeyStoreException;

import org.junit.Test;


public class KeystoreAccessTest extends BaseTest  {
	
	String store = "src/test/resources/keystore/keystore";
	String sPass = "changeit";
	String kPass = "changeit";
	String alias = "hit-testing.nist.gov";
	
	
	@Test
	public void testGetKeyPairFromKeystore() throws KeyStoreException {
		KeystoreAccess access = new KeystoreAccess( store, sPass, alias, kPass);
		assertNotNull(" key pair found" , access.keyPair);
		assertNotNull(" private key found" , access.privateKey);
		assertNotNull(" public key found" , access.publicKey);
		assertNotNull(" certificate found" , access.certificate);
	}
}
