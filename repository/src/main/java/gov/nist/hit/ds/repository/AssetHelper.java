package gov.nist.hit.ds.repository;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleRepository;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.search.SearchResultIterator;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria.Criteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm.Operator;

import org.apache.log4j.Logger;

public class AssetHelper {
	static Logger logger = Logger.getLogger(AssetHelper.class);
	
	static public Asset createChildAsset(Asset parent, String displayName, String description, SimpleType assetType) throws RepositoryException {
		logger.debug("Creating <" + displayName + ">,  child of <" + parent.toString() + "> in repo <" + parent.getRepository() +">");
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Asset a = fact.getRepository(parent.getRepository()).createAsset(displayName, description, assetType);
		logger.debug("Created <" + a.toString() + ">");
		a.setProperty("parent", parent.getId().getIdString());
		return a;
	}	

	/**
	 * Returns an iterator for an asset's immediate children 
	 * @return
	 */
	static public AssetIterator getChildren(final Asset a) throws RepositoryException {
		
		SearchCriteria criteria = new SearchCriteria(Criteria.AND);
		criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTO,a.getId().getIdString()));

		SimpleRepository repos = new SimpleRepository(a.getRepository());
		repos.setSource(a.getSource());		
		
		return new SearchResultIterator(new Repository[]{repos}, criteria, PropertyKey.DISPLAY_ORDER.toString());
		
	}

}
