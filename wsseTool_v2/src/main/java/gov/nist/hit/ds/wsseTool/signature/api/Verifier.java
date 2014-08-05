package gov.nist.hit.ds.wsseTool.signature.api;

import gov.nist.hit.ds.wsseTool.parsing.opensaml.OpenSamlSecurityHeader;

import java.security.Key;
import java.security.KeyException;
import java.security.PublicKey;

import javax.xml.crypto.KeySelector;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;

import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.validation.ValidationException;
import org.w3c.dom.Element;

/**
 * Template for XML signature verification. Subclasses must provide the key
 * selection mechanism
 * 
 * @author gerardin
 * 
 */

public class Verifier {

	// One factory is used for all signing jobs.
	// DOM is the only concrete factory shipped with the RI.
	static XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
			new org.jcp.xml.dsig.internal.dom.XMLDSigRI());

	/**
	 * Verify the timestamp signature. We assume the document is well-formed and
	 * valid. If any problem occurs in the parsing, it should be catched by
	 * earlier tests
	 * 
	 * @param header
	 *            : the opensaml representation of this document
	 * @return true if valid
	 */
	public boolean verifyTimestampSignature(OpenSamlSecurityHeader header)
			throws KeyException {
		
		// get the subjectConfirmationData
		SubjectConfirmation conf = header.getAssertion().getSubject()
				.getSubjectConfirmations().get(0);
		// from spec, keyInfo must be the only child of subjectConfirmationData TODO (verify assumption)
		org.opensaml.xml.signature.KeyInfo keyInfo = (org.opensaml.xml.signature.KeyInfo) conf
				.getSubjectConfirmationData().getOrderedChildren().get(0);

		//from spec, only on keyValue is present TODO (verify assumption)
		PublicKey pkey = KeyInfoHelper.getKey(keyInfo.getKeyValues().get(0));
		
		//check signature
		Credential cred = SecurityHelper.getSimpleCredential(pkey, null);
		SignatureValidator sigValidator = new SignatureValidator(cred);
		
		try {
			//FIXME assumption must be wrong : signature is the 3rd element
			Signature sign = (Signature) header.getSignature();
			sigValidator.validate(sign);
		} catch (ValidationException e) {
			// Indicates signature was not cryptographically valid, or possibly
			// a processing error. We just return false.
			return false;
		}
		return true;
	}
	
	public boolean verifySamlAssertionSignature(OpenSamlSecurityHeader header)
			throws KeyException {
		
		//get the signature
		Signature sign = header.getAssertion().getSignature();
		
		//get the signature keyInfo
		org.opensaml.xml.signature.KeyInfo keyInfo = sign.getKeyInfo();

		//from spec, only on keyValue is present TODO (verify assumption)
		PublicKey pkey = KeyInfoHelper.getKey(keyInfo.getKeyValues().get(0));
		
		//check signature
		Credential cred = SecurityHelper.getSimpleCredential(pkey, null);
		SignatureValidator sigValidator = new SignatureValidator(cred);
		
		try {
			sigValidator.validate(sign);
		} catch (ValidationException e) {
			// Indicates signature was not cryptographically valid, or possibly
			// a processing error. We just return false.
			return false;
		}
		return true;
	}
	

	/**
	 * @deprecated (replaced by verifyTimestampSignature)
	 * Verify the timestamp signature. We assume the document is well-formed and
	 * valid. If any problem occurs in the parsing, it should be catched by
	 * earlier tests
	 * 
	 * @param header
	 *            : the opensaml representation of this document
	 * @return true if valid
	 */
	public boolean verifyTimestampSignatureWithDOM(OpenSamlSecurityHeader header)
			throws MarshalException, KeyException, XMLSignatureException {

		/*
		 * get the subjectConfirmationData.
		 */
		SubjectConfirmation conf = header.getAssertion().getSubject()
				.getSubjectConfirmations().get(0);
		org.opensaml.xml.signature.KeyInfo keyInfo = (org.opensaml.xml.signature.KeyInfo) conf
				.getSubjectConfirmationData().getOrderedChildren().get(0);

		// Get the public key from the subjectConfirmationData keyInfo
		DOMStructure keyInfoElt = new DOMStructure(keyInfo.getDOM());
		javax.xml.crypto.dsig.keyinfo.KeyInfo DOMkeyInfo = KeyInfoFactory
				.getInstance().unmarshalKeyInfo(keyInfoElt);
		javax.xml.crypto.dsig.keyinfo.KeyValue keyValue = (javax.xml.crypto.dsig.keyinfo.KeyValue) DOMkeyInfo
				.getContent().get(0);
		PublicKey key = keyValue.getPublicKey();

		// get the signature
		// TODO change : might not always be in 3rd position (Use DOM document instead?)
		Signature signature = (Signature) header.getSecurity().getOrderedChildren().get(2);

		return verify(signature.getDOM(), key);
	}

	/**
	 * Verify
	 * 
	 * @param element
	 *            : the signature DOM element
	 * @param key
	 *            : the public key to chekc the signature with
	 * @return true if the signature is valid
	 * @throws MarshalException
	 * @throws XMLSignatureException
	 */
	private boolean verify(Element element, Key key) throws MarshalException,
			XMLSignatureException {

		DOMValidateContext valContext = new DOMValidateContext(
				KeySelector.singletonKeySelector(key), element);
	//TODO check! seems to work without giving the signature namespace!
		// valContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS,	NwhinNamespace.DS.prefix());
		
		XMLSignature signature = fac.unmarshalXMLSignature(valContext);

		boolean isValid = signature.validate(valContext);

		return isValid;
	}

}
