package gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.hit.ds.xdstools3.client.util.injection.Xdstools3GinInjector;
import gov.nist.toolkit.xdstools2.client.Xdstools2;

/**
 * This class is for the UI tab that hosts a log browser widget.
 */
public class V2HomeTab extends GenericCloseableTab {
    // RPC services declaration
    // --

    // tab's title and header
    private static String header="V2 Home";

    // UI components
    // --

    // Variables
    // --

    final VLayout layout = new VLayout(10);

    private static final Xdstools3GinInjector injector=Xdstools3GinInjector.injector;
    static Xdstools2 xdstools2 = injector.getXdstools2();

    /**
     * Default constructor
     */
    public V2HomeTab() {

        super(header);
        setPane((Canvas) createContents());
    }






    /**
     * Creates tab content
     * @return
     */
    private Widget createContents() {


//Window.alert("in createContent");
        try {

//
//            ScrollPanel sp = new ScrollPanel();
//            sp.setSize("100%","100%");

//            Xdstools2 xdstools2 = new Xdstools2();
//            xdstools2.loadTkProps();




//            Window.alert("getTabP is null? " + (getXdstools2().getTabPanel()==null));
            layout.setSize("100%", "100%");
            xdstools2.getTabPanel().setSize("100%","100%");
            layout.addMember(xdstools2.getTabPanel());


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
     * Abstract method implementation to set the tab name
     * @return
     */
    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getV2HomeTabCode();
    }

    /**
     * Method that binds the tab widgets together and their functionality
     */
    private void bindUI(){
       // Add event handlers
    }


}
