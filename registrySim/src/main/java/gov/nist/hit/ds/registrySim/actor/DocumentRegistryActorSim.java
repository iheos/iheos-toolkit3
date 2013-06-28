package gov.nist.hit.ds.registrySim.actor;

import gov.nist.hit.ds.simSupport.GenericSim;

import org.apache.log4j.Logger;

public class DocumentRegistryActorSim implements GenericSim {

	static Logger logger = Logger.getLogger(DocumentRegistryActorSim.class);

	public DocumentRegistryActorSim() {
		started();
	}
	
	public void started() {
		logger.info(getClass().getName() + " simulator started");
		System.out.println(getClass().getName() + " simulator started");
	}
	
	public String getState() {
		return "running";
	}
}
