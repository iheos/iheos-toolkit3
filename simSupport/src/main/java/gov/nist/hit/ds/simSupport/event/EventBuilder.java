package gov.nist.hit.ds.simSupport.event;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.sim.SimDb;

public class EventBuilder {

	public Event buildEvent(SimId simId, String actorShortName, String transactionShortName) throws RepositoryException {
		Event event = null;
			SimDb db = new SimDb(simId);
			RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
			Repository repos = fact.createNamedRepository(
					"Event_Repository", 
					"Event Repository", 
					new SimpleType("simEventRepos"),               // repository type
					ActorType.findActor(actorShortName) + "-" + simId    // repository name
					);
			Asset eventAsset = repos.createAsset(
					db.nowAsFilenameBase(), 
					transactionShortName + " Event", 
					new SimpleType("simEvent"));
			event = new Event(eventAsset);
		return event;
		
	}

}
