package gov.nist.toolkit.direct.impl.validationRequiredHeaders;

import gov.nist.direct.directValidator.impl.DirectMimeMessageValidatorFacade;
import gov.nist.direct.utils.TextErrorRecorderModif;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DirectMessageValidatorMIMEHeaderFieldsTest {

	
	// DTS 190, All Mime Header Fields, Required
	// Result: Success
	@Test
	public void testAllMimeHeaderFields() {
        IAssertionGroup er = new TextErrorRecorderModif();
		DirectMimeMessageValidatorFacade validator = new DirectMimeMessageValidatorFacade();
		validator.validateAllMimeHeaderFields(er, "attachment; filename=smime.p7m");
		assertTrue(!er.hasErrors());
	}
	
	// Result: Success
	@Test
	public void testAllMimeHeaderFields2() {
        IAssertionGroup er = new TextErrorRecorderModif();
		DirectMimeMessageValidatorFacade validator = new DirectMimeMessageValidatorFacade();
		validator.validateAllMimeHeaderFields(er, "attachment; comment:\"test comment (comment)\"; filename=smime.p7m");
		assertTrue(!er.hasErrors());
	}

	// Result: Fail
	@Test
	public void testAllMimeHeaderFields3() {
        IAssertionGroup er = new TextErrorRecorderModif();
		DirectMimeMessageValidatorFacade validator = new DirectMimeMessageValidatorFacade();
		validator.validateAllMimeHeaderFields(er, "attachment(comment); filename=smime.p7m");
		assertTrue(!er.hasErrors());
	}

	// Result: Fail
	@Test
	public void testAllMimeHeaderFields4() {
        IAssertionGroup er = new TextErrorRecorderModif();
		DirectMimeMessageValidatorFacade validator = new DirectMimeMessageValidatorFacade();
		validator.validateAllMimeHeaderFields(er, "attachment; filename=smime.p7m (comment)");
		assertTrue(!er.hasErrors());
	}

}
