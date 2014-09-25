package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;

import java.util.List;

public abstract class ActorFactory {
	abstract public Site loadActorSite(ActorSimConfig asc, Site site);
	abstract public void initializeActorSim(GenericActorSimBuilder genericBuilder, SimId simId);
    abstract public boolean supportsAsync();

	public List<TransactionType> getIncomingTransactions() {
        ActorTransactionTypeFactory factory = new ActorTransactionTypeFactory();
        return factory.getTransactionTypes();
    }
}
