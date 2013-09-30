package gov.nist.hit.ds.registrySim.actor;

import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

import org.apache.log4j.Logger;

/**
 * This is the Document Registry actor simulator.  It must be configured in 
 * the SimChain before the relevant transaction SimComponent. Its job
 * is to offer context to the transaction that is configured after it in the
 * SimChain.
 * @author bmajur
 *
 */
public class DocumentRegistryActorSim extends SimComponentBase {
	SimEndpoint simEndPoint;
	SimDb db;
	static Logger logger = Logger.getLogger(DocumentRegistryActorSim.class);

	@Inject
	public void setSimEndPoint(SimEndpoint simEndPoint) {
		this.simEndPoint = simEndPoint;
	}
	
	public SimDb getSimDb() {
		return db;
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		logger.trace("Run DocumentRegistryActorSim");
	}
}
