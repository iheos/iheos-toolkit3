package gov.nist.hit.ds.valSupport.engine;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.valSupport.message.AbstractMessageValidator;
import gov.nist.hit.ds.valSupport.message.NullMessageValidator;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Maintain collection of validation steps and run them on request.  New steps are added to the end of the list. 
 * Calling run will execute all un-executed steps in the order they are queued up. New
 * steps can be added any time and will be executed by the next call to the run method.
 * @author bill
 *
 */
public class MessageValidatorEngine {
	static Logger logger = Logger.getLogger(MessageValidatorEngine.class);

	
	public MessageValidatorEngine() {}
	

	List<ValidationStep> validationSteps = new ArrayList<ValidationStep>();
	
	public boolean hasErrors() {
		boolean error = false;
		for (ValidationStep vs : validationSteps) {
			if (vs.hasErrors())
				return true;
		}
		return false;
	}

	public int getErroredStepCount() {
		int cnt = 0;
		for (ValidationStep vs : validationSteps) {
			if (vs.hasErrors())
				if (vs.er.hasErrors())
				cnt++;
		}
		return cnt;
	}

	/**
	 * Get last validation step in the queue.
	 * @return
	 */
	public ValidationStep getLastValidationStep() {
		int n = validationSteps.size();
		if (n == 0)
			return null;
		return validationSteps.get(n-1);
	}

	
	public Enumeration<ValidationStep> getValidationStepEnumeration() {
		return new ValidationStepEnumeration(validationSteps);
	}
	
	public AbstractMessageValidator findMessageValidator(String className) {
		for (ValidationStep vs : validationSteps) {
			AbstractMessageValidator mv = vs.validator;
			if (mv == null)
				continue;
			String clasname = mv.getClass().getName();
			if (clasname.endsWith(className))
				return mv;
		}
		return null;
	}

	/**
	 * Get the names of all the validators in the queue.
	 * @return List<String>
	 */
	public List<String> getValidatorNames() {
		List<String> names = new ArrayList<String>();
		for (ValidationStep vs : validationSteps) {
			AbstractMessageValidator mv = vs.validator;
			if (mv == null)
				continue;
			String clasname = mv.getClass().getName();
			int lastdot = clasname.lastIndexOf('.');
			if (lastdot > 0)
				clasname = clasname.substring(lastdot+1);
			names.add(clasname);
		}
		return names;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("MessageValidatorContext:\n");

		for (ValidationStep step : validationSteps) {
			buf.append(step.toString()).append("\n");
		}

		return buf.toString();
	}

	/**
	 * Add a new message validator to the validation queue.
	 * @param stepName Name of the validation step
	 * @param v the validator
	 * @param er its private ErrorRecorder
	 * @return the ValidationStep structure which is used internally to the engine
	 */
	public ValidationStep addMessageValidator(String stepName, AbstractMessageValidator v, IAssertionGroup er) {
		ValidationStep step = new ValidationStep();
		step.stepName = stepName;
		step.validator = v;
		step.er = er;
		validationSteps.add(step);
		logger.debug("MVC: Adding: " + v.getClass().getSimpleName());
		return step;
	}
	
	public ValidationStep addMessageValidator(String stepName, AbstractMessageValidator v) {
		ValidationStep step = new ValidationStep();
		step.stepName = stepName;
		step.validator = v;
		step.er = v.getErrorRecorder();
		validationSteps.add(step);
		logger.debug("MVC: Adding: " + v.getClass().getSimpleName());
		return step;
	}
	
	/**
	 * Short cut way to add an ErrorRecorder to the output stream without performing any validation.
	 * @param stepName name of the validation step
	 * @param er its private ErrorRecorder
	 */
	public void addErrorRecorder(String stepName, IAssertionGroup er) {
		ValidationStep step = addMessageValidator(stepName, new NullMessageValidator(new ValidationContext(), er), er);
		step.ran = true;
	}
	
	/**
	 * Execute all validators that are queued up but never run.
	 */
	public void run() {
		// this iteration is tricky since a step can add
		// new steps. This causes ConcurrentModificationException
		// if typical iterator is used.
		for (int i=0; i<validationSteps.size(); i++ ) {
			ValidationStep step = validationSteps.get(i);
			if (step.ran)
				continue;
//			System.out.println("printf: MVC: Running: " + step.validator.getClass().getSimpleName());
			logger.debug("MVC: Running: " + step.validator.getClass().getSimpleName());
			step.ran = true;
// 			step.validator.run(step.er, this);
			step.validator.run(this);
		}
		
	}

	public int getValidationStepCount() {
		return validationSteps.size();
	}

	public ValidationStep getValidationStep(int i) {
		return validationSteps.get(i);
	}
}
