package gov.nist.hit.ds.simSupport.components;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent;
import gov.nist.hit.ds.soapSupport.SoapFaultException;

public class ActorSimEnvironment  implements ValComponent {
	private String actorCode;
	private String transCode;
	private String simId;
	
	public ActorSimEnvironment(String actorCode, String transCode, String simId) {
		this.actorCode = actorCode;
		this.transCode = transCode;
		this.simId = simId;
	}

	public String getActorCode() {
		return actorCode;
	}

	public String getTransCode() {
		return transCode;
	}

	public String getSimId() {
		return simId;
	}

	public void setAssertionGroup(AssertionGroup ag) {
	}

	public void setEvent(Event event) {
	}

	public String getName() {
		return null;
	}

	public void setName(String name) {
	}

	public String getDescription() {
		return null;
	}

	public void setDescription(String description) {
	}

	public void run() throws SoapFaultException,
			RepositoryException {
	}

	public boolean showOutputInLogs() {
		return false;
	}
}
