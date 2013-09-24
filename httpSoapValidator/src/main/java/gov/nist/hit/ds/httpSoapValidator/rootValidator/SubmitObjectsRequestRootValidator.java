package gov.nist.hit.ds.httpSoapValidator.rootValidator;

import gov.nist.hit.ds.eventLog.Event;

public class SubmitObjectsRequestRootValidator extends EbrsRootValidator {
	Event event;
	
	@Override
	String getExpectedRootName() {
		return "SubmitObjectsRequest";
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEvent(Event event) {
		this.event = event;
	}

}
