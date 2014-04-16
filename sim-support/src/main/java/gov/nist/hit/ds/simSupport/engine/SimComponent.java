package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

/**
 * Identifies a class as a valsim. 
 * @author bmajur
 *
 */
public interface SimComponent {
	void setAssertionGroup(AssertionGroup ag);
	void setEvent(Event event);
	String getName();
	void setName(String name);
	String getDescription();
	void setDescription(String description);
	void run(MessageValidatorEngine mve) throws SoapFaultException, RepositoryException;
	boolean showOutputInLogs();
}
