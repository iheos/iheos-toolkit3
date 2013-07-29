package gov.nist.hit.ds.registrySim.actor;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.registrySim.datatypes.RegistrySimTransactionType;
import gov.nist.hit.ds.simSupport.client.NoSimException;
import gov.nist.hit.ds.simSupport.datatypes.SimEndPoint;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.soapSupport.core.FaultCodes;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class DocumentRegistryActorSim extends SimComponentBase {
	SimEndPoint simEndPoint;

	static Logger logger = Logger.getLogger(DocumentRegistryActorSim.class);
	
	@Inject
	public void setSimEndPoint(SimEndPoint simEndPoint) {
		this.simEndPoint = simEndPoint;
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		RegistrySimTransactionType transType = RegistrySimTransactionType.valueOf(simEndPoint.getTransaction());
		
		SimDb db;
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

		db.getTransInstances("foo");
		
		if (transType.equals(RegistrySimTransactionType.RB)) {
			
		} else {
			throw new SoapFaultException(
					er,
					FaultCodes.EndpointUnavailable,
					new ErrorContext("Transaction type <" + transType +"> not supported"));
		}
	}
}
