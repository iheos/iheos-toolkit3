package gov.nist.hit.ds.registryMetadataValidator.field;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.xdsException.XdsInternalException;


//this gets invoked from both Validator.java and directly from Repository.  Should optimize the implementation so that codes.xml
//gets cached in memory.
public class CodeValidation extends CodeValidationBase {
	IAssertionGroup er;
	boolean is_submit;
	boolean xds_b;

	public CodeValidation(Metadata m, boolean is_submit, boolean xds_b, IAssertionGroup er) throws XdsInternalException {
		super(1);

		this.m = m;
		this.is_submit = is_submit;
		this.xds_b = xds_b;
		
		this.er = er; 
	}
	
	public CodeValidation(Metadata m) {
		super();
		this.m = m;
		is_submit = true;
		xds_b = true;
	}

	// this is used for easy access to mime lookup
	public CodeValidation() throws XdsInternalException {
		super();
	}
	
	public void run() {
		run(er);
	}



}


