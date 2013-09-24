package gov.nist.hit.ds.wsseTool.validation.tests.run

import gov.nist.hit.ds.wsseTool.api.config.ValConfig
import gov.nist.hit.ds.wsseTool.time.TimeUtil
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerWithOrder;
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Validation
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal
import gov.nist.hit.ds.wsseTool.validation.tests.CommonVal
import gov.nist.toolkit.wsseTool.validation.data.*
import groovy.util.slurpersupport.GPathResult

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.joda.time.DateTime
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestName
import org.junit.runner.RunWith;


@RunWith(ValRunnerWithOrder.class)
class AuthzDecisionStatementVal extends BaseVal {
	
	@Rule
	public TestName name = new TestName();

	/*
	 * Test initialization
	 */
	@Before
	public final void start() {
		System.out.println("------------ Test start : " + name.getMethodName()
				+ " -------------------");
	}

	@After
	public final void end() {
		System.out.println("------------ Test end : " + name.getMethodName()
				+ " -------------------");
	}

	@Validation(id="1090", rtm=["65"], category="optional")
	public void authnStatementParsing(){
		for(GPathResult authz : header.map.authzDecisionStatements){

			GPathResult children = authz.children()

			if(children[0].name() != "Action") log.error("authz decision statement first child must be an action but was ", children[0])
			if(children[1].name() != "Evidence") log.error("authz decision statement second child must be an evidence but was ", children[1])

			assert children[0].name() == "Action"
			assert children[1].name() == "Evidence"
		}
	}


	@Validation(id="1091", rtm=["131","133"])
	public void decision(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			if(authz.@Decision != 'Permit') log.error("@Decision must be 'Permit'")
			assert authz.@Decision == 'Permit'
		}
	}

	@Validation(id="1092", rtm=["131","134"] , status=ValConfig.Status.review)
	public void resource(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			if(authz.@Resource == null) log.error("@Resource must be present")
			assert authz.@Resource != null
			
			String to = context.getParams().get("To");
			String resource = authz.@Resource;
			
			if( resource != "" && resource != to) log.error(" @Resource = <value of S:Header/wsa:To> or ''. Header was {} and expected {} ", resource, to)
		}
	}

	@Validation(id="1093", rtm=["131","132"])
	public void action(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			if(authz.Action[0].text().trim() != "Execute") log.error("action should be : 'Execute' ")
			assert authz.Action[0].text().trim() == "Execute"

			if(authz.Action[0].@Namespace.text() != "urn:oasis:names:tc:SAML:1.0:action:rwedc") log.error("@Namespace should be : 'urn:oasis:names:tc:SAML:1.0:action:rwedc' ")
			assert authz.Action[0].@Namespace.text()  == "urn:oasis:names:tc:SAML:1.0:action:rwedc"
		}
	}

	@Validation(id="1094", rtm=["131"])
	public void evidence(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			if(authz.Evidence[0] == null) log.error("Evidence is required")
			assert authz.Evidence[0] != null
		}
	}

	@Validation(id="1095", rtm=["215"])
	public void containsSamlAssertion(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			if(authz.Evidence[0].Assertion[0] == null) log.error("Evidence should contain a saml assertion")
			assert authz.Evidence[0].Assertion[0] != null
		}
	}

	@Validation(id="1096", rtm=["135"])
	public void id() {
		for(GPathResult authz : header.map.authzDecisionStatements){
			String id = authz.Evidence[0].Assertion[0].@ID.text()
			Pattern pattern = Pattern.compile('^\\D', Pattern.UNICODE_CASE)
			Matcher m = pattern.matcher(id)

			if(!m.find()){
				log.error("ID shall not start with a digit, but {} starts with {}", id , id.charAt(0) )
			}
		}
	}

	@Validation(id="1097", rtm=["136"])
	public void issueInstant(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			String d =  authz.Evidence[0].Assertion[0].@IssueInstant.text()

			if(! TimeUtil.isDateInUTCorTimeZoneFormat(d)) log.error("invalid time format for issueInstant : {}", d)
			assert TimeUtil.isDateInUTCorTimeZoneFormat(d)

			DateTime ii = TimeUtil.parseDateString(d)
			String creationDate = header.map.timestamp.Created[0].text().trim()
			DateTime creationTime = TimeUtil.parseDateString(creationDate)

			if(ii.isAfter(creationTime)){
				log.error("saml2:AuthnStatement@authnInstant {} should be earlier than wsu:Timestamp/wsu:Created {}", ii ,creationTime)
			}

			assert(!ii.isAfter(creationTime))
		}
	}

	@Validation(id="1098", rtm=["66","137"])
	public void version() {
		for(GPathResult authz : header.map.authzDecisionStatements){
			String version =  authz.Evidence[0].Assertion[0].@Version.text()
			if("2.0" != version) log.error("invalid version : {} expected : 2.0", version)
			assert "2.0" == version
		}
	}

	@Validation(id="1099", rtm=["138"])
	public void issuerIsRequired() {
		for(GPathResult authz : header.map.authzDecisionStatements){
			GPathResult issuer =  authz.Evidence[0].Assertion[0].Issuer
			if(issuer == null) log.error("assertion must have an issuer")
			assert issuer != null
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
				if(nb.isAfter(creationTime)){
					log.error("@NotBefore {} should be earlier than wsu:Timestamp/wsu:Created {}", nb ,creationTime)
				}
			}

			if(!notOnOrAfter.isEmpty()){
				DateTime nooa = TimeUtil.parseDateString(notOnOrAfter)
				if(nooa.isBefore(creationTime)){
					log.error("@NotOnOrAfter {} should be later than wsu:Timestamp/wsu:Created {}", nooa ,creationTime)
				}
			}
		}
	}

	@Validation(id="1101", rtm=["139","140"])
	public void attributeStatement(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			GPathResult attrStatement =  authz.Evidence[0].Assertion[0].AttributeStatement[0]
			GPathResult children = attrStatement.children()
			GPathResult acp = children.findAll{ it.@Name == "AccessConsentPolicy"}
			GPathResult iacp = children.findAll{ it.@Name == "InstanceAccessConsentPolicy"}

			if(acp.isEmpty() && iacp.isEmpty() ) log.error(" AttributeStatement MUST contain at least one of the following Attributes:"+
			"Attribute/@Name = AccessConsentPolicy" +
			"Attribute/@Name = InstanceAccessConsentPolicy")
		}
	}

	@Validation(id="1102", rtm=["140"], status=ValConfig.Status.review, category="optional")
	public void AccessConsentPolicy(){
		for(GPathResult authz : header.map.authzDecisionStatements){
			GPathResult attrStatement =  authz.Evidence[0].Assertion[0].AttributeStatement[0]
			GPathResult children = attrStatement.children()
			GPathResult acp = children.findAll{ it.@Name == "AccessConsentPolicy"}

			if(acp.isEmpty()) {
				log.info("AccessConsentPolicy not present") ; return
			}

			if(acp[0].@NameFormat != "http://www.hhs.gov/healthit/nhin")
				log.error("@nameFormat must be {}, found {}", "http://www.hhs.gov/healthit/nhin", acp[0].@NameFormat )
			
			if(acp[0].children().isEmpty())
				log.error("accessConsentPolicy must have at least one attributeValue child element")
			
			for(GPathResult attr : acp[0].children()){
				boolean isValid = CommonVal.validURN( attr.text().trim() )
				
				if(!isValid)
					log.error("attribute value must be a valid urn. Found : {}", attr.text().trim() );
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

			if(iacp[0].@NameFormat != "http://www.hhs.gov/healthit/nhin")
				log.error("@nameFormat must be {}, found {}", "http://www.hhs.gov/healthit/nhin", iacp[0].@NameFormat )
			
			if(iacp[0].children().isEmpty()) 
				log.error("instanceAccessConsentPolicy must have at least one attributeValue child element")
			
			for(GPathResult attr : iacp[0].children()){
				boolean isValid = CommonVal.validURN( attr.text().trim() )
				
				if(!isValid)
					log.error("attribute value must be a valid urn. Found : {}", attr.text().trim() );
			}
		}
	}

}
