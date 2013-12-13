package gov.nist.hit.ds.wsseTool.parsing.opensaml;

import gov.nist.hit.ds.wsseTool.api.exceptions.GenerationException;
import gov.nist.hit.ds.wsseTool.generation.opensaml.OpenSamlFacade;
import gov.nist.hit.ds.wsseTool.generation.opensaml.OpenSamlWsseSecurityGenerator;
import gov.nist.hit.ds.wsseTool.util.MyXmlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Subject;
import org.opensaml.ws.wssecurity.Security;
import org.opensaml.ws.wssecurity.Timestamp;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.signature.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Build a security header representation from a template and provides
 * convenience methods to access the underlying model.
 * 
 * All the accessors lazy-load the elements they are responsible for as needed.
 * 
 * @author gerardin
 * 
 */

public class OpenSamlSecurityHeader {

	private Security sec;
	private Timestamp timestamp;
	private Assertion assertion;
	private Signature signature;
	private Subject subject;
	private List<AttributeStatement> attrStatements;
	private List<AuthnStatement> authnStatements;
	private Issuer issuer;

	private Map<String, String> prefixes;
	private Map<String, String> uris;

	private static final Logger log = LoggerFactory
			.getLogger(OpenSamlWsseSecurityGenerator.class);

	public OpenSamlSecurityHeader(OpenSamlFacade saml, String templateFile)
			throws GenerationException {

		try {
			Document tempDoc = MyXmlUtils
					.getDocumentWithResourcePath(templateFile);
			log.debug("successfully loaded template");

			Element temp = tempDoc.getDocumentElement();

			Unmarshaller un = Configuration.getUnmarshallerFactory()
					.getUnmarshaller(temp);
			assert un != null;
			log.debug("found opensaml unmarshaller...");

			sec = (Security) saml.fromElement(temp);
			log.debug("document successfully parsed with opensaml");
		} catch (Exception e) {
			throw new GenerationException(e);
		}
	}

	public OpenSamlSecurityHeader(OpenSamlFacade saml, Element element)
			throws GenerationException {

		try {
			Unmarshaller un = Configuration.getUnmarshallerFactory()
					.getUnmarshaller(element);
			assert un != null;
			log.debug("found opensaml unmarshaller...");

			sec = (Security) saml.fromElement(element);
			log.debug("document successfully parsed with opensaml");
		} catch (Exception e) {
			throw new GenerationException(e);
		}
	}

	public String getPrefix(String uri) {
		return getPrefixes().get(uri);
	}

	public Map<String, String> getPrefixes() {
		return (prefixes != null) ? prefixes : (prefixes = extractNamespaces());
	}

	public String getNamespace(String prefix) {
		return getUris().get(prefix);
	}

	public Map<String, String> getUris() {
		return (uris != null) ? uris : (uris = extractPrefixes());
	}

	public Security getSecurity() {
		return sec;
	}
	
	public Signature getSignature() {
		if (signature != null)
			return signature;

		Signature s = null;

		for (XMLObject xml : sec.getOrderedChildren()) {
			try {
				s = (Signature) xml;
				break;
			} catch (ClassCastException e) {
				// we have to blindly iterate until we find the good one!
			}
		}
		this.signature = s;
		return signature;
	}

	public Timestamp getTimestamp() {
		if (timestamp != null)
			return timestamp;

		Timestamp t = null;

		for (XMLObject xml : sec.getOrderedChildren()) {
			try {
				t = (Timestamp) xml;
				break;
			} catch (ClassCastException e) {
				// we have to blindly iterate until we find the good one!
			}
		}
		
		this.timestamp = t;
		return timestamp;
	}

	public Assertion getAssertion() {
		if (assertion != null)
			return assertion;

		Assertion ass = null;

		for (XMLObject xml : sec.getOrderedChildren()) {
			try {
				ass = (Assertion) xml;
				break;
			} catch (ClassCastException e) {
				// we have to blindly iterate until we find the good one!
			}
		}
		
		this.assertion = ass;
		return assertion;
	}

	public Subject getSubject() {
		Subject sub = getAssertion().getSubject();
		return (subject != null) ? subject : (subject = sub);
	}

	public Issuer getIssuer() {
		Issuer iss = getAssertion().getIssuer();
		return (issuer != null) ? issuer : (issuer = iss);
	}

	public List<AttributeStatement> getAttributeStatements() {
		List<AttributeStatement> stats = getAssertion()
				.getAttributeStatements();
		return (attrStatements != null) ? attrStatements
				: (attrStatements = stats);
	}

	public List<AuthnStatement> getAuthnStatements() {
		List<AuthnStatement> stats = getAssertion().getAuthnStatements();
		return (authnStatements != null) ? authnStatements
				: (authnStatements = stats);
	}

	private Map<String, String> extractNamespaces() {
		HashMap<String, String> p = new HashMap<String, String>();
		for (Namespace n : sec.getNamespaceManager()
				.getAllNamespacesInSubtreeScope()) {
			p.put(n.getNamespaceURI(), n.getNamespacePrefix());
		}
		return p;
	}

	private Map<String, String> extractPrefixes() {
		HashMap<String, String> p = new HashMap<String, String>();
		for (Namespace n : sec.getNamespaceManager()
				.getAllNamespacesInSubtreeScope()) {
			p.put(n.getNamespacePrefix(), n.getNamespaceURI());
		}
		return p;
	}

}
