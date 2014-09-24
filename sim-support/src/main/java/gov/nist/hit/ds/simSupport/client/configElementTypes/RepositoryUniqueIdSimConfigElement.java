package gov.nist.hit.ds.simSupport.client.configElementTypes;

import org.apache.log4j.Logger;

/**
 * Created by bmajur on 6/10/14.
 */

public class RepositoryUniqueIdSimConfigElement extends SimConfigElement {
    static String repositoryUniqueIdBase = "1.1.4567332.1.";
    static int repositoryUniqueIdIncr = 1;
    static Logger log = Logger.getLogger(RepositoryUniqueIdSimConfigElement.class);
    String value;

    public RepositoryUniqueIdSimConfigElement() {
        value = getNewValue();
    }

    public RepositoryUniqueIdSimConfigElement(String value) {
        this.value = value;
    }

    public String getNewValue() {
        return repositoryUniqueIdBase + repositoryUniqueIdIncr++;
    }
    public static String getRepositoryUniqueIdBase() { return repositoryUniqueIdBase; }

}
