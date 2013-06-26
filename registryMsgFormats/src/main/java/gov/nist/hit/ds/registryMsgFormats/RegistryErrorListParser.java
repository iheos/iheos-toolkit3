package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.valSupport.client.ValidationContext;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;

public class RegistryErrorListParser {
	List<RegistryError> errors = new ArrayList<RegistryError>();
	
	public RegistryErrorListParser(OMElement registryErrorListEle) {
		
		if (registryErrorListEle == null)
			return;
		
		if (!registryErrorListEle.getLocalName().equals("RegistryErrorList")) {
			registryErrorListEle = XmlUtil.firstDecendentWithLocalName(registryErrorListEle, "RegistryErrorList");
			if (registryErrorListEle == null)
				return;
		}
		
		for (OMElement registry_error : XmlUtil.childrenWithLocalName(registryErrorListEle, "RegistryError")) {
			RegistryError registryError = new RegistryError();
			errors.add(registryError);
			String severity = get_att(registry_error, "severity");

			if (severity == null)
				registryError.isWarning = false;
			else if (severity.endsWith("Warning"))
				registryError.isWarning = true;
			else
				registryError.isWarning = false;
			
			registryError.setContext(get_att(registry_error, "codeContext"));
			
			registryError.setErrorCode(get_att(registry_error, "errorCode"));

			registryError.setLocation(get_att(registry_error, "location"));

		}

	}
	
	String get_att(OMElement ele, String name) {
		OMAttribute att = ele.getAttribute(new QName(name));
		if (att == null)
			return null;
		return att.getAttributeValue();

	}

	public boolean hasError() {
		for (RegistryError e : errors) {
			if (e.isError())
				return true;
		}
		return false;
	}
	
	public void validate(ErrorRecorder er, ValidationContext vc) {
		for (RegistryError re : errors) {
			validate(re, er, vc);
		}
	}

	public void validate(RegistryError re, ErrorRecorder er, ValidationContext vc) {
		if (re.getCodeContext() == null)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext("CodeContext attribute is required", "ebRS 3.0 Section 2.1.6"), this);
		if (re.errorCode == null)
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext("ErrorCode attribute is required", "ebRS 3.0 Section 2.1.6"), this);
		if (!re.severity.startsWith("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:"))
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext("Severity attribute value must have prefix urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:", "ebRS 3.0 Section 2.1.6"), this);
		
		if (vc.isXC && (vc.isRet || vc.isSQ) && vc.isResponse) {
			if (re.location == null)
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext("The location attribute must be set to the homeCommunityId of Responding Gateway", "ITI TF-2b: 3.39.4.1.3"), this);
			else if (!re.location.startsWith("urn:oid:"))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, new ErrorContext("RegistryError: The location attribute contains an invalid homeCommunityId, it must have the prefix urn:oid:, value found was " + re.location, "ITI TF-2b: 3.38.4.1.2.1"), this);
		}
	}

	public List<RegistryError> getRegistryErrorList() { return errors; }
}
