package gov.nist.toolkit.wsseTool.signature.generation;

import gov.nist.toolkit.wsseTool.namespace.dom.NhwinNamespaceContextFactory;
import gov.nist.toolkit.wsseTool.namespace.dom.NwhinNamespace;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Collections;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


/**
 * Generates valid keyinfo information for both the assertion signature and the timestamp signature.
 * Each of them relies on a different mechanism.
 * 
 * @author gerardin
 *
 */
public class KeyInfoGenerator {

	// One factory is used for all signing jobs.
	// DOM is the only concrete factory shipped with the RI.
	static XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
			new org.jcp.xml.dsig.internal.dom.XMLDSigRI());

	//contains declaration of all namespaces
	static NamespaceContext context = NhwinNamespaceContextFactory.getDefaultContext();

	private static final String TOKEN_TYPE = "TokenType";
	private static final String VALUE_TYPE = "ValueType";
	private static final String SAML2_TOKEN_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0";
	private static final String SAML2_ASSERTION_VALUE_TYPE = "http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLID";
	private static final String TAG_SECURITY_TOKEN_REFERENCE = "SecurityTokenReference";
	private static final String TAG_KEY_IDENTIFIER = "KeyIdentifier";

	/**
	 * Generates a key info referencing a public key
	 * 
	 * @param publicKey
	 *            - the public key to use
	 * @return a key info
	 * @throws GeneralSecurityException
	 */
	public static KeyInfo generatePublicKeyKeyInfo(PublicKey publicKey) throws GeneralSecurityException {
		KeyInfoFactory keyInfoFac = fac.getKeyInfoFactory();
		KeyValue keyVal = keyInfoFac.newKeyValue(publicKey);
		KeyInfo keyInfo = keyInfoFac.newKeyInfo(Collections.singletonList(keyVal));
		return keyInfo;
	}

	/**
	 * Generates a key info referencing a saml token
	 * 
	 * @param parentDoc
	 *            - the DOM document to append this key info
	 * @param refId
	 *            - the refId of the saml token
	 * @return a key info
	 * @throws GeneralSecurityException
	 */
	public static KeyInfo generateSamlTokenKeyInfo(Node parentNode, String refId) throws GeneralSecurityException {
		Document parentDoc = parentNode.getOwnerDocument();
		
		String wsse11Prefix = NwhinNamespace.WSSE11.prefix();
		String wssePrefix = NwhinNamespace.WSSE.prefix();
		String wsseURI = NwhinNamespace.WSSE.uri();
		String wsse11URI = NwhinNamespace.WSSE11.uri();
		
		//create a security token tag with self-declared namespace.
		Element secTokenRef = parentDoc.createElementNS(wsseURI , wssePrefix + ":" + TAG_SECURITY_TOKEN_REFERENCE);
		secTokenRef.setAttributeNS(wsse11URI,wsse11Prefix + ":" + TOKEN_TYPE, SAML2_TOKEN_TYPE);

		//create a child key identifier tag
		Element keyIdentifier = parentDoc.createElementNS(wsseURI, wssePrefix + ":" + TAG_KEY_IDENTIFIER);
		keyIdentifier.setAttributeNS(null, VALUE_TYPE, SAML2_ASSERTION_VALUE_TYPE);
		Text value = parentDoc.createTextNode(refId.substring(1)); //remove starting #
		keyIdentifier.appendChild(value);
		secTokenRef.appendChild(keyIdentifier);
		
		XMLStructure structure = new DOMStructure(secTokenRef);

		KeyInfoFactory keyInfoFac = fac.getKeyInfoFactory();
		KeyInfo keyInfo = keyInfoFac.newKeyInfo(Collections.singletonList(structure));
		return keyInfo;
	}
	
	@Deprecated
	public static KeyInfo generateSamlTokenKeyInfo2(Node parentNode, String refId) throws GeneralSecurityException {
		Document parentDoc = parentNode.getOwnerDocument();
		
		String wsse11Prefix = NwhinNamespace.WSSE11.prefix();
		String wssePrefix = NwhinNamespace.WSSE.prefix();
		String wsseURI = NwhinNamespace.WSSE.uri();
		String wsse11URI = NwhinNamespace.WSSE11.uri();
		
		//create a security token tag with self-declared namespace.
		Element secTokenRef = parentDoc.createElement( wssePrefix + ":" + TAG_SECURITY_TOKEN_REFERENCE);
		secTokenRef.setAttribute("xmlns:"+wsse11Prefix, wsse11URI);
		secTokenRef.setAttribute("xmlns:"+wssePrefix, wsseURI);
		secTokenRef.setAttribute(wsse11Prefix + ":" + TOKEN_TYPE, SAML2_TOKEN_TYPE);

		//create a child key identifier tag
		Element keyIdentifier = parentDoc.createElement(wssePrefix + ":" + TAG_KEY_IDENTIFIER);
		keyIdentifier.setAttribute( VALUE_TYPE, SAML2_ASSERTION_VALUE_TYPE);
		Text value = parentDoc.createTextNode(refId.substring(1)); //remove starting #
		keyIdentifier.appendChild(value);
		secTokenRef.appendChild(keyIdentifier);
		
		

		XMLStructure structure = new DOMStructure(secTokenRef);

		KeyInfoFactory keyInfoFac = fac.getKeyInfoFactory();
		KeyInfo keyInfo = keyInfoFac.newKeyInfo(Collections.singletonList(structure));
		return keyInfo;
	}

}
