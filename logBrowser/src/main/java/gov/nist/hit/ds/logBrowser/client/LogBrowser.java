package gov.nist.hit.ds.logBrowser.client;


import gov.nist.hit.ds.logBrowser.client.event.AssetClickedEvent;
import gov.nist.hit.ds.logBrowser.client.event.AssetClickedEventHandler;
import gov.nist.hit.ds.logBrowser.client.sh.BrushFactory;
import gov.nist.hit.ds.logBrowser.client.sh.SyntaxHighlighter;
import gov.nist.hit.ds.logBrowser.client.widgets.SearchWidget;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryServiceAsync;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryTag;
import gov.nist.hit.ds.repository.simple.search.client.exception.RepositoryConfigException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;


public class LogBrowser implements EntryPoint {

	private static Logger logger = Logger.getLogger(LogBrowser.class.getName());
	TabLayoutPanel featureTlp = new TabLayoutPanel(20, Unit.PX);
	private static final int CONTEXTMENU_XOFFSET = 10;
	VerticalPanel treePanel = new VerticalPanel();
	SplitLayoutPanel splitPanel = new SplitLayoutPanel(5);
	ScrollPanel centerPanel = new ScrollPanel();
	SplitLayoutPanel westContent = new SplitLayoutPanel(2);
	ListBox reposLbx = new ListBox();
	
	HTML propsWidget = new HTML();
    ScrollPanel navScroller;
    ScrollPanel propsScroller;
    
	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
    protected ArrayList<String> propNames = new ArrayList<String>();
    protected Map<String, String> reposProps = new HashMap<String,String>();
    AssetTreeItem treeItemTarget = null;     

    SimpleEventBus eventBus;
    

	public LogBrowser() {}
	
	/**
	 * This is the entry point method.
	 */	

	  public void onModuleLoad() {

		 eventBus = new SimpleEventBus();
		  
		 featureTlp.add(setupBrowseFeature(),"Browse");
		 
	    
		 RootLayoutPanel.get().add(featureTlp);
	  }
	  
	  protected SplitLayoutPanel setupSearchFeature() {
		    SplitLayoutPanel searchMainLayoutPanel = new SplitLayoutPanel(5); // Search-main panel
		    searchMainLayoutPanel.getElement().getStyle()
	        .setProperty("border", "3px solid #e7e7e7"); //

		    final SplitLayoutPanel searchLbSplitPanel = new SplitLayoutPanel(1);
			final ScrollPanel searchLbCenterPanel = new ScrollPanel();
			SplitLayoutPanel searchLbWestContent = new SplitLayoutPanel(2);
		    			
			final VerticalPanel searchLbTreeHolder = new VerticalPanel();
			final HTML searchLbPropsWidget = new HTML();
			// searchLbTreeHolder.add(new HTML("&nbsp;Loading..."));
			ScrollPanel sp = new ScrollPanel(searchLbTreeHolder);
			ScrollPanel spProps = new ScrollPanel(searchLbPropsWidget);
			searchLbWestContent.addSouth(spProps, Math.round(0.2 * Window.getClientHeight()));
			searchLbWestContent.add(sp); 

			searchLbSplitPanel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
			searchLbSplitPanel.addWest(searchLbWestContent, 300); // 400  -- Math.round(.15 * Window.getClientWidth())
			searchLbSplitPanel.add(searchLbCenterPanel);
			
		    ScrollPanel searchPanel = new ScrollPanel(); 				// Search parameters 
		    
		    SearchWidget searchWidget = new SearchWidget(eventBus);
		    searchWidget.getElement().getStyle()
	        .setProperty("border", "none");
		    searchWidget.getElement().getStyle()
		    .setPaddingLeft(3, Unit.PX);

		    searchPanel.add(searchWidget);
		    searchMainLayoutPanel.addWest(searchPanel, 632); // Math.round(0.40 * Window.getClientWidth())
		    searchMainLayoutPanel.add(searchLbSplitPanel);

		    
		    final AsyncCallback<AssetNode> searchLbContentSetup = new AsyncCallback<AssetNode>() {
				public void onFailure(Throwable arg0) {
					searchLbSplitPanel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
					searchLbCenterPanel.clear();							
					searchLbCenterPanel.add(new HTML("Content could not be loaded. " + arg0.toString()));
					// propsWidget.setHTML("");
				}

				public void onSuccess(AssetNode an) {
					searchLbSplitPanel.getElement().getStyle().setVisibility(Visibility.VISIBLE);
					displayAssetContent(an, searchLbCenterPanel, searchLbPropsWidget);
				}
				
			};

		    
		    eventBus.addHandler(AssetClickedEvent.TYPE, new AssetClickedEventHandler() {								
				public void onAssetClick(AssetClickedEvent event) {
					try {
						final AssetNode target = event.getValue(); 
						reposService.getParentChain(target, new AsyncCallback<AssetNode>(){

							public void onFailure(Throwable arg0) {												
								searchLbPropsWidget.setHTML("Search result action could not be synchronized with the tree: " + arg0.toString()); 
							}
							public void onSuccess(AssetNode an) {
								searchLbTreeHolder.clear();
								List<AssetNode> topLevelAsset = new ArrayList<AssetNode>();
								topLevelAsset.add(an);
								searchLbTreeHolder.add(popTreeWidget(topLevelAsset,target,true, searchLbContentSetup));
								reposService.getAssetTxtContent(target, searchLbContentSetup);
							}});
					} catch (RepositoryConfigException e) {
						e.printStackTrace();
					}

				}
			}); 

		    						    
		    return searchMainLayoutPanel;

	  }
	  
	  protected SplitLayoutPanel setupBrowseFeature() {
				  
		  // splitPanel.addNorth(new HTML("Log Browser"), 20);
		  
		  splitPanel.getElement().getStyle()
	        .setProperty("border", "3px solid #e7e7e7"); // 
		  /*
		  StyleInjector.inject(".gwt-SplitLayoutPanel .gwt-SplitLayoutPanel-HDragger "
               + "{ width: 5px !important; background: green; }");
		  */
		  
		 
	    try {
			reposService.setRepositoryConfig(new AsyncCallback<Boolean>(){
				public void onSuccess(Boolean a){

					AsyncCallback<List<RepositoryTag>> reposTags = new AsyncCallback<List<RepositoryTag>>() {

						public void onFailure(Throwable a) {
							Window.alert("No repositories found. Error: " + a.toString());							
						}

						public void onSuccess(List<RepositoryTag> rtList) {
							
							SafeHtmlBuilder propsContent =  new SafeHtmlBuilder();
							propsWidget.setHTML(propsContent.toSafeHtml());
														
							String[][] reposData = new String[rtList.size()][2];
							int cx=0;
							for (RepositoryTag rt : rtList) {
//								if (cx==0) {
//									String propsTxt = a.get(key)[2];									
//									propsContent.appendHtmlConstant("<div style='margin:3px;'>Repository Properties:<pre style='margin-top:0px;'><span style='font-family:courier,fixed;font-size: 12px;color:maroon'>").appendEscaped(propsTxt).appendHtmlConstant("</span></pre>");
//									propsContent.appendHtmlConstant("</div>");
//									propsWidget.setHTML(propsContent.toSafeHtml());												
//								}
								reposData[cx][0] =  rt.getId(); //a.get(key)[0];
								reposData[cx++][1] = rt.getSource(); // a.get(key)[1];
								
								reposLbx.addItem(rt.getDisplayName(), rt.getCompositeId());
								reposProps.put(rt.getCompositeId(), rt.getProperties());
								
								
							}
														
							HTML lblRepos = new HTML("Repository: ");
							
							// lblRepos.setWidth("25%");
							reposLbx.setWidth("125px"); // 90
							FlexTable grid = new FlexTable();
							grid.setCellPadding(1);
							grid.setCellSpacing(3);
							grid.setBorderWidth(0);
//							grid.setWidget(0, 0, new HTML("&nbsp;"));
//							grid.getFlexCellFormatter().setColSpan(0, 0, 2);
							grid.setWidget(0, 0, lblRepos );
							grid.setWidget(0, 1, reposLbx);
							
							treePanel.add(new HTML("&nbsp;"));
							treePanel.add(grid);


							ScrollPanel spProps = new ScrollPanel(propsWidget);
							westContent.addSouth(spProps, Math.round(0.2 * Window.getClientHeight()));
							
							// treePanel.add(new HTML("&nbsp;"));
							final VerticalPanel treeHolder = new VerticalPanel();
							treeHolder.add(new HTML("&nbsp;Loading..."));
							treePanel.add(treeHolder);
							ScrollPanel sp = new ScrollPanel(treePanel);									
							westContent.add(sp); // added to north: Math.round(0.7 * Window.getClientHeight())
																
							splitPanel.addWest(westContent, 300); // 400  -- Math.round(.15 * Window.getClientWidth())
							// centerPanel.add(new HTML("<h2 style='color:maroon'>Recent Activity</h2><hr/>")); // Startup message
							
							// before tab: splitPanel.add(centerPanel);
							
						    centerPanel.getElement().getStyle()
					        .setProperty("border", "none");					   						    
						    
						    splitPanel.add(centerPanel);						   
							
							// RootLayoutPanel.get().add(splitPanel);
							
						    
						    final AsyncCallback<AssetNode> contentSetup = new AsyncCallback<AssetNode>() {
								public void onFailure(Throwable arg0) {
									centerPanel.clear();							
									centerPanel.add(new HTML("Content could not be loaded. " + arg0.toString()));
									propsWidget.setHTML("");
								}

								public void onSuccess(AssetNode an) {
									displayAssetContent(an, centerPanel, propsWidget);
								}
								
							};
						    
							final AsyncCallback<List<AssetNode>> treeSetup = new AsyncCallback<List<AssetNode>>() {

								public void onFailure(Throwable a) {
									Window.alert(a.toString());									
								}

								public void onSuccess(List<AssetNode> a) {
									treeHolder.clear();
									treeHolder.add(popTreeWidget(a,null,false, contentSetup));
									
									// populate repository props here
									propsWidget.setHTML("");
									SafeHtmlBuilder propsContent =  new SafeHtmlBuilder();
									int idx = reposLbx.getSelectedIndex();
									String propsTxt = reposProps.get(reposLbx.getValue(idx)); // use getItemText for display text 
									if (propsTxt!=null) {
										// margin-top:0px;margin-left:3px;
										propsContent.appendHtmlConstant("<div style='margin:3px;'>Repository Properties:<pre style='margin-top:0px;'><span style='font-family:courier,fixed;font-size: 12px;color:maroon'>").appendEscaped(propsTxt).appendHtmlConstant("</span></pre>");
										propsContent.appendHtmlConstant("</div>");
										// propsWidget.setWidth("250px");
										propsWidget.setHTML(propsContent.toSafeHtml());											
									}
									
								}
								
							};
							reposService.getAssetTree(new String[][]{{reposData[0][0],reposData[0][1]}}, treeSetup);
						
							if (reposLbx.getItemCount()>0) {
								reposLbx.addChangeHandler(new ChangeHandler() {
									
									public void onChange(ChangeEvent event) {
										treeHolder.clear();
										treeHolder.add(new HTML("&nbsp;Loading..."));
										centerPanel.clear();

										
										
										ListBox lbx = ((ListBox)event.getSource());
										int idx = lbx.getSelectedIndex();
										
										// reposService.getAssetTree(new String[][]{{lbx.getItemText(idx),lbx.getValue(idx)}}, treeSetup);
										String[] compositeKey = lbx.getValue(idx).split("\\^");
										
										reposService.getAssetTree(new String[][]{{compositeKey[0],compositeKey[1]}}, treeSetup);
										
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
					
					featureTlp.add(setupSearchFeature(), "Search");
				}
				public void onFailure(Throwable t) {Window.alert("Repository config failed: "+t.getMessage());}
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE,"main setRepositoryConfig failed: " + e.toString());
		}
	    
	    return splitPanel;
	    
	  }
	  
	  protected TabPanel addTab(Widget w, String lbl) {
	      	
	      	TabPanel tp = new TabPanel();
	      	tp.setVisible(true);

	      	w.setVisible(true);
	      	tp.add(w,lbl);
	      	return tp;
	      	
	    }


	  protected Widget popTreeWidget(List<AssetNode> a, AssetNode target, Boolean expandLeaf, final AsyncCallback<AssetNode> contentSetup) {
		    Tree tree = new Tree();
		    final PopupPanel menu = new PopupPanel(true);
		    
		    // dynamic children pop
		    tree.addOpenHandler(new OpenHandler<TreeItem>() {

		        public void onOpen(OpenEvent<TreeItem> event) {		   

		        	   final TreeItem item = event.getTarget();
		               if (item.getChildCount() == 1 && "HASCHILDREN".equals(item.getChild(0).getText())) {
		            	   
		            	   AssetNode an =  (AssetNode)item.getUserObject();
		            	   
		                 // Close the item immediately
		                 item.setState(false, false);
               
		                 final AsyncCallback<List<AssetNode>> addImmediateChildren = new AsyncCallback<List<AssetNode>>() {

								public void onFailure(Throwable a) {
									Window.alert(a.toString());									
								}

								public void onSuccess(List<AssetNode> a) {
									for (AssetNode an : a) {
								    	AssetTreeItem treeItem = createTreeItem(an, null, false);
								    	item.addItem(treeItem);
								    	item.setState(true); // Open node
								    }									
								}
								
							};

		                 try {
							reposService.getImmediateChildren(an, addImmediateChildren);
						} catch (RepositoryConfigException e) {
							e.printStackTrace();
						}
		                 

		                 // Remove the temporary item when we finish loading
		                 item.getChild(0).remove();

		                 // Reopen the item
		                 item.setState(true, false);
		               }
		        }

		      });
		    
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
							menuItemPanel.add(new Anchor("Download content"
									,GWT.getHostPageBaseURL() + "repository/downloadAsset?"
									+ "reposSrc="+ an.getReposSrc() 
									+"&reposId=" + an.getRepId() 
									+ "&asset=" + an.getLocation().replace(Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT, "") 
									+ "&contentDisp=attachment"));
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
					try {
						if (featureTlp.getSelectedIndex()==1 && treeItemTarget!=null) {
							treeItemTarget.setSelected(treeItemTarget.getUserObject().equals(treeItem.getSelectedItem().getUserObject()));									
						}
						
					} catch(Exception ex) {
						// Focus shifted from the search-browse mode to normal browsing mode
					}
					
					// Window.alert(((AssetTreeItem)treeItem.getSelectedItem()).getAssetId());
					AssetNode an = ((AssetTreeItem)treeItem.getSelectedItem()).getAssetNode();

					reposService.getAssetTxtContent(an, contentSetup); 					
				}
			});
		    
		    for (AssetNode an : a) {
		    	AssetTreeItem treeItem = createTreeItem(an, target, expandLeaf);
		    	if (expandLeaf) {
		    		treeItem.setState(true); // Open node
		    		if (featureTlp.getSelectedIndex()==1 && (target!=null && an.getLocation()!=null) && an.getLocation().equals(target.getLocation())) {
		    			try {
							treeItemTarget.setSelected(false);	
						} catch(Exception ex) {
							// Focus shifted from the search-browse mode to normal browsing mode
						}
		        		treeItemTarget = treeItem;
		        		treeItemTarget.setSelected(true);
		        	}
		    	}
		    	tree.addItem(treeItem);
		    }
		    
		    return tree;
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
		
	  protected void displayAssetContent(AssetNode an, ScrollPanel contentPanel, HTML propsWidget) {
		  contentPanel.clear();
			//// splitPanel.remove(centerPanel);
			
			// HTML safeHtml = new HTML(SafeHtmlUtils.fromString(an.getTxtContent()));
			
			// westContent.remove(propsWidget);
			SafeHtmlBuilder propsContent =  new SafeHtmlBuilder();
			String propsTxt = (an.getProps()!=null)?an.getProps().trim():"";
			// margin-top:0px;margin-left:3px;
			propsContent.appendHtmlConstant("<div style='margin:3px;'>Asset Properties:<pre style='margin-top:0px;'><span style='font-family:courier,fixed;font-size: 12px;color:maroon'>").appendEscaped(propsTxt).appendHtmlConstant("</span></pre>");
			if (an.getLocation()!=null) {
				propsContent.appendHtmlConstant("<!-- <br/>Asset Location:<br/><span style='font-family:courier,fixed;font-size: 12px;color:maroon'>" + an.getLocation()  + "</span>-->");
			}
			propsContent.appendHtmlConstant("</div>");
			propsWidget.setWidth("250px");
			propsWidget.setHTML(propsContent.toSafeHtml());
			
			/*
			propsWidget.getElement().getStyle()
	        .setProperty("borderTop", "1px dotted #e7e7e7"); // 1px solid #e7e7e7
			propsWidget.getElement().getStyle()
	        .setProperty("borderBottom", "1px dotted #e7e7e7"); // 1px solid #e7e7e7
			*/
			
			// westContent.add(propsWidget, DockPanel.SOUTH);
			
			// westContent.add(propsWidget);
			if (an.isContentAvailable()) {
				if ("text/csv".equals(an.getMimeType())) {
					   CellTable<List<String>> table = createCellTable(an.getCsv());								    
					   contentPanel.add(table);							    
				} else if ("text/xml".equals(an.getMimeType()) || "application/soap+xml".equals(an.getMimeType())) {
					String xmlStr = an.getTxtContent().replace("<br/>", "\r\n");
					String shStr = SyntaxHighlighter.highlight(xmlStr, BrushFactory.newXmlBrush() , false);
					contentPanel.add(new HTML(shStr));
				} else if ("text/json".equals(an.getMimeType())) {
					// centerPanel.add(new HTML("<pre>" + an.getTxtContent() + "</pre>"));
					String shStr = SyntaxHighlighter.highlight(an.getTxtContent(), BrushFactory.newCssBrush() , false);
					contentPanel.add(new HTML(shStr));
				} else {								
					contentPanel.add(new HTML(an.getTxtContent()));	
				} 
			} else {	
				if (an.getMimeType()!=null) { // Mime-type exists, but no content file
					Image img = new Image();					
					img.setUrl(GWT.getModuleBaseForStaticFiles() + "images/nocontent.gif");
					VerticalPanel imgPanel = new VerticalPanel();
					imgPanel.setWidth("100%");
					imgPanel.setHeight("100%");
					imgPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					imgPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);					
					imgPanel.add(img);
					contentPanel.add(imgPanel); // "There is no content for the selected asset."
					// new HTML("<img height=" src='" + GWT.getModuleBaseForStaticFiles() + "images/nocontent.gif'>")
				}
			}		        		 
			
			//// splitPanel.add(centerPanel);		

	  }
	  	  	  
	  protected AssetTreeItem createTreeItem(AssetNode an, AssetNode target, Boolean expandLeaf) {
	        AssetTreeItem item = new AssetTreeItem(an);

	        for (AssetNode child : an.getChildren()) {
	        	AssetTreeItem treeItem = createTreeItem(child, target, expandLeaf);
	        	if (expandLeaf && !(treeItem.getChildCount() == 1 && "HASCHILDREN".equals(treeItem.getChild(0).getText()))) {
		    		treeItem.setState(true); // Open node
		        	if ((target!=null && child.getLocation()!=null) && child.getLocation().equals(target.getLocation())) {
		        		treeItemTarget = treeItem;
		        		treeItemTarget.setSelected(true);
		        	}
		    	}

	            item.addItem(treeItem);
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
