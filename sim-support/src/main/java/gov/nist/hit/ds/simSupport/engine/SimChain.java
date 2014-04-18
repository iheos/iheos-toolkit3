package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.eventLog.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.eventLog.errorRecording.SystemErrorRecorder;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Define a simulator chain - mode up of
 * a base object and a list of simulator steps.
 * The base object is like other components except that its
 * execution is beyond the scope of the SimEngine.  It is used
 * to installRepositoryLinkage the chain with an outside object.  A good example
 * is a class that holds the parsed HTTP content from some external
 * package.
 * 
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

	public SimChain add(int index, SimStep ss) {
		((ArrayList<SimStep>)steps).add(index, ss);
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
			AssertionGroup er = step.getAssertionGroup();
			if (er != null && er.hasErrors())
				return true;
		}
		return false;
	}

	/**
	 * Get an informal list of errors - to support logging, not reporting
	 * @return
	 */
	public String getErrors() { return "There may be errors"; }


	public String getLog() {
		StringBuffer buf = new StringBuffer();
		buf.append("---------------------------------------------------------------\n");
		for (SimStep step : steps) {
			AssertionGroup er = step.getAssertionGroup();
			if (er == null) {
				throw new ToolkitRuntimeException("FATAL ERROR: Step <" + step.getName() + "> does not have an ErrorRecorder\n");
			}
			buf.append(er.toString());
			buf.append("---------------------------------------------------------------\n");
		}		
		return buf.toString();
	}
}
