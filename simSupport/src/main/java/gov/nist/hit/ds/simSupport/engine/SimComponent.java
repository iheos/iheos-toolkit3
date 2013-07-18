package gov.nist.hit.ds.simSupport.engine;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

/**
 * Identifies a class as a valsim. Two methods are required, setErrorRecorder 
 * and run.
 * @author bmajur
 *
 */
public interface SimComponent {
	void setErrorRecorder(ErrorRecorder er);
	String getName();
	void setName(String name);
	String getDescription();
	void setDescription(String description);
	void run(MessageValidatorEngine mve) throws SoapFaultException;
}
