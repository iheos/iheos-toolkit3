package gov.nist.hit.ds.errorRecording.client;



import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Collects together a step name and a collection of ValidatorErrorItems so that
 * a single object represents the error/status output of a validation step.
 * @author bill
 *
 */
public class ValidationStepResult implements IsSerializable {
	public String stepName;
	public List<ValidatorErrorItem> er;
	
	public ValidationStepResult() {} // For GWT
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		for (ValidatorErrorItem item : er)
			buf.append(item).append("\n");
		return buf.toString();
	}
	
}
