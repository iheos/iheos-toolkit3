package gov.nist.hit.ds.xdstools3.client.tabs;

import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.toolkit.xdstools2.client.Xdstools2;

/**
 * Holds the functions common to all tabs (Tool Tabs and Home Tabs).
 */

public abstract class GenericTab extends Tab {
    private String tabName;


    static final Xdstools2 xdstools2 = new Xdstools2();



    public GenericTab(String header) {
        tabName = setTabName();
        setTitle(header);
//            Create the v2 Xdstools2 Main
        xdstools2.loadTkProps();

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

    public static Xdstools2 getXdstools2() {
        return xdstools2;
    }


}
