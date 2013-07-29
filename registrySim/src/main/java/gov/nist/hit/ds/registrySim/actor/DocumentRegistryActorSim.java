package gov.nist.hit.ds.registrySim.actor;

import gov.nist.hit.ds.simSupport.datatypes.SimEndPoint;
import gov.nist.hit.ds.simSupport.engine.Inject;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;

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
		// TODO Auto-generated method stub
		
	}
}
