package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.IdFactory;
import gov.nist.hit.ds.repositoryDirectory.Directory;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.serializer.SimulatorSerializer;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.siteManagement.repository.SiteRepository;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Build a simulator.
 * 
 * Typical calling sequence is:
<pre>
	// Create empty Simulator
	SimulatorFactory sf = new SimulatorFactory();
	// Set the simulator ID
	sf.buildSimulator(SimId);
	// Add an Actor Simulator to it
	sf.addActorSim(ActorType.REGISTRY);
	// Persist it
	sf.save();
</pre>
 * @author bmajur
 *
 */
public class SimulatorFactory {
	static Logger logger = Logger.getLogger(SimulatorFactory.class);
	Simulator sim;
	SimId simId;
//	List<ActorFactory> actorFactories = new ArrayList<ActorFactory>();
	Site site = new Site();
	int actorSimsAdded = 0;

	// Default settings
	TlsType[] tlsTypes = new TlsType[] { TlsType.NOTLS, TlsType.TLS }; 
	AsyncType[] asyncTypes = new AsyncType[] { AsyncType.SYNC, AsyncType.ASYNC }; 


	public SimulatorFactory buildSimulator(SimId simId) {
		this.simId = simId;
		sim = new Simulator(simId);
		return this;
	}
	
	/**
	 * Build Empty Simulator that actorSims can be added to.
	 * @return
	 * @throws RepositoryException
	 */
	public SimulatorFactory buildSimulator() throws RepositoryException {
		return buildSimulator(new SimId(new IdFactory().getNewId().getIdString()));
	}

	/**
	 * Add an actorSim to a Simulator.
	 * @param actorType
	 * @return
	 * @throws RepositoryException
	 * @throws Exception
	 */
	public SimulatorFactory addActorSim(ActorType actorType) throws RepositoryException, Exception {
		// has its own asc field - shows up in printed output not but in actor def
		GenericActorSimBuilder genericBuilder = new GenericActorSimBuilder(simId).buildGenericConfiguration(actorType);

		// Sets asc field - this shows up in output
		ActorFactory actorFactory = buildActorFactory(actorType);
		// initialize actor simulator
		actorFactory.initializeActorSim(
				genericBuilder,
				simId 
				);

		List<TransactionType> incomingTransactions = actorFactory.getIncomingTransactions();

		// Request the ActorFactory define endpoints for these
		// types. A particular ActorFactory may not support all of them.
		for (int i=0; i<asyncTypes.length; i++) {
			AsyncType async = asyncTypes[i];
			for (int j = 0; j<tlsTypes.length; j++) {
				TlsType tls = tlsTypes[j];
				for (TransactionType transType : incomingTransactions) {
					genericBuilder.addEndpoint(actorType.getShortName(), 
							transType, 
							tls, 
							async);
				}
			}
		}
		
		ActorSimConfig asc = genericBuilder.getActorSimConfig();
		sim.add(asc);
		actorFactory.loadActorSite(asc, site);
		
		actorSimsAdded++;
		return this;
	}

	/**
	 * Create actor specific builder and call it.  This populates
	 * the actor specific configuration entries.
	 * @param actorType
	 * @return
	 * @throws RepositoryException
	 * @throws Exception
	 */
	ActorFactory buildActorFactory(ActorType actorType)
			throws RepositoryException, Exception {
		String simFactoryClassName = actorType.getActorSimFactoryClassName();
		if (simFactoryClassName == null) {
			logger.fatal("Actor Type <" + actorType.getName() + "> has no Simulator Factory Class configured");
			throw new XdsInternalException("Actor Type <" + actorType.getName() + "> has no Simulator Factory Class configured");
		}

		Class<?> clazz = getClass().getClassLoader().loadClass(simFactoryClassName);
		ActorFactory actorFactory = (ActorFactory) clazz.newInstance();
		return actorFactory;
	}

	/**
	 * Save the Simulator.  This saves off the simulator
	 * configuration and the actor configurations (actor files)
	 * that are generated from the simulator configuration.
	 * @return
	 * @throws RepositoryException
	 * @throws Exception
	 */
	public SimulatorFactory save() throws RepositoryException, Exception {
		
		if (actorSimsAdded == 0) {
			logger.error("Cannot save Simulator - no actor sims installed.");
			throw new XdsInternalException("Cannot save Simulator - no actor sims installed.");
		}
		
		// Save raw simulator state
		new SimulatorSerializer().save(sim);
		
		// Save generated Site (actor) files
		// These are needed because the rest of toolkit expects them
		// They are readonly - any updates are made to the simulator state
		// which is then saved as an updated site definition.

		SiteRepository siteRepo = new SiteRepository();
		siteRepo.save(new Directory().getExternalSimSiteRepository(), site);
		
		return this;
	}

	public Simulator getSimulator() {
		return sim;
	}
}
