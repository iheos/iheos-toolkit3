package gov.nist.hit.ds.registrySim.actor;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.registrySim.datatypes.RegistrySimTransactionType;
import gov.nist.hit.ds.simSupport.client.NoSimException;
import gov.nist.hit.ds.simSupport.datatypes.SimEndPoint;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.soapSupport.core.FaultCodes;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * This is the Document Registry actor simulator.  It must be configured in 
 * the SimChain before the relevant transaction SimComponent
 * @author bmajur
 *
 */
public class DocumentRegistryActorSim extends SimComponentBase {
	SimEndPoint simEndPoint;
	SimDb db;
	static Logger logger = Logger.getLogger(DocumentRegistryActorSim.class);

	@Inject
	public void setSimEndPoint(SimEndPoint simEndPoint) {
		this.simEndPoint = simEndPoint;
	}
	
	public SimDb getSimDb() {
		return db;
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		er.challenge("Document Registry Sim running");
		
		try {
			db = new SimDb(simEndPoint.getSimId(), simEndPoint.getActor(), simEndPoint.getTransaction());
		} catch (IOException e) {
			throw new SoapFaultException(
					er,
					FaultCodes.Receiver,
					new ErrorContext("Error loading Simulator <" + simEndPoint.getSimId() + ">"));
		} catch (NoSimException e) {
			throw new SoapFaultException(
					er,
					FaultCodes.Sender,
					new ErrorContext("Simulator <" + simEndPoint.getSimId() + "> does not exist"));
		}
	}
}
