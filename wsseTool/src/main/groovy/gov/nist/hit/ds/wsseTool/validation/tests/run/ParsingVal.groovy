package gov.nist.hit.ds.wsseTool.validation.tests.run

import static org.junit.Assert.*
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerWithOrder;
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.*
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal
import groovy.util.slurpersupport.GPathResult

import java.text.MessageFormat

import org.junit.runner.RunWith;

@RunWith(ValRunnerWithOrder.class)
public class ParsingVal extends BaseVal {

	@Order(order=1)
	@Validation(id="1019", rtm=["60", "175", "50", "55"],
	description="Check header structure."
	)
	public void wsseStructure() {
		assertTrue( "security header must have a timestamp" , !header.map.timestamp.isEmpty())
		assertTrue( "assertion must have an assertion" , !header.map.assertion.isEmpty())
		assertTrue( "assertion must have a signature" , !header.map.t_signature.isEmpty())
	}

	@Order(order=2)
	@Validation(id="1024", rtm=["45", "48", "60", "157", "165"],
	description="check required assertion signature elements are present with a valid cardinality. Check assertion elements ordering."
	)
	public void assertionStructure(){
		def issuers = header.map.assertion.children().findAll{it.name() == 'Issuer'}
		def a_signatures = header.map.assertion.children().findAll{it.name() == 'Signature'}
		def subjects = header.map.assertion.children().findAll{it.name() == 'Subject'}
		def attributeStatements = header.map.assertion.children().findAll{it.name() == 'AttributeStatement'}
		def authnStatements = header.map.assertion.children().findAll{it.name() == 'AuthnStatement'}
		def authzDecisionStatements = header.map.assertion.children().findAll{it.name() == 'AuthzDecisionStatement'} //optional
		def conditions = header.map.assertion.children().findAll{it.name() == 'Conditions'} //optional
		def advices = header.map.assertion.children().findAll{it.name() == 'Advice'} //optional

		assertTrue( MessageFormat.format("assertion must have a unique issuer but found {0}", issuers.size()) , issuers.size() == 1)
		assertTrue( MessageFormat.format("assertion must have a unique signature but found {0}", a_signatures.size()),  a_signatures.size() == 1)
		assertTrue( MessageFormat.format("assertion must have a unique subject but found {0}", subjects.size()), subjects.size() == 1)
		assertTrue( MessageFormat.format("assertion must have a unique attribute statement but found {0}", attributeStatements.size()), attributeStatements.size() == 1)
		assertTrue( MessageFormat.format("assertion must have a unique authorization statement but found {0}",authnStatements.size()),authnStatements.size() == 1)
		assertTrue( MessageFormat.format("assertion must have a unique authorization decision statement but found {0}", authnStatements.size()), authzDecisionStatements.size() == 1)

		GPathResult children = header.map.assertion.children()

		assertTrue ( MessageFormat.format("assertion first child must be an issuer but was {0}", children[0]), children[0] == header.map.issuer)
		assertTrue ( MessageFormat.format("assertion second child must be a signature but was {0}", children[1]),children[1] == header.map.a_signature)
		assertTrue ( MessageFormat.format("assertion third child must be a subject but was {0}", children[2]), children[2] == header.map.subject)

	}

	@Order(order=3)
	@Validation(id="1035", rtm=["59"],
	description="check required assertion signature elements are present with a valid cardinality. Check assertion elements ordering.")
	public void a_signatureStructure(){

		def as_signedInfo = header.map.a_signature.children().findAll{it.name() == 'SignedInfo'}
		def as_signatureValue = header.map.a_signature.children().findAll{it.name() == 'SignatureValue'}
		def as_keyInfo = header.map.a_signature.children().findAll{it.name() == 'KeyInfo'}
		def as_objects = header.map.a_signature.children().findAll{it.name() == 'Object'} //optional

		assertTrue( MessageFormat.format("assertion signature must have a unique signedInfo but found {0}",as_signedInfo.size()) , as_signedInfo.size() == 1)
		assertTrue( MessageFormat.format("assertion signature must have a unique signatureValue but found {0}",as_signatureValue.size()) , as_signatureValue.size() == 1)
		assertTrue( MessageFormat.format("assertion signature must have a unique keyInfo but found {0}",as_keyInfo.size()) , as_keyInfo.size() == 1)

		GPathResult children = header.map.a_signature.children()

		assertTrue( MessageFormat.format("assertion signature first child must be a signedInfo but was {0}", children[0]) , children[0] == header.map.as_signedInfo)
		assertTrue( MessageFormat.format("assertion signature second child must be a signatureValue but was {0}", children[1]) , children[1] == header.map.as_signatureValue)
		assertTrue( MessageFormat.format("assertion signature third child must be a keyInfo but was {0}", children[2]) , children[2] == header.map.as_keyInfo)
	}
}
