package gov.nist.toolkit.xdstools3.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.toolkit.xdstools3.client.customWidgets.Toolbar;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.OpenTabEvent;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.OpenTabEventHandler;
import gov.nist.toolkit.xdstools3.client.tabs.*;
import gov.nist.toolkit.xdstools3.client.tabs.MPQTab.MPQTab;
import gov.nist.toolkit.xdstools3.client.tabs.findDocumentsTab.FindDocumentTab;
import gov.nist.toolkit.xdstools3.client.tabs.homeTab.HomeTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;
import gov.nist.toolkit.xdstools3.client.util.Util;

public class Xdstools3 implements EntryPoint {

    private GenericTabSet topTabSet;


	public void onModuleLoad() {

		// Toolbar
		Toolbar configBar = new Toolbar();

		// Tabs
		topTabSet = new GenericTabSet();
        Tab homeTab = new HomeTab("Home");
        topTabSet.addTab(homeTab);

        // Main layout
        VLayout mainLayout = new VLayout();
		mainLayout.addMembers(configBar, topTabSet);
        mainLayout.setStyleName("mainLayout");
		
		// Attach the contents to the RootLayoutPanel
		HLayout container = new HLayout();
        container.setAlign(Alignment.CENTER);
        container.setWidth100();
        container.setHeight100();
        container.addMembers(new LayoutSpacer(), mainLayout, new LayoutSpacer());
        mainLayout.setWidth(900); // width has to be set here after use of LayoutSpacers, not in CSS, else it will not work.

		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(container);

        // Smartgwt Console - useful for development, mainly tracking RPC calls
        //SC.showConsole();

        // Add listener for Open Tab eventBusUtils. The tabs called must be defined in function "openTab".
        Util.EVENT_BUS.addHandler(OpenTabEvent.TYPE, new OpenTabEventHandler(){
            public void onEvent(OpenTabEvent event) {
                openTab(event.getTabName());
            }
        });

    }

    /**
     * Opens a given tab defined by its name. Updates the display to add this new tab and to bring it into focus.
     * This function must be updated when new tabs are added to the application, as well as its related classes.
     *
     * @param tabName the name of the tab to open.
     * @see TabNamesUtil, OpenTabEvent
     */
    public void openTab(String tabName) {
        GenericCloseableTab tab = null;

        // create tab depending on parameter
        if (tabName == TabNamesUtil.getInstance().getAdminTabCode()) {
            tab = new SettingsTab();
        }
        if (tabName == TabNamesUtil.getInstance().getEndpointsTabCode()) {
            tab = new EndpointConfigTab();
        }
        if (tabName == TabNamesUtil.getInstance().getFindDocumentsTabCode()) {
            tab = new FindDocumentTab();
        }
        if (tabName == TabNamesUtil.getInstance().getMpqFindDocumentsTabCode()) {
            tab = new MPQTab();
        }
        if (tabName == TabNamesUtil.getInstance().getMessageValidatorTabCode()) {
            tab = new MessageValidatorTab();
        }
        // update set of tabs
        if (tab != null) {
            topTabSet.addTab(tab);
            topTabSet.selectTab(tab);
        }
    }

}
