package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.xdsException.soapSupport.SoapFaultException;

public class MyComponent implements SimComponent {
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
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		
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