package gov.nist.toolkit.wsseTool.validation.tests.run

import gov.nist.toolkit.wsseTool.parsing.groovyXML.GroovyHeader
import gov.nist.toolkit.wsseTool.validation.engine.annotations.Validation
import gov.nist.toolkit.wsseTool.validation.tests.BaseVal;
import groovy.util.slurpersupport.GPathResult

import org.slf4j.Logger


public class ParsingVal extends BaseVal {

	@Validation(id="1019", rtm=["60", "175", "50", "55"])
	public void wsseStructure() {
		if(header.map.timestamp == null) log.error("security header must have a timestamp")
		if(header.map.assertion == null) log.error("assertion must have an assertion")
		if(header.map.t_signature == null) log.error("assertion must have a signature")	
	}

	@Validation(id="1024", rtm=["45","48","60","157","165"])
	public void assertionStructure(){
		log.info("check required assertion signature elements are present with a valid cardinality")
		def issuers = header.map.assertion.children().findAll{it.name() == 'Issuer'}
		def a_signatures = header.map.assertion.children().findAll{it.name() == 'Signature'}
		def subjects = header.map.assertion.children().findAll{it.name() == 'Subject'}
		def attributeStatements = header.map.assertion.children().findAll{it.name() == 'AttributeStatement'}
		def authnStatements = header.map.assertion.children().findAll{it.name() == 'AuthnStatement'}
		def authzDecisionStatements = header.map.assertion.children().findAll{it.name() == 'AuthzDecisionStatement'} //optional
		def conditions = header.map.assertion.children().findAll{it.name() == 'Conditions'} //optional
		def advices = header.map.assertion.children().findAll{it.name() == 'Advice'} //optional
		
		if(issuers.size() != 1) log.error("assertion must have a unique issuer but found {}",issuers.size())
		if( a_signatures.size() != 1) log.error("assertion must have a unique signature but found {}",a_signatures.size())
		if(subjects.size() != 1) log.error("assertion must have a unique signature but found {}",subjects.size())
		if(attributeStatements.size() != 1) log.error("assertion must have a unique attribute statement but found {}",attributeStatements.size())
		if(authnStatements.size() != 1) log.error("assertion must have a unique authorization statement but found {}",authnStatements.size())
		if(authzDecisionStatements.size() > 1) log.error("assertion must have a unique authorization decision statement but found {}", authnStatements.size())

		log.info("check required assertion elements ordering")
		GPathResult children = header.map.assertion.children()

		if(children[0] != header.map.issuer) log.error("assertion first child must be an issuer but was ", children[0])
		if(children[1] != header.map.a_signature) log.error("assertion second child must be a signature but was ", children[1])
		if(children[2] != header.map.subject) log.error("assertion third child must be a subject but was ", children[2])
		
	}

	@Validation(id="1035", rtm=["59"])
	public void a_signatureStructure(){

		log.info("check required assertion signature elements are present with a valid cardinality")
		def as_signedInfo = header.map.a_signature.children().findAll{it.name() == 'SignedInfo'}
		def as_signatureValue = header.map.a_signature.children().findAll{it.name() == 'SignatureValue'}
		def as_keyInfo = header.map.a_signature.children().findAll{it.name() == 'KeyInfo'}
		def as_objects = header.map.a_signature.children().findAll{it.name() == 'Object'} //optional

		if(as_signedInfo.size() != 1) log.error("assertion signature must have a unique signedInfo but found {}",as_signedInfo.size())
		if(as_signatureValue.size() != 1) log.error("assertion signature must have a unique signatureValue but found {}",as_signatureValue.size())
		if(as_keyInfo.size() != 1) log.error("assertion signature must have a unique keyInfo but found {}",as_keyInfo.size())

		log.info("check assertion elements ordering")
		GPathResult children = header.map.a_signature.children()
		if(children[0] != header.map.as_signedInfo) log.error("assertion signature first child must be an issuer but was ", children[0])
		if(children[1] != header.map.as_signatureValue) log.error("assertion signature second child must be an issuer but was ", children[1])
		if(children[2] != header.map.as_keyInfo) log.error("assertion signature third child must be an issuer but was ", children[2])
		
	}
}
