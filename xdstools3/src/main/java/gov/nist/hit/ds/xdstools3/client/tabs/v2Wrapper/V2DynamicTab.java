package gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.hit.ds.xdstools3.client.util.injection.Xdstools3GinInjector;
import gov.nist.toolkit.xdstools2.client.Xdstools2;
import gov.nist.toolkit.xdstools2.client.event.tabContainer.V2TabOpenedEvent;

/**
 * This class is for the UI tab that hosts a log browser widget.
 */
public abstract class V2DynamicTab extends GenericCloseableTab {
    // RPC services declaration
    // --

    // UI components
    // --

    // Variables
    // --
    private static final Xdstools3GinInjector injector= Xdstools3GinInjector.injector;
    static Xdstools2 xdstools2 = injector.getXdstools2();


    V2TabOpenedEvent event;

    final VLayout layout = new VLayout(10);


    public V2DynamicTab(V2TabOpenedEvent event) {
        super(event.getTitle());
        setEvent(event);

        int tabCount = xdstools2.getTabPanel().getWidgetCount();
        int tabIndex = event.getTabIndex();
        if (tabIndex < tabCount )
            setPane((Canvas) createContents(xdstools2.getTabPanel().getWidget(tabIndex)));
        else
            setPane((Canvas) createContents(new HTML("Tab error: index is less than total. index: " + event.getTabIndex() + " total: " + tabCount)));

        setPane((Canvas)layout);

    }






        /**
         * Creates tab content
         * @return
         */
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



    public V2TabOpenedEvent getEvent() {
        return event;
    }

    public void setEvent(V2TabOpenedEvent event) {
        this.event = event;
    }

}
