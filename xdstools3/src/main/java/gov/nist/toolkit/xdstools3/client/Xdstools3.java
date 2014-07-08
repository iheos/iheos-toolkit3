package gov.nist.toolkit.xdstools3.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.HLayout;
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


	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private static final String WINDOW_WIDTH = "994px"; // fits 1024 px res for all browsers with a little extra room
	private static final int WINDOW_WIDTH_TO_INTEGER = 994;
	private static final String WINDOW_HEIGHT = "700px";
	private static final String TOOLBAR_HEIGHT = "35px"; 	
	private static final String TEST_RESULT_SUMMARY_HEIGHT = "100px"; 	


	// sizes for Dock Panel Layout - units are arbitrary numbers, not pixels or percentages
	private static final double DOCKPANEL_TOOLBAR_HEIGHT = 7;
	private static final double DOCKPANEL_WEST_WIDTH = 25;
	private static final double DOCKPANEL_SOUTH_HEIGHT = 20;
	
	private static final String APP_TITLE_IHE = "XDS Toolkit";

    private GenericTabSet topTabSet;



	public void onModuleLoad() {


		// Toolbar
		Toolbar configBar = new Toolbar();

		// Tabs
		topTabSet = new GenericTabSet();
        topTabSet.setTabBarPosition(Side.TOP);
        topTabSet.setTabBarAlign(Alignment.CENTER);
        Tab homeTab = new HomeTab("Home");
		GenericCloseableTab findDocsTab = new FindDocumentTab();
        GenericCloseableTab mpqTab = new MPQTab();
        topTabSet.addTab(homeTab);
        topTabSet.addTab(findDocsTab);
        topTabSet.addTab(mpqTab);

        // Main layout
        VLayout mainLayout = new VLayout();
		mainLayout.setHeight100();
        mainLayout.setAlign(Alignment.CENTER);
		mainLayout.addMembers(configBar, topTabSet);
		
		// Attach the contents to the RootLayoutPanel
		HLayout container = new HLayout();
		container.setHeight100();
		container.setWidth100();
		container.setAlign(Alignment.CENTER); 
		container.addMember(mainLayout);
		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(container);

        SC.showConsole();

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
            GenericCloseableTab adminTab = new SettingsTab();
            tab = adminTab;
        }
        if (tabName == TabNamesUtil.getInstance().getEndpointsTabCode()) {
            GenericCloseableTab endpointsTab = new EndpointConfigTab();
            tab = endpointsTab;
        }
        if (tabName == TabNamesUtil.getInstance().getFindDocumentsTabCode()) {
            GenericCloseableTab findDocsTab = new FindDocumentTab();
            tab = findDocsTab;
        }
        if (tabName == TabNamesUtil.getInstance().getMpqFindDocumentsTabCode()) {
            GenericCloseableTab mpqTab = new MPQTab();
            tab = mpqTab;
        }
        // update set of tabs
        if (tab != null) {
            topTabSet.addTab(tab);
            topTabSet.selectTab(tab);
        }
    }

	public static int getWindowWidth() {
		return WINDOW_WIDTH_TO_INTEGER;
	}

}
