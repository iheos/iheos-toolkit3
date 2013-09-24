package gov.nist.hit.ds.wsseTool.validation.tests.run

import static org.junit.Assert.*
import gov.nist.hit.ds.wsseTool.time.TimeUtil
import gov.nist.hit.ds.wsseTool.validation.data.IssuerFormat
import gov.nist.hit.ds.wsseTool.validation.engine.*
import gov.nist.hit.ds.wsseTool.api.config.*
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.*
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal
import gov.nist.hit.ds.wsseTool.validation.tests.CommonVal
import gov.nist.hit.ds.wsseTool.validation.tests.ValDescriptor

import java.text.MessageFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.joda.time.DateTime
import org.junit.runner.RunWith



@RunWith(ValRunnerWithOrder.class)
public class AssertionVal extends BaseVal {

	@Validation(id="1025", rtm=["66", "157"])
	public void version() {	
		assertEquals( "invalid version", "2.0" , header.map.assertion.@Version.text())
	}

	@Validation(id="1026", rtm=["67"])
	public void id() {
		String id = header.map.assertion.@ID.text()
		// '^\\D*$' means should start with a non digit!
		Pattern pattern = Pattern.compile('^\\D', Pattern.UNICODE_CASE)
		Matcher m = pattern.matcher(id)
		
		assertTrue( MessageFormat.format("ID shall not start with a digit, but {0} starts with {1}", id , id.charAt(0) ), m.find());
	}

	@Validation(id="1027", rtm=["68"])
	public void issueInstant(){
		String d = header.map.assertion.@IssueInstant.text()
		assertTrue( MessageFormat.format("invalid time format for issueInstant : {0}"), TimeUtil.isDateInUTCorTimeZoneFormat(d) );
	}

	@Validation(id="1028" , rtm=["69"], status=ValConfig.Status.not_implementable)
	public void issuerIsSecurityOfficer() {
		log.info(ValDescriptor.ISSUER_IS_SECURITY_OFFICER)
	}

	@Validation(id="1029", rtm=["70"])
	public void issuerAllowedFormat() {
		String format = header.map.issuer.@Format.text().trim()

		if(!(format in IssuerFormat.values)) log.error("issuer format not allowed found : {}, but expected one of : {}", format, IssuerFormat.values)
		assert format in IssuerFormat.values
	}

	@Validation(id="1030", rtm=["69","70"])
	public void issuerEmailValid(){
		String format = header.map.issuer.@Format.text()

		if(format != "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress"){
			log.info("test conditional if @format : urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress")
			return
		}

		if(! CommonVal.validEmail(format) ) log.error("invalid email address : {}", format)
		assert CommonVal.validEmail(format)
	}

	@Validation(id="1031", rtm=["69","70"])
	public void issuerDNValid(){
		String format = header.map.issuer.@Format.text()
		if(format != "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName"){
			log.info("test conditional if @format : urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName")
			return
		}

		String name = header.map.issuer.text().trim()
		if(! CommonVal.validDistinguishedName(name) ){
			log.error("NameID is not a valid distinguishedName, found : {}", name)
		}
	}

	@Validation(id="1057", rtm=["62","71"])
	public void subjectRequired(){
		if(header.map.subject == null) log.error("subject is required")
		assert header.map.subject != null
	}

	@Validation(id="1058", rtm=["72"], status=ValConfig.Status.not_implementable)
	public void subjectNameID(){
		log.info(ValDescriptor.NOT_IMPLEMENTABLE)
		log.info(ValDescriptor.SUBJECT_NAMED_ID)
	}

	@Validation(id="1059", rtm=["72","73"])
	public void subjectNameIDformat(){
		String format = header.map.subject.NameID[0].@Format.text()
		if(format != "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress" &&
		format != "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName")
			log.error(ValDescriptor.MA1059)
	}

	@Validation(id="1060", rtm=["72","73"], category="conditional")
	public void subjectNameIDEmailValid(){
		String format = header.map.subject.NameID[0].@Format.text()

		if(format != "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress"){
			log.info("test conditional if @format : urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress")
			return
		}

		if(! CommonVal.validEmail(format) ) log.error("invalid email address : {}", format)
		assert CommonVal.validEmail(format)
	}

	@Validation(id="1061", rtm=["72","73"], category="conditional")
	public void nameIDX509SubjectNameValid(){
		String format = header.map.subject.NameID[0].@Format.text()
		if(format != "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName"){
			log.info("test conditional if @format : urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName")
			return
		}

		String name = header.map.subject.NameID[0].text().trim()
		if(! CommonVal.validDistinguishedName(name) ){
			log.error("NameID is not a valid distinguishedName, found {}",name)
		}
	}

	@Validation(id="1063", rtm=["31","33","36","168"])
	public void subjectConfirmationMethod(){
		String method = header.map.subject.SubjectConfirmation[0].@Method.text()
		if(method != "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key") log.error("@Method should be urn:oasis:names:tc:SAML:2.0:cm:holder-of-key")
		assert method == "urn:oasis:names:tc:SAML:2.0:cm:holder-of-key"
	}

	@Optional
	@Validation(id="1064", rtm=["34"], status=ValConfig.Status.not_implemented)
	public void subjectConfirmationBaseID(){
	}

	@Optional
	@Validation(id="1065", rtm=["34"], status=ValConfig.Status.not_implemented)
	public void subjectConfirmationNameID(){
	}

	@Optional
	@Validation(id="1066", rtm=["34"], status=ValConfig.Status.not_implemented)
	public void subjectConfirmationEncryptedID(){
	}

	@Optional
	@Validation(id="1067", rtm=["35","36","211"])
	public void subjectConfirmationDataRequired(){
		if(header.map.subject.SubjectConfirmation[0].SubjectConfirmationData[0] == null) log.error("subject confirmation data is required")
		assert header.map.subject.SubjectConfirmation[0].SubjectConfirmationData[0] != null
	}

	//TODO need to be check only if 1063 holds => this kind of pattern should be easy to code.Think about it and revisit
	@Optional
	@Validation(id="1068", rtm=["36"])
	public void subjectConfirmationDataType(){
		String type = header.map.subject.SubjectConfirmation[0].SubjectConfirmationData[0].@type
		if(!type.isEmpty()){
			if(type != "saml:KeyInfoConfirmationDataType") log.error("@type should be saml:KeyInfoConfirmationDataType")
			assert type == "saml:KeyInfoConfirmationDataType"
		}
	}

	@Optional
	@Validation(id="1073", rtm=["211"])
	public void subjectConfirmationDataNotBefore(){
		String creationDate = header.map.timestamp.Created[0].text().trim()
		DateTime creationTime = TimeUtil.parseDateString(creationDate)


		String notBefore = header.map.subject.SubjectConfirmation[0].SubjectConfirmationData[0].@NotBefore.text()
		String notOnOrAfter = header.map.subject.SubjectConfirmation[0].SubjectConfirmationData[0].@NotOnOrAfter.text()

		if(!notBefore.isEmpty()){
			DateTime nb = TimeUtil.parseDateString(notBefore)
			if(nb.isAfter(creationTime)){log.error("subjectConfirmationData@NotBefore {} should be earlier than wsu:Timestamp/wsu:Created {}", nb ,creationTime)}
		}

		if(!notOnOrAfter.isEmpty()){
			DateTime nooa = TimeUtil.parseDateString(notOnOrAfter)
			if(nooa.isBefore(creationTime)){log.error("subjectConfirmationData@NotOnOrAfter {} should be later than wsu:Timestamp/wsu:Created {}", nooa ,creationTime)}
		}
	}
	
	@Optional
	@Validation(id="1074", rtm=["157"])
	public void conditionsNotBefore(){
		String creationDate = header.map.timestamp.Created[0].text().trim()
		DateTime creationTime = TimeUtil.parseDateString(creationDate)


		String notBefore = header.map.conditions.@NotBefore.text()
		String notOnOrAfter = header.map.conditions.@NotOnOrAfter.text()

		if(!notBefore.isEmpty()){
			DateTime nb = TimeUtil.parseDateString(notBefore)
			if(nb.isAfter(creationTime)){log.error("conditions@NotBefore {} should be earlier than wsu:Timestamp/wsu:Created {}", nb ,creationTime)}
		}

		if(!notOnOrAfter.isEmpty()){
			DateTime nooa = TimeUtil.parseDateString(notOnOrAfter)
			if(nooa.isBefore(creationTime)){log.error("conditions@NotOnOrAfter {} should be later than wsu:Timestamp/wsu:Created {}", nooa ,creationTime)}
		}
	}


}
