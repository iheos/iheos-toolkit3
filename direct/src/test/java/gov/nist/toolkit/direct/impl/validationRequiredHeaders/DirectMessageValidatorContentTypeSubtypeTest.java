package gov.nist.toolkit.direct.impl.validationRequiredHeaders;

import gov.nist.direct.directValidator.impl.DirectMimeMessageValidatorFacade;
import gov.nist.direct.utils.TextErrorRecorderModif;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DirectMessageValidatorContentTypeSubtypeTest {

	// DTS 191, Content-Type Subtype, Required
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

}
