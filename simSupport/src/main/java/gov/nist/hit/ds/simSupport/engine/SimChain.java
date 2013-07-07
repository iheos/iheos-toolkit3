package gov.nist.hit.ds.simSupport.engine;

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
	Object base;
	List<SimStep> steps = new ArrayList<SimStep>();

	public SimChain setSteps(List<SimStep> steps) {
		this.steps = steps;
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
	
}
