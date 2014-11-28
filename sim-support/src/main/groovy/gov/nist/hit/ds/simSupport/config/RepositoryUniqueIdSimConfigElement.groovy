package gov.nist.hit.ds.simSupport.config;

import org.apache.log4j.Logger;

/**
 * Created by bmajur on 6/10/14.
 */

public class RepositoryUniqueIdSimConfigElement extends AbstractConfigElement {
    static String repositoryUniqueIdBase = "1.1.4567332.1.";
    static int repositoryUniqueIdIncr = 1;
    static Logger log = Logger.getLogger(RepositoryUniqueIdSimConfigElement.class);
    String value;

    def RepositoryUniqueIdSimConfigElement() { value = mkNewValue(); }

    def RepositoryUniqueIdSimConfigElement(String value) { this.value = value;}

    String mkNewValue() { repositoryUniqueIdBase + repositoryUniqueIdIncr++; }
}
