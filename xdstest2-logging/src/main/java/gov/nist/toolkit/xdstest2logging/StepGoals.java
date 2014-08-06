package gov.nist.toolkit.xdstest2logging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StepGoals implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5885436255799603133L;
	public String stepName;
	public List<String> goals;
	
	public StepGoals(String stepName) {
		this.stepName = stepName;
		goals = new ArrayList<String>();
	}
}
