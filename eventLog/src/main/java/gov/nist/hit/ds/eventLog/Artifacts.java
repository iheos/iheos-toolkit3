package gov.nist.hit.ds.eventLog;

import gov.nist.hit.ds.repository.AssetHelper;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.SimpleType;

import org.apache.log4j.Logger;

public class Artifacts {
	Asset artifactsAsset;
	int counter = 1;
	static Logger logger = Logger.getLogger(Artifacts.class);

	Asset init(Asset parent) throws RepositoryException {
		artifactsAsset = AssetHelper.createChildAsset(parent, "Artifacts", "", new SimpleType("simArtifacts"));
		return artifactsAsset;
	}
	
	public void add(String name, String value) throws RepositoryException {
		logger.debug("Artifact\n" + name + " = " + value);
		Asset a = AssetHelper.createChildAsset(artifactsAsset, name, "", new SimpleType("simpleType"));
		a.setOrder(counter++);
		a.updateContent(value, "text/plain");
	}
}
