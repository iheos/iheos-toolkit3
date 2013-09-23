package gov.nist.toolkit.wsseTool.validation

import gov.nist.toolkit.wsseTool.api.*
import gov.nist.toolkit.wsseTool.api.config.ValConfig
import gov.nist.toolkit.wsseTool.parsing.groovyXML.GroovyHeader
import gov.nist.toolkit.wsseTool.validation.data.*
import gov.nist.toolkit.wsseTool.validation.engine.annotations.Validation
import groovy.util.slurpersupport.GPathResult

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestName
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AttributeStatementVal extends BaseVal {

	private static final Logger log = LoggerFactory.getLogger(AttributeStatementVal.class)

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
	
	/*
	 * Test initialization
	 */
	@Before
	public final void attributesSetup2() {
			this.attrs = header.map.attributeStatement.children().findAll{it.name() == 'Attribute'}
	}
	
	private GPathResult attrs

	@Validation(id="1081", rtm=["64", "86", "166", "167"])
	public void uniqueAttributeNames(){

		for(GPathResult attr : attrs){
			boolean isUnique = attrs.findAll{ it.@Name == attr.name}.size() <= 1 //each attr has a unique name
			if(!isUnique) log.error("each attribute must have a unique name")
			assert isUnique
		}
	}

	@Validation(id="1082", rtm=["87", "97", "98"])
	public void subjectId(){
		GPathResult subjectId = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:1.0:subject:subject-id"}

		if(subjectId[0] == null) log.error("subjectId missing")
		if(subjectId[0].AttributeValue[0] == null) log.error("subjectId attribute value missing")

		assert subjectId[0] != null
		assert subjectId[0].AttributeValue[0] != null
	}

	@Validation(id="1083", rtm=["88", "99", "100"])
	public void organization(){
		GPathResult organization = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:1.0:subject:organization"}

		if(organization[0] == null) log.error("organization missing")
		if(organization[0].AttributeValue[0] == null) log.error("organization attribute value missing")

		assert organization[0] != null
		assert organization[0].AttributeValue[0] != null
	}

	@Validation(id="1084", rtm=["92", "101", "102", "103", "104"])
	public void organizationId(){
		GPathResult organizationId = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:1.0:subject:organization-id"}

		if(organizationId[0] == null) log.error("organization-id missing")
		if(organizationId[0].AttributeValue[0] == null) log.error("organization-id attribute value missing")

		String oid = organizationId[0].AttributeValue[0].text().trim()

		if(! CommonVal.validURNoid(oid)){
			log.error("invalid organization-id. Should be a valid urn starting with prefix urn:oid: , found {}", oid)
		}
	}

	@Validation(id="1085", rtm=["91", "105", "106"])
	public void homeCommunityId(){
		GPathResult homeCommunityId = attrs.findAll{ it.@Name == "urn:nhin:names:saml:homeCommunityId"}

		if(homeCommunityId[0] == null) log.error("homeCommunityId missing")
		if(homeCommunityId[0].AttributeValue[0] == null) log.error("homeCommunityId attribute value missing")

		String hcid = homeCommunityId[0].AttributeValue[0].text().trim()

		if(! CommonVal.validURNoid(hcid)){
			log.error("invalid homeCommunityId. Should be a valid urn starting with prefix urn:oid: , found {}", hcid)
		}
	}

	@Validation(id="1086", rtm=[
		"89",
		"107",
		"108",
		"109",
		"110",
		"111",
		"112"
	])
	public void role(){
		GPathResult role = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xacml:2.0:subject:role"}

		if(role[0] == null) log.error("role missing")
		if(role[0].AttributeValue[0] == null) log.error("role attribute value missing")

		GPathResult attr = role[0].AttributeValue[0]
		GPathResult r =  role[0].AttributeValue[0].Role[0]

		String prefix = header.namespaces.getPrefix("http://www.w3.org/2001/XMLSchema-instance")

		if(r.@"${prefix}:type" != "hl7:CE") log.error("wrong type, got : {}, expected : {}", r.@"${prefix}:type" ,"hl7:CE")
		if(r.@codeSystem != "2.16.840.1.113883.6.96") log.error("wrong codeSystem, got : {}, expected : {}", r.@codeSystem ,"2.16.840.1.113883.6.96")
		if(r.@codeSystemName != "SNOMED_CT") log.error("wrong codeSystemName, got : {}, expected : {}", r.@codeSystemName ,"SNOMED_CT")

		log.info("code is not checked")
		log.info("display name correlation with code is not checked")
	}

	@Validation(id="1087", rtm=[
		"90",
		"113",
		"114",
		"116",
		"117",
		"118",
		"120"
	])
	public void purposeOfUse(){
		GPathResult purposeOfUse = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse"}

		if(purposeOfUse[0] == null) log.error("purposeOfUse missing")
		if(purposeOfUse[0].AttributeValue[0] == null) log.error("purposeOfUse attribute value missing")

		assert purposeOfUse[0] != null
		assert purposeOfUse[0].AttributeValue[0] != null

		GPathResult attr = purposeOfUse[0].AttributeValue[0]
		GPathResult p =  purposeOfUse[0].AttributeValue[0].PurposeForUse[0]

		String prefix = header.namespaces.getPrefix("http://www.w3.org/2001/XMLSchema-instance")
		
		String type = p.@"${prefix}:type"
		
		if(type == null){
			log.error("no type")
		}
		else if(type == "CE"){
			String anonymous = p.lookupNamespace("")
			if(anonymous != "urn:hl7-org:v3"){
				log.error("wrong type. If CE is used, xmlns=\"urn:hl7-org:v3\" must be present");
			}
		}
		else {
			String hl7 = header.namespaces.getPrefix("urn:hl7-org:v3")
			if(type != "${hl7}:CE") log.error("wrong type, got : {}, expected : {} with hl7 defining the following namespace : urn:hl7-org:v3", p.@"${prefix}:type" ,"hl7:CE")
		}
		
		if(p.@codeSystem != "2.16.840.1.113883.3.18.7.1") log.error("wrong codeSystem, got : {}, expected : {}", p.@codeSystem ,"2.16.840.1.113883.3.18.7.1")
		if(p.@codeSystemName != "nhin-purpose") log.error("wrong codeSystemName, got : {}, expected : {}", p.@codeSystemName ,"nhin-purpose")

		log.info("code is not checked")
		log.info("display name correlation with code is not checked")
	}

	//need to check that instanceConsentAccessPolicy is present but it depends if multiple AuthDezStatement are possible or not.
	@Validation(id="1088", rtm=[
		"93",
		"122",
		"123",
		"124",
		"125",
		"126",
		"127"
	], category="optional", status=ValConfig.Status.review)
	public void resourceId(){
		GPathResult resourceId = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xacml:2.0:resource:resource-id"}

		if(resourceId[0] == null) {
			log.info("resource-id not present but is optional"); return
		}

		log.info("validation not fully implemented")
		log.info("resource id should match patient id from the requesting organization")
	}

	@Validation(id="1089", rtm=["129"], category="optional")
	public void npi(){
		GPathResult npi = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:2.0:subject:npi"}

		if(npi[0] == null) {
			log.info("npi not present but is optional"); return
		}

		if(npi[0].AttributeValue[0] == null) log.error("npi attribute value missing")

		assert npi[0] != null
		assert npi[0].AttributeValue[0] != null

		log.info("validation not fully implemented")
	}
}

