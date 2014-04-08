package gov.nist.hit.ds.logBrowser.client.widgets;

import gov.nist.hit.ds.logBrowser.client.event.AssetClickedEvent;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.ContextSupplement;
import gov.nist.hit.ds.repository.simple.search.client.PnIdentifier;
import gov.nist.hit.ds.repository.simple.search.client.QueryParameters;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryServiceAsync;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryTag;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria.Criteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm.Operator;
import gov.nist.hit.ds.repository.simple.search.client.SimpleData;
import gov.nist.hit.ds.repository.simple.search.client.exception.RepositoryConfigException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchWidget extends Composite {
	/**
	 *
	 * @author Sunil.Bhaskarla
	 */
	private static Logger logger = Logger.getLogger(SearchWidget.class.getName());
	public VerticalPanel topPanel;
	private static final String PROP_VALUE = "propValue";
	private static final String PROP_NAME = "propName";
	private static final String OPERATOR = "Operator";
	private static final String CRITERIA = "Criteria";
	
	private static final int NODETAG = 32768;
    private Option[] options;

    private Button moveRight = new Button("&rArr;");
    private Button moveLeft = new Button("&lArr;");

    private ListBox reposLeft = new ListBox(true);
    private ListBox reposRight = new ListBox(true);
    private Button moveUp = new Button("Move Up");
    private Button moveDown = new Button("Move Down");

    private VerticalPanel resultPanel = new VerticalPanel();

    Integer callCt = 0;
	Label lblTxt = new Label();
	Label lblAvailableRepos = new Label("Available Repositories");
	SimpleData sd = new SimpleData();


    SearchCriteria sc = new SearchCriteria(Criteria.AND);
	
	FlexTable msgs = new FlexTable();
	FlexTable grid = new FlexTable();
	FlexTable existingGrid = new FlexTable();
	DialogBox db = new DialogBox();
	
	
	String required = new String("*Required fields");

    public static enum Option {
        QUICK_SEARCH() {
            @Override
            public String toString() {
                return "Quick Search";
            }
        }, SEARCH_CRITERIA_REPOSITORIES() {
            @Override
            public String toString() {
                return "Search Criteria";
            }
        }, CRITERIA_BUILDER_MODE() {
            @Override
            public String toString() {
                return "Criteria Builder Mode";
            }
        }, CRITERIA_BUILDER() {
            @Override
            public String toString() {
                return "Criteria Builder";
            }
        }, TWO_SEARCH_TERMS_PER_ROW() {
            @Override
            public String toString() {
                return "Two search terms per row";
            }
        }, APPLY_CRITERIA_WITHOUT_RUNNING() {
            @Override
            public String toString() {
                return "Apply criteria";
            }
        }
    };

	protected ArrayList<String> propNames = new ArrayList<String>();
	
	static String PREFWIDTH = "10em"; // 20em
	static String DEFAULTWIDTH = "14em"; // 20em
	static String REPOSLBOXWIDTH = "15em"; // 17em
	static String DEFAULTTITLEWIDTH = "15em";
	static String MOVEMENTWIDTH = "7em";


	VerticalPanel criteriaPanel = new VerticalPanel();
	VerticalPanel scPanel = new VerticalPanel();

    RadioButton simpleQuery = new RadioButton("queryMode", "Simple");
    RadioButton advancedQuery = new RadioButton("queryMode", "Advanced");
    Button searchBtn = new Button("Search");
	ListBox qSelector = new ListBox();
	TextBox sqTxt = new TextBox();
	DisclosurePanel advancedDisclosure = new DisclosurePanel();
	
	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
    private EventBus eventBus;

	
	public SearchWidget(SimpleEventBus eventBus, Option[] options) {

        setEventBus(eventBus);
        setOptions(options);

	    searchBtn.setWidth("6em");
	    
		topPanel = new VerticalPanel();

		StyleInjector.inject(".searchCriteriaGroup {background-color: #F5F5F5} .searchHeader {background-color: #E6E6FA; width:100%}");
        StyleInjector.inject(".roundedButton1 {\n" +
                "  margin: 0;\n" +
                "  padding: 5px 7px;\n" +
                "  text-decoration: none;\n" +
                "  cursor: pointer;\n" +
                "  cursor: hand;\n" +
                "  font-size:small;\n" +
                "  background: url(\"images/hborder.png\") repeat-x 0px -2077px;\n" +
                "  border:1px solid #bbb;\n" +
                "  border-bottom: 1px solid #a0a0a0;\n" +
                "  border-radius: 16px;\n" +
                " -moz-border-radius: 16px;\n" +
                "}");
		
		grid.setCellSpacing(15);

		reposService.getRepositoryDisplayTags(reposSetup);
		reposService.getIndexablePropertyNames(propsSetup);
		
		topPanel.add(setupMainPanel());
		
	     // All composites must call initWidget() in their constructors.
	     initWidget(topPanel);

	     // Give the overall composite a style name.
	      setStyleName("example-OptionalCheckBox");
	}


	AsyncCallback<List<RepositoryTag>> reposSetup = new AsyncCallback<List<RepositoryTag>>() {
		
		public void onFailure(Throwable a) {
			Window.alert("No repositories found. Error: " + a.toString());
		}
		
		public void onSuccess(List<RepositoryTag> rtList) {
		
			if (rtList!=null) {
				lblAvailableRepos.setTitle("There are " + rtList.size() + " repositories.");
				
				for (RepositoryTag rt : rtList) {
					if ("savedQueryRepos".equals(rt.getType())) {
						popQuerySelector(rt.getCompositeId());
					}
					reposLeft.addItem(rt.getDisplayName(), rt.getCompositeId());
				}
							
				setRepositorySelectorCbs();
				
			}
		}
		
	};
	

	AsyncCallback<List<String>> propsSetup = new AsyncCallback<List<String>> () {

		public void onFailure(Throwable arg0) {
			Window.alert("No indexeable properties found: It is possible Asset Type's are not configured. propNames could not be loaded: " + arg0.getMessage());
		}

		public void onSuccess(List<String> props) {
			if (props!=null) {
				propNames.addAll(props);

				topPanel.add(setupSearchPanel());
				topPanel.add(scPanel);
                topPanel.add(resultPanel); ////
			}
			
		}
	};

	
	AsyncCallback<List<AssetNode>> searchResults = new AsyncCallback<List<AssetNode>> () {

		public void onFailure(Throwable arg0) {
			searchBtn.setEnabled(true);
			Window.alert("propNames could not be loaded: " + arg0.getMessage());
		}

		public void onSuccess(List<AssetNode> result) {
			searchBtn.setEnabled(true);
			
			resultPanel.clear();			
			resultPanel.add(new HTML("&nbsp;"));
			
			FlexTable resultFt = new FlexTable();
			int row = 1;
			
				
				try {
					
					if (result!=null && result.size()>0) {

						
						resultFt.setWidget(0, 0, new HTML("Repository Id"));
						resultFt.setWidget(0, 1, new HTML("Created Date"));
						resultFt.setWidget(0, 2, new HTML("Display Name"));
						resultFt.setWidget(0, 3, new HTML("Description"));
						
	
						for (final AssetNode an : result) {
							resultFt.setWidget(row,0,  new HTML(an.getRepId()));
							resultFt.setWidget(row,1,  new HTML(an.getCreatedDate()));
							
							Anchor assetLnk = new Anchor(an.getDisplayName());
							assetLnk.addClickHandler(new ClickHandler() {
								
								public void onClick(ClickEvent event) {
									eventBus.fireEvent(new AssetClickedEvent(an)); // Need to use AssetNode									
								}
							});
							resultFt.setWidget(row,2, assetLnk);
							resultFt.setWidget(row++,3,  new HTML(an.getDescription()));
						}
					}	
					
				} catch (Exception e) {
					Window.alert(e.toString());
				}
				
				resultFt.setWidget(row, 0, new HTML(""+ result.size() + " record(s) found."));
				if (result.size()>0) {
					resultFt.setCellSpacing(3);
					resultFt.getFlexCellFormatter().setStyleName(0, 0, "searchCriteriaGroup");
					resultFt.getFlexCellFormatter().setStyleName(0, 1, "searchCriteriaGroup");
					resultFt.getFlexCellFormatter().setStyleName(0, 2, "searchCriteriaGroup");
					resultFt.getFlexCellFormatter().setStyleName(0, 3, "searchCriteriaGroup");					
					
					resultFt.getFlexCellFormatter().setColSpan(row, 0, 4);
					
					resultFt.setWidth(
							scPanel.getWidget(0).getElement().getStyle().getWidth()
					);
					
				}
				
				resultPanel.add(resultFt);
				//// topPanel.add(resultPanel);
				
//				scPanel.add(new HTML("&nbsp;"));
//				scPanel.add(resultFt);


		}
	};

	protected void setRepositorySelectorCbs() {
		
		moveRight.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				moveItem(reposLeft, reposRight);
			}
		});
		
		moveLeft.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				moveItem(reposRight, reposLeft);
			}
		});
		
		moveUp.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				moveVertical(reposRight, -1);
			}
		});
		
		moveDown.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				moveVertical(reposRight, 2);
			}
		});
	}

	protected void moveVertical(ListBox l, int order) {
		int firstIdx = l.getSelectedIndex();
		int itemCt = l.getItemCount();
		
		if (itemCt<2) return;
		if (firstIdx==0 && order==-1) return;
		if (firstIdx==itemCt-1 && order>1) return;

		int newIdx = firstIdx + order;
		
		if (newIdx<=itemCt) {
			l.insertItem(l.getItemText(firstIdx), l.getValue(firstIdx), newIdx);
			
			if (order>1) {
				l.setSelectedIndex(newIdx);
				l.removeItem(firstIdx);
			} else {				
				l.removeItem(firstIdx+1);
				l.setSelectedIndex(newIdx);
			}
			
						
		}

	}

	protected void moveItem(ListBox l, ListBox r) {
		int firstIdx = l.getSelectedIndex();   
		if (firstIdx > -1) {
			int itemCt = l.getItemCount();
			
			for (int cx=firstIdx; cx<itemCt; cx++) {
				if (l.isItemSelected(cx)) {
					r.addItem(l.getItemText(cx), l.getValue(cx));								
				}
			}
			
			for (int cx=itemCt-1; cx>-1; cx--) {
				if (l.isItemSelected(cx)) {
					l.removeItem(cx);
				}
			}
		}
		
	}

    protected Boolean addSearchRepositories(VerticalPanel panel, String headerText) {
        FlexTable reposGrid = new FlexTable();
        int row = 0;
        int col = 0;

        panel.add(new HTML("<h4 class='searchHeader'>"+ headerText +"</h4>"));
        panel.add(new HTML("  "));

        panel.add(reposGrid);

        // HTML h;

        reposLeft.setVisibleItemCount(5);
        reposLeft.setWidth(REPOSLBOXWIDTH);

        reposRight.setVisibleItemCount(5);
        reposRight.setWidth(REPOSLBOXWIDTH);

        reposGrid.setWidget(row, col++, lblAvailableRepos);
        reposGrid.setWidget(row, col++, new HTML("&nbsp;"));
        reposGrid.setWidget(row, col++, new HTML("Selected Repositories"));
        reposGrid.setWidget(row, col, new HTML("Search Order"));


        HTMLTable.CellFormatter formatter = reposGrid.getCellFormatter();
        formatter.setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER);
        formatter.setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_MIDDLE);

        row++;

        col=0;

        reposGrid.setWidget(row, col++, reposLeft);

        FlexTable miniTable = new FlexTable();

        miniTable.setWidget(0, 0, moveRight);
        miniTable.setWidget(1, 0, moveLeft);

        reposGrid.setWidget(row, col++, miniTable);

        reposGrid.setWidget(row, col++, reposRight);

        miniTable = new FlexTable();
        moveUp.setWidth(MOVEMENTWIDTH);
        moveDown.setWidth(MOVEMENTWIDTH);
        miniTable.setWidget(0, 0, moveUp);
        miniTable.setWidget(1, 0, moveDown);

        reposGrid.setWidget(row, col++, miniTable);


        reposGrid.setWidget(++row, 0, lblTxt);
        reposGrid.getFlexCellFormatter().setColSpan(row, 0, col);

        reposGrid = new FlexTable();
        panel.add(reposGrid);

        return Boolean.TRUE;
    }

    protected Boolean addQuickSearchToPanel(VerticalPanel panel, String headerText) {
        panel.add(new HTML("<h4 class='searchHeader'>"+ headerText +"</h4>"));
        panel.add(new HTML("  "));
        FlexTable prefGrid = new FlexTable();
        int row = 0;
        int col = 0;

        prefGrid.setWidget(row++, col, new HTML("Select a Query/Filter "));
        qSelector.setWidth("142px");
        prefGrid.setWidget(row, col++, qSelector);
        Button btnRun = new Button(getQuickSearchCommandText());
        btnRun.setWidth("6em");
        prefGrid.setWidget(row, col, btnRun);

        btnRun.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                if (qSelector.getItemCount()==0) return;

                String queryLoc = qSelector.getValue(qSelector.getSelectedIndex());

                try {
                    reposService.getSearchCriteria(queryLoc, new AsyncCallback<QueryParameters>() {

                        public void onFailure(Throwable arg0) {
                            Window.alert("Could not get search criteria: " + arg0.getMessage());
                        }

                        public void onSuccess(QueryParameters qp) {
                            try {
                                if (qp.getSelectedRepos()!=null) {
                                    // 1. Move everything from right to left
                                    int items = reposRight.getItemCount();
                                    if (items>0) {
                                        for (int cx=0; cx<items; cx++) {
                                            reposRight.setSelectedIndex(cx);
                                        }
                                    }
                                    moveItem(reposRight, reposLeft);

                                    // 2. Clear all selections
                                    items = reposLeft.getItemCount();
                                    for (int cx=0; cx<items; cx++) {
                                        reposLeft.setItemSelected(cx, false);
                                    }

                                    // 3. Move selected based on selection criteria
                                    items = reposLeft.getItemCount();
                                    int selItems = qp.getSelectedRepos().length;
                                    for (int cx=0; cx<selItems; cx++) {
                                        for (int jx=0; jx<items; jx++) {
                                            String[] compositeKey = reposLeft.getValue(jx).split("\\^");
                                            if (compositeKey.length==2 && compositeKey[0]!=null && compositeKey[1]!=null)
                                                if (compositeKey[0].equals(qp.getSelectedRepos()[cx][0]) && compositeKey[1].equals(qp.getSelectedRepos()[cx][1])) {
                                                    reposLeft.setItemSelected(jx, true);
                                                }
                                        }
                                    }
                                    moveItem(reposLeft, reposRight);

                                }
                            } catch (Exception ex) {
                                Window.alert("One or more repositories could not selected.");
                            }

                            if (qp.getAdvancedMode()!=null) {
                                if (qp.getAdvancedMode()) {
                                    simpleQuery.setValue(false);
                                    advancedQuery.setValue(true);
                                } else {
                                    advancedQuery.setValue(false);
                                    simpleQuery.setValue(true);
                                }

                                advancedBuilderOption(qp.getAdvancedMode());
                            } else {
                                simpleQuery.setValue(true);
                                advancedBuilderOption(false);
                            }

                            if (qp.getSearchCriteria()!=null) {
                                try {
                                    if (sc.getSearchCriteria()!=null) sc.getSearchCriteria().clear();
                                    if (sc.getSearchTerms()!=null) sc.getSearchTerms().clear();
                                    if (sc.getProperties()!=null) sc.getProperties().clear();
                                    sc = null;
                                } catch (Exception ex) {
                                    logger.fine("Sc clear exception: " + ex.toString());
                                }
                                sc = qp.getSearchCriteria();
                                redrawTable();
                            }

                            if (!isOptionEnabled(Option.APPLY_CRITERIA_WITHOUT_RUNNING)) {
                                if (reposRight.getItemCount()>0) {
                                    runSearch();
                                }
                            }

                        }
                    } );
                } catch (RepositoryConfigException e) {

                    Window.alert("Call failed: " + e.toString());
                }

            }
        });


        // row++; col=0;

        panel.add (prefGrid);

        return Boolean.TRUE;

    }

    /**
     * NOTE: This could be a template to work with all types of Features
     * @param option
     * @return
     */
    private Boolean isOptionEnabled(Option option) {

        Option[] options =  getOptions();
        if (options!=null) {
            for (Option o : options) {
                if (o.equals(option)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

	protected VerticalPanel setupMainPanel() {
		VerticalPanel panel = new VerticalPanel();

        if (isOptionEnabled(Option.QUICK_SEARCH)) {
            String headerText = Option.QUICK_SEARCH.toString();
            if (isOptionEnabled(Option.APPLY_CRITERIA_WITHOUT_RUNNING)) {
                headerText = "Select a Filter";
            }
            addQuickSearchToPanel(panel, headerText );
        }

        if (isOptionEnabled(Option.SEARCH_CRITERIA_REPOSITORIES)) {
            addSearchRepositories(panel, Option.SEARCH_CRITERIA_REPOSITORIES.toString());
        }


		return panel;
	}
	

	int stGridRow = 0;
	FlexTable mainTable =  new FlexTable();				
	FlexTable stGrid = new FlexTable();
	
	protected SearchCriteria getScByPath(String path, int limit)  {		
			
		if (path==null || "".equals(path)) {
			return sc;
		}
		
		String ids[] = path.split("\\.");
		
		SearchCriteria scObj = sc;
		if (Math.abs(limit) > ids.length) {
			return null;
		}
				
		for (int cx=0; cx< (ids.length + (limit)); cx++) {
			int index = Integer.parseInt(ids[cx]);
			
			if (isLeafNode(index)) {
				return scObj;
			}
			
			scObj = scObj.getSearchCriteria().get(index);
		}
		return scObj;
	}

	/**
	 * @param index
	 * @return
	 */
	protected boolean isLeafNode(int index) {
		return (index & NODETAG) == NODETAG;
	}
	
	protected int getLeafId(int index) {
		return (index ^ NODETAG);
	}
	
	protected String makeLeafNodeId(int index) {
		return "" + (NODETAG | index);
	}
	
	protected int getLeafNodeId(String path) {
		String ids[] = path.split("\\.");
		
		int leaf = Integer.parseInt(ids[ids.length - 1]);
		if (isLeafNode(leaf)) { 
			return getLeafId(leaf);
		} else {
			Window.alert("Error: Not a leaf node " + path);
		}
		return -1;
	}
	
	protected void removeLeafNode(String path) {
		SearchCriteria scObj = getScByPath(path);
		scObj.getSearchTerms().remove(getLeafNodeId(path));
		redrawTable();
	}
	
	protected void setOpSelector(int index, String path) {
		Operator e = Operator.values()[index];
		getSt(path).setOperator(e);		
	}
	
	protected void setPropNameSelector(int index, String path) {
		String s = getPropNames().get(index);
		PropertyKey p = PropertyKey.getPropertyKey(s);
		
		if (p==null) { // Not part of original property key enumeration, automatic quoted identifier 
			getSt(path).setPropNameQuoted(s);
		} else { // identifier based on property key
			getSt(path).setPropName(p.getPropertyName());
		} 
		
	}
	
	protected void setStValue(String value, String path) {	
		getSt(path).setValues(new String[]{value});		
	}
	
	private SearchTerm getSt(String path) {
		int leafNodeId =  getLeafNodeId(path);
		SearchCriteria scObj = getScByPath(path);
		return scObj.getSearchTerms().get(leafNodeId);
	}
	
	protected void setCSelector(int index, String path) {
		Criteria e = Criteria.values()[index];
		SearchCriteria scObj = getScByPath(path);
		scObj.setCriteria(e);		
	}
	
	protected SearchCriteria getScByPath(String path) {
		return getScByPath(path,0);
	}
	
	
	
	protected void addNewSearchTerm(String id) {

		// Assignment
		// assign by id parse & attach to the right obj
		
		SearchCriteria scObj = getScByPath(id);
		
		SearchTerm st = new SearchTerm(getPropNames().get(0),SearchTerm.Operator.EQUALTO,"");
		scObj.append(st);

		redrawTable();
	}

	/**
	 * 
	 */
	public void redrawTable() {
		scPanel.clear();
		printTable(sc, "");
		// topPanel.add(scPanel);
	}

	/**
	 * 
	 */
	private void enterFixedBuilderMode() {
		// resultPanel.clear();
		simpleQuery.setEnabled(false);
		advancedQuery.setEnabled(false);
	}
	
	protected void addNewGroup(String id) {
		

		// Assignment
		// assign by id parse & attach to the right obj
		
		
		if ("".equals(id)) {
			SearchCriteria parent = new SearchCriteria(Criteria.AND);
			
			SearchCriteria child2 = new SearchCriteria(Criteria.AND);
			
			parent.append(sc);
			parent.append(child2);
			sc = parent;
						
		} else if (id!=null) {
			SearchCriteria parent = new SearchCriteria(Criteria.AND);
			
			SearchCriteria scObj = getScByPath(id);
			SearchCriteria child2 = new SearchCriteria(Criteria.AND);
			
			parent.append(scObj);
			parent.append(child2);
			
			
			 
			if (id.length()==1) { // off the root node ex. "0"
				sc.getSearchCriteria().remove(Integer.parseInt(id));
				// sc.getSearchCriteria().add(parent);
				
				sc.getSearchCriteria().add(Integer.parseInt(id), parent);
				
			} else {
				SearchCriteria superObj = getScByPath(id, -1);
				
				String[] ids =  id.split("\\.");
				int nodeIdx = Integer.parseInt(ids[ids.length-1]);
				superObj.getSearchCriteria().remove(nodeIdx);
				superObj.getSearchCriteria().add(nodeIdx, parent);
			}
			
		}
		
		redrawTable();
				
		
	}
	
	protected void appendCriteria(String id) {
				
		
		SearchCriteria scObj = getScByPath(id,-1);
		
		if (sc.equals(scObj) && scObj.getSearchCriteria().size()==0) {
			SearchCriteria parent = new SearchCriteria(Criteria.AND);
			
			SearchCriteria child2 = new SearchCriteria(Criteria.AND);
			
			parent.append(sc);
			parent.append(child2);
			sc = parent;
		} else {
			SearchCriteria scNew = new SearchCriteria(Criteria.AND);
			scObj.append(scNew);
		}	
				
		

		redrawTable();

	}

	/**
	 * 
	 */
	public void debugTxt(Object o) {
		// lblTxt.setText("setting " + o.toString());
		// new PopupMessage(o.toString());
		if (o!=null) {
			logger.fine("debug: " + o.toString());
		}
	}
	
	protected void printTable(SearchCriteria sc, String ancestorId) {
	
		if (sc.getSearchTerms().isEmpty()) {
			int stRow=0;
			FlexTable stGroup = new FlexTable();
			// stGroup.addStyleName("searchCriteriaGroup");

			
			FlexTable cmdGroup =  new FlexTable();
			
			Button addNewStBtn = new Button(newSeachTermTxt);
			Button addNewGroup = new Button(newCriteriaGroupTxt);
			Button appendCriteria = new Button(appendCriteriaTxt);
			Button removeGroupBtn = newRemoveGroupBtn(ancestorId);
			
			if (simpleQuery.getValue()) {
				addNewGroup.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);				
				appendCriteria.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
				removeGroupBtn.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
			}
			
	
			addNewGroup.addClickHandler(newGroupHandler(ancestorId));				
			appendCriteria.addClickHandler(appendCriteriaHandler(ancestorId));				
			addNewStBtn.addClickHandler(newSearchTermHandler(ancestorId));
			
			// search terms can only be added to free-standing sc's
			if (sc.getSearchCriteria()!=null && sc.getSearchCriteria().size()>0) {			
				addNewStBtn.setVisible(false);
			} else  {
				addNewStBtn.setVisible(true);
			}
			
			if (sc.getSearchCriteria()!=null && sc.getSearchCriteria().size()>0) {				
				appendCriteria.setVisible(true);
				appendCriteria.getElement().getStyle().setDisplay(Style.Display.BLOCK);
			} else  {
				appendCriteria.setVisible(false);
				appendCriteria.getElement().getStyle().setDisplay(Style.Display.NONE);
			}	
			
			ListBox cSelector = getCSelector();
			cSelector.setSelectedIndex(sc.getCriteria().ordinal());
			cSelector.addChangeHandler(new CSelectorChangeHandler(CRITERIA, ancestorId));
		
			int col=0;
			cmdGroup.setWidget(0, col++, cSelector);
			cmdGroup.setWidget(0, col++, addNewStBtn);
			cmdGroup.setWidget(0, col++, addNewGroup);
			
			if (appendCriteria.isVisible()) {
				cmdGroup.setWidget(0, col++, appendCriteria);
			}
			cmdGroup.setWidget(0, col++, removeGroupBtn);
					
			
			stGroup.getFlexCellFormatter().setColSpan(0, 0, 3); // stGroup is just empty container here
			
			stGroup.setWidget(stRow++, 0, cmdGroup);
									
			stGroup.addStyleName("searchCriteriaGroup");
			setGroup(ancestorId, stGroup);
			
		
		} else {
			
			FlexTable stGroup = new FlexTable();
			if (sc.getSearchTerms()!=null && !sc.getSearchTerms().isEmpty()) {

				int stRow=0;
			
				stGroup.addStyleName("searchCriteriaGroup");

				
				FlexTable cmdGroup =  new FlexTable();
				
				Button addNewStBtn = new Button(newSeachTermTxt);
				Button addNewGroup = new Button(newCriteriaGroupTxt);
				Button appendCriteria = new Button(appendCriteriaTxt);
				Button removeGroupBtn = newRemoveGroupBtn(ancestorId);
				
				if (simpleQuery.getValue()) {						
					addNewGroup.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);				
					appendCriteria.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
					appendCriteria.getElement().getStyle().setDisplay(Style.Display.NONE);
					removeGroupBtn.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
				} else {
					
					if (sc.getSearchCriteria()!=null && sc.getSearchCriteria().size()>0) {				
						appendCriteria.setVisible(true);
						appendCriteria.getElement().getStyle().setDisplay(Style.Display.BLOCK);
					} else  {
						appendCriteria.setVisible(false);
						appendCriteria.getElement().getStyle().setDisplay(Style.Display.NONE);
					}					
				}


				
				
				addNewGroup.addClickHandler(newGroupHandler(ancestorId));				
				appendCriteria.addClickHandler(appendCriteriaHandler(ancestorId));				
				addNewStBtn.addClickHandler(newSearchTermHandler(ancestorId));
				
				
				if (ancestorId!=null && "".equals(ancestorId)) {
					addNewStBtn.getElement().setId(newSeachTermTxt);
					addNewGroup.getElement().setId(newCriteriaGroupTxt);
					appendCriteria.getElement().setId(appendCriteriaTxt);
				} 
				
				
				ListBox cSelector = getCSelector();
				cSelector.setSelectedIndex(sc.getCriteria().ordinal());
				cSelector.addChangeHandler(new CSelectorChangeHandler(CRITERIA, ancestorId));


				
				int col=0;
				cmdGroup.setWidget(0, col++, cSelector);				
				cmdGroup.setWidget(0, col++, addNewStBtn);
				cmdGroup.setWidget(0, col++, addNewGroup);
				
				if (appendCriteria.isVisible()) {
					cmdGroup.setWidget(0, col++, appendCriteria);	
				}				
				cmdGroup.setWidget(0, col++, removeGroupBtn);


                int colSpan = 4;
                if (isOptionEnabled(Option.TWO_SEARCH_TERMS_PER_ROW)) {
                    colSpan *= 2;
                }

				stGroup.getFlexCellFormatter().setColSpan(0, 0, colSpan);
				stGroup.setWidget(stRow++, 0, cmdGroup);

				stGroup.setWidget(stRow, 0, new HTML("Property Name"));
				stGroup.setWidget(stRow, 1, new HTML("Operator"));
				stGroup.setWidget(stRow, 2, new HTML("Value"));
                stGroup.setWidget(stRow, 3, new HTML("")); // Command

                if (isOptionEnabled(Option.TWO_SEARCH_TERMS_PER_ROW) && (sc.getSearchTerms()!=null && sc.getSearchTerms().size()>1)) {
                    stGroup.setWidget(stRow, 4, new HTML("Property Name"));
                    stGroup.setWidget(stRow, 5, new HTML("Operator"));
                    stGroup.setWidget(stRow, 6, new HTML("Value"));
                    stGroup.setWidget(stRow, 7, new HTML("")); // Command
                }

                stRow++;


				int leafIndex = 0;
                int stRowCounter = 0;
                int stCol = 0;
				for (SearchTerm st : sc.getSearchTerms()) {
					
					ListBox stPn = new ListBox();
					ListBox stOp = new ListBox();
					TextBox stTxt = new TextBox();
					
					stTxt.setWidth(DEFAULTWIDTH);
					
					
					String nodeId = makeLeafNodeId(leafIndex++);
					
					if (!"".equals(ancestorId)) {
						nodeId = ancestorId + "." + nodeId;
					}
					
					stPn.getElement().setId(nodeId); // assign to the delete button
					int pnIdx = 0;
					
					for (int cx=0; cx<getPropNames().size();cx++) {
						if (getPropNames().get(cx).equals(PnIdentifier.stripQuotes(st.getPropName()))) {
							pnIdx = cx; 
						}
						stPn.addItem(getPropNames().get(cx));
					}					
					stPn.setSelectedIndex(pnIdx);
					stPn.addChangeHandler(new CSelectorChangeHandler(PROP_NAME, nodeId));					
					
					for (SearchTerm.Operator e : SearchTerm.Operator.values()) {
						if (!e.getMultipleValues()) { // Exclude multiple values from UI to simplify building criteria, although they can be used through the API 
							stOp.addItem(e.getDisplayName());
						}
					}					
					stOp.setSelectedIndex(st.getOperator().ordinal());
					stOp.addChangeHandler(new CSelectorChangeHandler(OPERATOR, nodeId));
					
					if (st.getValues()!=null && st.getValues().length>0) {
						stTxt.setValue(st.getValues()[0]);
					}
					stTxt.addChangeHandler(new CSelectorChangeHandler(PROP_VALUE, nodeId));
					
					
					Button remove = new Button("&ndash;");
                    remove.setTitle("Remove");
                    remove.setStyleName("roundedButton1");
					remove.addClickHandler(new ContextSupplement<String>(nodeId) {
						
						public void onClick(ClickEvent event) {
							debugTxt("remove " + getParameter());				
							removeLeafNode(getParameter());
						}
					});


                    if (isOptionEnabled(Option.TWO_SEARCH_TERMS_PER_ROW)) {
                        if (stRow> 0 && (stRowCounter % 2) !=0) {
                            stCol=4;
                        } else {
                            stCol=0;
                        }
                    } else {
                        stCol = 0;
                    }
							
					stGroup.setWidget(stRow, stCol, stPn);
					stGroup.setWidget(stRow, stCol+1, stOp);
					stGroup.setWidget(stRow, stCol+2, stTxt);
					stGroup.setWidget(stRow, stCol+3, remove);

                    if (isOptionEnabled(Option.TWO_SEARCH_TERMS_PER_ROW)) {
                       if ((++stRowCounter % 2)==0) {
                         stRow++;
                       }
                    } else {
                        stRow++;
                    }

				}
			
				
				setGroup(ancestorId, stGroup);
				
				// mainTable.setWidget(0, 0, stGroup);
			}

	}
		
		if (sc.getSearchCriteria()!=null) {
			Integer rootIndex = new Integer(0);
			for (SearchCriteria criteria : sc.getSearchCriteria()) {
				String groupId = rootIndex.toString();
				if (ancestorId != null && !"".equals(ancestorId)) {
					groupId = ancestorId + "." + rootIndex.toString();
				}
				printTable(criteria, groupId );
				rootIndex++;
			}
		}
		
		
	
	}

    /**
     *
     * @param path
     * @return
     */
	private Button newRemoveGroupBtn(String path) {
		
			Button removeGroupBtn = new Button(removeGroupTxt);			
			
			
			if ("".equals(path)) {
				removeGroupBtn.getElement().setId(removeGroupTxt);
			}
			
			
			
			removeGroupBtn.addClickHandler(new ContextSupplement<String>(path) {
				
				public void onClick(ClickEvent event) {
					HorizontalPanel hpDialog = new HorizontalPanel();
						
						FlexTable ft = new FlexTable();
						Button yesBtn = new Button("Yes");
						Button noBtn = new Button("No");
						
						ft.setWidget(0, 0, yesBtn);
						ft.setWidget(0, 1, noBtn);
						
						hpDialog.add(new HTML("Remove group?"));
						hpDialog.add(new HTML("&nbsp;"));
						hpDialog.add(ft);
						
						db.clear();
						db.setWidget(hpDialog);
						
						Button src = (Button)event.getSource();
						db.setPopupPosition(src.getAbsoluteLeft(),src.getAbsoluteTop());
						db.show();
						
						yesBtn.addClickHandler(new ContextSupplement<String>(getParameter()) {
							
							public void onClick(ClickEvent event) {

							//	new PopupMessage("" + getParameter());
								
								SearchCriteria scPar = getScByPath(getParameter(),-1);
								SearchCriteria scObj = getScByPath(getParameter());
								
								
								
								if (!scPar.equals(scObj)) {
									scPar.getSearchCriteria().remove(scObj);							
								} else {
									addNewSearchCriteria();
								}
								db.hide();
								redrawTable();
								
							}	
						});
						
						noBtn.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								db.hide();
							}
						});
					
						
				}
			});
			
		return removeGroupBtn;
	}

	/**
	 * @param ancestorId
	 * @return
	 */
	protected ContextSupplement<String> newSearchTermHandler(String ancestorId) {
		return new ContextSupplement<String>(ancestorId) {
		
			public void onClick(ClickEvent event) {
					debugTxt(getParameter());
					enterFixedBuilderMode();
					addNewSearchTerm(getParameter());
			}
		};
	}

	/**
	 * @param ancestorId
	 * @return
	 */
	protected ContextSupplement<String> appendCriteriaHandler(String ancestorId) {
		return new ContextSupplement<String>(ancestorId) {
			
			public void onClick(ClickEvent event) {
					debugTxt(getParameter());
					enterFixedBuilderMode();
					appendCriteria(getParameter());
			}
		};
	}

	/**
	 * @param ancestorId
	 * @return
	 */
	protected ContextSupplement<String> newGroupHandler(String ancestorId) {
		return new ContextSupplement<String>(ancestorId) {
			
			public void onClick(ClickEvent event) {
					debugTxt(getParameter());
					enterFixedBuilderMode();
					addNewGroup(getParameter());
			}
		};
	}

	/**
	 * @return
	 */
	private ListBox getCSelector() {
		ListBox cSelector = new ListBox();
		for (SearchCriteria.Criteria e : SearchCriteria.Criteria.values()) {
			cSelector.addItem(e.toString());
		}		

		return cSelector;
	}

    /**
     *
     * @param savedQueryReposId
     */
	private void popQuerySelector(String savedQueryReposId) {
		
		String[] compositeKey = savedQueryReposId.split("\\^");
//		String[][] selectedRepos = new String[1][2];
//		selectedRepos[0][0] = compositeKey[0];
//		selectedRepos[0][1] = compositeKey[1];

        popQuerySelector(compositeKey[0],compositeKey[1]);
	}

    /**
     *
     * @param id
     * @param acs
     */
    private void popQuerySelector(String id, String acs) {
        AsyncCallback<List<AssetNode>> searchResults = new AsyncCallback<List<AssetNode>> () {

            public void onFailure(Throwable arg0) {
                Window.alert("savedQueryRepos could not be loaded: " + arg0.getMessage());
            }

            public void onSuccess(List<AssetNode> result) {
                qSelector.clear();
                for (AssetNode an: result) {
                    qSelector.addItem(an.getDisplayName(), an.getLocation());
                }

            }
        };

        try {
            reposService.getSavedQueries(id,acs, searchResults);
        } catch (RepositoryConfigException e) {
            Window.alert("Call failed: " + e.toString());
        }

    }

	/**
	 * @param ancestorId
	 * @param stGroup
	 */
	private void setGroup(String ancestorId, FlexTable stGroup) {
		if (ancestorId!=null && !"".equals(ancestorId)) {
			int ancestorCt = ancestorId.split("\\.").length;
			
			if (ancestorCt>0) {
				
				Element e = DOM.getElementById(ancestorId);
				if (e!=null) {
					e.setAttribute("disabled", "true");
				}
				
				FlexTable stNestedGroup = new FlexTable();
				stNestedGroup.setCellPadding(10);
				for (int cx =0; cx< ancestorCt; cx++) {
					stNestedGroup.setWidget(0, cx, new Hidden("h_" + ancestorId));
				
				}
				stNestedGroup.setWidget(0, ancestorCt, stGroup);
				scPanel.add(stNestedGroup);
			}
		} else {
			scPanel.add(stGroup);	
		}
	}
	
	/**
	 * 
	 */
	public void addNewSearchCriteria() {
		sc = new SearchCriteria(Criteria.AND);
		
	
		SearchTerm st = new SearchTerm(getPropNames().get(0),Operator.EQUALTO,"");
		sc.append(st);
//		st = new SearchTerm(getPropNames().get(0),Operator.EQUALTO,"");
//		sc.append(st);
		

		
		
		
		simpleQuery.setEnabled(true);
		advancedQuery.setEnabled(true);

		advancedBuilderOption(advancedQuery.getValue());
		
		// Preserve builder mode
		// simpleQuery.setValue(true);
		
		
		resultPanel.clear();
		scPanel.clear();
		printTable(sc, "");
	}

	/**
	 * 
	 */
	public void advancedBuilderOption(boolean abo) {
		
		Style.Visibility v = (abo)?Style.Visibility.VISIBLE:Style.Visibility.HIDDEN;
		Style.Display d = (abo)?Style.Display.BLOCK:Style.Display.NONE;
		
		Element e = Document.get().getElementById(newCriteriaGroupTxt);	
		e.getStyle().setVisibility(v);
		e.getStyle().setDisplay(d);
		
//		e = Document.get().getElementById(appendCriteriaTxt);
//		e.getStyle().setVisibility(v);
//		e.getStyle().setDisplay(d);
//		
		e = Document.get().getElementById(removeGroupTxt);
		e.getStyle().setVisibility(v);
		e.getStyle().setDisplay(d);
		
		
	}

	
	String newSeachTermTxt = "New Search Term";
	String newCriteriaGroupTxt = "Sub-criteria";
	String appendCriteriaTxt = "Append Criteria";
	String removeGroupTxt = "Remove Group";
	Button newSeachTerm = new Button(newSeachTermTxt);
	Button newCriteriaGroup = new Button(newCriteriaGroupTxt);




	/**
	 * 
	 */
	public void runSearch() {
		int itemCt = reposRight.getItemCount();
		
		if (itemCt==0) {
			Window.alert("Please select at least one repository.");
			return;
		} else {
			resultPanel.clear();
			resultPanel.add(new HTML("&nbsp;"));
			resultPanel.add(new HTML("Searching..."));
			//// topPanel.add(resultPanel);
			
			searchBtn.setEnabled(false);
			
			String[][] selectedRepos = getSelectedRepos(itemCt);
			 reposService.search(selectedRepos, sc, searchResults);
			 // ((ListBox)event.getSource()).setEnabled(false);
		}
	}

    protected VerticalPanel setupCriteriaBuilderMode() {
        if (getPropNames()==null || (getPropNames()!=null && getPropNames().size()==0)) {
            return new VerticalPanel();
        }

        criteriaPanel.setWidth("100%");

        FlexTable queryMode = new FlexTable();


        simpleQuery.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                advancedBuilderOption(false);

            }

        });

        advancedQuery.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

                advancedBuilderOption(true);

            }
        });

        Button resetCriteriaBtn = new Button("Reset");
        resetCriteriaBtn.setWidth("6em");
        resetCriteriaBtn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                addNewSearchCriteria();

            }
        });


        searchBtn.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                runSearch();
            }
        });

        // simpleQuery.setValue(true);

        queryMode.setWidget(0, 0, new HTML("Criteria Builder Mode"));
        queryMode.setWidget(1, 0, simpleQuery);
        queryMode.setWidget(1, 1, advancedQuery);
        queryMode.setWidget(1, 2, resetCriteriaBtn);
        queryMode.setWidget(1, 3, searchBtn);
        queryMode.getFlexCellFormatter().setColSpan(0, 0, 4);

        FlexTable saveGrid = new FlexTable();
        saveGrid.setWidget(0, 0, new HTML("Save Workspace Query as"));
        sqTxt.setWidth("132px");
        saveGrid.setWidget(1, 0, sqTxt);
        Button btnSave = new Button("Save");
        btnSave.setWidth("6em");
        saveGrid.setWidget(1, 1, btnSave);
        saveGrid.getFlexCellFormatter().setColSpan(0, 0, 2);

        queryMode.setWidget(2, 0, saveGrid);
        queryMode.getFlexCellFormatter().setColSpan(2, 0, 4);

        btnSave.addClickHandler(new ContextSupplement<String>("") {
            public void onClick(ClickEvent event) {
                if ("".equals(sqTxt.getText())) {
                    Window.alert("Please enter a name");
                    return;
                }
                QueryParameters qp = new QueryParameters();

                String[][] selectedRepos = getSelectedRepos(reposRight.getItemCount());
                qp.setName(sqTxt.getText());
                qp.setAdvancedMode(advancedQuery.getValue());
                qp.setSelectedRepos(selectedRepos);
                qp.setSearchCriteria(sc);

                try {
                    reposService.saveSearchCriteria(qp, new AsyncCallback<AssetNode>() {
                        public void onFailure(Throwable arg0) {
                            Window.alert("Criteria could not be saved: " + arg0.toString());
                        }

                        public void onSuccess(AssetNode an) {
                            popQuerySelector(an.getRepId(),an.getReposSrc());
//                            qSelector.addItem(an.getDisplayName(), an.getLocation());
                            sqTxt.setText("");
                            Window.alert("Save successful");
                        }

                    });
                } catch (RepositoryConfigException e) {
                    Window.alert("Criteria error: " + e.toString());
                }
            }
        });




        criteriaPanel.add(queryMode);
        criteriaPanel.add(new HTML("&nbsp;"));

        // Start in simple builder mode
        simpleQuery.setValue(true);

        return criteriaPanel;

    }

	protected VerticalPanel setupSearchPanel() {

        if (isOptionEnabled(Option.CRITERIA_BUILDER_MODE)) {
            setupCriteriaBuilderMode();
        }

		addNewSearchCriteria();
		
		return criteriaPanel;

	}
	
	/**
	 * @param itemCt
	 * @return
	 */
	protected String[][] getSelectedRepos(int itemCt) {
		String[][] selectedRepos = new String[itemCt][2];
		for (int cx=0; cx<itemCt; cx++ ) {
			// System.out.println(reposRight.getValue(cx) + " -- " + reposRight.getItemText(cx));
			if (null==reposRight.getValue(cx) || "".equals(reposRight.getValue(cx))) {
				Window.alert(reposRight.getItemText(cx) +" value is null");
			} else {
				String[] compositeKey = reposRight.getValue(cx).split("\\^");
				selectedRepos[cx][0] = compositeKey[0];
				selectedRepos[cx][1] = compositeKey[1];							
			}
		}
		return selectedRepos;
	}
	
	class CSelectorChangeHandler implements ChangeHandler {

		private String nodePath; 
		private String propType; 
		
		CSelectorChangeHandler(String type, String path) {
			this.propType = type;
			this.nodePath = path;
		}

		public void onChange(ChangeEvent event) {
			
			if (CRITERIA.equals(this.propType)) {
				setCSelector(((ListBox)event.getSource()).getSelectedIndex(),this.nodePath);	
			} else if (OPERATOR.equals(this.propType)) {
				setOpSelector(((ListBox)event.getSource()).getSelectedIndex(),this.nodePath);	
			} else if (PROP_NAME.equals(this.propType)) {
				setPropNameSelector(((ListBox)event.getSource()).getSelectedIndex(),this.nodePath);
			} else if (PROP_VALUE.equals(this.propType)) {
				setStValue(((TextBox)event.getSource()).getValue(),this.nodePath);
			}							
			
		}
	}

	public ArrayList<String> getPropNames() {
		return propNames;
	}

	public void setPropNames(ArrayList<String> propNames) {
		this.propNames = propNames;
	}

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Option[] getOptions() {
        return options;
    }

    public void setOptions(Option[] features) {
        this.options = features;
    }
    public ListBox getReposLeft() {
        return reposLeft;
    }

    public void setReposLeft(ListBox reposLeft) {
        this.reposLeft = reposLeft;
    }


    public ListBox getReposRight() {
        return reposRight;
    }

    public void setReposRight(ListBox reposRight) {
        this.reposRight = reposRight;
    }
    public VerticalPanel getResultPanel() {
        return resultPanel;
    }

    public void setResultPanel(VerticalPanel resultPanel) {
        this.resultPanel = resultPanel;
    }
    public SearchCriteria getSc() {
        return sc;
    }

    public void setSc(SearchCriteria sc) {
        this.sc = sc;
    }
    public String getQuickSearchCommandText() {
        if (!isOptionEnabled(Option.APPLY_CRITERIA_WITHOUT_RUNNING)) {
            return "Run";
        } else {
            return "Apply";
        }
    }

}
