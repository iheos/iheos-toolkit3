package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator;

import gov.nist.hit.ds.ebMetadata.Metadata;
import gov.nist.hit.ds.toolkit.environment.Environment;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;

//this gets invoked from both Validator.java and directly from Repository.  Should optimize the implementation so that codes.xml
//gets cached in memory.

// TODO:  hook in as real validator
public class CodeValidation extends CodeValidationBase {
	boolean is_submit;
	boolean xds_b;

	public CodeValidation(Metadata m, Environment environment, boolean is_submit, boolean xds_b) throws XdsInternalException {
		super(environment);

		this.m = m;
		this.is_submit = is_submit;
		this.xds_b = xds_b;
		
	}
	
	public CodeValidation(Metadata m, Environment environment) {
		super(environment);
		this.m = m;
		is_submit = true;
		xds_b = true;
	}

	public void run() {
//		run(er);
	}




}


