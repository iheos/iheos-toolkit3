package gov.nist.hit.ds.registrySim.actor;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.ActorTypeFactory;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.actorTransaction.TransactionTypeFactory;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.components.ActorSimEnvironment;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.annotations.ParserOutput;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.factory.SimulatorFactory;
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
	private Simulator sim;
	private ActorType actorType;
	private TransactionType transactionType;
	ActorSimConfig actorSimConfig;
	static Logger logger = Logger.getLogger(DocumentRegistryActorSim.class);

	@Inject
	public void setActorSimEnvironment(ActorSimEnvironment actorSimEnvironment) {
		this.actorSimEnvironment = actorSimEnvironment;
	}
		
	@ParserOutput
	public DocumentRegistryActorSim getDocumentRegistryActorSim() {
		return this;
	}
	
	@ParserOutput
	public Simulator getSimulator() {
		return sim;
	}
	
	@ParserOutput
	public ActorType getActorType() {
		return actorType;
	}
	
	@ParserOutput
	public TransactionType getTransactionType() {
		return transactionType;
	}
	
	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		logger.trace("Run DocumentRegistryActorSim");
		sim = SimulatorFactory.load(new SimId(actorSimEnvironment.getSimId()));
		actorType = ActorTypeFactory.find(actorSimEnvironment.getActorCode());
		transactionType = TransactionTypeFactory.find(actorSimEnvironment.getTransCode());
		actorSimConfig = sim.getActorSimConfig(actorType);
	}

	@Override
	public boolean showOutputInLogs() {
		return true;
	}
}
