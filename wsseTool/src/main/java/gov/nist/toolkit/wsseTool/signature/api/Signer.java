package gov.nist.toolkit.wsseTool.signature.api;

import gov.nist.toolkit.wsseTool.api.exceptions.GenerationException;
import gov.nist.toolkit.wsseTool.namespace.dom.NhwinNamespaceContextFactory;
import gov.nist.toolkit.wsseTool.signature.generation.KeyInfoGenerator;
import gov.nist.toolkit.wsseTool.signature.generation.SignatureGenerator;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.namespace.NamespaceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

/**
 * Signer let you sign a xml element once the xml signature has been generated.
 * 
 * @author gerardin
 *
 */

public class Signer {

	private static final Logger log = LoggerFactory.getLogger(Signer.class);
	
	//contains declaration of all namespaces
	private static NamespaceContext context = NhwinNamespaceContextFactory.getDefaultContext();

	/**
	 * Sign the assertion
	 * 
	 * @param xml
	 *            - the security element
	 * @param nextSibling
	 *            - the subject element
	 * @param id
	 *            - the assertion id
	 * @param keyPair
	 *            - the keypair used to sign
	 * @throws GeneralSecurityException
	 */
	public static void signAssertion(Node xml, Node nextSibling, String id, KeyPair keyPair)
			throws GenerationException {
		try {
			KeyInfo keyInfo = KeyInfoGenerator.generatePublicKeyKeyInfo(keyPair.getPublic());
			sign(xml, nextSibling, id, keyPair.getPrivate(), keyInfo);
		} catch (Exception e) {
			throw new GenerationException(e);
		}
	}

	/**
	 * Sign the timestamp
	 * 
	 * @param assertion
	 *            - the assertion element
	 * @param id
	 *            - the timestamp id
	 * @param refId
	 *            - the reference id of the saml token
	 * @param keyPair
	 *            - the keypair used to sign
	 * @throws GenerationException
	 */
	public static void signTimeStamp(Node assertion, String id, String refId, KeyPair keyPair)
			throws GenerationException {
		try {
			KeyInfo keyInfo = KeyInfoGenerator.generateSamlTokenKeyInfo(assertion, refId);
			sign(assertion, null, id, keyPair.getPrivate(), keyInfo);
		} catch (Exception e) {
			throw new GenerationException(e);
		}
	}

	/**
	 * Insert a key info referencing a public key
	 * 
	 * @param xml
	 *            - the DOM element where to insert the key info
	 * @param publicKey
	 *            - the referenced public key
	 * @throws GenerationException
	 */
	public static void insertPublicKeyKeyInfo(Node xml, PublicKey publicKey) throws GenerationException {
		KeyInfo keyInfo;
		try {
			keyInfo = KeyInfoGenerator.generatePublicKeyKeyInfo(publicKey);
			insertKeyInfo(xml, publicKey, keyInfo);
		} catch (Exception e) {
			throw new GenerationException(e);
		}
	}

	/**
	 * Sign a element
	 * 
	 * @param xml
	 *            - the parent element
	 * @param nextSibling
	 *            - the element before which the signature should be written
	 * @param id
	 *            - the id of the element to sign
	 * @param privateKey
	 *            - the signing private key
	 * @param keyInfo
	 *            - the keyinfo to use for this signature
	 * @throws GeneralSecurityException
	 * @throws MarshalException
	 * @throws XMLSignatureException
	 */
	private static void sign(Node xml, Node nextSibling, String id, PrivateKey privateKey, KeyInfo keyInfo)
			throws GeneralSecurityException, MarshalException, XMLSignatureException {

		log.info("try to sign DOM element : {} ", xml.getLocalName());

		XMLSignature signature = SignatureGenerator.generateSignatureWithCustomKeyInfo(id, keyInfo);

		DOMSignContext dsc = null;
		if (nextSibling != null) {
			log.info("signature written before DOM element : {}", nextSibling.getLocalName());
			dsc = new DOMSignContext(privateKey, xml, nextSibling);
		} else {
			log.info("signature written at the end of DOM element : {}", xml.getLocalName());
			dsc = new DOMSignContext(privateKey, xml);
		}

		dsc.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, context.getPrefix("http://www.w3.org/2000/09/xmldsig#"));

		signature.sign(dsc);

		log.info("successfully signed DOM element : {} ", xml.getLocalName());

	}

	/**
	 * Insert a key info in a DOM element
	 * 
	 * @param xml
	 *            - the DOM element where to insert the key info
	 * @param publicKey
	 *            - the public key (it is unclear with it is required to build
	 *            the context)
	 * @param keyInfo
	 *            - the key info to insert
	 * @throws MarshalException
	 */
	private static void insertKeyInfo(Node xml, PublicKey publicKey, KeyInfo keyInfo) throws MarshalException {
		log.info("try to create keyinfo (Holder of Key) for DOM element : {} ", xml.getLocalName());

		// need to switch representation to be compatible with java.security
		// interfaces
		DOMStructure d = new DOMStructure(xml);

		DOMCryptoContext DOMcontext = new DOMSignContext(publicKey, xml);

		//TODO remove context
		DOMcontext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, context.getPrefix("http://www.w3.org/2000/09/xmldsig#"));

		keyInfo.marshal(d, DOMcontext);

		log.info("successfully created keyinfo for DOM element : {} ", xml.getLocalName());
	}

}
