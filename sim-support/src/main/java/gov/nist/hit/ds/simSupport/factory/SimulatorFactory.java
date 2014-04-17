package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.toolkit.actortransaction.obsolete.client.ActorType;
import gov.nist.toolkit.actortransaction.obsolete.client.TransactionType;
import gov.nist.hit.ds.repository.api.*;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.IdFactory;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.serializer.SimulatorSerializer;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.utilities.xdsException.ToolkitRuntimeException;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Build a simulator.
 *
 * TODO: Needs IT tests
 * Typical calling sequence is:
 <pre>
 // Create empty Simulator
 SimulatorFactory sf = new SimulatorFactory();
 // Set the simulator ID
 sf.installRepositoryLinkage(SimId);
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
        try {
            return initializeSimulator(new SimId(new IdFactory().getNewId().getIdString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        // installRepositoryLinkage actor simulator
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
            if (sim.size() == 0) {
                logger.error("Cannot save Simulator - no actor sims installed.");
                throw new ToolkitRuntimeException("Cannot save Simulator - no actor sims installed.");
            }

            Repository repos = getSimulatorRepository();

            // Save raw simulator state
            SimulatorSerializer simser = new SimulatorSerializer();
            simser.setSimRepository(repos);
            Asset asset = simser.save(sim);
            sim.setSimAsset(asset);
    }

    public static void save(Site site) {
//        SiteRepository siteRepo = new SiteRepository();
//        siteRepo.save(new Directory().getExternalSimSiteRepository(), site);
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
        SimulatorSerializer simser = new SimulatorSerializer();
        simser.setSimRepository(getSimulatorRepository());
        return simser.load(simId);
    }

    private static Repository getSimulatorRepository()  {
        RepositoryFactory fact = null;
        try {
            fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
        } catch (RepositoryException e) {
            throw new ToolkitRuntimeException(e);
        }
        Repository simRepository;
        try {
            simRepository = fact.getRepository(new SimpleId("simulators"));
        } catch (RepositoryException e) {
            if (e.getMessage() != null && e.getMessage().startsWith(RepositoryException.UNKNOWN_REPOSITORY)) {
                // first use - create repository
                try {
                    simRepository = fact.createNamedRepository(
                            "Simulators",
                            "Simulators",
                            new SimpleType("simulatorsRepos"),               // repository type
                            "simulators"    // repository name
                    );
                } catch (RepositoryException e1) {
                    throw new ToolkitRuntimeException(e1);
                }
            } else throw new ToolkitRuntimeException("Cannot access or create simulators repository");
        }
        return simRepository;
    }

}
