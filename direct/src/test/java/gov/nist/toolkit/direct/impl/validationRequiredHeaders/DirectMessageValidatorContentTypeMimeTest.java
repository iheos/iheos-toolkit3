package gov.nist.toolkit.direct.impl.validationRequiredHeaders;

import gov.nist.direct.directValidator.impl.DirectMimeMessageValidatorFacade;
import gov.nist.direct.utils.TextErrorRecorderModif;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DirectMessageValidatorContentTypeMimeTest {

	// DTS 133-145-146, Content-Type, Required
	// Result: Success
	@Test
	public void testContentType() {
        IAssertionGroup er = new TextErrorRecorderModif();
		DirectMimeMessageValidatorFacade validator = new DirectMimeMessageValidatorFacade();
		validator.validateContentType(er, "plain/text");
		assertTrue(!er.hasErrors());
	}

	// Result: Fail
	@Test
	public void testContentType2() {
        IAssertionGroup er = new TextErrorRecorderModif();
		DirectMimeMessageValidatorFacade validator = new DirectMimeMessageValidatorFacade();
		validator.validateContentType(er, "text");
		assertTrue(er.hasErrors());
	}

	// Result: Success
	@Test
	public void testContentTypeName() {
        IAssertionGroup er = new TextErrorRecorderModif();
		DirectMimeMessageValidatorFacade validator = new DirectMimeMessageValidatorFacade();
		validator.validateContentType(er, "X-test");
		assertTrue(!er.hasErrors());
	}

}
