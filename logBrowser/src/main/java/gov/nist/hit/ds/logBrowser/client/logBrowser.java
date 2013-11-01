package gov.nist.hit.ds.logBrowser.client;


import gov.nist.hit.ds.logBrowser.client.sh.BrushFactory;
import gov.nist.hit.ds.logBrowser.client.sh.SyntaxHighlighter;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryServiceAsync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;



public class LogBrowser implements EntryPoint {

	private static Logger logger = Logger.getLogger(LogBrowser.class.getName());
	private static final int CONTEXTMENU_XOFFSET = 10;
	VerticalPanel treePanel = new VerticalPanel();
	SplitLayoutPanel splitPanel = new SplitLayoutPanel(5);
	ScrollPanel centerPanel = new ScrollPanel();
	SplitLayoutPanel westContent = new SplitLayoutPanel(2);
	
	HTML propsWidget = new HTML();
    ScrollPanel navScroller;
    ScrollPanel propsScroller;
    
	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
    protected ArrayList<String> propNames = new ArrayList<String>();
 
    		
    // private HandlerRegistration handlerRegistration;
    
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
							Window.alert("No repositories found. Error: " + a.toString());							
						}

						public void onSuccess(Map<String, String[]> a) {							
							ListBox reposLbx = new ListBox();
							
							String[][] reposData = new String[a.size()][2];
							int cx=0;
							for (String key : a.keySet()) {
								reposData[cx][0] = key;
								reposData[cx++][1] = a.get(key)[1];
								
								reposLbx.addItem(key, a.get(key)[1]);
							}
														
							Label lblRepos = new Label("Repository:");
							
							// lblRepos.setWidth("25%");
							reposLbx.setWidth("90px");
							FlexTable grid = new FlexTable();
							grid.setCellPadding(1);
							grid.setCellSpacing(2);
							grid.setWidget(0, 0, lblRepos );
							grid.setWidget(0, 1, reposLbx);
							
							treePanel.add(grid);

							SafeHtmlBuilder propsContent =  new SafeHtmlBuilder();
							propsWidget.setHTML(propsContent.toSafeHtml());
							ScrollPanel spProps = new ScrollPanel(propsWidget);
							westContent.addSouth(spProps, Math.round(0.2 * Window.getClientHeight()));
							
							treePanel.add(new HTML("&nbsp;"));
							final VerticalPanel treeHolder = new VerticalPanel();
							treeHolder.add(new HTML("&nbsp;Loading..."));
							treePanel.add(treeHolder);
							ScrollPanel sp = new ScrollPanel(treePanel);									
							westContent.add(sp); // added to north: Math.round(0.7 * Window.getClientHeight())
																
							splitPanel.addWest(westContent, 300); // 400  -- Math.round(.15 * Window.getClientWidth())
							centerPanel.add(new HTML("<h2 style='color:maroon'>Recent Activity</h2><hr/>")); // Startup message
							splitPanel.add(centerPanel);
							
							RootLayoutPanel.get().add(splitPanel);
							
							final AsyncCallback<List<AssetNode>> treeSetup = new AsyncCallback<List<AssetNode>>() {

								public void onFailure(Throwable a) {
									Window.alert(a.toString());									
								}

								public void onSuccess(List<AssetNode> a) {
									treeHolder.clear();
									treeHolder.add(popTreeWidget(a));

								}
								
							};
							reposService.getAssetTree(new String[][]{{reposData[0][0],reposData[0][1]}}, treeSetup);
						
							if (reposLbx.getItemCount()>0) {
								reposLbx.addChangeHandler(new ChangeHandler() {
									
									public void onChange(ChangeEvent event) {
										treeHolder.clear();
										treeHolder.add(new HTML("&nbsp;Loading..."));
										centerPanel.clear();
										propsWidget.setHTML("");
										
										ListBox lbx = ((ListBox)event.getSource());
										int idx = lbx.getSelectedIndex();
										
										reposService.getAssetTree(new String[][]{{lbx.getItemText(idx),lbx.getValue(idx)}}, treeSetup);		
									}
								});
							}
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
			logger.log(Level.SEVERE,"main setRepositoryConfig failed: " + e.toString());
		}


	    
	    
	  }

	  protected Widget popTreeWidget(List<AssetNode> a) {
		    Tree tree = new Tree();
		    final PopupPanel menu = new PopupPanel(true);
		    
		    // context menu
		    tree.addDomHandler(new ContextMenuHandler() {
				
				public void onContextMenu(ContextMenuEvent event) {
					event.preventDefault();
					event.stopPropagation();
					
					menu.addDomHandler(new ClickHandler() {
						
						public void onClick(ClickEvent arg0) {
							menu.hide();
						}
					}, ClickEvent.getType());
					menu.addDomHandler(new MouseOutHandler() {
						
						public void onMouseOut(MouseOutEvent arg0) {
							menu.hide();
							
						}
					}, MouseOutEvent.getType());

					// Window.alert(((Tree)event.getSource()).getSelectedItem().getText());					
					
					VerticalPanel menuItemPanel = new VerticalPanel();
					
					try {
						
						AssetNode an =  (AssetNode)((Tree)event.getSource()).getSelectedItem().getUserObject();
						if (an.isContentAvailable()) {
							menuItemPanel.add(new Anchor("Download content",GWT.getHostPageBaseURL() + "repository/downloadAsset?reposSrc="+ an.getReposSrc() +"&reposId=" + an.getRepId() + "&assetId=" + an.getAssetId() + "&contentDisp=attachment"));
						}
					} catch (Exception ex) {
						logger.log(Level.SEVERE,"Download link builder failed:" + ex.toString());
					}
					menuItemPanel.add(new Label("Copy file path"));
					
					menu.setWidget(menuItemPanel);
					menu.setPopupPosition(event.getNativeEvent().getClientX()+CONTEXTMENU_XOFFSET, event.getNativeEvent().getClientY());
					menu.show();
					
					
				}
			}, ContextMenuEvent.getType());

		    // on selected handler here
		    tree.addSelectionHandler(new SelectionHandler<TreeItem>() {				
		    	
				public void onSelection(SelectionEvent<TreeItem> treeItem) {
					// Window.alert(((AssetTreeItem)treeItem.getSelectedItem()).getAssetId());
					AssetNode an = ((AssetTreeItem)treeItem.getSelectedItem()).getAssetNode();
					
					AsyncCallback<AssetNode> contentSetup = new AsyncCallback<AssetNode>() {

						public void onFailure(Throwable arg0) {
							centerPanel.clear();							
							centerPanel.add(new HTML("Content could not be loaded. " + arg0.toString()));
							propsWidget.setHTML("");
						}

						public void onSuccess(AssetNode an) {
							centerPanel.clear();
							splitPanel.remove(centerPanel);
							
							// HTML safeHtml = new HTML(SafeHtmlUtils.fromString(an.getTxtContent()));
							
							// westContent.remove(propsWidget);
							SafeHtmlBuilder propsContent =  new SafeHtmlBuilder();
							propsContent.appendHtmlConstant("Asset Properties:<pre><span style='font-style:arial,verdana;font-size: 12px;color:maroon'>").appendEscaped(an.getProps()).appendHtmlConstant("</span></pre>");
							propsWidget.setWidth("250px");
							propsWidget.setHTML(propsContent.toSafeHtml());
							
							propsWidget.getElement().getStyle()
					        .setProperty("borderTop", "1px dotted #e7e7e7"); // 1px solid #e7e7e7
							propsWidget.getElement().getStyle()
					        .setProperty("borderBottom", "1px dotted #e7e7e7"); // 1px solid #e7e7e7
							
							// westContent.add(propsWidget, DockPanel.SOUTH);
							
							// westContent.add(propsWidget);
							if (an.isContentAvailable()) {
								if ("text/csv".equals(an.getMimeType())) {
									   CellTable<List<String>> table = createCellTable(an.getCsv());								    
									    centerPanel.add(table);							    
								} else if ("text/xml".equals(an.getMimeType()) || "application/soap+xml".equals(an.getMimeType())) {
									String xmlStr = an.getTxtContent().replace("<br/>", "\r\n");
									String shStr = SyntaxHighlighter.highlight(xmlStr, BrushFactory.newXmlBrush() , false);
									centerPanel.add(new HTML(shStr));
								} else {
									centerPanel.add(new HTML(an.getTxtContent()));	
								}								
							} else {
								centerPanel.add(new HTML("<!-- Content not available. -->"));
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
		    	AssetTreeItem treeItem = createTreeItem(an);
		    	treeItem.setState(true);
		    	tree.addItem(treeItem);
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
	        	 String displayName = "" + an.getAssetId();
	        	 
	        	if (an.getDisplayName()!=null && !"".equals(an.getDisplayName())) {
	        		displayName = an.getDisplayName();
	        	}
	            setText(displayName);
	            
	            String title = "";
	            
	            if (an.getMimeType()!=null && !"".equals(an.getMimeType())) {
	            	title += "mimeType: " + an.getMimeType();
	            }
	            
	            if (an.getDescription()!=null && !"".equals(an.getDescription())) {
	            	title += " Description: " + an.getDescription();
	            }
	            setTitle(title);
	            setUserObject(an);	            
	            // setWidget(new Label(displayName));	            
	        }
	    
			public AssetNode getAssetNode() {
	            return (AssetNode) getUserObject();
	        }
		
	     }


}
