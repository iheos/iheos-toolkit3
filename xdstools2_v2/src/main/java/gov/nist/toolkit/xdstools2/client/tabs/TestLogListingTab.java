package gov.nist.toolkit.xdstools2.client.tabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gov.nist.toolkit.results.client.Result;
import gov.nist.toolkit.xdstools2.client.TabContainer;
import gov.nist.toolkit.xdstools2.client.Toolkit2Service;
import gov.nist.toolkit.xdstools2.client.Toolkit2ServiceAsync;
import gov.nist.toolkit.xdstools2.client.adapter2v3.PopupMessageV3;
import gov.nist.toolkit.xdstools2.client.adapter2v3.TopWindowPanel;
import gov.nist.toolkit.xdstools2.client.inspector.MetadataInspectorTab;
import gov.nist.toolkit.xdstools2.client.siteActorManagers.GetDocumentsSiteActorManager;
import gov.nist.toolkit.xdstools2.client.tabs.genericQueryTab.GenericQueryTab;

import java.util.List;

public class TestLogListingTab extends GenericQueryTab {
	final protected Toolkit2ServiceAsync toolkitService = GWT
	.create(Toolkit2Service.class);

	FlexTable grid = new FlexTable();
	HorizontalPanel testSessionPanel = new HorizontalPanel();
	ListBox testSessionList = new ListBox();
	String testSession = null;
	boolean isPrivateTesting = false;


	public TestLogListingTab() {
		super(new GetDocumentsSiteActorManager());
	}

	public void onTabLoad(TabContainer container, boolean select, String eventName) {
		myContainer = container;
		topPanel = new TopWindowPanel();


		container.addTab(topPanel, "TestLog Listing", select);
		addCloseButton(container,topPanel, null);

		HTML title = new HTML();
		title.setHTML("<h2>TestLog Listing</h2>");
		topPanel.add(title);
		
		// test session
		testSessionPanel.setVisible(false);
		topPanel.add(testSessionPanel);
		
		HTML testSessionLabel = new HTML();
		testSessionLabel.setText("Test Session: ");
		testSessionPanel.add(testSessionLabel);
		
		testSessionPanel.add(testSessionList);
		testSessionList.addChangeHandler(new TestSessionChangeHandler());
		loadTestSessionEnabled();
		
		
		topPanel.add(grid);
		
	}
	
	void loadTestSessionEnabled() {
		toolkitService.isPrivateMesaTesting(new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				new PopupMessageV3("isPrivateMesaTesting: " + caught.getMessage());
			}

			public void onSuccess(Boolean result) {
				isPrivateTesting = result;
				if (!result)
					return;
				loadTestSessionNames();
			}
			
		});
	}
	
	void loadTestSessionNames() {
		toolkitService.getMesaTestSessionNames(new AsyncCallback<List<String>>() {

			public void onFailure(Throwable caught) {
				new PopupMessageV3("getMesaTestSessionNames: " + caught.getMessage());
			}

			public void onSuccess(List<String> result) {
				testSessionList.clear();
				testSessionList.addItem("-- Choose --", "");
				for (String val : result)
					testSessionList.addItem(val);
				
				testSessionPanel.setVisible(true);
			}
			
		});
	}
	
	
	void loadTestNumbers() {
		toolkitService.getTestlogListing(testSession, new AsyncCallback<List<String>>() {

			public void onFailure(Throwable caught) {
				new PopupMessageV3("getTestlogListing: " + caught.getMessage());
			}

			public void onSuccess(List<String> result) {
				int row=0;
				
				for (String tnum : result) {
					HTML h = new HTML();
					h.setText(tnum);
					grid.setWidget(row, 0, h);
					Button inspect = new Button("Inspect");
					inspect.addClickHandler(new InspectButtonClickHandler(tnum));
					grid.setWidget(row, 1, inspect);
					
					row++;
				}
			}
			
		});
	}
	
	class InspectButtonClickHandler implements ClickHandler {
		String testName;
		
		InspectButtonClickHandler(String testName) {
			this.testName = testName;
		}

		public void onClick(ClickEvent event) {
			toolkitService.getLogContent(testSession, testName, new AsyncCallback<List<Result>>() {

				public void onFailure(Throwable caught) {
					new PopupMessageV3("getLogsForTest: " + caught.getMessage());
				}

				public void onSuccess(List<Result> results) {
					MetadataInspectorTab itab = new MetadataInspectorTab();
					itab.setToolkitService(toolkitService);
					itab.setResults(results);
					itab.setSiteSpec(null);
					itab.onTabLoad(myContainer, true, null);
				}
				
			});
		}
	}

	class TestSessionChangeHandler implements ChangeHandler {

		public void onChange(ChangeEvent event) {
			int selectionI = testSessionList.getSelectedIndex();
			testSession = testSessionList.getItemText(selectionI);
			if ("".equals(testSession))
				testSession = null;

			loadTestNumbers();

		}
		
	}
	

	public String getWindowShortName() {
		return "testloglisting";
	}

}
