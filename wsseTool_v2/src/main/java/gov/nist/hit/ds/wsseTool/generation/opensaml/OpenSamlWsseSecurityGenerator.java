package gov.nist.hit.ds.wsseTool.generation.opensaml;

import gov.nist.hit.ds.wsseTool.api.config.Context;
import gov.nist.hit.ds.wsseTool.api.config.GenContext;
import gov.nist.hit.ds.wsseTool.api.config.KeystoreAccess;
import gov.nist.hit.ds.wsseTool.api.exceptions.GenerationException;
import gov.nist.hit.ds.wsseTool.namespace.dom.NhwinNamespaceContextFactory;
import gov.nist.hit.ds.wsseTool.parsing.opensaml.OpenSamlSecurityHeader;
import gov.nist.hit.ds.wsseTool.signature.api.Signer;
import gov.nist.hit.ds.wsseTool.time.TimeUtil;
import gov.nist.hit.ds.wsseTool.util.MyXmlUtils;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.joda.time.DateTime;
import org.opensaml.ws.wssecurity.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * Implementation of the wsse generator using XML templates, DOM manipulation
 * and Opensaml XMLObject model.
 * 
 * @author gerardin
 * 
 */

public class OpenSamlWsseSecurityGenerator {

	private static final Logger log = LoggerFactory
			.getLogger(OpenSamlWsseSecurityGenerator.class);
	private String templateFile = "templates/basic_template_unsigned.xml";

	private DateTime created;
	private DateTime expires;

	// contains declaration of all namespaces
	private static NamespaceContext namespaces = NhwinNamespaceContextFactory
			.getDefaultContext();

	private Context context;

	public Document generateWsseHeader(GenContext context)
			throws GenerationException {

		Document securityHeader; // the DOM document we build
		OpenSamlSecurityHeader sec; // the XMLObject representation of this
									// document. Provides methods that
									// facilitates
									// document creation

		try {

			this.context = context;
			
			if(context.getKeystore() == null){
				throw new GenerationException("No keystore info found in context.");
			}

			OpenSamlFacade saml = new OpenSamlFacade();
			log.info("unmarshall DOM template to XMLObject representation",
					templateFile);
			sec = saml.createSecurityFromTemplate(templateFile);

			log.info("generate time values");
			generateTime();

			log.info("update message timestamp");
			updateTimestamp(sec);

			log.info("marshall back to DOM");
			securityHeader = saml.marshallAsNewDOMDocument(sec.getSecurity());

			log.info("update message template");
			updateTemplate(securityHeader);

			log.info("sign message");
			signWsseHeader(sec, securityHeader, context.getKeystore());

			log.info("wsse header successfully generated");

		} catch (Exception e) {
			// we can't do nothing interesting so we return a standard failure
			throw new GenerationException(e);
		}

		return securityHeader;
	}

	private void generateTime() {
		created = TimeUtil.getCurrentDate();
		expires = created.plusYears(1);
	}

	private void updateTemplate(Document sec) throws GenerationException {
		updateTimeInfo(sec);
		updatePatientId(sec);
		updateHomeCommunityId(sec);
	}

	private void updatePatientId(Document sec) throws GenerationException {

		String patientId = null;

		try {
			patientId = (String) context.getParams().get("patientId");
		} catch (Exception e) {
			log.error("bad parameter 'patientId' in security context.", e);
			return;
		}

		if (patientId == null) {
			log.error("Error while generating security headers : no parameter 'patientId' has been provided in the security context.");
			return;
		}
		
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		Node node = null;
    	
		try {
			node = (Node) xp.evaluate("//*[@Name='urn:oasis:names:tc:xacml:2.0:resource:resource-id']", sec.getDocumentElement(),
					XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			log.error("could not update 'resource-id' attribute statement value", e);
		}
		
		Node attrValue = node.getChildNodes().item(1);
		
		attrValue.setTextContent(patientId);
		log.debug("Replace resource-id : {} in template by patientId : {} ",attrValue.getTextContent(), patientId);
	}
	
	
	private void updateHomeCommunityId(Document sec) throws GenerationException {

		String homeCommunityId = null;

		try {
			homeCommunityId = (String) context.getParams().get("homeCommunityId");
		} catch (Exception e) {
			log.error("bad parameter 'homeCommunityId' in security context.", e);
			return;
		}

		if (homeCommunityId == null) {
			log.error("Error while generating security headers : no parameter 'homeCommunityId' has been provided in the security context.");
			return;
		}
		
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		Node node;
    	
		try {
			node = (Node) xp.evaluate("//*[@Name='urn:nhin:names:saml:homeCommunityId']", sec.getDocumentElement(),
					XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new GenerationException("could not update 'homeCommunityId' attribute statement value", e);
		}
		
		Node attrValue = node.getChildNodes().item(1);
		
		attrValue.setTextContent(homeCommunityId);
		log.debug("Replace homeCommunityId : {} in template by homeCommunityId : {} ",attrValue.getTextContent(), homeCommunityId);
	}

	private void updateTimeInfo(Document sec) {
		try {
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			NodeList nodes;

			nodes = (NodeList) xp.evaluate("//@*", sec.getDocumentElement(),
					XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++) {
				String name = nodes.item(i).getNodeName();
				if (name.equals("NotOnOrAfter")) {
					nodes.item(i).setNodeValue(expires.plusHours(1).toString());
				} else if (name.equals("NotBefore")) {
					nodes.item(i)
							.setNodeValue(created.minusHours(1).toString());
				} else if (name.equals("IssueInstant")) {
					nodes.item(i).setNodeValue(
							created.minusMinutes(5).toString());
				} else if (name.equals("AuthnInstant")) {
					nodes.item(i).setNodeValue(
							created.minusMinutes(7).toString());
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateTimestamp(OpenSamlSecurityHeader sec) {
		log.info("update time stamp dates : {}", sec.getTimestamp()
				.getElementQName().getLocalPart());

		Timestamp t = sec.getTimestamp();

		t.getCreated().setDateTime(created);
		t.getExpires().setDateTime(expires);
	}

	private void signWsseHeader(OpenSamlSecurityHeader sec,
			Document securityHeader, KeystoreAccess access)
			throws GenerationException {

		String assertURI = sec.getAssertion().getElementQName()
				.getNamespaceURI();
		Element assertion = (Element) securityHeader.getElementsByTagNameNS(
				assertURI, "Assertion").item(0);

		// Find subjectConfirmationData and add keyinfo
		String confURI = sec.getSubject().getElementQName().getNamespaceURI();
		Node subjectConfirmationData = assertion.getElementsByTagNameNS(
				confURI, "SubjectConfirmationData").item(0);
		Signer.insertPublicKeyKeyInfo(subjectConfirmationData, access.publicKey);

		// Sign the assertion
		String samlId = sec.getAssertion().getID();
		String samlIdRef = "#" + samlId; // subtlety of idref format
		String subURI = sec.getSubject().getElementQName().getNamespaceURI();
		Node subject = assertion.getElementsByTagNameNS(subURI, "Subject")
				.item(0);
		
		
		//FIX FOR ALL JAVA ABOVE JAVA 6 update 37. The ID must be explicitly set.
		Attr attr = (Attr)sec.getAssertion().getDOM().getAttributeNode("ID");
		sec.getAssertion().getDOM().setIdAttributeNode(attr,true);
		
		Signer.signAssertion(assertion, subject, samlIdRef, access.keyPair);

		// Sign the timestamp
		String timestampId = sec
				.getTimestamp()
				.getDOM()
				.getAttribute(
						namespaces
								.getPrefix("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd")
								+ ":Id");
		String timestampIdRef = "#" + timestampId; // subtlety of idref format
		Signer.signTimeStamp(securityHeader.getDocumentElement(),
				timestampIdRef, samlIdRef, access.keyPair);

		log.info("header to validate : \n {}",
				MyXmlUtils.DomToString(securityHeader));

		String s = MyXmlUtils.DomToString(securityHeader);
	}
}
