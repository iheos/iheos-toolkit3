package gov.nist.hit.ds.simSupport.client.configElementTypes;

import gov.nist.hit.ds.actorTransaction.EndpointLabel;
import gov.nist.hit.ds.simSupport.endpoint.Endpoint;
import org.apache.log4j.Logger;

/**
 * Created by bmajur on 6/10/14.
 */

public class RepositoryUniqueIdSimConfigElement extends EndpointActorSimConfigElement  {
    static String repositoryUniqueIdBase = "1.1.4567332.1.";
    static int repositoryUniqueIdIncr = 1;
    static Logger log = Logger.getLogger(RepositoryUniqueIdSimConfigElement.class);

    public RepositoryUniqueIdSimConfigElement(EndpointLabel label, Endpoint endpoint) {
        super(label, endpoint);
        log.debug("RepositoryUniqueIdSimConfigElement: " + label);
        value = getNewValue();
    }

    public String getNewValue() {
        return repositoryUniqueIdBase + repositoryUniqueIdIncr++;
    }
    public static String getRepositoryUniqueIdBase() { return repositoryUniqueIdBase; }

}
