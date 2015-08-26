package gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr;

import gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.V2DynamicTab;
import gov.nist.toolkit.xdstools2.client.event.tabContainer.V2TabOpenedEvent;
import gov.nist.toolkit.xdstools2.client.tabs.TabLauncher;

/**
 * Created by skb1 on 8/11/15.
 */
public class DocRetrieveTab extends V2DynamicTab {

    // tab's title and header
    private static String header = TabLauncher.documentRetrieveTabLabel;

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

    public DocRetrieveTab(V2TabOpenedEvent event) {
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
