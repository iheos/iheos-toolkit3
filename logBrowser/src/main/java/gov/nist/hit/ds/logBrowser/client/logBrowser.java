package gov.nist.hit.ds.logBrowser.client;


import java.util.ArrayList;
import java.util.List;


import gov.nist.hit.ds.logBrowser.client.widgets.CwOptionalTextBox;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryServiceAsync;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;



public class LogBrowser implements EntryPoint {

	TabPanel topPanel;
	VerticalPanel ht = null;

	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
    protected ArrayList<String> propNames = new ArrayList<String>();
    
	public LogBrowser() {}
	
	/**
	 * This is the entry point method.
	 */	

	  public void onModuleLoad() {
//	    Button b = new Button("Test Ajax", new ClickHandler() {
//	      public void onClick(ClickEvent event) {
//	        Window.alert("Ajax");
//	      }
//	    });
//
//	    RootPanel.get().add(b);
	 
		  VerticalPanel panel = new VerticalPanel();
		  FlexTable grid = new FlexTable();

			panel.add(new HTML("<h3>Test area</h3>"));
			panel.add(new HTML("  " ));

			panel.add(grid);

			int row = 0;
			int col = 0;
			HTML h;
			
		    CwOptionalTextBox otb = new CwOptionalTextBox("Enable text input");
			
			
			grid.setWidget(row, col, otb);
			
			
			HTMLTable.CellFormatter formatter = grid.getCellFormatter();
			 formatter.setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
			 formatter.setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_MIDDLE);
			 
			
			
			// Element e = DOM.getElementById("demoTable");
			
			panel.setWidth(	"800px");
	    
	    RootPanel.get().add(panel);
	    
	    Tree tree = new Tree();
	    List<AssetNode> assetNodes = null;


	    
	    try {
			reposService.setRepositoryConfig(new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean a){
					AsyncCallback<List<String>> propsSetup = new AsyncCallback<List<String>> () {

						public void onFailure(Throwable a) {
							Window.alert("No indexeable properties found: It is possible Asset Type's are not configured. propNames could not be loaded: " + a.getMessage());
						}

						public void onSuccess(List<String> props) {
							if (props!=null) {
								propNames.addAll(props);

								String hOut = "";
								for (String s : propNames) {
									hOut += s;
								}
								RootPanel.get().add(new HTML(hOut));
							}
							

							
						}
					};
					reposService.getIndexablePropertyNames(propsSetup);
				}
				public void onFailure(Throwable t) {Window.alert("Repository config failed: "+t.getMessage());}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}


	    
	    
	  }


	private void setup() {
		HTML title = new HTML();
		title.setHTML("<h2>Composite widget prototype</h2>");
		topPanel.add(title);
		
		RootPanel.get().add(topPanel);
	}





}
