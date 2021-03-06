package gov.nist.hit.ds.registryMetadataValidator.field;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
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


