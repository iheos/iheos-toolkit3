package gov.nist.hit.ds.xdstools3.client.tabs.findDocumentsTab;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools2.client.tabs.FindDocumentsTab;

/**
 * This class is for the UI tab that hosts a log browser widget.
 */
public class FindDocumentsV2Tab extends GenericCloseableTab {
//    private Logger logger=Logger.getLogger(FindDocumentsV2Tab.class.getName());
    // RPC services declaration
    // --

    // tab's title and header
    private static String header="Find Documents2";

    // UI components
    // --

    // Variables
    // --

    final VLayout layout = new VLayout(10);
    /**
     * Default constructor
     */
    public FindDocumentsV2Tab() {

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


//            Window.alert("v2 is null? " +  (xdstools2==null));
//            Window.alert("tab is null?" + (xdstools2.getTabPanel()==null));

             FindDocumentsTab findDocumentsTabV2 =  new FindDocumentsTab();
             findDocumentsTabV2.onAbstractTabLoad(getXdstools2(), false, null);



//            layout.addMember(xdstools2.getTabPanel());

            ScrollPanel sp = new ScrollPanel(findDocumentsTabV2.topPanel);
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
     * Abstract method implementation to set the tab name
     * @return
     */
    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getFindDocumentsV2TabCode();
    }

    /**
     * Method that binds the tab widgets together and their functionality
     */
    private void bindUI(){
       // Add event handlers
    }


}
