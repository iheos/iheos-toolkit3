package gov.nist.toolkit.valregmsg.message;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valsupport.message.MessageValidator;
import org.apache.axiom.om.OMElement;

import java.util.List;

/**
 * Validate a RetrieveResponse message.
 * @author bill
 *
 */
public class RetrieveResponseValidator extends MessageValidator {
	OMElement xml;
	ErrorRecorderBuilder erBuilder;
	MessageValidatorEngine mvc;
	
	public RetrieveResponseValidator(ValidationContext vc, OMElement xml, ErrorRecorderBuilder erBuilder, MessageValidatorEngine mvc) {
		super(vc);
		this.xml = xml;
		this.erBuilder = erBuilder;
		this.mvc = mvc;
	}

	public void run(IAssertionGroup er, MessageValidatorEngine mvc) {
		this.er = er;
		
		if (xml == null) {
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("RetrieveDocumentSetResponse: top element null", ""), this);
			return;
		}
		
		OMElement registryResponse = MetadataSupport.firstChildWithLocalName(xml, "RegistryResponse");
		if (registryResponse == null)
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("RegistryResponse missing", "Schema"), this);
		else {
			mvc.addMessageValidator("RegistryResponse", new RegistryResponseValidator(vc, registryResponse), erBuilder.buildNewErrorRecorder());
		}

		List<OMElement> documentRequests = MetadataSupport.childrenWithLocalName(xml, "DocumentResponse");
		for (OMElement dr : documentRequests) {
			mvc.addMessageValidator("DocumentResponse element ordering", new RetrieveResponseOrderValidator(vc, dr), erBuilder.buildNewErrorRecorder());
			mvc.addMessageValidator("DocumentResponse Validator", new DocumentResponseValidator(vc, dr), erBuilder.buildNewErrorRecorder());
		}

	}

}
