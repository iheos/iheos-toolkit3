package gov.nist.toolkit.xdstools3.client;

import gov.nist.toolkit.xdstools3.client.customWidgets.ConfigToolbar;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.CloseableTabWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.ConfigurationTab;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.FindDocumentTab;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.GenericTabSetWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

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



	public void onModuleLoad() {

		// Toolbar
		ConfigToolbar configBar = new ConfigToolbar();

		// Tabs
		final GenericTabSetWidget topTabSet = new GenericTabSetWidget();  
		CloseableTabWidget findDocsTab = new FindDocumentTab(); 
		CloseableTabWidget configTab = new ConfigurationTab(); 
        CloseableTabWidget homeTab = new CloseableTabWidget("Home"); 
		 homeTab.setCanClose(false);
        
        // Add all tabs to the main TabSet
        topTabSet.addTab(homeTab); 
        topTabSet.addTab(findDocsTab); 
        topTabSet.addTab(configTab);

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

		// Starting data controller
		//  Controller c = new Controller();
	}



	public static int getWindowWidth() {
		return WINDOW_WIDTH_TO_INTEGER;
	}

}
