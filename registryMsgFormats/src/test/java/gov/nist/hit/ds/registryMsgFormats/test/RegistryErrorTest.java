package gov.nist.hit.ds.registryMsgFormats.test;

import static org.junit.Assert.assertEquals;
import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.registryMsgFormats.RegistryError;

import org.junit.Test;

public class RegistryErrorTest {

	@Test
	public void genParseTest() {
		RegistryError re = new RegistryError();
		re
		.setContext(new ErrorContext("Message", "Resource"))
		.setErrorCode("ErrorCode")
		.setSeverity("Error")
		.setLocation("Here");
		
		RegistryError re2 = new RegistryError(re.toXML());
		assertEquals(re.getCodeContext().toString(), re2.getCodeContext().toString());
		assertEquals(re.getErrorCode().toString(), re2.getErrorCode().toString());
		assertEquals(re.getLocation().toString(), re2.getLocation().toString());
	}
}
