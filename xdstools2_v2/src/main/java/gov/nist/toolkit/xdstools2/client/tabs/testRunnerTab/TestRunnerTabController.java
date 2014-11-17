package gov.nist.toolkit.xdstools2.client.tabs.testRunnerTab;

import gov.nist.toolkit.xdstools2.client.TabContainer;
import gov.nist.toolkit.xdstools2.client.adapter2v3.TopWindowPanel;
import gov.nist.toolkit.xdstools2.client.siteActorManagers.GetDocumentsSiteActorManager;
import gov.nist.toolkit.xdstools2.client.tabs.genericQueryTab.GenericQueryTab;

public class TestRunnerTabController extends GenericQueryTab {

	public TestRunnerTabController() {
		super(new GetDocumentsSiteActorManager());
	}

	public void onTabLoad(TabContainer container, boolean select) { }

	public void onTabLoad(TabContainer container, boolean select, String eventName) {
		myContainer = container;
		
		// build panel infrastructure
		// relies on GenericQueyTab which inherits from TabbedWindow
		topPanel = new TopWindowPanel();
		container.addTab(topPanel, eventName, select);
		addCloseButton(container, topPanel, null);
		
		TestRunnerView view = new TestRunnerView();
		new TestRunnerPresenter(container, view, toolkitService);
		
		view.build(eventName, select, container, topPanel, toolkitService);
	}

	  
	public String getWindowShortName() {
		return "TestRunner";
	}


}
