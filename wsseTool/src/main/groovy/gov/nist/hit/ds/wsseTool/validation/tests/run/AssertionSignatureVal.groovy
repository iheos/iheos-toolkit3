package gov.nist.hit.ds.wsseTool.validation.tests.run

import static org.junit.Assert.*
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerWithOrder
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Validation
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal
import gov.nist.hit.ds.wsseTool.validation.tests.CommonVal

import java.text.MessageFormat

import org.junit.runner.RunWith

@RunWith(ValRunnerWithOrder.class)
public class AssertionSignatureVal extends BaseVal {
	
	@Validation(id="1036", rtm=["59"])
	public void uniqueId(){
		if(! header.map.a_signature.@Id.isEmpty())
			assertTrue(CommonVal.uniqueId(header.map.a_signature.@Id))
	}
	
	@Validation(id="1038", rtm=["141"])
	public void signInfoUniqueId(){
		if(! header.map.as_signedInfo.@Id.isEmpty())
			assertTrue(CommonVal.uniqueId(header.map.a_signature.@Id))
	}
	
	@Validation(id="1043", rtm=["147"])
	public void referenceUriIsAssertionId(){
		String id = header.map.as_signedInfo.'*'.findAll { it.name() == "Reference" }.@URI.text()
		String referenceURI = id.stripIndent(1) //we remove the starting # in the ref
		String assertionId = header.map.assertion.@ID.text();
		assertEquals(MessageFormat.format("reference URI should match assertionID : {1} but was : {0}", referenceURI, assertionId), referenceURI , assertionId); 
	}
}
