package gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.hit.ds.xdstools3.client.util.injection.Xdstools3GinInjector;
import gov.nist.toolkit.xdstools2.client.Xdstools2;
import gov.nist.toolkit.xdstools2.client.tabs.FindDocumentsTab;
import gov.nist.toolkit.xdstools2.client.tabs.TabLauncher;

/**
 * Created by skb1 on 8/11/15.
 */
public class FindDocumentsPlaceTab extends GenericCloseableTab {

    private static final Xdstools3GinInjector injector= Xdstools3GinInjector.injector;
    static Xdstools2 xdstools2 = injector.getXdstools2();
    final VLayout layout = new VLayout(10);


    // tab's title and header
    private static String header = TabLauncher.findDocumentsTabLabel;

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

    public FindDocumentsPlaceTab() {
        super(header);

        FindDocumentsTab findDocumentsTab = new gov.nist.toolkit.xdstools2.client.tabs.FindDocumentsTab();
                findDocumentsTab.onAbstractTabLoad(xdstools2, false, null);


        setPane((Canvas) createContents(findDocumentsTab.topPanel));

    }

    private Widget createContents(final Widget w) {


        try {


            ScrollPanel sp = new ScrollPanel(w);
            sp.setSize("100%","100%");


            layout.addMember(sp);

            bindUI();

            return layout;

        } catch (Throwable t) {
            Window.alert(t.toString());
            t.printStackTrace();
        }

        layout.addMember(new HTML("No content"));

        return layout;
    }


    /**
     * Method that binds the tab widgets together and their functionality
     */
    private void bindUI(){
        // Add event handlers
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
