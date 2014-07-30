package gov.nist.hit.ds.logBrowser.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import gov.nist.hit.ds.logBrowser.client.CsvTableFactory;
import gov.nist.hit.ds.logBrowser.client.IndexedColumn;
import gov.nist.hit.ds.repository.api.PropertyKey;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryServiceAsync;
import gov.nist.hit.ds.repository.simple.search.client.SearchCriteria;
import gov.nist.hit.ds.repository.simple.search.client.SearchTerm;
import gov.nist.hit.ds.repository.simple.search.client.exception.RepositoryConfigException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This widget builds an aggregate view of the messages, which are embedded in CSV formatted content files, logged by toolkit components.
 */
public class EventMessageAggregatorWidget extends Composite {

    public static final int FIXED_HEADER_LAST_IDX = 1;
    private static Logger logger = Logger.getLogger(EventMessageAggregatorWidget.class.getName());
    private ScrollPanel contentPanel = new ScrollPanel();
    private SimpleEventBus eventBus;
    private String eventAssetId;
    private String assetType;
    private String externalRepositoryId;
    private String[] displayColumns;
    final private RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
    private CellTable<List<SafeHtml>> table = new CellTable<List<SafeHtml>>(300); // new CsvTableFactory().createCellTable(rows.toArray(new String[rows.size()][]));
    private ListDataProvider<List<SafeHtml>> dataProvider  = new ListDataProvider<List<SafeHtml>>();

//    private DataGrid<List<SafeHtml>> table = new DataGrid<List<SafeHtml>>(5);
    private CsvTableFactory csvFactory = new CsvTableFactory();

    private List<List<SafeHtml>> dataRows = new ArrayList<List<SafeHtml>>();
    private List<String[]> rows = new ArrayList<String[]>();

    /**
     * Builds a tabular data display based on an aggregate view of the event messages.
     * @param eventBus The event bus to receive asset click event (to display in the log browser for instance).
     * @param externalRepositoryId The Id of the external repository containing the asset Id.
     * @param eventAssetId The Id of the upper-level Events asset.
     * @param assetType The type of assets to aggregate under the upper-level Events asset.
     * @param csvColumnNames The column row values to aggregate and display in the table. Column names are case-sensitive.
     */
   public EventMessageAggregatorWidget(SimpleEventBus eventBus, String externalRepositoryId, String eventAssetId, String assetType, String[] csvColumnNames) {
       setEventBus(eventBus);
       setExternalRepositoryId(externalRepositoryId);
       setEventAssetId(eventAssetId);
       setAssetType(assetType);
       setDisplayColumns(csvColumnNames);

     // All composites must call initWidget() in their constructors.
     initWidget(setupLayout());

   }

    protected ScrollPanel setupLayout() {
        logger.log(Level.FINE, "initializing aggregator...");
        setSize("52%", "20%"); // Default size

//        CellTable<List<SafeHtml>> table = new CsvTable().createCellTable(an.getCsv());
//        contentPanel.add(table);

        AsyncCallback<List<AssetNode>> searchResults = new AsyncCallback<List<AssetNode>> () {

            public void onFailure(Throwable arg0) {
                setError("Event assetId "+ getEventAssetId() +" could not be found in "+ getExternalRepositoryId() +": " + arg0.getMessage());
            }
            public void onSuccess(List<AssetNode> result) {
                logger.fine("agg main event find success");
                if (result.size()==1) { // Only one is expected
                    AssetNode an = result.get(0);
                    logger.fine("agg main event size match success");
                    try {
                        // 1.
                        reposService.getImmediateChildren(an, new AsyncCallback<List<AssetNode>>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                logger.log(Level.SEVERE, caught.toString());
                                setError("getImmediateChildren error:" + caught.toString());
                            }

                            @Override
                            public void onSuccess(List<AssetNode> children) {
                                /* 2. Events
                                      -> child1
                                      -> child2
                                */
                                logger.info("Processing nodes... size:" + children.size());

                                for (AssetNode child : children) {
                                      if (getAssetType().equals(child.getType())) {
                                          logger.log(Level.INFO,"processing..." + getAssetType() + " id:" + child.getAssetId());
                                          try {

                                              aggregateMessage(child, "assertionGroup", rows, getDisplayColumns());
                                          } catch (RepositoryConfigException rce) {
                                              logger.warning(rce.toString());
                                          }

                                      }
                                }




                            }
                        });

                    } catch (Exception ex) {
                        setError("getImmediateChildren call failed: " + ex.toString());
                    }
                } else {
                    setError("Event assetId did not match the expected size=1: Id:" + getEventAssetId());
                }

            }
        };

        //                                Window.alert("row sz: " + rows.size() + " rows[0][1]" + rows.get(0)[1]);

        logger.info("Aggregated rows: " +table.getRowCount());
        //                                List<List<SafeHtml>> rowList = dataProvider.getList();


//        table.setTableBuilder(new DefaultCellTableBuilder<List<SafeHtml>>(table));

        //txTable.setWidth("100%", true);
//        table.setWidth("50%");
//        table.setHeight("50%");
//        table.setSkipRowHoverCheck(true);
//        table.setSkipRowHoverFloatElementCheck(true);
//        table.setSkipRowHoverStyleUpdate(true);
//        txTable.setStyleName("txDataGridNoTableSpacing");



        // Create table columns
        for (int c = 0; c < getDisplayColumns().length; c++) {
            table.addColumn(new IndexedColumn(c),
                    new TextHeader(getDisplayColumns()[c]));
        }

        rows.add(getDisplayColumns()); // Header row

        logger.info("updating rows:" + dataRows.size());
        dataProvider.setList(dataRows);
        dataProvider.addDataDisplay(table);
        getContentPanel().add(table);


//        new CsvTableFactory().updateList(1, rows.toArray(new String[rows.size()][]),dataProvider.getList());



        SearchCriteria searchCriteria = new SearchCriteria(SearchCriteria.Criteria.AND);
        searchCriteria.append(new SearchTerm(PropertyKey.ASSET_ID, SearchTerm.Operator.EQUALTO, getEventAssetId()));

        String[][] repository = new String[][]{{getExternalRepositoryId(), "RW_EXTERNAL"}};
        reposService.search(repository, searchCriteria, searchResults);

        return getContentPanel();
    }

    private void aggregateMessage(AssetNode an, final String assetType, final List<String[]> rows, final String[] columnNames) throws RepositoryConfigException {

            logger.info("entering aggregateMessage: an displayName:" + an.getDisplayName() + " an type:" + an.getType() + " search type:" + assetType + " mimeType:" + an.getMimeType() + " hasContent:" + an.isContentAvailable() +  " csv:" + (an.getCsv()!=null));
            if (assetType.equals(an.getType()) && "text/csv".equals(an.getMimeType()) && an.isContentAvailable() && an.getCsv()==null) {
                reposService.getAssetTxtContent(an, new AsyncCallback<AssetNode>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        logger.warning("getAssetTxtContent failed: " + caught.toString());
                    }

                    @Override
                    public void onSuccess(AssetNode result) {
                        logger.info("retrieved content for:" + result.getAssetId());
                        String[][] csv = result.getCsv();
                        int rowLen = csv.length;
                        int colLen = csv[0].length;

                        Map<String,Integer> colIdxMap = new HashMap<String, Integer>();

                        // Index the column list
                        for (int colNameIdx = 0; colNameIdx < colLen; colNameIdx++) {
                            for (int displayColIdx = FIXED_HEADER_LAST_IDX; displayColIdx < columnNames.length; displayColIdx++) {
                                if (!colIdxMap.containsKey(columnNames[displayColIdx]) && columnNames[displayColIdx].equals(csv[0][colNameIdx])) {
                                    colIdxMap.put(columnNames[displayColIdx],colNameIdx);
                                }
                            }

                        }

                        logger.info("indexed column size:" + colIdxMap.size());

//                String[][] aggregateList = new String[][];


                        String[] extractedRowValues = new String[columnNames.length];
                        String[][] extractedRowValuesTemp = new String[1][];

                        // Extract values
                        for (int rowIdx=1 /* skip header */; rowIdx < rowLen; rowIdx++) {

                            Anchor a = new Anchor(result.getDisplayName(),"javascript:void(0)");

                            extractedRowValues[0] = "$htmlConstant" +  a.getElement().getString();

                            for (int col=FIXED_HEADER_LAST_IDX; col < columnNames.length; col++) {
                                if (colIdxMap.containsKey(columnNames[col])) {
                                    int colIdx = colIdxMap.get(columnNames[col]);
                                    extractedRowValues[col]=csv[rowIdx][colIdx];
                                }
                            }

                            // Add row only if not empty value
                            for (int col=FIXED_HEADER_LAST_IDX; col < columnNames.length; col++) {
                                String val = extractedRowValues[col];
                                if (val!=null && !"".equals(val)) {
                                    rows.add(extractedRowValues);
                                    extractedRowValuesTemp[0] = extractedRowValues;
                                    csvFactory.updateList(0, extractedRowValuesTemp, dataProvider.getList());
                                    logger.fine("total rows:" + rows.size());
                                    dataProvider.flush();
                                    dataProvider.refresh();
                                    table.redraw();
                                    break;
                                }
                            }
                        }



                    }

                });
            }

            if (an.getChildren().size() == 1 && "HASCHILDREN".equals(an.getChildren().get(0).getDisplayName())) {
                reposService.getImmediateChildren(an, new AsyncCallback<List<AssetNode>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        logger.log(Level.SEVERE, caught.toString());
                        setError("aggregate getImmediateChildren error:" + caught.toString());
                    }

                    @Override
                    public void onSuccess(List<AssetNode> children)  {
                        for (AssetNode child : children) {
                            try {
                               aggregateMessage(child, assetType, rows, columnNames);
                            } catch (RepositoryConfigException rce) {
                                logger.warning(rce.toString());
                            }
                        }
                    }
                });
            }

        return;
    }



    public void setError(String msg) {
        getContentPanel().clear();
        getContentPanel().add(new HTML(msg));

    }

    public void setContent(Widget w) {
        contentPanel.clear();
        contentPanel.add(w);
    }

    public void setSize(String width, String height) {
        this.contentPanel.setSize(width, height);
    }

    public void setPixelSize(int width, int height) {
        this.contentPanel.setPixelSize(width, height);
    }




    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }



    public String getEventAssetId() {
        return eventAssetId;
    }

    public void setEventAssetId(String eventAssetId) {
        this.eventAssetId = eventAssetId;

    }

    public SimpleEventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(SimpleEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public String getExternalRepositoryId() {
        return externalRepositoryId;
    }

    public void setExternalRepositoryId(String externalRepositoryId) {
        this.externalRepositoryId = externalRepositoryId;
    }
    public ScrollPanel getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(ScrollPanel contentPanel) {
        this.contentPanel = contentPanel;
    }


    public String[] getDisplayColumns() {
        return displayColumns;
    }

    public void setDisplayColumns(String[] displayColumns) {
        this.displayColumns = new String[FIXED_HEADER_LAST_IDX+displayColumns.length] ;

        this.displayColumns[0] = "Assertion Name";
        for (int cx = 0; cx < displayColumns.length; cx++) {
            this.displayColumns[FIXED_HEADER_LAST_IDX+cx] = displayColumns[cx];
        }

    }


}
