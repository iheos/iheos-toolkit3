package gov.nist.hit.ds.wsseTool.api.config;

import gov.nist.hit.ds.wsseTool.api.WsseHeaderGenerator;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * KeystoreAccess provides easy access to a keystore info. It expects the
 * keystore to be configured with a unique private key
 * 
 * 
 * @author gerardin
 * 
 */
public class KeystoreAccess {

	private static final Logger log = LoggerFactory.getLogger(KeystoreAccess.class);
	
	private KeyStore keystore;
	public KeyPair keyPair;
	public PrivateKey privateKey;
	public PublicKey publicKey;
	public Certificate certificate;

	public KeystoreAccess(String storePath, String storePass, String privateKeyAlias, String privateKeyPass)
			throws KeyStoreException {

		try {
			keystore = loadKeyStore(storePath, storePass);
			loadKeyStoreInfo(privateKeyAlias, privateKeyPass);
			
			log.info("keystore successfully loaded from :" + storePath);
		} catch (KeyStoreException e) {
			throw new KeyStoreException("cannot properly access keystore located at : " + storePath, e);
		}
	}

	private void loadKeyStoreInfo(String privateKeyAlias, String privateKeyPass) throws KeyStoreException {
		if (!keystore.containsAlias(privateKeyAlias)) {
			//for debugging problem related to aliases not found
			String debug = "aliases in this keystore:";
			Enumeration<String> aliases = keystore.aliases();
			while(aliases.hasMoreElements()){
				debug += aliases.nextElement();
			}
			log.debug(debug);
			
			throw new KeyStoreException("alias not found : " + privateKeyAlias);
		}

		try {
			privateKey = (PrivateKey) keystore.getKey(privateKeyAlias, privateKeyPass.toCharArray());
			certificate = keystore.getCertificate(privateKeyAlias);
			publicKey = certificate.getPublicKey();
			keyPair = new KeyPair(publicKey, privateKey);
		} catch (Exception e) {
			throw new KeyStoreException("cannot retrieve info from keystore for alias : " + privateKeyAlias, e);
		}
	}

	private KeyStore loadKeyStore(String store, String sPass) throws KeyStoreException {
		try {
			KeyStore  mykeystore = KeyStore.getInstance("JKS");
			//try first on the classpath
			InputStream is = new FileInputStream(store);
			mykeystore.load(is, sPass.toCharArray());
			is.close();
			return mykeystore;
		} catch (Exception e) {
			throw new KeyStoreException("cannot load keystore with pass : " + sPass, e);
		}
	}
}
