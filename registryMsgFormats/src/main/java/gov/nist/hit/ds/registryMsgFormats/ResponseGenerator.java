package gov.nist.hit.ds.registryMsgFormats;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.client.ValidationStepResult;
import gov.nist.hit.ds.errorRecording.client.ValidatorErrorItem;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.utilities.xml.OMFormatter;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;
import gov.nist.toolkit.registrysupport.MetadataSupport;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

import java.util.List;

public abstract class ResponseGenerator extends SimComponentBase {
	boolean isXCA = false;
	protected OMNamespace ebRSns =  MetadataSupport.ebRSns3;
	protected OMNamespace ebRIMns = MetadataSupport.ebRIMns3;
	protected OMNamespace ebQns = MetadataSupport.ebQns3;

	String forcedStatus = null;

	boolean errors_and_warnings_included = false;

	//String errors_and_warnings = "";
	//boolean has_errors = false;

	public abstract OMElement getRoot();
	abstract public void addQueryResults(OMElement metadata) throws ToolkitRuntimeException;

	public OMElement response = null;

	OMElement content = null;
	public RegistryErrorListGenerator registryErrorList;

	public IAssertionGroup getErrorRecorder() { return registryErrorList; }

	public RegistryErrorListGenerator getRegistryErrorList() { return registryErrorList; }

	public void setIsXCA() { 
		isXCA = true;
	}

	public String getStatus() throws ToolkitRuntimeException { 
		if (response != null) {
			String status = response.getAttributeValue(MetadataSupport.status_qname);
			if (status == null)
				throw new ToolkitRuntimeException("status not yet set");
			else
				return status;
		}
		else
			throw new ToolkitRuntimeException("Message not yet formed");
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("Response:\n");
//		buf.append("\tquery_results is ").append(query_results).append("\n");
		buf.append("\tregistryErrorList is ").append(registryErrorList).append("\n");

		return buf.toString();
	}

	public ResponseGenerator() {
		registryErrorList = new RegistryErrorListGenerator();
	} 

	public ResponseGenerator(RegistryErrorListGenerator rel)  throws ToolkitRuntimeException {
		registryErrorList = rel;		
	}

	public void addErrors(List<ValidationStepResult> results) throws ToolkitRuntimeException {

		for (ValidationStepResult vsr : results) {
			for (ValidatorErrorItem vei : vsr.er) {
				if (vei.level == ValidatorErrorItem.ReportingLevel.ERROR) {
					String msg = vei.msg;
					if (vei.resource != null && !vei.resource.equals(""))
						msg = msg + " (" + vei.resource + ")";
					registryErrorList.addError(vei.getCodeString(), new ErrorContext(msg, null), null);
				}
				if (vei.level == ValidatorErrorItem.ReportingLevel.WARNING) {
					String msg = vei.msg;
					if (vei.resource != null && !vei.resource.equals(""))
						msg = msg + " (" + vei.resource + ")";
					registryErrorList.addWarning(vei.getCodeString(), new ErrorContext(msg, null), null);
				}
			}
		}	
	}

	public void setForcedStatus(String status) {
		forcedStatus = status;
	}

	public OMElement getResponse()  {
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

		return getRoot();
		//		return response;
	}
	
	public String getResponseAsString() {
		return new OMFormatter(getResponse()).toString();
	}

	public void add_error(String code, ErrorContext errorContext, String location) {
		registryErrorList.addError(code, errorContext, location);
	}

	public void addRegistryErrorList(OMElement rel) throws ToolkitRuntimeException {
		registryErrorList.addRegistryErrorList(rel);
	}

	public void add(RegistryErrorListGenerator rel) throws ToolkitRuntimeException {
		registryErrorList.addRegistryErrorList(rel.getRegistryErrorList());
	}

	public void add_warning(String code, ErrorContext errorContext, String location) {
		registryErrorList.addWarning(code, errorContext, location);
	}

	public boolean has_errors() {
		return registryErrorList.has_errors();
	}




}
