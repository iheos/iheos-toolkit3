package gov.nist.hit.ds.eventLog

import gov.nist.hit.ds.repository.AssetHelper
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.repository.simple.SimpleType
import org.apache.log4j.Logger

/**
 * Created by bill on 4/15/14.
 */
class ArtifactsDAO {
    Asset artifactsAsset;
    int counter = 1;
    static Logger logger = Logger.getLogger(Artifacts.class);

    def save(Artifacts artifacts, Asset parent) throws RepositoryException {
        artifactsAsset = AssetHelper.createChildAsset(parent, "Artifacts", "", new SimpleType("simArtifacts"));
        artifacts.artifactOrder.each { name ->
            logger.trace("Artifact: " + name + " = " + value);
            Asset a = AssetHelper.createChildAsset(artifactsAsset, name, "", new SimpleType("simpleType"));
            a.setOrder(counter++);
            a.updateContent(artifacts.artifactMap[name], "text/plain");
        }
    }

}
