package gov.nist.toolkit.xdstools3.client.tabs;

import com.smartgwt.client.widgets.tab.Tab;

/**
 * Holds the functions common to all tabs (Tool Tabs and Home Tabs).
 */

public abstract class GenericTab extends Tab {
    private String tabName;

    public GenericTab(String header) {
        tabName = setTabName();
        setTitle(header);
    }

    /**
     * Returns the tab name, used for navigation.
     * These are defined in TabNamesUtil.
     * @return tab name (String)
     * @See TabNamesUtil
     */
    public String getTabName(){
        return tabName;
    }

    /**
     * Sets the tab name.
     * @return tab name
     */
    protected abstract String setTabName();


}
