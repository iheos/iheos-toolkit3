package gov.nist.hit.ds.wsseTool.validation.tests.run

import static org.junit.Assert.*
import gov.nist.hit.ds.wsseTool.api.config.*
import gov.nist.hit.ds.wsseTool.time.TimeUtil
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerWithOrder
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Optional
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Validation
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal
import gov.nist.hit.ds.wsseTool.validation.tests.CommonVal
import gov.nist.toolkit.wsseTool.validation.data.*
import groovy.util.slurpersupport.GPathResult

import java.text.MessageFormat
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.joda.time.DateTime
import org.junit.runner.RunWith


@RunWith(ValRunnerWithOrder.class)
public class AuthzDecisionStatementVal extends BaseVal {

	@Validation(id="1090", rtm=["65"], category="optional")
	public void authnStatementParsing(){
		for(GPathResult authz : header.map.authzDecisionStatements){

			GPathResult children = authz.children()

			assertEquals("authz decision statement first child must be an action","Action", children[0].name())
			assertEquals("authz decision statement second child must be an evidence", "Evidence", children[1].name())
		}
	}


	@Validation(id="1091", rtm=["131", "133"])
	public void decision(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			assertEquals("@Decision must be 'Permit'","Permit", authz.@Decision.text())
		}
	}

	@Validation(id="1092", rtm=["131", "134"], status=ValConfig.Status.review)
	public void resource(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			assertTrue("@Resource must be present", authz.@Resource != null)

			String to = context.getParams().get("To")
			String resource = authz.@Resource

			assertTrue(MessageFormat.format(" @Resource = <value of S:Header/wsa:To> or ''. Header was {0} and expected {1} ", resource, to), resource == "" || resource == to)
		}
	}

	@Validation(id="1093", rtm=["131", "132"])
	public void action(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			assertEquals("action should be : 'Execute' ", "Execute", authz.Action[0].text().trim())
			assertEquals("@Namespace should be : 'urn:oasis:names:tc:SAML:1.0:action:rwedc' ","urn:oasis:names:tc:SAML:1.0:action:rwedc",  authz.Action[0].@Namespace.text())
		}
	}

	@Validation(id="1094", rtm=["131"])
	public void evidence(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			assertTrue("Evidence is required", authz.Evidence[0] != null)
		}
	}

	@Validation(id="1095", rtm=["215"])
	public void containsSamlAssertion(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			assertTrue("Evidence should contain a saml assertion", authz.Evidence[0].Assertion[0] != null)
		}
	}

	@Validation(id="1096", rtm=["135"])
	public void id() {
		for(GPathResult authz : header.map.authzDecisionStatements){
			String id = authz.Evidence[0].Assertion[0].@ID.text()
			Pattern pattern = Pattern.compile('^\\D', Pattern.UNICODE_CASE)
			Matcher m = pattern.matcher(id)

			assertTrue(MessageFormat.format("ID shall not start with a digit, but {0} starts with {0}", id , id.charAt(0)), m.find())
		}
	}

	@Validation(id="1097", rtm=["136"])
	public void issueInstant(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			String d =  authz.Evidence[0].Assertion[0].@IssueInstant.text()

			assertTrue(MessageFormat.format("invalid time format for issueInstant : {0}",d), TimeUtil.isDateInUTCorTimeZoneFormat(d))

			DateTime ii = TimeUtil.parseDateString(d)
			String creationDate = header.map.timestamp.Created[0].text().trim()
			DateTime creationTime = TimeUtil.parseDateString(creationDate)

			assertTrue(MessageFormat.format("saml2:AuthnStatement@authnInstant {0} should be earlier than wsu:Timestamp/wsu:Created {1}", ii ,creationTime), !ii.isAfter(creationTime))
		}
	}

	@Validation(id="1098", rtm=["66", "137"])
	public void version() {
		for(GPathResult authz : header.map.authzDecisionStatements){
			String version =  authz.Evidence[0].Assertion[0].@Version.text()
			assertEquals("invalid version", "2.0" , version )
		}
	}

	@Validation(id="1099", rtm=["138"])
	public void issuerIsRequired() {
		for(GPathResult authz : header.map.authzDecisionStatements){
			GPathResult issuer =  authz.Evidence[0].Assertion[0].Issuer
			assertTrue("assertion must have an issuer", issuer != null)
		}
	}

	@Validation(id="1100", rtm=["157"], category="optional")
	public void conditions() {
		for(GPathResult authz : header.map.authzDecisionStatements){

			GPathResult conditions =  authz.Evidence[0].Assertion[0].Conditions[0]

			String creationDate = header.map.timestamp.Created[0].text().trim()
			DateTime creationTime = TimeUtil.parseDateString(creationDate)

			String notBefore = conditions.@NotBefore.text()
			String notOnOrAfter = conditions.@NotOnOrAfter.text()

			if(!notBefore.isEmpty()){
				DateTime nb = TimeUtil.parseDateString(notBefore)
				assertTrue(MessageFormat.format("@NotBefore {0} should be earlier than wsu:Timestamp/wsu:Created {1}", nb ,creationTime), !nb.isAfter(creationTime))
			}

			if(!notOnOrAfter.isEmpty()){
				DateTime nooa = TimeUtil.parseDateString(notOnOrAfter)
				assertTrue(MessageFormat.format("@NotOnOrAfter {0} should be later than wsu:Timestamp/wsu:Created {1}", nooa ,creationTime), !nooa.isBefore(creationTime))
			}
		}
	}

	@Validation(id="1101", rtm=["139", "140"])
	public void attributeStatement(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			GPathResult attrStatement =  authz.Evidence[0].Assertion[0].AttributeStatement[0]
			GPathResult children = attrStatement.children()
			GPathResult acp = children.findAll{ it.@Name == "AccessConsentPolicy"}
			GPathResult iacp = children.findAll{ it.@Name == "InstanceAccessConsentPolicy"}

			assertTrue(" AttributeStatement MUST contain at least one of the following Attributes:"+
					"Attribute/@Name = AccessConsentPolicy" +
					"Attribute/@Name = InstanceAccessConsentPolicy", !acp.isEmpty() || !iacp.isEmpty() )
		}
	}

	@Optional
	@Validation(id="1102", rtm=["140"], status=ValConfig.Status.review)
	public void AccessConsentPolicy(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			GPathResult attrStatement =  authz.Evidence[0].Assertion[0].AttributeStatement[0]
			GPathResult children = attrStatement.children()
			GPathResult acp = children.findAll{ it.@Name == "AccessConsentPolicy"}

			if(acp.isEmpty()) {
				log.info("AccessConsentPolicy not present") ; return
			}

			assertEquals("@nameFormat must be http://www.hhs.gov/healthit/nhin","http://www.hhs.gov/healthit/nhin", acp[0].@NameFormat.text())

			assertTrue("accessConsentPolicy must have at least one attributeValue child element",!acp[0].children().isEmpty())

			for(GPathResult attr : acp[0].children()){
				boolean isValid = CommonVal.validURN( attr.text().trim() )

				assertTrue(MessageFormat.format("attribute value must be a valid urn. Found : {0}", attr.text().trim() ), isValid)
			}
		}
	}

	@Validation(id="1103", rtm=["140"], category="optional")
	public void InstanceAccessConsentPolicy(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			GPathResult attrStatement =  authz.Evidence[0].Assertion[0].AttributeStatement[0]
			GPathResult children = attrStatement.children()
			GPathResult iacp = children.findAll{ it.@Name == "InstanceAccessConsentPolicy"}

			if(iacp.isEmpty()) {
				log.info("InstanceAccessConsentPolicy not present") ; return
			}

			assertEquals("@nameFormat must be http://www.hhs.gov/healthit/nhin", "http://www.hhs.gov/healthit/nhin", iacp[0].@NameFormat.text())

			assertFalse("instanceAccessConsentPolicy must have at least one attributeValue child element", iacp[0].children().isEmpty())

			for(GPathResult attr : iacp[0].children()){
				boolean isValid = CommonVal.validURN( attr.text().trim() )
				assertTrue(MessageFormat.format("attribute value must be a valid urn. Found : {0}", attr.text().trim()), isValid )
			}
		}
	}
}
