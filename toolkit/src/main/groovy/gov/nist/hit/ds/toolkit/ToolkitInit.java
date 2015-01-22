package gov.nist.hit.ds.toolkit;

import gov.nist.hit.ds.toolkit.installation.Installation;
import org.apache.log4j.Logger;

/**
 * Created by bmajur on 6/30/14.
 */
public class ToolkitInit {
    static Logger logger = Logger.getLogger(ToolkitInit.class);
    static {
        try {
            Installation.installation().initialize();
        } catch (Exception e) {
            logger.fatal(e.getMessage());
            throw new RuntimeException(e);
        }
        Toolkit.initialize();
    }
}
