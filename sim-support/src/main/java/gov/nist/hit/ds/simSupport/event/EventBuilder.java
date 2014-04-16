package gov.nist.hit.ds.simSupport.event;

import gov.nist.hit.ds.eventLog.Event;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.simrepo.SimRepoFactory;

import org.apache.log4j.Logger;

public class EventBuilder {
	static Logger logger = Logger.getLogger(EventBuilder.class);

	public Event buildEvent(SimId simId, String actorShortName, String transactionShortName) throws RepositoryException {
		Event event = null;
			RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
			Repository repos = fact.createNamedRepository(
					"SimLogs", 
					"SimLogs", 
					new SimpleType("simEventRepos"),               // repository type
					actorShortName + "-" + simId    // repository name
					);
			Asset eventAsset = repos.createAsset(
					SimRepoFactory.nowAsFilenameBase(), 
					transactionShortName + " Event", 
					new SimpleType("simEvent"));
			event = new Event(eventAsset);
			logger.debug("New Event asset <" + eventAsset.getId() + ">");
		return event;
		
	}

}
