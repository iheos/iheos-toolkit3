package gov.nist.toolkit.xdstools3.client.util;

import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.tabs.SettingsTab;

/**
 * Singleton
 */
public class TabUtils {
    private TabUtils instance = null;

    private TabUtils(){}

    public TabUtils getInstance(){
        if (instance == null){
            instance = new TabUtils();
        }
        return instance;
    }

    // Tabs

}
