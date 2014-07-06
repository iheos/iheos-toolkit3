package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.utilities.xml.XmlUtil;

import org.apache.axiom.om.OMElement;
/**
 * Stores registry errors. Also contains validator for the required
 * content of a RegistryError. The validator should be moved somewhere else.
 * @author bmajur
 *
 */
public class RegistryError {
	ErrorContext context = null;
	String errorCode = null;
	String severity = null;
	String location = null;
	boolean isWarning;
	
	public RegistryError setContext(ErrorContext context) {
		this.context = context;
		return this;
	}

	public RegistryError setContext(String context) {
		this.context = new ErrorContext(context);
		return this;
	}

	public RegistryError setErrorCode(String errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public RegistryError setSeverity(String severity) {
		this.severity = severity;
		return this;
	}

	public RegistryError setLocation(String location) {
		this.location = location;
		return this;
	}

	public void setWarning(boolean isWarning) {
		this.isWarning = isWarning;
	}

	public RegistryError() {
	}
	
	public OMElement toXML() {
		OMElement error = XmlUtil.om_factory.createOMElement("RegistryError", MetadataSupport.ebRSns3);
		error.addAttribute("codeContext", context.toString(), null);
		error.addAttribute("errorCode", errorCode, null);
		error.addAttribute("location", location, null);
		String severity;
		severity = MetadataSupport.error_severity_type_namespace + "Error";
		error.addAttribute("severity", severity, null);
		return error;
	}
	
	public RegistryError(OMElement xml) {
		if (xml  == null)
			return;
		
		context = new ErrorContext(xml.getAttributeValue(MetadataSupport.code_context_qname));
		errorCode = xml.getAttributeValue(MetadataSupport.error_code_qname);
		severity = xml.getAttributeValue(MetadataSupport.severity_qname);
		location = xml.getAttributeValue(MetadataSupport.location_qname);
		
		if (severity == null)
			severity = "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error";
	}
	
	
	public boolean isError() {
		if (severity == null)
			severity = "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error";
		return severity.endsWith("Error");
	}
	
	public String getErrorCode() { return errorCode; }
	public ErrorContext getCodeContext() { return context; }
	public boolean isWarning() { return isWarning; }
	public String getLocation() { return location; }

}
