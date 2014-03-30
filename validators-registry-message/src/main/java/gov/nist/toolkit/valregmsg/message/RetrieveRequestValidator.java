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
 * Validate a RetreiveDocumentSetRequest message.
 * @author bill
 *
 */
public class RetrieveRequestValidator  extends MessageValidator {
	OMElement xml;
	ErrorRecorderBuilder erBuilder;
	MessageValidatorEngine mvc;

	public RetrieveRequestValidator(ValidationContext vc, OMElement xml, ErrorRecorderBuilder erBuilder, MessageValidatorEngine mvc) {
		super(vc);
		this.xml = xml;
		this.erBuilder = erBuilder;
		this.mvc = mvc;
	}

	public void run(IAssertionGroup er, MessageValidatorEngine mvc) {
		this.er = er;
		
		if (xml == null) {
			er.err(XdsErrorCode.Code.XDSRepositoryError, new ErrorContext("RetrieveDocumentSetRequest: top element null", ""), this);
			return;
		}
		

		List<OMElement> documentRequests = MetadataSupport.childrenWithLocalName(xml, "DocumentRequest");
		for (OMElement dr : documentRequests) {
			mvc.addMessageValidator("DocumentRequest element ordering", new RetrieveOrderValidator(vc, dr), erBuilder.buildNewErrorRecorder());
			if (vc.isXC) {
				OMElement homeElement = MetadataSupport.firstChildWithLocalName(dr, "HomeCommunityId");
				if (homeElement == null) {
					er.err(XdsErrorCode.Code.XDSMissingHomeCommunityId, new ErrorContext("Cross-Community Retrieve request must include homeCommunityId", "ITI TF-2b: 3.39.1"), this);
				}
				else  {
					String homeValue = homeElement.getText();
					if (!homeValue.startsWith("urn:oid:"))
						er.err(XdsErrorCode.Code.XDSRepositoryError, new ErrorContext("HomeCommunityId must have urn:oid: prefix", "ITI TF-2b: 3.38.4.1.2.1"), this);
				} 
			}
		}

	}

}
