package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.ValidatorErrorItem;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.utilities.xml.Util;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.ExceptionUtil;
import gov.nist.hit.ds.xdsException.XDSMissingDocumentException;
import gov.nist.hit.ds.xdsException.XDSRepositoryMetadataException;
import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;

public class RegistryErrorListGenerator implements ErrorRecorder {
	boolean has_errors = false;
	boolean has_warnings = false;
	OMElement rel = null;
	protected OMNamespace ebRSns = MetadataSupport.ebRSns3;
	protected OMNamespace ebRIMns = MetadataSupport.ebRIMns3;
	protected OMNamespace ebQns = MetadataSupport.ebQns3;
	boolean format_for_html = false;
	private final static Logger logger = Logger.getLogger(RegistryErrorListGenerator.class);

	public String getString() {
		if (rel == null) return "Null";
		return getRegistryErrorList().toString();
	}

	public void format_for_html(boolean value) { this.format_for_html = value; }

	public boolean has_errors() {
		return has_errors;
	}

	public String getStatus() {
		if (has_errors())
			return "Failure";
		return "Success";
	}

	OMElement getRegistryErrorList() {
		if (rel == null)
			rel = XmlUtil.om_factory.createOMElement("RegistryErrorList", ebRSns);
		return rel;
	}

	public void setLocationPrefix(String prefix) {
		for (OMElement e : XmlUtil.decendentsWithLocalName(getRegistryErrorList(), "RegistryError")) {
			OMAttribute at = e.getAttribute(MetadataSupport.location_qname);
			if (at == null) {
				at = XmlUtil.om_factory.createOMAttribute("location", null, "");
				e.addAttribute(at);
			}
			at.setAttributeValue(prefix + at.getAttributeValue());
		}
	}

	static final QName codeContextQName = new QName("codeContext");

	public void add_error(String code, String msg, XdsException e) {
		addError(code, 
				new ErrorContext(msg, (e == null) ? null : e.getResource()), 
				(e == null) ? null : e.getDetails());
	}
	
	HashMap<String, String> getErrorDetails(OMElement registryError) {
		HashMap<String, String>  err = new HashMap<String, String>();

		for (@SuppressWarnings("unchecked")
		Iterator<OMAttribute> it=registryError.getAllAttributes(); it.hasNext(); ) {
			OMAttribute att = it.next();
			String name = att.getLocalName();
			String value = att.getAttributeValue();
			err.put(name, value);
		}

		return err;
	}

	public void addRegistryErrorsFromResponse(OMElement registryResponse) throws XdsInternalException {
		OMElement rel = XmlUtil.firstChildWithLocalName(registryResponse, "RegistryErrorList");
		if (rel != null)
			addRegistryErrorList(rel);
	}

	public void addRegistryErrorList(OMElement rel) throws XdsInternalException {
		addRegistryErrorList(rel, new ArrayList<String>());
	}

	void addRegistryErrorList(OMElement rel, List<String> errorCodesToFilter) throws XdsInternalException {
		for (@SuppressWarnings("unchecked")
		Iterator<OMElement> it=rel.getChildElements(); it.hasNext(); ) {
			OMElement registry_error = (OMElement) it.next();

			String code = registry_error.getAttributeValue(MetadataSupport.error_code_qname);
			if (errorCodesToFilter.contains(code))
				continue;

			OMElement registry_error_2 = Util.deep_copy(registry_error);

			logger.error("registry_error2 is \n" + registry_error_2.toString());

			registry_error_2.setNamespace(MetadataSupport.ebRSns3);
			getRegistryErrorList().addChild(registry_error_2);
			String severity = registry_error.getAttributeValue(MetadataSupport.severity_qname);
			severity = new Metadata().stripNamespace(severity);
			if (severity.equals("Error")) 
				has_errors = true;
			else
				has_warnings = true;
		}
	}


	public boolean hasContent() {
		return this.has_errors || this.has_warnings;
	}

	public void addError(String code, ErrorContext context, String location)  {
		if (context == null) context = new ErrorContext(null, null);
		if (code == null) code = "";
		if (location == null) location = "";
		OMElement error = XmlUtil.om_factory.createOMElement("RegistryError", ebRSns);
		error.addAttribute("codeContext", context.toString(), null);
		error.addAttribute("errorCode", code, null);
		error.addAttribute("location", location, null);
		String severity;
		severity = MetadataSupport.error_severity_type_namespace + "Error";
		error.addAttribute("severity", severity, null);
		getRegistryErrorList().addChild(error);
		this.has_errors = true;
	}


	public void delError(String context) {
		OMElement errs = getRegistryErrorList();

		for (@SuppressWarnings("unchecked")
		Iterator<OMElement> it = errs.getChildElements(); it.hasNext(); ) {
			OMElement e = it.next();
			if (context != null) {
				String ctx = e.getAttributeValue(codeContextQName);
				if (ctx != null && ctx.indexOf(context) != -1)
					e.detach();
				continue;
			}
		}
	}

	public void addWarning(String code, ErrorContext context, String location) {
		if (context == null) context = new ErrorContext(null, null);
		if (code == null) code = "";
		if (location == null) location = "";
		OMElement error = XmlUtil.om_factory.createOMElement("RegistryError", ebRSns);
		error.addAttribute("codeContext", context.toString(), null);
		error.addAttribute("errorCode", code, null);
		error.addAttribute("location", location, null);
		error.addAttribute("severity", MetadataSupport.error_severity_type_namespace + "Warning", null);
		getRegistryErrorList().addChild(error);
		has_warnings = true;
	}

	public static String exception_details(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
	}

	public void err(XDSMissingDocumentException e) {
		add_error("XDSMissingDocument", e.getMessage(), e);
	}

	public void err(XDSRepositoryMetadataException e) {
		add_error("XDSRepositoryMetadataError", e.getMessage(), e);
	}

	public void finish() {
	}

	public void sectionHeading(String msg) {
	}

	public void challenge(String msg) {
	}

	public void showErrorInfo() {
	}

	public void detail(String msg) {
		// TODO Auto-generated method stub

	}

	public void externalChallenge(String msg) {
		// TODO Auto-generated method stub

	}

	public void err(String code, ErrorContext errorContext, String location) {
		addError(code, errorContext, location);
	}

	public void err(Code code, ErrorContext errorContext, String location) {
		addError(code.name(), errorContext, location);

	}

	public void err(Code code, Exception e) {
		addError(code.name(), new ErrorContext(ExceptionUtil.exception_details(e), null), null);
	}

	public void err(Code code, ErrorContext errorContext, Object location) {
		String loc = "";
		if (location != null)
			loc = location.getClass().getSimpleName();
		addError(code.name(), errorContext, loc);
	}

	public boolean hasErrors() {
		return has_errors;
	}

	public void err(String code, ErrorContext errorContext, String location, String severity) {
		addError(code, errorContext, location + " - " + severity);
	}

	public void err(Code code, ErrorContext errorContext, String location, String severity) {
		if (severity != null && severity.equalsIgnoreCase("Warning"))
			addWarning(code.toString(), errorContext, location);
		else
			addError(code.name(), errorContext, location);
	}

	public void warning(Code code, ErrorContext errorContext, String location) {
		// TODO Auto-generated method stub
	}

	public ErrorRecorder buildNewErrorRecorder() {
		return this;
	}

	public int getNbErrors() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void concat(ErrorRecorder er) {
		// TODO Auto-generated method stub

	}

	public List<ValidatorErrorItem> getErrMsgs() {
		// TODO Auto-generated method stub
		return null;
	}

	public ErrorRecorderBuilder getErrorRecorderBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	public void success(String dts, String name, String found, String expected, String RFC) {
		// TODO Auto-generated method stub

	}

	public void error(String dts, String name, String found, String expected, String RFC) {
		// TODO Auto-generated method stub

	}

	public void warning(String dts, String name, String found, String expected, String RFC) {
		// TODO Auto-generated method stub

	}

	public void info(String dts, String name, String found, String expected, String RFC) {
		// TODO Auto-generated method stub

	}

	public void summary(String msg, boolean success, boolean part) {
		// TODO Auto-generated method stub

	}

	public void warning(String code, ErrorContext context, String location) {
		// TODO Auto-generated method stub
		
	}
}
