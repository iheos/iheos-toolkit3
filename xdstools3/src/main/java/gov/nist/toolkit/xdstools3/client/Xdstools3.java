package gov.nist.toolkit.xdstools3.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.RESTClient.RestDs;
import gov.nist.toolkit.xdstools3.client.customWidgets.ConfigToolbar;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.CloseableTabWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.MPQTab.MPQTab;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.SettingsTab;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.TabSetWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.findDocumentsTab.FindDocumentTab;
import gov.nist.toolkit.xdstools3.client.events.EventHandler;
import gov.nist.toolkit.xdstools3.client.events.OpenTabEvent;

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

    private TabSetWidget topTabSet;





	public void onModuleLoad() {

		
		// Event handling in the GUI
				final SimpleEventBus bus = new SimpleEventBus();
        				bus.addHandler(OpenTabEvent.TYPE, new EventHandler(){
        					            public void onEvent(OpenTabEvent event) {
                                            openTab(event.getTabName());
        					            }
        					        });
				
	// The second part of this Eventbus example is located in LoginDialog.
	//				bus.addHandler(PingEvent.TYPE, new PingEventHandler(){
	//					            public void onEvent(PingEvent event) {
	//					                System.out.print("Inside Ping --> ");
	//					                new Timer(){
	//					                    public void run() {
	//					                       SC.say("pong event fired");
	//					                    }
	//					                }.schedule(1000);
	//					            }
	//					        });


		// Toolbar
		ConfigToolbar configBar = new ConfigToolbar(bus);

		// Tabs
		topTabSet = new TabSetWidget();
        CloseableTabWidget homeTab = new CloseableTabWidget("Home");  homeTab.setCanClose(false);
		CloseableTabWidget findDocsTab = new FindDocumentTab();
        CloseableTabWidget mpqTab = new MPQTab();
		
        // Add all tabs to the main TabSet
        topTabSet.addTab(homeTab);
        topTabSet.addTab(findDocsTab);
        topTabSet.addTab(mpqTab);

        //test
        CloseableTabWidget rest = new RestDs();
        topTabSet.addTab(rest);

        // Main layout
        VLayout mainLayout = new VLayout(); 
		mainLayout.setWidth(WINDOW_WIDTH);
		mainLayout.setHeight100(); 
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

	}

public void openTab(String tabName) {
    CloseableTabWidget tab = null;

    // create tab depending on parameter
    if (tabName == "ADMIN") {
        CloseableTabWidget adminTab = new SettingsTab();
        tab = adminTab;
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
