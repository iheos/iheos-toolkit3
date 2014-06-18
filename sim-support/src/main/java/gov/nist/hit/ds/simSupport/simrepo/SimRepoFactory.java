package gov.nist.hit.ds.simSupport.simrepo;

import gov.nist.hit.ds.repository.api.ArtifactId;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.util.Calendar;
import java.util.Date;

/**
 * Initialization and access functions for simulator repository.
 * @author bmajur
 *
 */
public class SimRepoFactory {
//	static Logger logger = Logger.getLogger(SimRepoFactory.class);

    public void installRepositoryLinkage(Simulator simulator) {
        ArtifactId repositoryId = null;
        try {
            repositoryId = simulator.getSimAsset().getRepository();
        } catch (RepositoryException e) {
            throw new ToolkitRuntimeException(e);
        }
        simulator.setRepositoryId(repositoryId);
    }


}
