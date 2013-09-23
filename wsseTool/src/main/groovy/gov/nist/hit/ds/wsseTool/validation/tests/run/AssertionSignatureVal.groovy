package gov.nist.hit.ds.wsseTool.validation.tests.run

import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Validation
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal
import gov.nist.hit.ds.wsseTool.validation.tests.CommonVal

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestName

class AssertionSignatureVal extends BaseVal {

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
	
	@Validation(id="1036", rtm=["59"])
	public void uniqueId(){
		if(! header.map.a_signature.@Id.isEmpty())
			assert CommonVal.uniqueId(header.map.a_signature.@Id)
	}
	
	@Validation(id="1038", rtm=["141"])
	public void signInfoUniqueId(){
		if(! header.map.as_signedInfo.@Id.isEmpty())
			assert CommonVal.uniqueId(header.map.a_signature.@Id)
	}
	
	@Validation(id="1043", rtm=["147"])
	public void referenceUriIsAssertionId(){
		String id = header.map.as_signedInfo.'*'.findAll { it.name() == "Reference" }.@URI.text()
		assert id.stripIndent(1) == header.map.assertion.@ID.text(); //we remove the starting # in the ref
	}
}
