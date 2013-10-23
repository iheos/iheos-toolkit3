package gov.nist.hit.ds.logBrowser.client;


import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryServiceAsync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;



public class LogBrowser implements EntryPoint {

	TabPanel topPanel;
	VerticalPanel ht = null;
	SplitLayoutPanel splitPanel = new SplitLayoutPanel(5);
	ScrollPanel centerPanel = new ScrollPanel();
	SplitLayoutPanel westContent = new SplitLayoutPanel(2);
	HTML propsWidget = new HTML();
    ScrollPanel navScroller;
    ScrollPanel propsScroller;
    		
	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
    protected ArrayList<String> propNames = new ArrayList<String>();
 
    		
    private HandlerRegistration handlerRegistration;
    
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

									/*
									handlerRegistration = Window.addResizeHandler(new ResizeHandler() {

										public void onResize(ResizeEvent arg0) {

											//scroller.setSize(Math.round(.15 * Window.getClientWidth())+"px" , Math.round(0.8 * Window.getClientHeight()) + "px");
											navScroller.setHeight(Math.round(0.8 * Window.getClientHeight()) + "px");
											propsScroller.setHeight(Math.round(0.2 * Window.getClientHeight()) + "px");
											
											// Window.alert(Math.round(0.8 * Window.getClientHeight()) + "px");
											
											
										}
										
									});
									*/
									
									
									// westContent.setStyleName("cw-DockPanel");
//									westContent.setSpacing(1);
//									westContent.setVerticalAlignment(DockPanel.ALIGN_TOP);
								    
//									navScroller = new ScrollPanel(popTreeWidget(a));
//								    navScroller.setSize("300px",  Math.round(0.8 * Window.getClientHeight()) + "px");
//									// navScroller.setHeight(Math.round(0.8 * Window.getClientHeight()) + "px");
//								    navScroller.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
									
									westContent.addNorth(popTreeWidget(a), Math.round(0.7 * Window.getClientHeight()));
									
									// westContent.add(navScroller, DockPanel.NORTH);
									SafeHtmlBuilder propsContent =  new SafeHtmlBuilder();
									propsWidget.setHTML(propsContent.toSafeHtml());
									
//									propsScroller = new ScrollPanel(propsWidget);
//									propsScroller.setHeight(Math.round(0.2 * Window.getClientHeight()) + "px");
//									propsScroller.getElement().getStyle().setOverflowY(Overflow.AUTO);
									//// propsScroller.setSize(Math.round(.15 * Window.getClientWidth())+"px", Math.round(0.8 * Window.getClientHeight()) + "px");
									
									// westContent.add(propsScroller, DockPanel.SOUTH);									
									
									westContent.add(propsWidget);
									
									splitPanel.addWest(westContent, 300); // 400  -- Math.round(.15 * Window.getClientWidth())
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
					
					AsyncCallback<AssetNode> contentSetup = new AsyncCallback<AssetNode>() {

						public void onFailure(Throwable arg0) {
							centerPanel.clear();
							splitPanel.remove(centerPanel);
							centerPanel.add(new HTML("Content could not be loaded."));
							
						}

						public void onSuccess(AssetNode an) {
							centerPanel.clear();
							splitPanel.remove(centerPanel);
							
							// HTML safeHtml = new HTML(SafeHtmlUtils.fromString(an.getTxtContent()));
							
							
							
							westContent.remove(propsWidget);
							SafeHtmlBuilder propsContent =  new SafeHtmlBuilder();
							propsContent.appendHtmlConstant("Asset Properties:<pre><span style='font-style:arial,verdana;font-size: 12px;color:maroon'>").appendEscaped(an.getProps()).appendHtmlConstant("</span></pre>");
							propsWidget.setWidth("250px");
							propsWidget.setHTML(propsContent.toSafeHtml());
							
							propsWidget.getElement().getStyle()
					        .setProperty("borderTop", "1px dotted #e7e7e7"); // 1px solid #e7e7e7
							propsWidget.getElement().getStyle()
					        .setProperty("borderBottom", "1px dotted #e7e7e7"); // 1px solid #e7e7e7
							
							// westContent.add(propsWidget, DockPanel.SOUTH);
							
							westContent.add(propsWidget);
							
							if ("text/csv".equals(an.getMimeType())) {
								   CellTable<List<String>> table = createCellTable(an.getCsv());								    
								    centerPanel.add(table);							    
							} else {
								centerPanel.add(new HTML(an.getTxtContent()));	
							}
							
							
							splitPanel.add(centerPanel);		
						}

						/**
						 * @param an
						 * @return
						 */
						private CellTable<List<String>> createCellTable(String [][]csv) {
							// Create a CellTable (based on Stack ans. 15122103).
							 CellTable<List<String>> table = new CellTable<List<String>>();							 							 
							 
							 
							 // Get the rows as List
							    int rowLen = csv.length;
							    int colLen = csv[0].length;
							    List<List<String>> rows = new ArrayList<List<String>>(rowLen);
							    
							    for (int r = 1; r < rowLen; r++) {
							        List<String> row = Arrays.asList(csv[r]);
							        rows.add(row);
							    }  

							    // Create table columns
							    for (int c = 0; c < colLen; c++) {
							        table.addColumn(new IndexedColumn(c), 
							              new TextHeader(csv[0][c]));
							    }
							    
							    // Create a list data provider.
							    final ListDataProvider<List<String>> dataProvider  = new ListDataProvider<List<String>>();
							    dataProvider.setList(rows);
							    
							    dataProvider.addDataDisplay(table);
							return table;
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
	  
	  class IndexedColumn extends Column<List<String>, String> {
		    private final int index;
		    public IndexedColumn(int index) {
		        super(new TextCell());
		        this.index = index;
		    }
		    @Override
		    public String getValue(List<String> object) {
		        return object.get(this.index);
		    }
	  }
	  
	  protected class AssetTreeItem extends TreeItem {
	         public AssetTreeItem(AssetNode an) {
	            // super(an.getDisplayName());
	            setText(an.getDisplayName());
	            
	            String title = "";
	            
	            if (an.getMimeType()!=null && !"".equals(an.getMimeType())) {
	            	title += "mimeType: " + an.getMimeType();
	            }
	            
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
