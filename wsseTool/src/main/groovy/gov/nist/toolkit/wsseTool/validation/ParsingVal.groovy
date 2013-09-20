package gov.nist.toolkit.wsseTool.validation

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element

import gov.nist.toolkit.wsseTool.util.MyXmlUtils
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild

import gov.nist.toolkit.wsseTool.context.SecurityContextImpl
import gov.nist.toolkit.wsseTool.engine.annotations.Validation;
import gov.nist.toolkit.wsseTool.generation.opensaml.OpenSamlFacade
import gov.nist.toolkit.wsseTool.namespace.dom.NhwinNamespaceContextFactory
import gov.nist.toolkit.wsseTool.parsing.groovyXML.GroovyHeader
import javax.xml.namespace.NamespaceContext


public class ParsingVal {

	private static final Logger log = LoggerFactory.getLogger(ParsingVal.class)
	
	private SecurityContextImpl context
	private GroovyHeader header
	
	public ParsingVal(SecurityContextImpl context){
		this.context = context
		this.header = context.groovyHeader
	}

	@Validation(id="1019", rtm=["60", "175", "50", "55"])
	public void wsseStructure() {
		header.map.timestamp = header.map.wsse.Timestamp[0]; 
		header.map.assertion = header.map.wsse.Assertion[0]
		header.map.t_signature = header.map.wsse.Signature[0]; 

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

		//store in the symbol table
		header.map.issuer= issuers[0]
		header.map.a_signature = a_signatures[0]
		header.map.subject = subjects[0]
		header.map.attributeStatement = attributeStatements[0]
		header.map.authnStatement = authnStatements[0]
		header.map.authzDecisionStatements = authzDecisionStatements //TODO fix only one possible
		header.map.conditions = conditions
		header.map.advices = advices

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

		header.map.as_signedInfo = as_signedInfo[0]
		header.map.as_signatureValue = as_signatureValue[0]
		header.map.as_keyInfo = as_keyInfo[0]

		log.info("check assertion elements ordering")
		GPathResult children = header.map.a_signature.children()
		if(children[0] != header.map.as_signedInfo) log.error("assertion signature first child must be an issuer but was ", children[0])
		if(children[1] != header.map.as_signatureValue) log.error("assertion signature second child must be an issuer but was ", children[1])
		if(children[2] != header.map.as_keyInfo) log.error("assertion signature third child must be an issuer but was ", children[2])
		
	}
}
