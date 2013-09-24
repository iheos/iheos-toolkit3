package gov.nist.hit.ds.wsseTool.api;

import gov.nist.hit.ds.wsseTool.api.config.Context;
import gov.nist.hit.ds.wsseTool.api.config.ContextFactory;
import gov.nist.hit.ds.wsseTool.api.config.GenContext;
import gov.nist.hit.ds.wsseTool.api.config.KeystoreAccess;
import gov.nist.hit.ds.wsseTool.api.exceptions.GenerationException;
import gov.nist.hit.ds.wsseTool.generation.opensaml.OpenSamlWsseSecurityGenerator;
import gov.nist.hit.ds.wsseTool.util.MyXmlUtils;

import java.security.KeyStoreException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;


/**
 * This is the api to the WsseToolkit module.
 * 
 * @author gerardin
 *
 */
public class WsseHeaderGenerator {
	
	private static final Logger log = LoggerFactory.getLogger(WsseHeaderGenerator.class);
	
	public static void main(String[] args) throws GenerationException, KeyStoreException {
		String store = "src/test/resources/keystore/keystore";
		String sPass = "changeit";
		String kPass = "changeit";
		String alias = "hit-testing.nist.gov";
		GenContext context = ContextFactory.getInstance();
		context.setKeystore(new KeystoreAccess(store,sPass,alias,kPass));
		context.setParam("patientId", "D1234");
		new WsseHeaderGenerator().generateWsseHeader(context);
	}

	private OpenSamlWsseSecurityGenerator wsse;
	
	public WsseHeaderGenerator(){
		wsse = new OpenSamlWsseSecurityGenerator();
	}
	
	/**
	 * 
	 * @param context TODO
	 * @return a generated standard-compliant wsseHeader
	 * @throws GenerationException 
	 */
	public Document generateWsseHeader(GenContext context) throws GenerationException {
		
		log.info("\n =============================" +
				 "\n generation of the wsse header" +
				 "\n ============================="
				);
		
		Document doc = wsse.generateWsseHeader(context);
		
		log.debug("header to validate : \n {}", MyXmlUtils.DomToString(doc) );
		
		return doc;
	}
}
