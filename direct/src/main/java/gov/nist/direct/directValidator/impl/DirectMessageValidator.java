/**
 This software was developed at the National Institute of Standards and Technology by employees
of the Federal Government in the course of their official duties. Pursuant to title 17 Section 105 of the
United States Code this software is not subject to copyright protection and is in the public domain.
This is an experimental system. NIST assumes no responsibility whatsoever for its use by other parties,
and makes no guarantees, expressed or implied, about its quality, reliability, or any other characteristic.
We would appreciate acknowledgement if the software is used. This software can be redistributed and/or
modified freely provided that any derivative works bear some notice that they are derived from it, and any
modified versions bear some notice that they have been modified.

Project: NWHIN-DIRECT
Authors: William Majurski
		 Frederic de Vaulx
		 Diane Azais
		 Julien Perugini
		 Antoine Gerardin

 */

package gov.nist.direct.directValidator.impl;

import gov.nist.direct.directValidator.interfaces.MessageContentValidator;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import junit.framework.Assert;

public class DirectMessageValidator implements MessageContentValidator {

	// ************************************************
	// *********** Message headers checks *************
	// ************************************************


	// xxxxxxxxxxxxxxx SMTP Commands  xxxxxxxxxxxxxxx

	// DTS 198, ?, Required
	public void validateDTS198(IAssertionGroup er, String dts198) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 100, MAIL FROM SMTP, Required
	public void validateMailFromSMTP(IAssertionGroup er, String mailFromSmtp) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 101, RCPT TO, Required
	public void validateRcptTo(IAssertionGroup er, String RcptTo) {
		Assert.fail("Not Yet Implemented");

	}


	// xxxxxxxxxxxxxxx Outer Enveloped Message  xxxxxxxxxxxxxxx


	// DTS 199, Non-MIME Message Headers, Required
	public void validateNonMIMEMessageHeaders(IAssertionGroup er, String nonMIMEHeader) {
		String rfc = "-";
		if(!er.hasErrors()) {
			er.success("199", "Non-MIME Message Headers", "All Message Headers are valid", "All Non-MIME Message Headers must be valid", rfc);
		} else {
			er.error("199", "Non-MIME Message Headers", "Some Message Headers are not valid", "All Non-MIME Message Headers must be valid", rfc);
		}

	}

	// DTS 200, MIME Entity, Required
	public void validateMIMEEntity(IAssertionGroup er, String MIMEEntity) {
		String rfc = "-";
		if(!er.hasErrors()) {
			er.success("200", "MIME Entity", "All MIME Entity Headers are valid", "All MIME Entity Headers must be valid", rfc);
		} else {
			er.error("200", "MIME Entity", "Some MIME Entity Headers are not valid", "All MIME Entity Headers must be valid", rfc);
		}

	}

	// DTS 133a, Content-Type, Required
	public void validateMessageContentTypeA(IAssertionGroup er, String messageContentTypeA) {
		String rfc = "RFC 5751: 3.2;http://tools.ietf.org/html/rfc5751#section-3.2";
		messageContentTypeA = messageContentTypeA.split(";")[0];
		if (messageContentTypeA.equals("application/pkcs7-mime")){
			er.success("133a", "Content-Type", messageContentTypeA, "Must be application/pkcs7-mime", rfc);
		} else {
			er.error("133a", "Content-Type", messageContentTypeA, "Must be application/pkcs7-mime", rfc);
		}

	}

	// DTS 201, Content-Type Name, Optional
	public void validateContentTypeNameOptional(IAssertionGroup er, String contentTypeName) {
		String rfc = "RFC 5751: Section 3.2.1;http://tools.ietf.org/html/rfc5751#section-3.2.1";
		if(contentTypeName.contains("name")) {
			contentTypeName = contentTypeName.split("name=")[1];
			contentTypeName = contentTypeName.split(";")[0];
			if (contentTypeName.equals("smime.p7m")) {
				er.success("201", "Content-Type Name", contentTypeName, "Should be smime.p7m", rfc);
			} else {
				er.warning("201", "Content-Type Name", contentTypeName, "Should be smime.p7m", rfc);
			}
		} else {
			er.error("201", "Content-Type Name", "Not present", "Should be present", rfc);
		} 

	}

	// DTS 202, Content-Type S/MIME-Type, Optional
	public void validateContentTypeSMIMETypeOptional(IAssertionGroup er, String contentTypeSMIME) {
		String rfc = "RFC 5751: 3.2;http://tools.ietf.org/html/rfc5751#section-3.2;RFC 5751: 3.3;http://tools.ietf.org/html/rfc5751#section-3.3";
		if(contentTypeSMIME.contains("smime-type")) {
			contentTypeSMIME = contentTypeSMIME.split("smime-type=")[1];
		}
		if(contentTypeSMIME.contains(";")) {
			contentTypeSMIME = contentTypeSMIME.split(";")[0];
		}
		if (contentTypeSMIME.equals("enveloped-data")) {
			er.success("201", "Content-Type S/MIME Type", contentTypeSMIME, "Should be enveloped-data", rfc);
		} else {
			er.error("201", "Content-Type S/MIME Type", contentTypeSMIME, "Should be enveloped-data", rfc);
		}

	}

	// DTS 203, Content Disposition, Optional
	public void validateContentDispositionOptional(IAssertionGroup er, String contentDisposition) {
		String rfc = "RFC 5751: Section 3.2.1;http://tools.ietf.org/html/rfc5751#section-3.2.1";
		if(!contentDisposition.equals("")) {
			if (contentDisposition.contains("smime.p7m")) {
				er.success("203", "ContentDisposition", contentDisposition, "Should have filename smime.p7m", rfc);
			} else {
				er.warning("203", "ContentDisposition", contentDisposition, "Should have filename smime.p7m", rfc);
			}
		} else {
			er.error("203", "ContentDisposition", "Not present", "Should have filename smime.p7m", rfc);
		}

	}

	// DTS 129, Message Body, Required
	public void validateMessageBody(IAssertionGroup er, boolean decrypted) {
		String rfc = "-";
		if(decrypted) {
			er.success("129", "Message Body", "Message has been decrypted", "Must be encrypted", rfc);
		} else {
			er.error("129", "Message Body", "Message has not been decrypted", "Must be encrypted", rfc);
		}

	}


	// xxxxxxxxxxxxxxx Inner Decrypted Message  xxxxxxxxxxxxxxx


	// DTS 204, MIME Entity, Required
	public void validateMIMEEntity2(IAssertionGroup er, boolean mimeEntity) {
		String rfc = "-";
		if(mimeEntity) {
			er.success("204", "MIME Entity", "All MIME Entity are valid", "All MIME Entity must be valid", rfc);
		} else {
			er.error("204", "MIME Entity", "Some MIME Entity are not valid", "All MIME Entity must be valid", rfc);
		}

	}

	// DTS 133b, Content-Type, Required
	public void validateMessageContentTypeB(IAssertionGroup er, String messageContentTypeB) {
		String rfc = "RFC 2045: Section 5;http://tools.ietf.org/html/rfc2045#section-5;RFC 5751: Section 3.4.3.2;http://tools.ietf.org/html/rfc5751#section-3.4.3.2";
		if (messageContentTypeB.contains("multipart/signed")){
			er.success("133b", "Content-Type", messageContentTypeB, "Must be multipart/signed", rfc);
		} else {
			er.error("133b", "Content-Type", messageContentTypeB, "Must be multipart/signed", rfc);
		}

	}

	// DTS 160, Content-Type micalg, Required
	public void validateContentTypeMicalg(IAssertionGroup er, String contentTypeMicalg) {
		String rfc = "RFC 5751: Section 2.2;http://tools.ietf.org/html/rfc5751#section-2.2;RFC 5751: Section 3.4.3.2;http://tools.ietf.org/html/rfc5751#section-3.4.3.2";
		if (contentTypeMicalg.equals("")) {
			er.error("160", "Content-Type micalg", "Not present", "Must be present", rfc);
		} else {
			// Validates the "micalg" parameter value
			if ((contentTypeMicalg.contains("sha")) && (contentTypeMicalg.contains("1") || contentTypeMicalg.contains("256"))) {
				er.success("160", "Content-Type micalg", contentTypeMicalg, "Must be sha-1, sha-256 or sha1", rfc);
			} else {
				// error code 133-3
				er.error("160", "Content-Type micalg", contentTypeMicalg, "Must be sha-1, sha-256 or sha1", rfc);
			}
		}

	}

	// DTS 205, Content-Type protocol, Required
	public void validateContentTypeProtocol(IAssertionGroup er, String contentTypeProtocol) {
		String rfc = "RFC 5751: Section 3.4.3.2;http://tools.ietf.org/html/rfc5751#section-3.4.3.2";
		if (contentTypeProtocol.equals("\"application/pkcs7-signature\"")){
			er.success("205", "Content-Type protocol", contentTypeProtocol, "Must be application/pkcs7-signature", rfc);
		} else {
			er.error("205", "Content-Type protocol", contentTypeProtocol, "Must be application/pkcs7-signature", rfc);
		}

	}

	// DTS 206, Content-Transfer-Encoding, Required
	public void validateContentTransferEncoding(IAssertionGroup er, String contentTransfertEncoding) {
		String rfc = "RFC 5751: Section 3.1.3;http://tools.ietf.org/html/rfc5751#section-3.1.3";
		if (contentTransfertEncoding.equals("quoted-printable") || contentTransfertEncoding.equals("base64") || contentTransfertEncoding.equals("7-bit")){
			er.success("206", "Content-Transfer-Encoding", contentTransfertEncoding, "Must be quoted-printable, base64 or 7-bit", rfc);
		} else if(contentTransfertEncoding.equals("")) {
			er.warning("206", "Content-Transfer-Encoding", "Not present", "Must be quoted-printable, base64 or 7-bit", rfc);
		} else {
			er.error("206", "Content-Transfer-Encoding", contentTransfertEncoding, "Must be quoted-printable, base64 or 7-bit", rfc);
		}

	}

	// DTS 207, MIME Entity Body, Required
	public void validateMIMEEntityBody(IAssertionGroup er, int nbBody) {
		String rfc = "-";
		if (nbBody != 2){
			er.error("207", "MIME Entity Body", "Number of part: " + nbBody, "Must have 2 parts", rfc);
		} else {
			er.success("207", "MIME Entity Body", "Number of part: " + nbBody, "Must have 2 parts", rfc);
		}

	}


	// xxxxxxxxxxxxxxx Health Content Container  xxxxxxxxxxxxxxx


	// DTS 139, First MIME Part, Required
	public void validateFirstMIMEPart(IAssertionGroup er, boolean firstMIMEPart) {
		String rfc = "-";
		if(firstMIMEPart) {
			er.success("139", "First MIME Part", "MIME Entity are valid", "MIME Entity of Health Container", rfc);
		} else {
			er.error("139", "First MIME Part", "MIME Entity are not valid", "MIME Entity of Health Container", rfc);
		}

	}

	// DTS 151, First MIME Part Body, Required
	public void validateFirstMIMEPartBody(IAssertionGroup er, boolean firstMIMEPartBody) {
		String rfc = "RFC 2046: Section 4.1.1;http://tools.ietf.org/html/rfc2046#section-4.1.1";
		if(firstMIMEPartBody) {
			er.success("151", "First MIME Part Body", "Valid First Part Body", "", rfc);
		} else {
			er.error("151", "First MIME Part Body", "Invalid First Part Body", "", rfc);
		}
	}


	// xxxxxxxxxxxxxxx Signature  xxxxxxxxxxxxxxx


	// DTS 152, Second MIME Part, Required
	public void validateSecondMIMEPart(IAssertionGroup er, boolean secondMIMEPart) {
		String rfc = "-";
		if(secondMIMEPart) {
			er.success("152", "Second MIME Part", "MIME Entity are valid", "MIME Entity of Second Part", rfc);
		} else {
			er.error("152", "Second MIME Part", "MIME Entity are not valid", "MIME Entity of Second Part", rfc);
		}

	}

	// DTS 208, All Non-MIME Message Headers
	public void validateAllNonMIMEMessageHeaders(IAssertionGroup er, String nonMIMEHeader) {
		String rfc = "-";
		if(er.hasErrors()) {
			er.error("152", "All Non-MIME Message Headers", "Some Non-MIME Headers are not valid", "All Non-MIME Headers must be valid", rfc);
		} else {
			er.success("152", "All Non-MIME Message Headers", "All Non-MIME Headers are valid", "All Non-MIME Headers must be valid", rfc);
		}

	}

	// DTS 155, Content-Type, Required
	public void validateContentType2(IAssertionGroup er, String contentType) {
		String rfc = "RFC 5751: Section 3.2.1;http://tools.ietf.org/html/rfc5751#section-3.2.1;RFC 5751: Section 5.1.1;http://tools.ietf.org/html/rfc5751#section-5.1.1;RFC 5751: 3.4.3.2;http://tools.ietf.org/html/rfc5751#section-3.4.3.2";
		if(contentType.contains("multipart/signed")) {
			er.success("155", "Content-Type", contentType, "Must be application/pkcs7-signature", rfc);
		} else {
			er.error("155", "Content-Type", contentType, "Must be application/pkcs7-signature", rfc);
		}

	}

	// DTS 158, Second MIME Part Body, Required
	public void validateSecondMIMEPartBody(IAssertionGroup er, String secondMIMEPartBody) {
		String rfc = "RFC 5751: Section 3.4.3.2;http://tools.ietf.org/html/rfc5751#section-3.4.3.2";
		er.success("155", "Second MIME Part Body", "Second MIME Part Body is base64 encoded", "Must be base64 encoded", rfc);

	}

	// DTS 163, ?, Required
	public void validateDTS163(IAssertionGroup er, String dts163) {
		Assert.fail("Not Yet Implemented");

	}


	// DTS 165, DigestAlgorithm, Required
	public void validateDigestAlgorithm(IAssertionGroup er, String digestAlgorithm) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 166, EncapsuledContentInfo, Required
	public void validateEncapsuledInfo(IAssertionGroup er, String encapsulatedInfo) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 182, EncapsuledContentInfo.eContentInfo, Required
	public void validateEncapsuledInfo2(IAssertionGroup er, String encapsulatedInfo) {
		String rfc = "RFC 5652: 5.2;http://tools.ietf.org/html/rfc5652#section-5.2";
		if(encapsulatedInfo.equals("")) {
			er.success("182", "EncapsuledContentInfo.eContentInfo", "Not present", "Must be not be present", rfc);
		} else {
			er.error("182", "EncapsuledContentInfo.eContentInfo", encapsulatedInfo, "Must be not be present", rfc);
		}

	}

	// DTS 183, EncapsuledContentInfo.eContent, Optional
	public void validateEncapsuledInfo3(IAssertionGroup er, String encapsulatedInfo) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 167, Certificates
	public void validateCertificates(IAssertionGroup er, String certificates) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 168, Crls
	public void validateCrls(IAssertionGroup er, String crls) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 169, SignerInfos, Optional
	public void validateSignerInfos(IAssertionGroup er, String signerInfos) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 173, SignerInfos.sid, Optional
	public void validateSignerInfosSid(IAssertionGroup er, String signerInfosSid) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 174, SignerInfos.signerIdentifier, Required
	public void validateSignerIdentifier(IAssertionGroup er, String signerIdentifier) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 175, SignerInfos.signerIdentifier.issuerAndSerialNumber, Conditional
	public void validateSignerIdentifierIssueAndSerialNumber(IAssertionGroup er, String signerInfos) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 176, SignerInfos.signerIdentifier.subjectKeyIdentifier, Condtional
	public void validateSignerIdentifierSubjectKeyIdentifier(IAssertionGroup er, String signerInfos) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 177, SignerInfos.digestAlgorithm, Required
	public void validateSignerInfosDigestAlgorithm(IAssertionGroup er, String signerInfosDigestAlgorithm) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 178, SignerInfos.signedAttrs, Conditional
	public void validateSignedAttrs(IAssertionGroup er, String signerInfos) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 179, SignerInfos.signedAttrs.messageDigest, Conditional
	public void validateSignedAttrsMessageDigest(IAssertionGroup er, String signerInfos) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 180, SignerInfos.signedAttrs.contentType, Conditional
	public void validateSignedAttrsContentType(IAssertionGroup er, String signerInfos) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 170, SignerInfos.SignatureAlgorithm, Required
	public void validateSignerInfosSignatureAlgorithm(IAssertionGroup er, String signerInfosSignatureAlgorithm) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 171, SignerInfos.Signature, Required
	public void validateSignerInfosSignature(IAssertionGroup er, String signerInfosSignature) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 181, SignerInfos.unsignedAttrs, Optional
	public void validateSignerInfosUnsignedAttrs(IAssertionGroup er,
			String signerInfosUnsignedAttrs) {
		Assert.fail("Not Yet Implemented");

	}

	// DTS 172, Boundary, Required
	public void validateBoundary(IAssertionGroup er, String boundary) {
		Assert.fail("Not Yet Implemented");

	}

}
