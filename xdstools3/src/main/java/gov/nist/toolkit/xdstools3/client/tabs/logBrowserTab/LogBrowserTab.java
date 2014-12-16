package gov.nist.toolkit.xdstools3.client.tabs.logBrowserTab;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.repository.rpc.search.client.exception.RepositoryConfigException;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.ui.client.widgets.LogBrowserWidget;
import gov.nist.toolkit.xdstools3.client.manager.Manager;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;

import java.util.logging.Logger;

/**
 * This class is for the UI tab that hosts a log browser widget.
 */
public class LogBrowserTab extends GenericCloseableTab {
    private Logger logger=Logger.getLogger(LogBrowserTab.class.getName());
    // RPC services declaration
    // --

    // tab's title and header
    private static String header="Log Browser";

    // UI components
    // --

    // Variables
    // --



    private AssetNode target;

    /**
     * Default constructor
     */
    public LogBrowserTab() {
        super(header);
        try {
            setContents(createContent(null));
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    /**
     * This constructor is target assetNode context specific
     * @param target The target asset to focus on
     */
    public LogBrowserTab(AssetNode target) {
        super(header);


        try {

            getPane().clear();
            getPanel().clear();
            getPane().redraw();
            getPanel().addMember(createContent(target));
            setPane(getPanel());
            getPane().redraw();

        } catch (Throwable t) {
            t.printStackTrace();
        }


    }


    /**
     * Abstract method implementation that build the UI.
     *
     * @return tab UI as a Widget
     */
    @Override
    protected Widget createContents() {
        Widget w = new HTML("");
        w.setSize("1px","1px");
        w.getElement().getStyle().setDisplay(Style.Display.NONE);
        w.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);

       return w; // This is a placeholder
    }



    /**
     * Creates tab content
     * @return
     */
    private Widget createContent(AssetNode target) {
        VStack vStack=new VStack();


        try {
            LogBrowserWidget logBrowserWidget = null;
            if (target!=null) {
                // This is target assetNode specific
                logBrowserWidget = new LogBrowserWidget(Manager.EVENT_BUS,target);

            } else {
                // Normal browse mode
                try {
                    logBrowserWidget = new LogBrowserWidget(Manager.EVENT_BUS,
                            new LogBrowserWidget.Feature[] {
                                    LogBrowserWidget.Feature.BROWSE
                            });

                } catch (RepositoryConfigException e) {
                    Window.alert("Could not initialize log browser widget: " + e.toString());
                }
            }

            logBrowserWidget.setSize("1000px", "600px");

            vStack.addMember(logBrowserWidget);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        bindUI();

        return vStack;
    }

    /**
     * Abstract method implementation to set the tab name
     * @return
     */
    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getMHDValidatorTabCode();
    }

    /**
     * Method that binds the tab widgets together and their functionality
     */
    private void bindUI(){
       // Add event handlers
    }


    public AssetNode getTarget() {
        return target;
    }

    public void setTarget(AssetNode target) {
        this.target = target;
    }




}
