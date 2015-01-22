package gov.nist.hit.ds.repository.ui.client.widgets;


import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryService;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryServiceAsync;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryTag;
import gov.nist.hit.ds.repository.rpc.search.client.exception.RepositoryConfigException;
import gov.nist.hit.ds.repository.shared.PropertyKey;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.ui.client.CsvTableFactory;
import gov.nist.hit.ds.repository.ui.client.event.NewTxMessageEvent;
import gov.nist.hit.ds.repository.ui.client.event.NewTxMessageEventHandler;
import gov.nist.hit.ds.repository.ui.client.event.asset.InContextAssetClickedEvent;
import gov.nist.hit.ds.repository.ui.client.event.asset.InContextAssetClickedEventHandler;
import gov.nist.hit.ds.repository.ui.client.event.asset.OutOfContextAssetClickedEvent;
import gov.nist.hit.ds.repository.ui.client.event.asset.OutOfContextAssetClickedEventHandler;
import gov.nist.hit.ds.repository.ui.client.event.asset.SearchResultAssetClickedEvent;
import gov.nist.hit.ds.repository.ui.client.event.asset.SearchResultAssetClickedEventHandler;
import gov.nist.hit.ds.repository.ui.client.sh.BrushFactory;
import gov.nist.hit.ds.repository.ui.client.sh.SyntaxHighlighter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class builds a repository log browser widget. Important: setSize must be called explicitly in an fixed-width display environment, otherwise this widget will be completely invisible or empty.
 */
public class LogBrowserWidget extends Composite {


    /**
	 *
	 * @author Sunil.Bhaskarla
	 */
	private static Logger logger = Logger.getLogger(LogBrowserWidget.class.getName());
	private static final int WESTERNBAR = 545;	
	private static final int CONTEXTMENU_XOFFSET = 10;
    private static final int LOG_BROWSER_FEATURE_IDX = 2;
    TabLayoutPanel multiContentTabPanel = new TabLayoutPanel(20, Unit.PX);
    TabLayoutPanel featureTlp = new TabLayoutPanel(20, Unit.PX);
	LayoutPanel featureLp = new LayoutPanel();
	
	VerticalPanel treePanel = new VerticalPanel();
	SplitLayoutPanel splitPanel = new SplitLayoutPanel(5);
	LayoutPanel centerPanel = new LayoutPanel();
	SplitLayoutPanel westContent = new SplitLayoutPanel(2);
	ListBox reposLbx = new ListBox();
	
	HTML propsWidget = new HTML();
    ScrollPanel navScroller;
    ScrollPanel propsScroller;
    
	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
    protected ArrayList<String> propNames = new ArrayList<String>();
    protected Map<String, String> reposProps = new HashMap<String,String>();
    AssetTreeItem lbPreviousTreeItem = null;
    TreeItem previousTreeItem = null;

    AssetTreeItem treeItemTarget = null;
    HTML tabTitle = new HTML(Feature.TRANSACTION_MONITOR.toString());
    int txMonTab = -1;
    final VerticalPanel treeHolder = new VerticalPanel();
    Image refreshTreeImg = new Image();

    TransactionMonitorFilterAdvancedWidget txFilter = null;

    LogBrowserWidget searchLbWidget;
    final SimpleEventBus searchLbEventBus = new SimpleEventBus();

    // Temp

    /* begin */
    Canvas canvas;
    Canvas backBuffer;
    LoggingControlWidget loggingControlWidget;
    double mouseX;
    double mouseY;
    //timer refresh rate, in milliseconds
    static final int refreshRate = 25;

    // canvas size, in px
    static final int height = 95;
    static final int width = 95;

    static final String upgradeMessage = "Your browser does not support the HTML5 Canvas. Please upgrade your browser to view this widget.";


    /** end */


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
            refreshTreeImg.getElement().setAttribute("disabled","false");
        }

    };

    EventBus eventBus;

    // TODO: refactor widgets for internal use
    
	public static enum Feature {
		BROWSE() {
			@Override
			public String toString() {
				return "Browse";	
			}
		}, SEARCH() {
			@Override
			public String toString() {
				return "Search";	
			}
		}, TRANSACTION_MONITOR() {
            @Override
            public String toString() {
                return "Transaction Monitor";
            }
        }, TRANSACTION_FILTER() {
            @Override
            public String toString() {
                return "Messages";
            }
        } , TRANSACTION_FILTER_ADVANCED() {
            @Override
            public String toString() {
                return "Proxy Monitor";
            }
        } , EVENT_MESSAGE_AGGREGATOR() {
            @Override
            public String toString() {
                return "Event Messages";
            }
        } , LOGGING_CONTROL() {
            @Override
            public String toString() {
                return "Logging Control";
            }
        }

	};

    /**
     * This constructor is used to load an asset via internal event bus
     * @param eventBus
     * @param target
     * @throws RepositoryConfigException
     */
    public LogBrowserWidget(EventBus eventBus, final AssetNode target) throws RepositoryConfigException {
        final Feature[] features = new Feature[]{Feature.BROWSE};
        configure(eventBus, features, target);
        initFeaturePanel(features);
    }

    /**
     * This constructor is used to load an asset using external params
     * @param reposSrc
     * @param reposId
     * @param assetId
     */
    public LogBrowserWidget(final EventBus eventBus, final String reposSrc, final String reposId, final String assetId) throws RepositoryConfigException {

        final Feature[] features = new Feature[]{Feature.BROWSE};

        // Get the assetNode using the params and then load it in log browser

        reposService.isRepositoryConfigured(new AsyncCallback<Boolean>(){

            public void onFailure(Throwable t) {
                Window.alert("The repository system configuration is not available: " + t.toString());
            }

            public void onSuccess(Boolean rs) {
                try {
                    reposService.getAssetNode(reposSrc, reposId, assetId, new AsyncCallback<AssetNode>() {
                        @Override
                        public void onFailure(Throwable t) {
                            Window.alert(t.toString());
                        }

                        @Override
                        public void onSuccess(AssetNode result)  {
                            try {
                                configure(eventBus, features, result);
                            } catch (RepositoryConfigException rce) {
                                rce.printStackTrace();
                                Window.alert(rce.toString());
                            }
                        }
                    });
                } catch (RepositoryConfigException rce) {
                    rce.printStackTrace();
                    Window.alert(rce.toString());
                }
            }
        });

        initFeaturePanel(features);
    }


	public LogBrowserWidget(EventBus eventBus, final Feature[] features) throws RepositoryConfigException {
        configure(eventBus, features, null);

        initFeaturePanel(features);
		 //// Use this in embedded mode: RootLayoutPanel.get().add(featureTlp);
	}

    private void configure(EventBus eventBus, final Feature[] features, final AssetNode target) throws RepositoryConfigException {
        this.eventBus = eventBus;

        reposService.isRepositoryConfigured(new AsyncCallback<Boolean>(){

            public void onFailure(Throwable arg0) {
                Window.alert("The repository system configuration is not available: " + arg0.toString());
            }

            public void onSuccess(Boolean rs) {
                if (features!=null) {
                    if (features.length>1) {
                        int cx=0;
                        for (Feature f : features) {

                        if (Feature.TRANSACTION_MONITOR.equals(f)) {
                            txMonTab = cx;
                        } else if (Feature.EVENT_MESSAGE_AGGREGATOR.equals(f)) {

                            final EventAggregatorWidget eventAggregatorWidget = (EventAggregatorWidget)setupFeature(f,null);
                            featureTlp.add(eventAggregatorWidget,f.toString());
                            featureTlp.getTabWidget(cx).addDomHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                eventAggregatorWidget.getTable().redraw(); // This is required to force redraw on the table that is otherwise empty (headers are present but no data). Only an issue when the widget is embedded in tabs -- but not in a single-page view.
                            }
                            }, ClickEvent.getType());
                        } else
                            featureTlp.add(setupFeature(f,null),f.toString());

                        if (Feature.TRANSACTION_FILTER_ADVANCED.equals(f)) {
                            featureTlp.getTabWidget(cx).addDomHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {

                                    getTxFilter().getTxMonitorLive().getTxTable().redraw();
                                    getTxFilter().getTxFilter().getTxTable().redraw();

                                    // This is required to force redraw on the table that is otherwise empty (headers are present but no data). Only an issue when the widget is embedded in tabs -- but not in a single-page view.
                                }
                            }, ClickEvent.getType());
                        }



                        cx++;

                        }
                    } else if (features.length==1) {
                        featureLp.add(setupFeature(features[0], target));
                    }
                } else {
                    Window.alert("No log browser features were selected");
                }



            }
        });


    }

    private void initFeaturePanel(Feature[] features) {
        if (features!=null) {
            if (features.length>1) {
                initWidget(featureTlp);
            } else {
                initWidget(featureLp);
            }
        }
    }

    protected Widget setupFeature(Feature f, final AssetNode targetContext) {
		
		if (Feature.BROWSE==f) {

            // This handler is specific to the widget launch from the log browser tree context menu
            eventBus.addHandler(InContextAssetClickedEvent.TYPE, new InContextAssetClickedEventHandler() {

                public void onAssetClick(InContextAssetClickedEvent event) {
                    // FIXME: selecting another row from the same asset does not fire the event or the event is cancelled
//                                                Window.alert("from incontext");
                    try {
                        final AssetNode target = event.getValue();
                        String rowNumberToHighlightStr = "" + event.getRowNumber();

                        // Scroll to the tree item
                        final Tree lbTree =  (Tree)treeHolder.getWidget(0);

                        int itemCt = lbTree.getItemCount();
                        for (int cx = 0; cx < itemCt; cx++) { // Scan all top-level assets already in tree
                            AssetTreeItem assetTreeItem = (AssetTreeItem)lbTree.getItem(cx);
                            AssetTreeItem treeItemTarget = locateAndOpenTreeItem(assetTreeItem, target);
                            if (treeItemTarget !=null) {
                                lbTree.getSelectedItem().setSelected(false);
                                treeItemTarget.setSelected(true);
                                target.getExtendedProps().put("rowNumberToHighlight",rowNumberToHighlightStr);
//                                reposService.getAssetTxtContent(target, contentSetup);
                                displayAssetContent(target, centerPanel, propsWidget);
                                break;
                            }
                        }

//                                                    reposService.getParentChainInTree(target, treeSetup);


                    } catch (Throwable t) {
                        logger.warning("InContextAssetClickedEvent:" + t.toString());
                        t.printStackTrace();
                    }
                }
            });



            return setupBrowseFeature(targetContext);
		} 
		else if (Feature.SEARCH==f) {
			return setupSearchFeature();				
		} else if (Feature.TRANSACTION_MONITOR==f) {
            return setupTxMonitorFeature();
        } else if (Feature.TRANSACTION_FILTER==f) {
            return setupTxFilter();
        } else if (Feature.TRANSACTION_FILTER_ADVANCED==f) {


            // This handler is specific to the widget launch from the proxy asset maximized-viewer focus
            eventBus.addHandler(OutOfContextAssetClickedEvent.TYPE, new OutOfContextAssetClickedEventHandler() {
                public void onAssetClick(OutOfContextAssetClickedEvent event) {
                    try {
                        final AssetNode target = event.getValue();

                        if (!"text/csv".equals(target.getMimeType())) {
                            String rowNumberToHighlightStr = "" + event.getRowNumber();

                            target.getExtendedProps().put("rowNumberToHighlight",rowNumberToHighlightStr);

                            featureTlp.add(new LogBrowserWidget(eventBus,target),target.getRepId());
                        }

                    } catch (Throwable t) {
                        logger.warning("OutOfContextAssetClickedEvent:" + t.toString());
                    }

                }
            });


            return setupTxFilterAdvanced();
        }  else if (Feature.EVENT_MESSAGE_AGGREGATOR==f) {
            /**
             * local test only
             * C:\e\artrep_test_resources\Installation\IHE-Testing\xdstools2_environment\repositories\data\Sim\123\Events\2014_07_29_13_17_30_089
             */
            String id = null; // "f721daed-d17c-4109-b2ad-c1e4a8293281", "052c21b6-18c2-48cf-a3a7-f371d6dd6caf";
            String type = "validators";
            String[] displayColumns = new String[]{"ID","STATUS","MSG"};


            // This handler is specific to the widget launch from the event aggregation widget
            eventBus.addHandler(OutOfContextAssetClickedEvent.TYPE, new OutOfContextAssetClickedEventHandler() {
                public void onAssetClick(OutOfContextAssetClickedEvent event) {
                    try {
                        final AssetNode target = event.getValue();

                        if ("text/csv".equals(target.getMimeType())) {
                            String rowNumberToHighlightStr = "" + event.getRowNumber();

                            target.getExtendedProps().put("rowNumberToHighlight",rowNumberToHighlightStr);

                            featureTlp.add(new LogBrowserWidget(eventBus,target),target.getRepId());
                        }
                    } catch (Throwable t) {
                        logger.warning("OutOfContextAssetClickedEvent:" + t.toString());
                    }

                }
            });



            return setupEventMessagesWidget(EventAggregatorWidget.ASSET_CLICK_EVENT.OUT_OF_CONTEXT , "Sim", id,type,displayColumns);
        } else if (Feature.LOGGING_CONTROL==f) {
            canvas = Canvas.createIfSupported();
            backBuffer = Canvas.createIfSupported();
            if (canvas == null) {
                return new Label(upgradeMessage);
            }

            canvas.setWidth(width + "px");
            canvas.setHeight(height + "px");
            canvas.setCoordinateSpaceWidth(width);
            canvas.setCoordinateSpaceHeight(height);

            backBuffer.setWidth(width + "px");
            backBuffer.setHeight(height + "px");
            backBuffer.setCoordinateSpaceWidth(width);
            backBuffer.setCoordinateSpaceHeight(height);

            loggingControlWidget = new LoggingControlWidget(backBuffer, canvas, eventBus,"title",width,height);
            initMouseHandlers();

            final Timer timer = new Timer() {
                @Override
                public void run() {
                    doUpdate();
                }
            };
            timer.scheduleRepeating(refreshRate);
            return loggingControlWidget;
        }
		return null;
	}

    /** begin */
    void doUpdate() {
        loggingControlWidget.update(mouseX, mouseY);
        loggingControlWidget.draw(canvas.getContext2d());

    }

    boolean activeControl = false;
    void initMouseHandlers() {


        canvas.addMouseMoveHandler(new MouseMoveHandler() {
            public void onMouseMove(MouseMoveEvent event) {
                if (!activeControl)
                    return;
                mouseX = event.getRelativeX(canvas.getElement());
                mouseY = event.getRelativeY(canvas.getElement());

            }
        });

        canvas.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                if (!activeControl)
                    return;
//                mouseX = -200;
//                mouseY = -200;
            }
        });


        canvas.addMouseDownHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                mouseX = event.getRelativeX(canvas.getElement());
                mouseY = event.getRelativeY(canvas.getElement());
                activeControl = true;
            }
        });

        canvas.addMouseUpHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                mouseX = -1;
                mouseY = -1;
                activeControl = false;

            }
        });

        /*

                canvas.addMouseOutHandler(new MouseOutHandler() {
                    public void onMouseOut(MouseOutEvent event) {
                        mouseX = -200;
                        mouseY = -200;
                    }
                });

                */

        /*
        canvas.addTouchMoveHandler(new TouchMoveHandler() {
            public void onTouchMove(TouchMoveEvent event) {
                event.preventDefault();
                if (event.getTouches().length() > 0) {
                    Touch touch = event.getTouches().get(0);
                    mouseX = touch.getRelativeX(canvas.getElement());
                    mouseY = touch.getRelativeY(canvas.getElement());
                }
                event.preventDefault();
            }
        });

        canvas.addTouchEndHandler(new TouchEndHandler() {
            public void onTouchEnd(TouchEndEvent event) {
                event.preventDefault();
                mouseX = -200;
                mouseY = -200;
            }
        });
        */
    }



    /* end */

    protected Widget setupEventMessagesWidget(EventAggregatorWidget.ASSET_CLICK_EVENT assetClickEvent, String externalRepositoryId, String eventAssetId, String type, String[] displayColumns) {
        try {
            /* manual setup:
            1) change also the assertionGroup type in event widget.
             */

            EventAggregatorWidget eventMessageAggregatorWidget = new EventAggregatorWidget(eventBus, assetClickEvent, externalRepositoryId,eventAssetId,type,displayColumns);

            return eventMessageAggregatorWidget;


        } catch (Exception ex) {
            Window.alert(ex.toString());
        }
        return null;
    }

    protected Widget setupTxFilterAdvanced() {
        setTxFilter(new TransactionMonitorFilterAdvancedWidget(eventBus));
        getTxFilter().getElement().getStyle()
                .setProperty("border", "none");


        return  getTxFilter();
    }


    protected Widget setupTxFilter() {
        TransactionMonitorFilterWidget txFilter = new TransactionMonitorFilterWidget(eventBus);
        txFilter.getElement().getStyle()
                .setProperty("border", "none");

        return  txFilter;
    }

    protected Widget setupTxMonitorFeature() {

        TransactionMonitorWidget txMonitor = new TransactionMonitorWidget(eventBus, Boolean.TRUE, Boolean.FALSE,  Boolean.TRUE);
        txMonitor.getElement().getStyle()
                .setProperty("border", "none");


        eventBus.addHandler(NewTxMessageEvent.TYPE, new NewTxMessageEventHandler() {
            @Override
            public void onNewTxMessage(NewTxMessageEvent event) {

                if (txMonTab>-1) {
                    featureTlp.getTabWidget(txMonTab).getElement().setInnerText(Feature.TRANSACTION_MONITOR.toString() + " ("+ event.getValue() + ")");
                }

            }
        });

        return txMonitor;
    }

	  protected SplitLayoutPanel setupSearchFeature() {
		    final SplitLayoutPanel searchMainLayoutPanel = new SplitLayoutPanel(5); // Search-main panel
		    searchMainLayoutPanel.getElement().getStyle()
	        .setProperty("border", "3px solid #e7e7e7"); //

//          final VerticalPanel searchLbTreeHolder = new VerticalPanel();
//		    final SplitLayoutPanel searchLbSplitPanel = new SplitLayoutPanel(1);
          final LayoutPanel searchLbCenterPanel = new LayoutPanel();
//			SplitLayoutPanel searchLbWestContent = new SplitLayoutPanel(2);
		    			

//			final HTML searchLbPropsWidget = new HTML();
			// searchLbTreeHolder.add(new HTML("&nbsp;Loading..."));
//			ScrollPanel sp = new ScrollPanel(searchLbTreeHolder);
//			ScrollPanel spProps = new ScrollPanel(searchLbPropsWidget);
//			searchLbWestContent.addSouth(spProps, Math.round(0.2 * Window.getClientHeight()));
//			searchLbWestContent.add(sp);

//			searchLbSplitPanel.getElement().getStyle().setVisibility(Visibility.HIDDEN);
//			searchLbSplitPanel.addWest(searchLbWestContent, 300); // 400  -- Math.round(.15 * Window.getClientWidth())
//			searchLbSplitPanel.add(searchLbCenterPanel);
			
		    ScrollPanel searchPanel = new ScrollPanel(); 				// Search parameters 
		    
		    SearchWidget searchWidget = new SearchWidget(searchLbEventBus, new SearchWidget.Option[]{
                    SearchWidget.Option.QUICK_SEARCH,
                    SearchWidget.Option.SEARCH_CRITERIA_REPOSITORIES,
                    SearchWidget.Option.CRITERIA_BUILDER_MODE
            });

		    searchWidget.getElement().getStyle()
	        .setProperty("border", "none");
		    searchWidget.getElement().getStyle()
		    .setPaddingLeft(3, Unit.PX);

		    searchPanel.add(searchWidget);
		    searchMainLayoutPanel.addWest(searchPanel, WESTERNBAR); // 632, Math.round(0.40 * Window.getClientWidth())
            searchMainLayoutPanel.add(searchLbCenterPanel);

//		    searchMainLayoutPanel.add(searchLbSplitPanel);

//            try {
//                searchLbWidget = new LogBrowserWidget(searchLbEventBus, new Feature[]{Feature.BROWSE});
//                searchLbWidget.setVisible(false); // Hide on startup
//                searchMainLayoutPanel.add(searchLbWidget);
//            } catch (RepositoryConfigException e) {
//                e.printStackTrace();
//            }



          /*
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
    */

          searchLbEventBus.addHandler(SearchResultAssetClickedEvent.TYPE, new SearchResultAssetClickedEventHandler() {
              public void onAssetClick(SearchResultAssetClickedEvent event) {
//                    Window.alert("from search click");
                  try {
                      final AssetNode target = event.getValue();

                      searchLbCenterPanel.clear();

                      searchLbCenterPanel.add(new LogBrowserWidget(searchLbEventBus, target));

//                        reposService.getParentChainInTree(target, new AsyncCallback<List<AssetNode>>() {
//
//                            public void onFailure(Throwable arg0) {
//                                searchLbPropsWidget.setHTML("Search result action could not be synchronized with the tree: " + arg0.toString());
//                            }
//
//                            public void onSuccess(List<AssetNode> topLevelAssets) {
//                                searchLbTreeHolder.clear();
//                                searchLbTreeHolder.add(popTreeWidget(topLevelAssets, target, true, searchLbContentSetup));
//                                reposService.getAssetTxtContent(target, searchLbContentSetup);
//                            }
//                        });


                  } catch (Throwable t) {
                      t.printStackTrace();
                  }

              }
          });

		    						    
		    return searchMainLayoutPanel;

	  }
	  
	  protected SplitLayoutPanel setupBrowseFeature(final AssetNode targetContext) {
				  
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
							int cx = 0;
                            int targetContextReposIdx = -1;

							for (RepositoryTag rt : rtList) {

                                if (targetContext!=null) {
                                    if (rt.getId().equals(targetContext.getRepId()) && rt.getSource().equals(targetContext.getReposSrc())) {
                                        targetContextReposIdx = cx;
                                    }
                                }

                                reposData[cx][0] =  rt.getId(); //a.get(key)[0];
                                reposData[cx++][1] = rt.getSource(); // a.get(key)[1];

								reposLbx.addItem(rt.getDisplayName(), rt.getCompositeId());
								reposProps.put(rt.getCompositeId(), rt.getProperties());
							}

                            if (targetContext!=null && targetContextReposIdx>-1) {
                                reposLbx.setSelectedIndex(targetContextReposIdx);
                            }
																			
														
							HTML lblRepos = new HTML("Select a Repository");
							
							// lblRepos.setWidth("25%");
							reposLbx.setWidth("16em"); // 90
							FlexTable grid = new FlexTable();
							// grid.setCellPadding(0); // 1
							// grid.setCellSpacing(0); // 3
							grid.setBorderWidth(0);
//							grid.setWidget(0, 0, new HTML("&nbsp;"));
//							grid.getFlexCellFormatter().setColSpan(0, 0, 2);
							HTML browseHeader = new HTML("&nbsp;"); // <h4>Browse Available Repositories</h4>
							// browseHeader.setStyleName("searchCriteriaGroup");
							grid.setWidget(0, 0, browseHeader);
//							grid.getFlexCellFormatter().setColSpan(0, 0, 2);
							grid.setWidget(1, 0, lblRepos );
//							grid.getFlexCellFormatter().setColSpan(1, 0, 2);

                            HorizontalPanel hpLbx = new HorizontalPanel();
                            hpLbx.add(reposLbx);

//							grid.setWidget(2, 0, reposLbx);


                            refreshTreeImg.setUrl(GWT.getModuleBaseForStaticFiles() + "images/refresh-sm.png");
                            refreshTreeImg.setHeight("16px");
                            refreshTreeImg.setWidth("16px");
                            refreshTreeImg.setAltText("Refresh");
                            refreshTreeImg.setTitle("Refresh");
                            refreshTreeImg.getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
//                            Anchor refAnchor = new Anchor();
//                            refAnchor.set
                            refreshTreeImg.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    refreshTree();
                                }
                            });
                            hpLbx.add(new HTML("&nbsp;"));
                            hpLbx.add(refreshTreeImg);
                            hpLbx.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
                            grid.setWidget(2,0,hpLbx);

//                            HTMLTable.CellFormatter formatter = grid.getCellFormatter();
//                            formatter.setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);

                            grid.setWidth("100%");
							
							// treePanel.add(new HTML("&nbsp;"));
							treePanel.add(grid);
							
							treePanel.getElement().getStyle()
					        .setProperty("border", "none");
							treePanel.getElement().getStyle()
						    .setPaddingLeft(3, Unit.PX);

							ScrollPanel spProps = new ScrollPanel(propsWidget);
							westContent.addSouth(spProps, Math.round(0.2 * Window.getClientHeight()));
							
							// treePanel.add(new HTML("&nbsp;"));
							//final VerticalPanel treeHolder = new VerticalPanel();
							treeHolder.add(new HTML("&nbsp;Loading..."));
                            refreshTreeImg.getElement().setAttribute("disabled","true");
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
							

						    
                           if (targetContext==null) {
							reposService.getAssetTree(new String[][]{{reposData[0][0],reposData[0][1]}},0, treeSetup);
                           }
						
							if (reposLbx.getItemCount()>0) {
								reposLbx.addChangeHandler(new ChangeHandler() {
									
									public void onChange(ChangeEvent event) {
					                    refreshTree();
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
					
					//// featureTlp.add(setupSearchFeature(), "Search");



                    // Handle out of context case
                    if (targetContext!=null) {

                        try {
                            reposService.getParentChainInTree(targetContext, new AsyncCallback<List<AssetNode>>() {

                                public void onFailure(Throwable arg0) {
                                    Window.alert("Search result action could not be synchronized with the tree: " + arg0.toString());
                                }

                                public void onSuccess(List<AssetNode> topLevelAssets) {
                                    treeHolder.clear();
                                    treeHolder.add(popTreeWidget(topLevelAssets, targetContext, true, contentSetup));

                                    if (targetContext.getTxtContent()!=null) {
                                        displayAssetContent(targetContext, centerPanel, propsWidget);
                                    } else {
                                        reposService.getAssetTxtContent(targetContext, contentSetup);
                                    }

                                }
                            });

                        } catch (RepositoryConfigException rce) {
                            Window.alert(rce.toString());
                        }

                    }
				}
				public void onFailure(Throwable t) {Window.alert("Repository config failed: "+t.getMessage());}
			});
		} catch (Exception e) {
			logger.log(Level.SEVERE,"main setRepositoryConfig failed: " + e.toString());
		}
	    
	    return splitPanel;
	    
	  }

    protected void refreshTree() {
        refreshTreeImg.getElement().setAttribute("disabled","true");
        lbPreviousTreeItem = null;
        treeHolder.clear();
        treeHolder.add(new HTML("&nbsp;Loading..."));
        centerPanel.clear();
        closeSummaryTab();


        String[] compositeKey = getReposCompositeKey();

        reposService.getAssetTree(new String[][]{{compositeKey[0],compositeKey[1]}},0, treeSetup);

    }

    private String[] getReposCompositeKey() {
        ListBox lbx = reposLbx; // ((ListBox)event.getSource());
        int idx = lbx.getSelectedIndex();

        // reposService.getAssetTree(new String[][]{{lbx.getItemText(idx),lbx.getValue(idx)}}, treeSetup);
        return lbx.getValue(idx).split("\\^");
    }

    protected TabPanel addTab(Widget w, String lbl) {
	      	
	      	TabPanel tp = new TabPanel();
	      	tp.setVisible(true);

	      	w.setVisible(true);
	      	tp.add(w,lbl);
	      	return tp;
	      	
	    }


      private void popTreeItem(final TreeItem item, final boolean openTreeItem) {

          final AssetNode focusAn =  (AssetNode)item.getUserObject();
          AssetNode parentAn = null;

          if (item.getParentItem()!=null) {
                // Child expansion
              parentAn = (AssetNode)item.getParentItem().getUserObject();
          } else {
                // No parent, treat as repository root top-level
          }

          final String offsetValue = focusAn.getExtendedProps().get("_offset");
          if ((focusAn!=null && offsetValue!=null)) {

//              Close the item immediately
              item.setState(false, false);

              final AsyncCallback<List<AssetNode>> addImmediateChildrenByOffset = new AsyncCallback<List<AssetNode>>() {

                  public void onFailure(Throwable a) {
                      Window.alert(a.toString());
                  }

                  public void onSuccess(List<AssetNode> a) {
//                      logger.info("widgetCount: " + treeHolder.getWidgetCount() +  " offsetValue: " + offsetValue + " --- got: " + ((a!=null)?a.size():"null"));

                      final Tree tree = (Tree)treeHolder.getWidget(0);

                      for (AssetNode childAn : a) {
                          AssetTreeItem treeItem = createTreeItem(childAn, null, false, false);

//                          logger.info("_offset prop found for assetId: " + focusAn.getAssetId());
//                          logger.info(" parent exists? " + (item.getParentItem()!=null));
//                          logger.info(" display name: " + childAn.getDisplayName());
//                          logger.info(" offsetValue: " + offsetValue);

                          if (item.getParentItem()!=null) {
                              int index = item.getParentItem().getChildIndex(item);

                              item.getParentItem().insertItem(index, treeItem); // .addItem(treeItem); // Offset based retrieval adds an item to parent
                          } else {
                              if (tree!=null) {
                                tree.addItem(treeItem);
                              } else {
                                  logger.warning("tree widget is null!");
                              }

                          }
//                          logger.info("adding --- " + treeItem.getAssetNode().getDisplayName() + " child count: " + item.getChildCount());
                      }

                        item.remove(); // Remove ellipses

//                      if (item.getParentItem()!=null) {
//                          item.getParentItem().removeItem(item);
//                          item.remove();
//                      } else {
//                          tree.removeItem(item);
//                      }


                  }

              };

              try {
                  int offsetVal = Integer.parseInt(offsetValue);
                  boolean stopFlag = Boolean.parseBoolean(focusAn.getExtendedProps().get("_stopFlag"));
                  if (parentAn!=null) { // parent node exists
                      reposService.getImmediateChildren(parentAn, offsetVal, (!stopFlag), addImmediateChildrenByOffset);
                  } else { // top level
                      String[] compositeKey = getReposCompositeKey();
                      reposService.getAssetTree(new String[][]{{compositeKey[0],compositeKey[1]}}, offsetVal, addImmediateChildrenByOffset);
                  }



              } catch (RepositoryConfigException e) {
                  e.printStackTrace();
              }

//              logger.info("2 _offset prop found for assetId: " + focusAn.getAssetId());
//              logger.info("2 parent exists? " + (item.getParentItem()!=null));




          } else if (item.getChildCount() == 1 && "HASCHILDREN".equals(item.getChild(0).getText())) {


              // Close the item immediately
              item.setState(false, false);

              final AsyncCallback<List<AssetNode>> addImmediateChildren = new AsyncCallback<List<AssetNode>>() {

                  public void onFailure(Throwable a) {
                      Window.alert(a.toString());
                  }

                  public void onSuccess(List<AssetNode> a) {
                      for (AssetNode childAn : a) {
                          AssetTreeItem treeItem = createTreeItem(childAn, null, false, true);

                              item.addItem(treeItem);

//                          logger.info("adding --- " + treeItem.getAssetNode().getDisplayName() + " child count: " + item.getChildCount());
                      }

                      if (a!=null && a.size()>0) {
                          item.setState(openTreeItem);
                      }


                  }

              };

              try {

                    reposService.getImmediateChildren(focusAn, 0, addImmediateChildren);


              } catch (RepositoryConfigException e) {
                  e.printStackTrace();
              }

              // Remove the temporary HASCHILDREN item when we finish loading
              item.getChild(0).remove();

              // Reopen the item
              item.setState(true, false);


          }
      }

    private void popTreeItemDeep(final TreeItem item, final boolean openTreeItem) {
        if (item.getChildCount() == 1 && "HASCHILDREN".equals(item.getChild(0).getText())) {

            AssetNode an =  (AssetNode)item.getUserObject();
            an.getChildren().clear();

            // Close the item immediately
            item.setState(false, false);

            final AsyncCallback<AssetNode> addChildren = new AsyncCallback<AssetNode>() {

                public void onFailure(Throwable a) {
                    Window.alert(a.toString());
                }

                public void onSuccess(AssetNode parent) {
                    List<AssetNode> assetNodeList =  parent.getChildren();
//                    logger.info(" ** ct: " + assetNodeList.size());
                    for (AssetNode an : assetNodeList) {
                        AssetTreeItem treeItem = createTreeItem(an, null, true, true);
                        item.addItem(treeItem);
//                          logger.info("adding --- " + treeItem.getAssetNode().getDisplayName() + " child count: " + item.getChildCount());
                    }
                    if (assetNodeList!=null && assetNodeList.size()>0) {
                        item.setState(openTreeItem);
                    }

                }

            };

            try {
                reposService.getChildren(an, addChildren);
            } catch (RepositoryConfigException e) {
                e.printStackTrace();
            }

            // Remove the temporary HASCHILDREN item when we finish loading
            item.getChild(0).remove();

            // Reopen the item
            item.setState(true, false);

        }
    }


	  protected Widget popTreeWidget(List<AssetNode> anList, AssetNode target, Boolean expandLeaf, final AsyncCallback<AssetNode> contentSetup) {
            Tree tree = new Tree(); // Keep this here because both search tab and log browser tab need their own tree

		    final PopupPanel menu = new PopupPanel(true);
		    
		    // dynamic children pop
		    tree.addOpenHandler(new OpenHandler<TreeItem>() {

                public void onOpen(OpenEvent<TreeItem> event) {

                    final TreeItem item = event.getTarget();

                    if (previousTreeItem!=item) {
//                        logger.info("___________ calling popTreeItem");
                        popTreeItem(item, true);
                        previousTreeItem = item;
                    } /* else {
                        logger.fine("___________ caught same treeItem event");
                    } */


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
						AssetNode an =  null;

						try {
							// Browse method
							an =  (AssetNode)((Tree)event.getSource()).getSelectedItem().getUserObject();
						} catch (Exception ex) {
							// Search method
							an = treeItemTarget.getAssetNode(); // ((AssetTreeItem)((Tree)event.getSource()).getSelectedItem()).getAssetNode();								
						}
//                        try {
//                            assetTreeItem =
//                        } catch (Exception ex) {
//                            logger.warning(ex.toString());
//                        }

//                        Anchor autoRefreshLink = new Anchor("Refresh");
//                        autoRefreshLink.addClickHandler(new ClickHandler() {
//                            @Override
//                            public void onClick(ClickEvent event) {
//                                Window.alert("Feature not yet implemented.");
//                            }
//                        });
//                        menuItemPanel.add(autoRefreshLink);

                        // Browse method only
                        // Show Summary link here.
                        try {
                            final AssetNode parentNode = (AssetNode)((Tree)event.getSource()).getSelectedItem().getParentItem().getUserObject();

                            if ("validators".equals(an.getType()) && parentNode!=null && "event".equals(parentNode.getType())) {

                                final String id = parentNode.getAssetId();
                                final String type = "validators";
                                final String[] displayColumns = new String[]{"ID","STATUS","MSG"};

                                Anchor summaryLink = new Anchor("Show validation summary");

                                summaryLink.addClickHandler(new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                        showEventMessagesWidget(parentNode.getRepId(), id, type, displayColumns);
                                    }
                                });

                                menuItemPanel.add(summaryLink);
                            } else if ("event".equals(an.getType())) {
                                // See if a "validators" child node exists directly under a "event" type
                                final AssetTreeItem assetTreeItem = (AssetTreeItem)((Tree)event.getSource()).getSelectedItem();


                                int childCt = assetTreeItem.getChildCount();

                                if (!(childCt == 1 && "HASCHILDREN".equals(assetTreeItem.getChild(0).getText()))) {

                                    for (int cx = 0; cx < childCt; cx++) {
                                        AssetTreeItem child = (AssetTreeItem)assetTreeItem.getChild(cx);
                                        if ("validators".equals(child.getAssetNode().getType())) {
                                            // If so, then show menu
                                            final String id =  assetTreeItem.getAssetNode().getAssetId();
                                            final String type = "validators";
                                            final String[] displayColumns = new String[]{"ID","STATUS","MSG"};

                                            Anchor summaryLink = new Anchor("Show validation summary");

                                            summaryLink.addClickHandler(new ClickHandler() {
                                                @Override
                                                public void onClick(ClickEvent event) {
                                                    showEventMessagesWidget(parentNode.getRepId(), id, type, displayColumns);
                                                }
                                            });

                                            menuItemPanel.add(summaryLink);
                                            break;
                                        }

                                    }
                                }


                            }
                        } catch (Exception ex) {
                            // Fine, no summary menu
                        }


						if (an.isContentAvailable()) {
							menuItemPanel.add(new Anchor("Download content"
									,GWT.getHostPageBaseURL() + "repository/downloadAsset?"
									+ "reposSrc="+ an.getReposSrc() 
									+"&reposId=" + an.getRepId() 
									+ "&asset=" + an.getRelativePath().replace(Configuration.DOT_SEPARATOR + Configuration.PROPERTIES_FILE_EXT, "")
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
		    

		    // on selected handler here, this is separate from the dynamic pop handler above
		    tree.addSelectionHandler(new SelectionHandler<TreeItem>() {				
		    	
				public void onSelection(SelectionEvent<TreeItem> treeItem) {

                    // Window.alert(((AssetTreeItem)treeItem.getSelectedItem()).getAssetId());
                    AssetTreeItem assetTreeItem = ((AssetTreeItem)treeItem.getSelectedItem());
                    AssetNode an = assetTreeItem.getAssetNode();


                    // Optimization block that prevents unnecessary round trips, okay to fail
                    try {
                        if (lbPreviousTreeItem!=null && lbPreviousTreeItem.getAssetNode()!=null) {
                            if (an.getAssetId().equals(lbPreviousTreeItem.getAssetNode().getAssetId())) {

//                                logger.info("safe block (same item activated) return");
                                return;
                            }
                            lbPreviousTreeItem.setSelected(false);
                        }

                        // Cache current item as previous item reference
                        lbPreviousTreeItem = (AssetTreeItem)treeItem.getSelectedItem();

                    } catch (Throwable t) {
                        // Fine, pay an extra round trip to the server
                    }

					try {
						if (featureTlp.getSelectedIndex() < LOG_BROWSER_FEATURE_IDX && treeItemTarget!=null) {
							treeItemTarget.setSelected(treeItemTarget.getUserObject().equals(treeItem.getSelectedItem().getUserObject()));									
						}
						
					} catch(Exception ex) {
						// Focus shifted from the search-browse mode to normal browsing mode
					}

                    // Pre-load, load items for future use in case of Event type
                    if ("event".equals(an.getType())  || "validators".equals(an.getType())) {

                        if ((assetTreeItem.getChildCount() == 1 && "HASCHILDREN".equals(assetTreeItem.getChild(0).getText())))
                            popTreeItemDeep(assetTreeItem, false);
                    }

                    if (an.getExtendedProps().get("_offset")==null) {
                        // Load content on selection
                        reposService.getAssetTxtContent(an, contentSetup);
                    }


				}
			});
		    
		    for (AssetNode an : anList) {
		    	AssetTreeItem treeItem = createTreeItem(an, target, expandLeaf, true);

		    	if (expandLeaf && !(treeItem.getChildCount() == 1 && "HASCHILDREN".equals(treeItem.getChild(0).getText()))) {
                    // Selected index is -1 when featureLp is used instead of featureTlp
		    		treeItem.setState(true); // Open node
		    		if (featureTlp.getSelectedIndex() < LOG_BROWSER_FEATURE_IDX && isSameAssetReference(target, an)) {
		    			try {
							treeItemTarget.setSelected(false);
						} catch(Exception ex) {
							// Focus shifted from the search-browse mode to normal browsing mode
						}
		    			treeItem.setSelected(true);
		        		treeItemTarget = treeItem;
                        try {
                            lbPreviousTreeItem = treeItem;
                        } catch (Exception ex) {

                        }
		        	}
		    	}
		    	tree.addItem(treeItem);
		    }
		    
		    return tree;
	  }

    private boolean isSameAssetReference(AssetNode target, AssetNode an) {
        return (an != null && target != null)
                && (an.getRelativePath() != null && an.getRelativePath().equals(target.getRelativePath()) || (an.getFullPath()!=null && an.getFullPath().equals(target.getFullPath())));
    }

    private void showEventMessagesWidget(String externalRepositoryId, String eventAssetId, String type, String[] displayColumns) {
        splitPanel.remove(centerPanel);
        splitPanel.remove(multiContentTabPanel);

        multiContentTabPanel.clear();

        EventAggregatorWidget eventMessageAggregatorWidget =  (EventAggregatorWidget)setupEventMessagesWidget(EventAggregatorWidget.ASSET_CLICK_EVENT.IN_CONTEXT, externalRepositoryId, eventAssetId,type,displayColumns);
        multiContentTabPanel.add(eventMessageAggregatorWidget,"Validation Summary");
        eventMessageAggregatorWidget.setSize("98%", "98%");
        eventMessageAggregatorWidget.getTable().redraw();

        multiContentTabPanel.add(centerPanel, "Document Viewer");
        addContentViewerTabPanelOption(centerPanel, 0);
        splitPanel.add(multiContentTabPanel);
    }


    private AssetTreeItem locateAndOpenTreeItem(final AssetTreeItem assetTreeItem, final AssetNode an) {
//        logger.info("entering locateAndOpenTreeItem: " + ((assetTreeItem != null) ? assetTreeItem.getAssetNode().getDisplayName() : "null") + " an: " + (an != null));
        if (assetTreeItem.getChildCount()>0) {

            if ("validators".equals(assetTreeItem.getAssetNode().getType()) &&  (assetTreeItem.getChildCount() == 1 && "HASCHILDREN".equals(assetTreeItem.getChild(0).getText()))) {
                // Read ahead scan from parent level  ?
//               // taken care of in the on-selection handler of the "event" asset

            } else {
                int childCt = assetTreeItem.getChildCount();
                for (int cx = 0; cx < childCt; cx++) {
                    AssetTreeItem child = (AssetTreeItem)assetTreeItem.getChild(cx);
//                    logger.info("an.getParentId()" + (an.getParentId()!=null) + "; child exists?" + (child!=null));
                    if (an.getParentId().equals(child.getAssetNode().getAssetId())) {
//                        logger.info("** opening " + child.getAssetNode().getDisplayName());
                        assetTreeItem.setState(true);
                        child.setState(true);
                    }

                    AssetTreeItem  target = locateAndOpenTreeItem(child, an);
                    if (target!=null) {
                        return target;
                    }
                }
            }



        } else if (assetTreeItem.getChildCount()==0)  {
            if (an.getAssetId().equals(assetTreeItem.getAssetNode().getAssetId())) {

                if (lbPreviousTreeItem!=null && lbPreviousTreeItem.getAssetNode()!=null) {
                    lbPreviousTreeItem.setSelected(false);
                }
                lbPreviousTreeItem = assetTreeItem;

                return assetTreeItem;
            }
        }
        return null;
    }

    protected void displayAssetContent(AssetNode an, Panel contentPanel, HTML propsWidget) {

            boolean reload =  (an.getExtendedProps().get("rowNumberToHighlight")!=null);

            // Optimization block to prevent reloading of the same block
            if (!reload && contentPanel.getElement().getId().equals(an.getAssetId()))
                return;

            contentPanel.getElement().setId(an.getAssetId());

		    contentPanel.clear();
			//// splitPanel.remove(centerPanel);
			
			// HTML safeHtml = new HTML(SafeHtmlUtils.fromString(an.getTxtContent()));
			
			// westContent.remove(propsWidget);
			SafeHtmlBuilder propsContent =  new SafeHtmlBuilder();
			String propsTxt = (an.getProps()!=null)?an.getProps().trim():"";

			// margin-top:0px;margin-left:3px;
			propsContent.appendHtmlConstant("<div style='margin:3px;'>Asset Properties:<pre style='margin-top:0px;'><span style='font-family:courier,fixed;font-size: 12px;color:maroon'>").appendEscaped(propsTxt).appendHtmlConstant("</span></pre>");
            propsContent.appendHtmlConstant("Web Address:<pre style='margin-top:0px;'><span style='font-family:courier,fixed;font-size: 12px;color:maroon'>");
            propsContent.appendEscaped(""
                    + GWT.getHostPageBaseURL()
                    + GWT.getModuleName()
                    + ".html?reposSrc="
                    + an.getReposSrc()
                    +"&reposId=" + an.getRepId()
                    +"&assetId=" + an.getAssetId())
            .appendHtmlConstant("</span></pre>");

			if (an.getRelativePath()!=null) {
				propsContent.appendHtmlConstant("<!-- <br/>Asset Relative Location:<br/><span style='font-family:courier,fixed;font-size: 12px;color:maroon'>" + an.getRelativePath()  + "</span>-->");
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
            if ("transaction".equals(an.getType())) {
                // TODO: Load transaction viewer
            } else if (an.isContentAvailable()) {
				if ("text/csv".equals(an.getMimeType())) {
                    // Create a list data provider.
                       final ListDataProvider<List<SafeHtml>> dataProvider  = new ListDataProvider<List<SafeHtml>>();
//					   CellTable<List<SafeHtml>> table = new CsvTableFactory().createCellTable(dataProvider, an.getCsv());
                        DataGrid<List<SafeHtml>> table = new CsvTableFactory().createDataGrid(dataProvider, an.getCsv(), an.getExtendedProps().get(PropertyKey.COLUMN_WIDTHS.toString()));

                       if (an.getExtendedProps()!=null && an.getExtendedProps().get("rowNumberToHighlight")!=null) {
                           int rowNumberToHighlight = Integer.parseInt(an.getExtendedProps().get("rowNumberToHighlight")) - 1; /* the rowNumberToHighlight starts with 1, so compensate for the zero-based get */
                           if (rowNumberToHighlight>-1) {
                               logger.fine("rowNumberToHighlight: " + rowNumberToHighlight + " isnull?" + new Boolean(dataProvider.getList().get(rowNumberToHighlight) == null) + " isnull2:" + new Boolean(table.getSelectionModel() == null));
                               table.getSelectionModel().setSelected(dataProvider.getList().get(rowNumberToHighlight), true);

                           }
                       }

                    table.getElement().getStyle().setProperty("wordWrap","break-word");

                    SplitLayoutPanel layoutPanel = new SplitLayoutPanel(0);

                    SimplePager pager = CsvTableFactory.getPager();
                    pager.setDisplay(table);
                    table.redraw();
                    layoutPanel.addSouth(pager, 26);
                    layoutPanel.add(table);
                    table.redraw();
                    contentPanel.add(layoutPanel);

				} else if ("text/xml".equals(an.getMimeType()) || "application/soap+xml".equals(an.getMimeType())) {
					String xmlStr = an.getTxtContent().replace("<br/>", "\r\n");
					String shStr = SyntaxHighlighter.highlight(xmlStr, BrushFactory.newXmlBrush() , false);
					contentPanel.add(new ScrollPanel(new HTML(shStr)));
				} else if ("text/json".equals(an.getMimeType())) {
					String shStr = SyntaxHighlighter.highlight(an.getTxtContent(), BrushFactory.newCssBrush() , false);
					contentPanel.add(new ScrollPanel(new HTML(shStr)));
				} else if ("text/plain".equals(an.getMimeType())) {
                    contentPanel.add(new ScrollPanel(new HTML("<pre style='margin-top:0px;'>" + an.getTxtContent() + "</pre>")));
                } else {
					contentPanel.add(new ScrollPanel(new HTML(an.getTxtContent())));
				} 
			} else {	

//					  StyleInjector.inject(".centerImg{position:relative;top:250px;left:50%;}");

					VerticalPanel imgPanel = new VerticalPanel(); // ;
//                    imgPanel.setWidth("50%");
//                    imgPanel.setHeight("50%");
                    Label imgText = new Label();
					imgPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					imgPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
					imgPanel.setStyleName("centerImg");
					Image img = new Image();					
					img.setUrl(GWT.getModuleBaseForStaticFiles() + "images/nocontent.png");
                    if (an.getMimeType()!=null) { // Mime-type exists, but no content file
                        imgText.setText("Formatting error");
                        imgText.setStyleName("serverResponseLabelError");
                    } else {
                        imgText.setText("No document");
                        imgText.setStyleName("grayText");
                    }
                    img.setAltText(imgText.getText());
					imgPanel.add(img);
                    imgPanel.add(imgText);
					contentPanel.add(new ScrollPanel(imgPanel)); // "There is no content for the selected asset."
					// new HTML("<img height=" src='" + GWT.getModuleBaseForStaticFiles() + "images/nocontent.gif'>")

			}

        addContentViewerTabPanelOption(contentPanel, 1);


        //// splitPanel.add(centerPanel);

	  }

    private void addContentViewerTabPanelOption(final Panel contentPanel, int activeTabIdx) {
        try {
            if (multiContentTabPanel.getWidgetCount() > 1 && multiContentTabPanel.getTabWidget(activeTabIdx) !=null) {
                multiContentTabPanel.selectTab(activeTabIdx);

                final PopupPanel tabMenu = new PopupPanel(true);

                multiContentTabPanel.getTabWidget(0).addDomHandler(new ContextMenuHandler() {
                    @Override
                    public void onContextMenu(ContextMenuEvent event) {
                        event.preventDefault();
                        event.stopPropagation();

                        tabMenu.addDomHandler(new ClickHandler() {

                            public void onClick(ClickEvent arg0) {
                                tabMenu.hide();
                            }
                        }, ClickEvent.getType());
                        tabMenu.addDomHandler(new MouseOutHandler() {

                            public void onMouseOut(MouseOutEvent arg0) {
                                tabMenu.hide();

                            }
                        }, MouseOutEvent.getType());

                        // Window.alert(((Tree)event.getSource()).getSelectedItem().getText());

                        VerticalPanel menuItemPanel = new VerticalPanel();

                        Label option = new Label("Close");
                        option.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                tabMenu.hide();
                                closeSummaryTab();

                            }
                        });

                        menuItemPanel.add(option);

                        tabMenu.setWidget(menuItemPanel);
                        tabMenu.setPopupPosition(event.getNativeEvent().getClientX()+CONTEXTMENU_XOFFSET, event.getNativeEvent().getClientY());
                        tabMenu.show();
                    }
                }, ContextMenuEvent.getType());
            }

        } catch (Exception ex) {
            // Fine
        }
    }

    private void closeSummaryTab() {
        //                                int selectedIdx = multiContentTabPanel.getSelectedIndex();
        if (multiContentTabPanel.getWidgetCount()>1)
            multiContentTabPanel.getTabWidget(0).removeFromParent();

    }

    protected AssetTreeItem createTreeItem(AssetNode an, AssetNode target, Boolean expandLeaf, Boolean openItem) {
	        AssetTreeItem item = new AssetTreeItem(an);

	        for (AssetNode child : an.getChildren()) {
//                logger.info("** entering " + an.getDisplayName());
	        	AssetTreeItem treeItem = createTreeItem(child, target, expandLeaf, openItem);
	        	if (expandLeaf && !(treeItem.getChildCount() == 1 && "HASCHILDREN".equals(treeItem.getChild(0).getText()))) {
                    if (openItem)
		    		    treeItem.setState(true,false); // Open node
		        	if (isSameAssetReference(target, child)) {
		        		treeItemTarget = treeItem;
		        		treeItemTarget.setSelected(true);
		        	}
		    	}

	            item.addItem(treeItem);
	        }	        
	        return item;
	    }

	  
	  protected class AssetTreeItem extends TreeItem {
		     
	         public AssetTreeItem(AssetNode an) {
	            // super(an.getDisplayName());
	        	 String displayName = "" + an.getAssetId();

                String hoverTitle = "";
	            
	            if (an.getMimeType()!=null && !"".equals(an.getMimeType())) {
	            	hoverTitle += "mimeType: " + an.getMimeType();
	            }
	            
	            if (an.getDescription()!=null && !"".equals(an.getDescription())) {
	            	hoverTitle += " Description: " + an.getDescription();
	            }

                 if (an.getDisplayName()!=null && !"".equals(an.getDisplayName())) {
                     displayName = an.getDisplayName();
                 }

                 if (an.getColor()!=null && !"".equals(an.getColor())) {
                     SafeHtmlBuilder nodeSafeHtml =  new SafeHtmlBuilder();
                     nodeSafeHtml.appendHtmlConstant("<span style=\"color:" + an.getColor() + "\">"
                             + displayName + "</span>");
                     setHTML(nodeSafeHtml.toSafeHtml());
                    hoverTitle += " Color: " + an.getColor();
                 } else {
                     setText(displayName);
                 }

                setTitle(hoverTitle);
	            setUserObject(an);

	            // setWidget(new Label(displayName));	            
	        }
	    
			public AssetNode getAssetNode() {
	            return (AssetNode) getUserObject();
	        }
		
	     }

    public TransactionMonitorFilterAdvancedWidget getTxFilter() {
        return txFilter;
    }

    public void setTxFilter(TransactionMonitorFilterAdvancedWidget txFilter) {
        this.txFilter = txFilter;
    }
}
