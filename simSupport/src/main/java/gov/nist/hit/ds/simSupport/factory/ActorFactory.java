package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.IOException;
import java.util.List;

public interface ActorFactory {
	public Site loadActorSite(ActorSimConfig asc, Site site) throws XdsInternalException;
	public void initializeActorSim(GenericActorSimBuilder genericBuilder, SimId simId) throws IOException, XdsInternalException, RepositoryException, Exception;
	public List<TransactionType> getIncomingTransactions();
}
