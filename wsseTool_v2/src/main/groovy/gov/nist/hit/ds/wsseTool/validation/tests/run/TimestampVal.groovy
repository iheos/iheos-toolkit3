package gov.nist.hit.ds.wsseTool.validation.tests.run

import org.junit.runner.RunWith

import gov.nist.hit.ds.wsseTool.time.TimeUtil
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerWithOrder
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.Validation
import gov.nist.hit.ds.wsseTool.validation.tests.BaseVal
import gov.nist.hit.ds.wsseTool.validation.tests.CommonVal
import gov.nist.hit.ds.wsseTool.validation.tests.ValDescriptor

import static org.junit.Assert.*
import static org.junit.Assume.*

@RunWith(ValRunnerWithOrder.class)
public class TimestampVal extends BaseVal {

	@Validation(id="1020", rtm=["45", "50", "53"])
	public void ordering() {
		def children = header.map.timestamp.children()

		assertEquals("timestamp first child should be Created", children[0].name(), 'Created')
		assertEquals("timestamp first child should be Expires", "Expires", children[1].name())
	}

	@Validation(id="1021" , rtm=["51", "54"])
	public void created() {
		String d = header.map.timestamp.Created[0].text()

		assertTrue("timestamp creation date not in UTC format: " + d, TimeUtil.isDateInUTCFormat(d))
	}

	@Validation(id="1022" , rtm=["52", "54"])
	public void expires() {
		String d = header.map.timestamp.Expires[0].text()

		assertTrue("timestamp expiration date not in UTC format: "+ d, TimeUtil.isDateInUTCFormat(d))

	}
	
	@Validation(id="1022" , rtm=["52", "54"])
	public void expiresLaterThanCapturedTime() {
		String d = header.map.timestamp.Expires[0].text()

		assumeTrue(ValDescriptor.NOT_IMPLEMENTED + ValDescriptor.LATER_THAN_CAPTURED_TIME , false );
	}

	@Validation(id="1023", rtm=["168"])
	public void uniqueId() {
		def timestamp = header.map.timestamp

		String prefix = header.namespaces.getPrefix("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd")

		GString id = "${prefix}:Id"
		assertTrue("timestamp does not have a valid Id attribute", header.map.timestamp.@"${prefix}:Id" != "")
		assertTrue("timestamp id is not unique in the document: " + id, CommonVal.uniqueId(header,header.map.timestamp.@"${prefix}:Id".text()))
	}
}
