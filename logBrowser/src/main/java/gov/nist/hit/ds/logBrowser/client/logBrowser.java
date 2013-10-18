package gov.nist.hit.ds.logBrowser.client;


import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryServiceAsync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;



public class LogBrowser implements EntryPoint {

	TabPanel topPanel;
	VerticalPanel ht = null;
	SplitLayoutPanel splitPanel = new SplitLayoutPanel(5);
	ScrollPanel centerPanel = new ScrollPanel();
	
	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
    protected ArrayList<String> propNames = new ArrayList<String>();

	      
	public LogBrowser() {}
	
	/**
	 * This is the entry point method.
	 */	

	  public void onModuleLoad() {

	    // CwOptionalTextBox otb = new CwOptionalTextBox("Enable text input");
	    // RootPanel.get().add(panel);
	    
		  // splitPanel.addNorth(new HTML("Log Browser"), 20);
		  
		  splitPanel.getElement().getStyle()
	        .setProperty("border", "3px solid #e7e7e7");
		  /*
		  StyleInjector.inject(".gwt-SplitLayoutPanel .gwt-SplitLayoutPanel-HDragger "
                  + "{ width: 5px !important; background: green; }");
		  */

	    
	    try {
			reposService.setRepositoryConfig(new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean a){
					
					
					
					
					
					AsyncCallback<Map<String, String[]>> reposTags = new AsyncCallback<Map<String, String[]>>() {

						public void onFailure(Throwable a) {
							Window.alert(a.toString());
							
						}

						public void onSuccess(Map<String, String[]> a) {
							if (a==null) return;
							
							String[][] reposData = new String[a.size()][2];
							int cx=0;
							for (String key : a.keySet()) {
								reposData[cx][0] = key;
								reposData[cx++][1] = a.get(key)[1];								
							}
							AsyncCallback<List<AssetNode>> treeSetup = new AsyncCallback<List<AssetNode>>() {

								public void onFailure(Throwable a) {
									Window.alert(a.toString());
									
								}

								public void onSuccess(List<AssetNode> a) {
		
									splitPanel.addWest(popTreeWidget(a), 250);
									centerPanel.add(new HTML("Doc viewer panel goes here"));
									splitPanel.add(centerPanel);

									RootLayoutPanel.get().add(splitPanel);
								}
								
							};
							reposService.getAssetTree(reposData, treeSetup);
							
						}
						
					};
					reposService.getRepositoryDisplayTags(reposTags);
					
					/*
					AsyncCallback<List<String>> propsSetup = new AsyncCallback<List<String>>() {

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
					 */
				}
				public void onFailure(Throwable t) {Window.alert("Repository config failed: "+t.getMessage());}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}


	    
	    
	  }

	  protected Widget popTreeWidget(List<AssetNode> a) {
		    Tree tree = new Tree();
		    
		    // on selected handler here
		    tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
				
				public void onSelection(SelectionEvent<TreeItem> treeItem) {
					// Window.alert(((AssetTreeItem)treeItem.getSelectedItem()).getAssetId());

					
					
					AssetNode an = ((AssetTreeItem)treeItem.getSelectedItem()).getAssetNode();
					
					AsyncCallback<String> contentSetup = new AsyncCallback<String>() {

						public void onFailure(Throwable arg0) {
							centerPanel.clear();
							splitPanel.remove(centerPanel);
							centerPanel.add(new HTML("Content could not be loaded."));
							
						}

						public void onSuccess(String arg0) {
							centerPanel.clear();
							splitPanel.remove(centerPanel);
							
							HTML safeHtml = new HTML(SafeHtmlUtils.fromString(arg0));
							centerPanel.add(safeHtml);
							splitPanel.add(centerPanel);		
						}
						
					};
					reposService.getAssetTxtContent(an, contentSetup); 
					
					
					
					
				}
			});
		    
		    for (AssetNode an : a) {
		    	tree.addItem(createTreeItem(an));
		    }
		    
		    return tree;
	  }
	  
	  protected AssetTreeItem createTreeItem(AssetNode an) {
	        AssetTreeItem item = new AssetTreeItem(an);

	        for (AssetNode child : an.getChildren()) {
	            item.addItem(createTreeItem(child));
	        }
	        return item;
	    }
	  
	  protected class AssetTreeItem extends TreeItem {
	         public AssetTreeItem(AssetNode an) {
	            // super(an.getDisplayName());
	            setText(an.getDisplayName());
	            
	            String title = "mimeType: " + an.getMimeType();
	            
	            if (an.getDescription()!=null && !"".equals(an.getDescription())) {
	            	title += " Description: " + an.getDescription();
	            }
	            setTitle(title);
	            setUserObject(an);	            
	        }
	        public AssetNode getAssetNode() {
	            return (AssetNode) getUserObject();
	        }
	     }





}
