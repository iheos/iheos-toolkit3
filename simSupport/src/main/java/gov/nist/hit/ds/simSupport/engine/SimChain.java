package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.SystemErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.ValidatorErrorItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Define a simulator chain - mode up of
 * a base object and a list of simulator steps.
 * @author bmajur
 *
 */
public class SimChain  {
	Object base = null;
	List<SimStep> steps = new ArrayList<SimStep>();

	public SimChain setSteps(List<SimStep> steps) {
		this.steps.addAll(steps);
		return this;
	}
	
	public List<SimStep> getSteps() {
		return steps;
	}

	public SimChain add(SimStep ss) {
		steps.add(ss);
		return this;
	}

	public Object getBase() {
		return base;
	}

	public SimChain setBase(Object base) {
		this.base = base;
		return this;
	}

	public Iterator<SimStep> iterator() {
		return steps.iterator();
	}

	public boolean hasErrors() {
		for (SimStep step : steps) {
			ErrorRecorder er = step.getErrorRecorder(); 
			if (er != null && er.hasErrors())
				return true;
		}
		return false;
	}

	public String getError() {
		for (SimStep step : steps) {
			ErrorRecorder er = step.getErrorRecorder(); 
			if (er != null && er.hasErrors()) {
				List<ValidatorErrorItem> ei = er.getErrMsgs();
				if (ei.size() > 0)
					return ei.get(0).msg;
			}
		}
		return null;
	}

	public String getLog() {
		StringBuffer buf = new StringBuffer();
		buf.append("---------------------------------------------------------------\n");
		for (SimStep step : steps) {
			ErrorRecorder er = step.getErrorRecorder();
			if (!(er instanceof SystemErrorRecorder))
				continue;
			SystemErrorRecorder ser = (SystemErrorRecorder) er;
			buf.append(ser.toString());
			buf.append("---------------------------------------------------------------\n");
		}		
		return buf.toString();
	}
}
