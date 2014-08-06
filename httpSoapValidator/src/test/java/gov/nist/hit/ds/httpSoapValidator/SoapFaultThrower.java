package gov.nist.hit.ds.httpSoapValidator;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.validationEngine.ValComponent;
import gov.nist.hit.ds.soapSupport.SoapFaultException;

public class SoapFaultThrower implements ValComponent {
    @Override
    public void setAssertionGroup(AssertionGroup ag) {

    }

    @Override
    public void setEvent(Event event) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {

    }

    @Override
    public void run() throws SoapFaultException, RepositoryException {

    }

    @Override
    public boolean showOutputInLogs() {
        return false;
    }
//	IAssertionGroup er;
//	Event event;
//
//	@Override
//	public void setAssertionGroup(AssertionGroup er) {
//		this.er = er;
//	}
//
//	@Override
//	public String getName() {
//		return getClass().getSimpleName();
//	}
//
//	@Override
//	public String getDescription() {
//		return "SimComponent that throws a SOAP Fault";
//	}
//
//	@Override
//	public void run(MessageValidatorEngine mve) throws SoapFaultException {
//		throw new SoapFaultException(er, FaultCode.ActionNotSupported, "This is a test");
//	}
//
//	@Override
//	public void setName(String name) {
//
//	}
//
//	@Override
//	public void setDescription(String description) {
//
//	}
//
//	@Override
//	public void setEvent(Event event) {
//		this.event = event;
//	}
//
//	@Override
//	public boolean showOutputInLogs() {
//		return false;
//	}

}
