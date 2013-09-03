package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.simple.SimpleType;

import org.apache.log4j.Logger;

public class AssetHelper {
	static Logger logger = Logger.getLogger(AssetHelper.class);
	
	static public Asset createChildAsset(Asset parent, String displayName, String description, SimpleType assetType) throws RepositoryException {
		logger.info("Creating <" + displayName + ">,  child of <" + parent.getId() + "> in repo <" + parent.getRepository() +">");
		RepositoryFactory fact = new RepositoryFactory();
		Asset a = fact.getRepository(parent.getRepository()).createAsset(displayName, description, assetType);
		logger.info("Created <" + a.getId() + ">");
		a.setProperty("parent", parent.getId().getIdString());
		return a;
	}
	
	static public Asset setOrder(Asset a, int order) throws RepositoryException {
		a.setProperty("order", Integer.toString(order));
		return a;
	}

	static public Asset setMimeType(Asset a, String mimeType) throws RepositoryException {
		a.setProperty("mimeType", mimeType);
		return a;
	}
	
}
