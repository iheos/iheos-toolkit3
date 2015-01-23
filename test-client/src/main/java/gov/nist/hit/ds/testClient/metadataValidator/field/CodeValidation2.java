package gov.nist.hit.ds.testClient.metadataValidator.field;

import gov.nist.hit.ds.ebMetadata.Metadata;
import gov.nist.hit.ds.testClient.logging.ValidationContext;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;

//this gets invoked from both Validator.java and directly from Repository.  Should optimize the implementation so that codes.xml
//gets cached in memory.
public class CodeValidation2 extends CodeValidationBase {
	ValidationContext vc;

	public CodeValidation2(Metadata m, ValidationContext vc)  {
		super();
		this.m = m;
		this.vc = vc;

		try {
			loadCodes();
		} catch (XdsInternalException e) {
			startUpError = e;
		}
	}

	// this is used for easy access to mime lookup
	public CodeValidation2() throws XdsInternalException {
		super(1);
	}




}


