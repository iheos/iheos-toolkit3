package gov.nist.hit.ds.wsseTool.validation.tests.run

import static org.junit.Assert.*
import static org.junit.Assume.*
import gov.nist.hit.ds.wsseTool.api.config.*
import gov.nist.hit.ds.wsseTool.util.MyXmlUtils;
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerWithOrder
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Optional
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Validation
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal
import gov.nist.hit.ds.wsseTool.validation.tests.CommonVal
import gov.nist.hit.ds.wsseTool.validation.tests.ValDescriptor
import gov.nist.toolkit.wsseTool.api.*
import gov.nist.toolkit.wsseTool.validation.data.*
import groovy.util.slurpersupport.GPathResult

import java.text.MessageFormat

import javax.xml.xpath.XPath
import javax.xml.xpath.XPathExpression
import javax.xml.xpath.XPathFactory

import org.junit.Before
import org.junit.runner.RunWith
import org.slf4j.Logger


@RunWith(ValRunnerWithOrder.class)
public class AttributeStatementVal extends BaseVal {

	/*
	 * Test initialization
	 */
	@Before
	public final void getAttributesList() {
		this.attrs = header.map.attributeStatement.children().findAll{it.name() == 'Attribute'}
	}

	private GPathResult attrs

	@Validation(id="1081", rtm=["64", "86", "166", "167"])
	public void uniqueAttributeNames(){

		for(GPathResult attr : attrs){
			boolean isUnique = attrs.findAll{ it.@Name == attr.name}.size() <= 1 //each attr has a unique name
			assertTrue("each attribute must have a unique name", isUnique)
		}
	}

	@Validation(id="1082", rtm=["87", "97", "98"])
	public void subjectId(){
		GPathResult subjectId = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:1.0:subject:subject-id"}

		assertTrue("subjectId missing", subjectId[0] != null)
		assertTrue("subjectId attribute value missing", subjectId[0].AttributeValue[0] != null)
	}

	@Validation(id="1083", rtm=["88", "99", "100"])
	public void organization(){
		GPathResult organization = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:1.0:subject:organization"}

		assertTrue("organization missing", organization[0] != null)
		assertTrue("organization attribute value missing", organization[0].AttributeValue[0] != null)
	}

	@Validation(id="1084", rtm=[
		"92",
		"101",
		"102",
		"103",
		"104"
	])
	public void organizationId(){
		GPathResult organizationId = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:1.0:subject:organization-id"}

		assertFalse("organization-id missing", organizationId[0] == null)
		assertFalse("organization-id attribute value missing", organizationId[0].AttributeValue[0] == null)

		String oid = organizationId[0].AttributeValue[0].text().trim()

		assertTrue(MessageFormat.format("invalid organization-id. Should be a valid urn starting with prefix urn:oid: , found {0}",oid), CommonVal.validURNoid(oid))
	}

	@Validation(id="1085", rtm=["91", "105", "106"])
	public void homeCommunityId(){
		GPathResult homeCommunityId = attrs.findAll{ it.@Name == "urn:nhin:names:saml:homeCommunityId"}

		assertFalse("homeCommunityId missing", homeCommunityId[0] == null)
		assertFalse("homeCommunityId attribute value missing", homeCommunityId[0].AttributeValue[0] == null)

		String hcid = homeCommunityId[0].AttributeValue[0].text().trim()

		assertTrue(MessageFormat.format("invalid homeCommunityId. Should be a valid urn starting with prefix urn:oid: , found {0}", hcid), CommonVal.validURNoid(hcid))
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

		assertFalse("role missing", role[0] == null)
		assertFalse("role attribute value missing", role[0].AttributeValue[0] == null)

		GPathResult attr = role[0].AttributeValue[0]
		GPathResult r =  role[0].AttributeValue[0].Role[0]

		String prefix = header.namespaces.getPrefix("http://www.w3.org/2001/XMLSchema-instance")

		String type = r.@"${prefix}:type"

		assertTrue("no type", type != null)

		if(type == "CE"){
			String anonymous = r.lookupNamespace("")
			assertTrue("wrong type. If CE is used, xmlns=\"urn:hl7-org:v3\" must be present", anonymous == "urn:hl7-org:v3")
		}
		else {
			String hl7 = header.namespaces.getPrefix("urn:hl7-org:v3")
			assertTrue(MessageFormat.format("wrong type, got : {0}, expected : {1} with hl7 defining the following namespace : urn:hl7-org:v3", r.@"${prefix}:type" ,"hl7:CE") ,  r.@"${prefix}:type".text() == "hl7:CE")
		}


		assertEquals("wrong codeSystem", "2.16.840.1.113883.6.96", r.@codeSystem.text())
		assertEquals("wrong codeSystemName", "SNOMED_CT", r.@codeSystemName.text())

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
	], status=ValConfig.Status.review, description="we are not making sure purposeOfUse is in the hl7 namespace.Not sure we should either.")
	public void purposeOfUse(){
		GPathResult purposeOfUse = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse"}

		assertTrue("purposeOfUse missing", purposeOfUse[0] != null)

		assertTrue("purposeOfUse attribute value missing", purposeOfUse[0].AttributeValue[0] != null)

		GPathResult attr = purposeOfUse[0].AttributeValue[0]
		GPathResult p =  purposeOfUse[0].AttributeValue[0].PurposeForUse[0]

		String prefix = header.namespaces.getPrefix("http://www.w3.org/2001/XMLSchema-instance")

		String type = p.@"${prefix}:type"

		assertTrue("no type", type != null)

		if(! (type == "CE" || type == "hl7:CE")){
			assertTrue(MessageFormat.format("wrong type value. expected : 'CE' or 'hl7:CE', got : {0}",type));
		}

		assertEquals("wrong codeSystem", "2.16.840.1.113883.3.18.7.1", p.@codeSystem.text() )
		assertEquals("wrong codeSystemName", "nhin-purpose", p.@codeSystemName.text() )

		log.info("code is not checked")
		log.info("display name correlation with code is not checked")
	}

	@Validation(id="1036-1056")
	public void verifyPurposeOfUse(){
		// 1. Instantiate an XPathFactory.
  javax.xml.xpath.XPathFactory factory = 
                    javax.xml.xpath.XPathFactory.newInstance();
  
  // 2. Use the XPathFactory to create a new XPath object
  javax.xml.xpath.XPath xpath = factory.newXPath();
  
  // 3. Compile an XPath string into an XPathExpression
  javax.xml.xpath.XPathExpression expression = xpath.compile(".//Security");
  
  log.info("header to validate : \n {}", MyXmlUtils.DomToString(context.domHeader) );
  
  // 4. Evaluate the XPath expression on an input document
 Node result = (Node)expression.evaluate(context.domHeader, javax.xml.xpath.XPathConstants.NODE);
 
 Object o = null;
	}

	@Optional
	//TODO check
	//TODO need to check that instanceConsentAccessPolicy is present but it depends if multiple AuthDezStatement are possible or not.
	@Validation(id="1088", rtm=[
		"93",
		"122",
		"123",
		"124",
		"125",
		"126",
		"127"
	], status=ValConfig.Status.review)
	public void resourceId(){
		GPathResult resourceId = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xacml:2.0:resource:resource-id"}

		assertTrue("resource-id not present", resourceId[0] != null);

	}

	@Validation(id="1088", rtm=[
		"93",
		"122",
		"123",
		"124",
		"125",
		"126",
		"127"
	], status=ValConfig.Status.review)
	public void resourceIdMatchPatientId(){
		GPathResult resourceId = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xacml:2.0:resource:resource-id"}

		assumeTrue(ValDescriptor.NOT_IMPLEMENTED + "resource id should match patient id from the requesting organization",false);
	}

	//TODO check. Second assertion does not seem to work properly
	@Optional
	@Validation(id="1089", rtm=["129"])
	public void npi(){
		GPathResult npi = attrs.findAll{ it.@Name == "urn:oasis:names:tc:xspa:2.0:subject:npi"}

		assertTrue("npi not present", npi.size() != 0)
		assertTrue("npi attribute value missing", npi[0].AttributeValue[0] != null)

		log.info("validation not fully implemented")
	}

}

