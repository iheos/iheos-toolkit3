package gov.nist.hit.ds.xdstools3.client.tabs;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.hit.ds.xdstools3.client.customWidgets.design.Formatter;

/**
 * Holds the functions common to all tabs (Tool Tabs and Home Tabs).
 */

public abstract class GenericTab extends Tab {
    private String tabName;
    private Label headerLabel = new Label();


    /**
     *
     * @param header The title of the tab, to be displayed at the top of the page
     */
    public GenericTab(String header) {
        tabName = setTabName();
        setTitle(header);
    }


    /**
     * Sets the tab name.
     * @return tab name
     */
    protected abstract String setTabName();

    /**
     * Returns the tab name, used for navigation.
     * These are defined in TabNamesUtil.
     * @return tab name (String)
     * @See TabNamesUtil
     */
    public String getTabName(){
        return tabName;
    }



}
