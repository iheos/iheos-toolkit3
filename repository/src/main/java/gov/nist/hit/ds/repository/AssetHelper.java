package gov.nist.hit.ds.repository;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.AssetIterator;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.simple.SimpleRepository;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.repository.simple.search.SearchResultIterator;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria.Criteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm.Operator;

import java.util.logging.Logger;


public class AssetHelper {
	static Logger logger = Logger.getLogger(AssetHelper.class.getName());
	
	static public Asset createChildAsset(Asset parent, String displayName, String description, SimpleType assetType) throws RepositoryException {
		logger.info("Creating <" + displayName + ">,  child of <" + parent.getId() + "> in repo <" + parent.getRepository() +">");
		
		RepositoryFactory fact = new RepositoryFactory(parent.getSource());
		
		Asset a = fact.getRepository(parent.getRepository()).createAsset(displayName, description, assetType);
		logger.info("Created <" + a.getId() + ">");
		a.setProperty(PropertyKey.PARENT_ID , parent.getId().getIdString());

		parent.addChild(a);
		return a;
	}
	
	/*
	 * static public Asset createChildAsset(Asset parent, String displayName, String description, SimpleType assetType) throws RepositoryException {
		logger.info("Creating <" + displayName + ">,  child of <" + parent.getId() + "> in repo <" + parent.getRepository() +">");
		
		// RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		
		Asset a = fact.getRepository(parent.getRepository()).createAsset(displayName, description, assetType);
		logger.info("Created <" + a.getId() + ">");
		a.setProperty("parent", parent.getId().getIdString());
		return a;
	}	
	 */

	/**
	 * Returns an iterator for an asset's immediate children 
	 * @return
	 */
	static public AssetIterator getChildren(final Asset a) throws RepositoryException {

        String idString = "";

        if (a.getId()!=null) {
            idString = a.getId().getIdString();
        }

        SearchCriteria criteria = new SearchCriteria(Criteria.AND);

        criteria.append(new SearchTerm(PropertyKey.PARENT_ID,Operator.EQUALTOANY, new String[]{idString, a.getPropFileRelativePart()}));

		SimpleRepository repos = new SimpleRepository(a.getRepository());
		repos.setSource(a.getSource());		

		return new SearchResultIterator(new Repository[]{repos}, criteria, new PropertyKey[]{PropertyKey.DISPLAY_ORDER, PropertyKey.CREATED_DATE });
		
	}

}
