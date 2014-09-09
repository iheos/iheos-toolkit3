package gov.nist.toolkit.xdstools2.client.tabs.testRunnerTab;

import gov.nist.toolkit.xdstools2.client.TabContainer;
import gov.nist.toolkit.xdstools2.client.Toolkit2ServiceAsync;
import gov.nist.toolkit.xdstools2.client.tabs.testRunnerTab.TestRunnerSectionsView.ViewLogButton;

import java.util.ArrayList;
import java.util.List;

public class TestRunnerSectionsPresenter {
	
	interface SectionsDisplay {
		void initTestGrid(List<TestDetailsModel> details);
		public ViewLogButton updateTestRow(TestDetailsModel model);
	}
	
	SectionsDisplay display;
	Toolkit2ServiceAsync toolkitService;
	TabContainer container;
	String selectedTest;
	List<String> sections = new ArrayList<String>();
	
	TestRunnerSectionsPresenter(TabContainer container, SectionsDisplay d, Toolkit2ServiceAsync toolkitService, String selectedTest) {
		display = d;
		this.toolkitService = toolkitService;
		this.container = container;
		this.selectedTest = selectedTest;
		
		bind();
		
		
	}
	
	void bind() {
		
	}



}
