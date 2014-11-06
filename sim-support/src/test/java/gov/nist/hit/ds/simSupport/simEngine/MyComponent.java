package gov.nist.hit.ds.simSupport.simEngine;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent;
import gov.nist.hit.ds.soapSupport.SoapFaultException;

public class MyComponent implements ValComponent {
	String name;
	String description;
	String myStuff;
	Event event;

	public String getMyStuff() {
		return myStuff;
	}

	public void setMyStuff(String myStuff) {
		this.myStuff = myStuff;
	}

	public void setAssertionGroup(AssertionGroup er) {
	}

	public String getName() {
		return name;
	}

	public void run() throws SoapFaultException {
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public boolean showOutputInLogs() {
		return false;
	}
	
}