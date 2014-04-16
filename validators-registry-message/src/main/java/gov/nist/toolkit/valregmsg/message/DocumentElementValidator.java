package gov.nist.toolkit.valregmsg.message;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;
import gov.nist.hit.ds.http.parser.MultipartParserBa;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valsupport.message.MessageValidator;

import java.util.List;

public class DocumentElementValidator extends MessageValidator {
	MessageValidatorEngine mvc;
	
	public DocumentElementValidator(ValidationContext vc, ErrorRecorderBuilder erBuilder, MessageValidatorEngine mvc) {
		super(vc);
		this.mvc = mvc;
	}

	public void run(IAssertionGroup er, MessageValidatorEngine mvc) {
		this.er = er;
		
		MessageValidator mcmv = mvc.findMessageValidator("MultipartContainer");
		if (mcmv == null) {
			er.detail("DocumentElementValidator: Document contents not available");
			return;
		}
		MultipartContainer mpc = (MultipartContainer) mcmv;
		
		MessageValidator mmv = mvc.findMessageValidator("MetadataContainer");
		if (mmv == null) {
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("DocumentElementValidator: cannot retrieve MetadataContainer class from validator stack", "Data not available"), this);
			return;
		}
		MetadataContainer mc = (MetadataContainer) mmv;
		
		MultipartParserBa mp = mpc.mp;
		Metadata m = mc.m;
		
		List<String> eoIds = m.getExtrinsicObjectIds();
	}

}
