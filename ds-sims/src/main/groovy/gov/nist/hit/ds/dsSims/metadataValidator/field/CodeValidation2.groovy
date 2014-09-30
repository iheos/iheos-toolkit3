package gov.nist.hit.ds.dsSims.metadataValidator.field

import gov.nist.hit.ds.dsSims.client.ValidationContext
import gov.nist.hit.ds.metadata.Metadata
import gov.nist.hit.ds.xdsException.XdsInternalException;


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


