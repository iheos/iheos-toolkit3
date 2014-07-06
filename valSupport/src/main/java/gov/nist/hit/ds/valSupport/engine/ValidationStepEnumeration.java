package gov.nist.hit.ds.valSupport.engine;

import java.util.List;
import java.util.Enumeration;

public class ValidationStepEnumeration implements Enumeration<ValidationStep> {
	List<ValidationStep> steps;
	int i = 0;
	
	ValidationStepEnumeration(List<ValidationStep> steps) {
		this.steps = steps;
	}

	public boolean hasMoreElements() {
		return i < steps.size();
	}

	public ValidationStep nextElement() {
		ValidationStep v = steps.get(i);
		i++;
		return v;
	}
}
