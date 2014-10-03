package gov.nist.hit.ds.registrySim.actor;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.ActorTypeFactory;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.actorTransaction.TransactionTypeFactory;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.components.ActorSimEnvironment;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.SimComponentInject;
import gov.nist.hit.ds.simSupport.engine.annotations.SimComponentOutput;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.factory.SimulatorFactory;
import gov.nist.hit.ds.simSupport.simrepo.SimRepoFactory;
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
	private ActorSimEnvironment actorSimEnvironment;
	private Simulator simulator;
	private ActorType actorType;
	private TransactionType transactionType = TransactionTypeFactory.find(actorSimEnvironment.getTransCode());
    private RepositoryFactory repositoryFactory = new RepositoryFactory(Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL));
    private Asset metadataDirectory = null;
	static Logger logger = Logger.getLogger(DocumentRegistryActorSim.class);

    @SimComponentInject
	public void setActorSimEnvironment(ActorSimEnvironment actorSimEnvironment) {
		this.actorSimEnvironment = actorSimEnvironment;
	}
		
	@SimComponentOutput
	public DocumentRegistryActorSim getDocumentRegistryActorSim() { return this; }
	
	@SimComponentOutput
	public Simulator getSimulator() {
		return simulator;
	}
	
	@SimComponentOutput
	public ActorType getActorType() {
		return actorType;
	}
	
	@SimComponentOutput
	public TransactionType getTransactionType() {
		return transactionType;
	}

    @SimComponentOutput
    public Asset getMetadataDirectory() { return metadataDirectory; }
	
	@Override
	public void run() throws SoapFaultException {
		logger.trace("Run DocumentRegistryActorSim");

        actorType = ActorTypeFactory.find(actorSimEnvironment.getActorCode());
		simulator = SimulatorFactory.load(new SimId(actorSimEnvironment.getSimId()));
        new SimRepoFactory().installRepositoryLinkage(simulator);
    }

	@Override
	public boolean showOutputInLogs() { return true; }
}
