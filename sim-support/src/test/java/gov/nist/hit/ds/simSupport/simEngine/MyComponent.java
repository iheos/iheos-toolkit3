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

	@Override
	public void setAssertionGroup(AssertionGroup er) {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void run() throws SoapFaultException {
		
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public boolean showOutputInLogs() {
		return false;
	}
	
}