package gov.nist.hit.ds.httpSoap;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent;
import gov.nist.hit.ds.soapSupport.SoapFaultException;

public class SoapFaultThrower implements ValComponent {
    
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

    
    public void run() throws SoapFaultException, RepositoryException {

    }

    
    public boolean showOutputInLogs() {
        return false;
    }
//	IAssertionGroup er;
//	Event event;
//
//	
//	public void setAssertionGroup(AssertionGroup er) {
//		this.er = er;
//	}
//
//	
//	public String getName() {
//		return getClass().getSimpleName();
//	}
//
//	
//	public String getDescription() {
//		return "SimComponent that throws a SOAP Fault";
//	}
//
//	
//	public void run(MessageValidatorEngine mve) throws SoapFaultException {
//		throw new SoapFaultException(er, FaultCode.ActionNotSupported, "This is a test");
//	}
//
//	
//	public void setName(String displayName) {
//
//	}
//
//	
//	public void setDescription(String description) {
//
//	}
//
//	
//	public void setEvent(Event event) {
//		this.event = event;
//	}
//
//	
//	public boolean showOutputInLogs() {
//		return false;
//	}

}
