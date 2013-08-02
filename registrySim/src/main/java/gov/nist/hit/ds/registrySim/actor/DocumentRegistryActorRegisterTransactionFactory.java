package gov.nist.hit.ds.registrySim.actor;

import gov.nist.hit.ds.actorSim.factory.ActorFactory;
import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.httpSoapValidator.Launcher;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.FaultCodes;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

import org.apache.log4j.Logger;

/**
 * Factory for launching an instance of the Registry Actor / Register Transaction.
 * @author bmajur
 *
 */
public class DocumentRegistryActorRegisterTransactionFactory extends SimComponentBase implements ActorFactory  {
	static final Logger logger = Logger.getLogger(DocumentRegistryActorRegisterTransactionFactory.class);

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		try {
			new Launcher().launch("xdsRegistryRegisterSim.properties");
		} catch (Exception e) {
			logger.error("Error launching SimChain for Document Registry Actor, Register Transaction",  e);
			throw new SoapFaultException(
					er,
					FaultCodes.Receiver,
					new ErrorContext(e.getMessage())
					);
		} 
	}

}
