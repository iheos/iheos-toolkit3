package gov.nist.hit.ds.simSupport.simChain

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup
import gov.nist.hit.ds.simSupport.engine.SimStep;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

/**
 * Define a simulator chain - made up of
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

    def init(Event event) { steps.each { it.init(event) } }

	def addSteps(List<SimStep> steps) { this.steps.addAll(steps) }
	
	def addStep(SimStep ss) { steps.add(ss) }

	def addStep(int index, SimStep ss) { ((ArrayList<SimStep>)steps).add(index, ss) }

    SimStep getRunableStep() { steps.find { !it.completed } }

    boolean isDone() { hasErrors() || getRunableStep() == null }

    List<Object> getComponents() { steps.collect { it.simComponent } }

	public boolean hasErrors() { steps.find { it.getAssertionGroup()?.hasErrors() } }

    public boolean hasInternalErrors() { steps.find { it.getAssertionGroup()?.hasInternalError() }}

    public String getStepStatusString() {
        StringBuffer buf = new StringBuffer()
        buf.append('Name').append('\t').append('Completed').append('\n')
        buf.append('----').append('\t').append('---------').append('\n')

        steps.each {
            buf.append(it.simComponent.name).append('\t').append(it.completed).append('\n')
        }

        return buf.toString()
    }

	/**
	 * Get an informal list of errors - to support logging, not reporting
	 * @return
	 */
	List<String> getErrorMessages() {
        List<String> msgs = []
        steps.each { step ->
            msgs.addAll(step.getAssertionGroup().getErrorMessages())
        }
        return msgs
    }


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
