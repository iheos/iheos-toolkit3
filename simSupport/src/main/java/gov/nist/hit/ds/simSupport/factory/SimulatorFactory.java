package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.IdFactory;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repositoryDirectory.Directory;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.serializer.SimulatorSerializer;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.siteManagement.repository.SiteRepository;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Build a simulator.
 * 
 * TODO: Needs IT tests
 * Typical calling sequence is:
<pre>
	// Create empty Simulator
	SimulatorFactory sf = new SimulatorFactory();
	// Set the simulator ID
	sf.initializeSimulator(SimId);
	// Add an Actor Simulator to it
	sf.addActorSim(ActorTypeFactory.find("registry");
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


	public SimulatorFactory initializeSimulator(SimId simId) {
		logger.info("Creating simulator <" + simId + ">");
		this.simId = simId;
		sim = new Simulator(simId);
		return this;
	}

	/**
	 * Build Empty Simulator that actorSims can be added to.
	 * @return
	 * @throws RepositoryException
	 */
	public SimulatorFactory initializeSimulator()  {
		return initializeSimulator(new SimId(new IdFactory().getNewId().getIdString()));
	}
	
	/**
	 * Add an actorSim to a Simulator.
	 * @param actorType
	 * @return
	 * @throws RepositoryException
	 * @throws Exception
	 */
	public SimulatorFactory addActorSim(ActorType actorType)  {
		// has its own asc field - shows up in printed output not but in actor def
		GenericActorSimBuilder genericBuilder = new GenericActorSimBuilder(simId).buildGenericConfiguration(actorType);

		// Sets asc field - this shows up in output
		ActorFactory actorFactory = lookupActorFactory(actorType);
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
	 * Save the Simulator.  This saves off the simulator
	 * configuration and the actor configurations (actor files)
	 * that are generated from the simulator configuration.
	 * @return
	 * @throws RepositoryException
	 * @throws Exception
	 */
	public static void save(Simulator sim)  {
		
		try {
			if (sim.size() == 0) {
				logger.error("Cannot save Simulator - no actor sims installed.");
				throw new ToolkitRuntimeException("Cannot save Simulator - no actor sims installed.");
			}

			Repository repos = getSimulatorRepository();

			// Save raw simulator state
			SimulatorSerializer simser = new SimulatorSerializer();
			simser.setSimRepository(repos);
			simser.save(sim);

		} catch (RepositoryException e) {
			throw new ToolkitRuntimeException("Cannot save Simulator <" + sim.getSimId() + "> state to repository", e);
		}
	}
	
	public static void save(Site site) {
		SiteRepository siteRepo = new SiteRepository();
		siteRepo.save(new Directory().getExternalSimSiteRepository(), site);
	}
	
	public Site getSite() {
		return site;
	}

	public Simulator getSimulator() {
		return sim;
	}

	/**
	 * Lookup up the classname of the ActorFactory needed to construct a simulator of type actorType. 
	 * These configurations are kept in ActorType, an emun holding the configurations.
	 * @param actorType
	 * @return instance of actor factory
	 */
	private static ActorFactory lookupActorFactory(ActorType actorType) 
	{
		String simFactoryClassName = actorType.getActorSimFactoryClassName();
		if (simFactoryClassName == null) {
			logger.fatal("Actor Type <" + actorType.getName() + "> has no Simulator Factory Class configured");
			throw new ToolkitRuntimeException("Actor Type <" + actorType.getName() + "> has no Simulator Factory Class configured");
		}

		Class<?> clazz;
		try {
			clazz = SimulatorFactory.class.getClassLoader().loadClass(simFactoryClassName);
		} catch (ClassNotFoundException e) {
			throw new ToolkitRuntimeException("Factory class <" + simFactoryClassName + "> for ActorType <" + actorType + "> does not exist.", e);
		}
		try {
			ActorFactory actorFactory = (ActorFactory) clazz.newInstance();
			return actorFactory;
		} catch (Exception e) {
			throw new ToolkitRuntimeException("Cannot create an instance of <" + actorType + ">",e);
		}
	}

	public static Simulator load(SimId simId)  {
		try {
			SimulatorSerializer simser = new SimulatorSerializer();
			simser.setSimRepository(getSimulatorRepository());
			return simser.load(simId);
		} catch (RepositoryException e) {
			throw new ToolkitRuntimeException("Cannot load Simulator <" + simId + ">", e);
		}
	}

	private static Repository getSimulatorRepository() throws RepositoryException {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createNamedRepository(
				"Simulators", 
				"Simulators", 
				new SimpleType("simulatorsRepos"),               // repository type
				"simulators"    // repository name
				);
		return repos;
	}

}
