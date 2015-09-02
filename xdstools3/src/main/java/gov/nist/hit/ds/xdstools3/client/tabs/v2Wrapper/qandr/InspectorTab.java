package gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr;

import gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.V2DynamicTab;
import gov.nist.toolkit.xdstools2.client.event.tabContainer.V2TabOpenedEvent;

/**
 * The Inspector Tab is not really defined as an independent tab in v2, it is dependent on the FindDocument Tab
 * Created by skb1 on 8/11/15.
 */
public class InspectorTab extends V2DynamicTab {

    // tab's title and header
    private static String header = "Inspector";

    /**
     * Returns the tab name, used for navigation.
     * These are defined in TabNamesUtil.
     *
     * @return tab name (String)
     * @See TabNamesUtil
     */
    @Override
    public String getTabName() {
        return header;
    }

    public InspectorTab(V2TabOpenedEvent event) {
        super(event);

    }

    /**
     * Abstract method implementation to set the tab name
     * @return
     */
    @Override
    protected String setTabName() {
        return header;
    }
}