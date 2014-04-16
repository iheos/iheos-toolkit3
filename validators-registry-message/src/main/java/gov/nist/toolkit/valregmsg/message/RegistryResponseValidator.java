package gov.nist.toolkit.valregmsg.message;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.toolkit.registrymsgformats.registry.RegistryErrorList;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valsupport.message.MessageValidator;
import org.apache.axiom.om.OMElement;

import java.util.Arrays;
import java.util.List;

/**
 * Validate a RegistryResponse message.
 * @author bill
 *
 */
public class RegistryResponseValidator extends MessageValidator {
	OMElement xml;

	static List<String> statusValues = 
		Arrays.asList(
				MetadataSupport.response_status_type_namespace + "Success",
				MetadataSupport.ihe_response_status_type_namespace + "PartialSuccess",
				MetadataSupport.response_status_type_namespace + "Failure"
		);

	public RegistryResponseValidator(ValidationContext vc, OMElement xml) {
		super(vc);
		this.xml = xml;
	}

	public void run(IAssertionGroup er, MessageValidatorEngine mvc) {
		this.er = er;
		
		if (xml == null) {
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("RegistryResponseValidator: no RegistryResponse found", ""), this);
			return;
		}

		String longStatus = xml.getAttributeValue(MetadataSupport.status_qname);
		if (longStatus == null) {
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("RegistryResponseValidator: required attribute status is missing", "ebRS 3.0 Schema"), this);
			return;
		}

		if (!statusValues.contains(longStatus))
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("RegistryResponseValidator: status attribute must be one of these values: "
					+ statusValues + " found instead " + longStatus,
					"ITI TF-3: 4.1.13"), this);

		boolean isPartialSuccess = longStatus.endsWith(":PartialSuccess");
		boolean isSuccess = longStatus.endsWith(":Success");

		RegistryErrorList rel = new RegistryErrorList(MetadataSupport.firstChildWithLocalName(xml, "RegistryErrorList"));
		rel.validate(er, vc);

		boolean hasErrors = rel.hasError();		

		if (isPartialSuccess && !isPartialSuccessPermitted())
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("Status is PartialSuccess but this status not allowed on this transaction", "ITI TF-3: 4.1.13"), this);


		if (hasErrors && isSuccess)
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("RegistryResponse contains errors but status is Success", "ebRS 3.0 Section 2.1.6.2"), this);

		if (!isSuccess && !hasErrors)
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("Status attribute is " + longStatus + " but no errors are present", "ebRS 3.0 Section 2.1.3.2"), this);


	}

	boolean isPartialSuccessPermitted() {
		return (vc.isSQ && vc.isResponse) ||
		(vc.isRet && vc.isResponse);
	}

}
