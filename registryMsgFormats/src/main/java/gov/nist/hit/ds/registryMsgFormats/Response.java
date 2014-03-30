package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.ValidationStepResult;
import gov.nist.hit.ds.errorRecording.client.ValidatorErrorItem;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import gov.nist.toolkit.registrysupport.logging.ErrorLogger;
import gov.nist.toolkit.registrysupport.logging.LogMessage;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

import java.util.ArrayList;
import java.util.List;

public abstract class Response implements ErrorLogger {
	public final static short version_2 = 2;
	public final static short version_3 = 3;
	public short version;
	boolean isXCA = false;
	//String status = "Success";
	protected OMNamespace ebRSns;
	protected OMNamespace ebRIMns;
	protected OMNamespace ebQns;
	String forcedStatus = null;

	ArrayList query_results = null;

	boolean errors_and_warnings_included = false;

	//String errors_and_warnings = "";
	//boolean has_errors = false;

	public abstract OMElement getRoot() throws XdsInternalException;

	public OMElement response = null;

	OMElement content = null;
	public RegistryErrorListGenerator registryErrorList;

	public IAssertionGroup getErrorRecorder() { return registryErrorList; }

	public RegistryErrorListGenerator getRegistryErrorList() { return registryErrorList; }

	public void setIsXCA() { 
		isXCA = true;
	}

	public String getStatus() throws XdsInternalException { 
		if (response != null) {
			String status = response.getAttributeValue(MetadataSupport.status_qname);
			if (status == null)
				throw new XdsInternalException("status not yet set");
			else
				return status;
		}
		else
			throw new XdsInternalException("Message not yet formed");
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("Response:\n");
		buf.append("\tversion is ").append(version).append("\n");
		buf.append("\tquery_results is ").append(query_results).append("\n");
		buf.append("\tregistryErrorList is ").append(registryErrorList).append("\n");

		return buf.toString();
	}

	public Response(short version)  {
		init(version, new RegistryErrorListGenerator());
	} 

	public Response() throws XdsInternalException {
		init(version_3, new RegistryErrorListGenerator());
	} 

	public Response(short version, RegistryErrorListGenerator rel)  throws XdsInternalException {
		init(version, rel);
	}

	public Response(RegistryErrorListGenerator rel)  throws XdsInternalException {
		init(version_3, rel);
	}

	public void addErrors(List<ValidationStepResult> results) throws XdsInternalException {

		for (ValidationStepResult vsr : results) {
			for (ValidatorErrorItem vei : vsr.er) {
				if (vei.level == ValidatorErrorItem.ReportingLevel.ERROR) {
					String msg = vei.msg;
					if (vei.resource != null && !vei.resource.equals(""))
						msg = msg + " (" + vei.resource + ")";
					registryErrorList.addError(vei.getCodeString(), new ErrorContext(msg, null), "");
				}
				if (vei.level == ValidatorErrorItem.ReportingLevel.WARNING) {
					String msg = vei.msg;
					if (vei.resource != null && !vei.resource.equals(""))
						msg = msg + " (" + vei.resource + ")";
					registryErrorList.addWarning(vei.getCodeString(), new ErrorContext(msg, null), "");
				}
			}
		}	
	}



	void init(short version, RegistryErrorListGenerator rel)   {
		this.version = version;
		if (version == version_2) {
			ebRSns =  MetadataSupport.ebRSns2;  
			ebRIMns = MetadataSupport.ebRIMns2;
			ebQns = MetadataSupport.ebQns2;
		} else {
			ebRSns =  MetadataSupport.ebRSns3;
			ebRIMns = MetadataSupport.ebRIMns3;
			ebQns = MetadataSupport.ebQns3;
		}

		registryErrorList = rel;		

	}

	abstract public void addQueryResults(OMElement metadata) throws XdsInternalException;

	public void setForcedStatus(String status) {
		forcedStatus = status;
	}

	public OMElement getResponse()  throws XdsInternalException {
		if (version == version_2) {
			response.addAttribute("status", registryErrorList.getStatus(), null);
			if (registryErrorList.hasContent()) {
				OMElement error_list = registryErrorList.getRegistryErrorList();
				if (error_list != null)
					response.addChild(error_list);
			}
			return response;
		}
		if (registryErrorList.hasContent()) {
			OMElement error_list = registryErrorList.getRegistryErrorList();
			if (error_list != null)
				response.addChild(error_list);
		}

		if (forcedStatus != null) {
			response.addAttribute("status", forcedStatus, null);
		} else {
			response.addAttribute("status", MetadataSupport.response_status_type_namespace + registryErrorList.getStatus(), null);
		}

		setLocationForXCA();

		//			if (this instanceof RetrieveMultipleResponse) {
		//				return ((RetrieveMultipleResponse) this).rdsr;
		//			} 
		//			else if (this instanceof RegistryResponse) {
		//				
		//			} 
		//			else if (this instanceof AdhocQueryResponse) {
		//				AdhocQueryResponse a = (AdhocQueryResponse) this;
		//				OMElement query_result = a.getQueryResult();
		//				if (query_result != null)
		//					response.addChild(query_result);
		//			} else {
		//				throw new XdsInternalException("Response.getResponse(): unknown extending class: " + getClass().getName());
		//			}

		return getRoot();



//		return response;
	}

	void setLocationForXCA() {
	}

	public void add_error(String code, String msg, String location, String resource, LogMessage log_message) {
		registryErrorList.addError(code, new ErrorContext(msg, resource), location);
	}

//	public void addRegistryErrorList(OMElement rel, LogMessage log_message) throws XdsInternalException {
//		registryErrorList.addRegistryErrorList(rel);
//	}
//
//	public void add(RegistryErrorListGenerator rel, LogMessage log_message) throws XdsInternalException {
//		registryErrorList.addRegistryErrorList(rel.getRegistryErrorList());
//	}
//
//	public void add_warning(String code, String msg, String location, LogMessage log_message) {
//		registryErrorList.addWarning(code, new ErrorContext(msg, location), location);
//	}
//
//	public String getErrorsAndWarnings() {
//		return registryErrorList.getErrorsAndWarnings();
//	}

//	public void error(String msg) {
//		registryErrorList.error(msg);
//	}
//
//	public void warning(String msg) {
//		registryErrorList.warning(msg);
//	}
//
//	public boolean has_errors() {
//		return registryErrorList.has_errors();
//	}




}
