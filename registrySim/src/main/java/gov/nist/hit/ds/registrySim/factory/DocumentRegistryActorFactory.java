package gov.nist.hit.ds.registrySim.factory;

import gov.nist.hit.ds.actorTransaction.ATConfigLabels;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.client.AbstractActorSimConfigElement;
import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.factory.ActorFactory;
import gov.nist.hit.ds.simSupport.factory.GenericActorSimBuilder;
import gov.nist.hit.ds.siteManagement.client.Site;
import gov.nist.hit.ds.siteManagement.client.TransactionBean;
import gov.nist.hit.ds.siteManagement.client.TransactionBean.RepositoryType;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * Build an instance of the Document Registry Actor.
 * @author bmajur
 *
 */
public class DocumentRegistryActorFactory implements ActorFactory {
	static Logger logger = Logger.getLogger(DocumentRegistryActorFactory.class);

	@Override
	public void initializeActorSim(GenericActorSimBuilder genericBuilder, SimId simId) throws RepositoryException, Exception {
		
		// set option defaults
		genericBuilder.addConfig(ATConfigLabels.update_metadata_option, true);
		genericBuilder.addConfig(ATConfigLabels.extraMetadataSupported, true);

		/**
		 * Install codes file for default environment. This is the best we can do this deep in the code
		 * since this package does not depend on the Session package which has information regarding
		 * the current selected environment.  Better selection will have to come from the user
		 * interface.
		 */
		genericBuilder.addConfig(ATConfigLabels.codesEnvironment, 
				Installation.installation().getDefaultCodesFile().toString());

	}
	
	@Override
	public List<TransactionType> getIncomingTransactions() {
		return Arrays.asList(
				TransactionType.REGISTER,
				TransactionType.STORED_QUERY,
				TransactionType.UPDATE
				);
	}

	@Override
	public Site loadActorSite(ActorSimConfig asc, Site site) throws XdsInternalException {
		String siteName = asc.getByName(ATConfigLabels.name).getValue();
		
		if (siteName == null) {
			logger.fatal("Cannot retrieve site name to build actor definition");
			throw new XdsInternalException("Cannot retrieve site name to build actor definition");
		}

		if (site == null)
			site = new Site(siteName);
		
		if (site.getName() == null)
			site.setName(siteName);

		site.addTransaction(
				new TransactionBean(
						TransactionType.REGISTER.getCode(),
						RepositoryType.NONE,
						asc.getEndpoint(TransactionType.REGISTER, TlsType.NOTLS, AsyncType.SYNC),
						false, 
						false));
		site.addTransaction(
				new TransactionBean(
						TransactionType.REGISTER.getCode(),
						RepositoryType.NONE,
						asc.getEndpoint(TransactionType.REGISTER, TlsType.TLS, AsyncType.SYNC),
						true, 
						false));

		site.addTransaction(
				new TransactionBean(
						TransactionType.STORED_QUERY.getCode(),
						RepositoryType.NONE,
						asc.getEndpoint(TransactionType.STORED_QUERY, TlsType.NOTLS, AsyncType.SYNC),
						false, 
						false));
		site.addTransaction(
				new TransactionBean(
						TransactionType.STORED_QUERY.getCode(),
						RepositoryType.NONE,
						asc.getEndpoint(TransactionType.STORED_QUERY, TlsType.TLS, AsyncType.SYNC),
						true, 
						false));

		AbstractActorSimConfigElement ele = asc.getByName(ATConfigLabels.update_metadata_option);
		if (ele == null) {
			logger.error("Update Metadata Option setting cannot be retrieved from Actor Sim Configuration");
		} else {
			if (ele.asBoolean()) {
				site.addTransaction(
						new TransactionBean(
								TransactionType.UPDATE.getCode(),
								RepositoryType.NONE,
								asc.getEndpoint(TransactionType.UPDATE, TlsType.NOTLS, AsyncType.SYNC),
								false, 
								false));
				site.addTransaction(
						new TransactionBean(
								TransactionType.UPDATE.getCode(),
								RepositoryType.NONE,
								asc.getEndpoint(TransactionType.UPDATE, TlsType.TLS, AsyncType.SYNC),
								true, 
								false));
			}
		}

		return site;
	}

}
