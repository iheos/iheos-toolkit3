package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.siteManagement.client.Site;

import java.util.List;

public interface ActorFactory {
	public Site loadActorSite(ActorSimConfig asc, Site site);
	public void initializeActorSim(GenericActorSimBuilder genericBuilder, SimId simId);
	public List<TransactionType> getIncomingTransactions();
}
