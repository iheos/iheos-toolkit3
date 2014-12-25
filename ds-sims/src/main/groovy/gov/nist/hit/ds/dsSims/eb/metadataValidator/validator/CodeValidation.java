package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator;

import gov.nist.hit.ds.dsSims.eb.metadata.Metadata;
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.xdsException.XdsInternalException;

//this gets invoked from both Validator.java and directly from Repository.  Should optimize the implementation so that codes.xml
//gets cached in memory.
public class CodeValidation extends CodeValidationBase {
//	RegistryErrorListGenerator rel;       all errors to go through the ErrorRecorder interface
	ErrorRecorder er;
	boolean is_submit;
	boolean xds_b;

	public CodeValidation(Metadata m, boolean is_submit, boolean xds_b, ErrorRecorder rel) throws XdsInternalException {
		super(1);

		this.m = m;
//		this.rel = rel;
		this.is_submit = is_submit;
		this.xds_b = xds_b;
		
		er = rel;    // all errors to go through the ErrorRecorder interface
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


