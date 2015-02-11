package gov.nist.hit.ds.repository.ui.client.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.DefaultCellTableBuilder;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.EventBus;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryService;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryServiceAsync;
import gov.nist.hit.ds.repository.rpc.search.client.exception.RepositoryConfigException;
import gov.nist.hit.ds.repository.shared.PropertyKey;
import gov.nist.hit.ds.repository.shared.SearchCriteria;
import gov.nist.hit.ds.repository.shared.SearchTerm;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.ui.client.CsvTableFactory;
import gov.nist.hit.ds.repository.ui.client.event.asset.InContextAssetClickedEvent;
import gov.nist.hit.ds.repository.ui.client.event.asset.OutOfContextAssetClickedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This widget builds an aggregate view of the messages, which are embedded in CSV formatted content files, logged by toolkit components.
 * The following asset structure is required:
 *
 *  (Events)
 *      - 'Date' - Event Id type=event (required)
 *          - 'Validations' type=validators (required)
 *             - Assertions type=assertions (required) -- This is where the widget scans for the CSV files.
 */
public class EventMessageAggregatorWidget extends Composite {

    public static final int FIXED_HEADER_LAST_IDX = 1;
    public static final int START_ROW_ZERO = 0;
    private static Logger logger = Logger.getLogger(EventMessageAggregatorWidget.class.getName());

    //    private ScrollPanel contentPanel = new ScrollPanel();
    private SplitLayoutPanel contentPanel = new SplitLayoutPanel();
    private EventBus eventBus;
    private ASSET_CLICK_EVENT assetClickEvent;
    private String eventAssetId;
    private String assetType;
    private String externalRepositoryId;
    private String[] displayColumns;
    final private RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);

    public static enum ASSET_CLICK_EVENT {
        IN_CONTEXT,
        OUT_OF_CONTEXT
    };


    //    private CellTable<List<SafeHtml>> table = new CellTable<List<SafeHtml>>(300); // new CsvTableFactory().createCellTable(rows.toArray(new String[rows.size()][]));

//    private DataGrid<List<SafeHtml>> table = new DataGrid<List<SafeHtml>>(300); // TODO: add pager
//    private ListDataProvider<List<SafeHtml>> dataProvider  = new ListDataProvider<List<SafeHtml>>();
//    private DataGrid<List<SafeHtml>> table = new DataGrid<List<SafeHtml>>(5);
    private CsvTableFactory csvFactory = new CsvTableFactory();
//    private List<List<SafeHtml>> dataRows = new ArrayList<List<SafeHtml>>();
    private List<String[]> rows = new ArrayList<String[]>();


    private DataGrid<List<EventMessageCell>> table = new DataGrid<List<EventMessageCell>>(500); // TODO: add pager
    private ListDataProvider<List<EventMessageCell>> dataProvider  = new ListDataProvider<List<EventMessageCell>>();
    private List<List<EventMessageCell>> dataRows = new ArrayList<List<EventMessageCell>>();

    private class EventMessageCell {
        private AssetNode an;
        private SafeHtml cellValue;
        private int rowNumber;

        private EventMessageCell() {
            super();
        }

        private EventMessageCell(AssetNode an, SafeHtml cellValue, int rowNumber) {
            this.an = an;
            this.cellValue = cellValue;
            this.rowNumber = rowNumber;
        }

        private EventMessageCell(SafeHtml cellValue) {
            this.cellValue = cellValue;
        }

        public AssetNode getAn() {
            return an;
        }

        public void setAn(AssetNode an) {
            this.an = an;
        }

        public SafeHtml getCellValue() {
            return cellValue;
        }

        public void setCellValue(SafeHtml cellValue) {
            this.cellValue = cellValue;
        }

        public int getRowNumber() {
            return rowNumber;
        }

        public void setRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
        }

    }

    /**
     * Builds a tabular data display based on an aggregate view of the event messages.
     * @param eventBus The event bus to receive asset click event (to display in the log browser for instance).
     * @param assetClickEvent The type of event to fire.
     * @param externalRepositoryId The Id of the external repository containing the asset Id.
     * @param eventAssetId The Id of the upper-level Events asset.
     * @param assetType The type of assets to aggregate under the upper-level Events asset.
     * @param csvColumnNames The column row values to aggregate and display in the table. Column names are case-sensitive.
     */
   @Deprecated
   public EventMessageAggregatorWidget(EventBus eventBus, ASSET_CLICK_EVENT assetClickEvent, String externalRepositoryId, String eventAssetId, String assetType, String[] csvColumnNames) throws RepositoryConfigException {
       setEventBus(eventBus);
       setAssetClickEvent(assetClickEvent);
       setExternalRepositoryId(externalRepositoryId);
       setEventAssetId(eventAssetId);
       setAssetType(assetType);
       setDisplayColumns(csvColumnNames);

       Window.alert("This widget is obsolete, please use EventAggregatorWidget.");
       reposService.isRepositoryConfigured(new AsyncCallback<Boolean>() {
           public void onFailure(Throwable arg0) {
               Window.alert("EventMessageAggregatorWidget: The repository system configuration is not available: " + arg0.toString());
           }

           public void onSuccess(Boolean rs) {
               setupLayout();
           }
       });

       // All composites must call initWidget() in their constructors.
       initWidget(getContentPanel());


   }

    protected Widget setupLayout() {
        logger.log(Level.FINE, "In setupLayout of the event aggregator widget...");
//        setSize("52%", "20%"); // Default size

//        CellTable<List<SafeHtml>> table = new CsvTable().createCellTable(an.getCsv());
//        contentPanel.add(table);

        AsyncCallback<List<AssetNode>> searchResults = new AsyncCallback<List<AssetNode>> () {

            public void onFailure(Throwable arg0) {
                setError("Event assetId "+ getEventAssetId() +" could not be found in "+ getExternalRepositoryId() +": " + arg0.getMessage());
            }
            public void onSuccess(List<AssetNode> result) {
                logger.fine("agg main event findSimple success");
                if (result.size()==1) { // Only one is expected
                    AssetNode an = result.get(0);
                    logger.fine("agg main event size match success");
                    try {
                        // 1.
                        reposService.getImmediateChildren(an,0, new AsyncCallback<List<AssetNode>>() {
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
                                logger.fine("Processing nodes... size:" + children.size());

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
                    setError("Event assetId did not match the expected size=1, got <"+ result.size() +">: Id:" + getEventAssetId());
                }

            }
        };

        //                                Window.alert("row sz: " + rows.size() + " rows[0][1]" + rows.get(0)[1]);

        logger.fine("Aggregated rows: " +table.getRowCount());
        //                                List<List<SafeHtml>> rowList = dataProvider.getList();


        table.setTableBuilder(new DefaultCellTableBuilder<List<EventMessageCell>>(table));

        //txTable.setWidth("100%", true);
//        table.setWidth("50%");
//        table.setHeight("50%");
//        table.setSkipRowHoverCheck(true);
//        table.setSkipRowHoverFloatElementCheck(true);
//        table.setSkipRowHoverStyleUpdate(true);
//        txTable.setStyleName("txDataGridNoTableSpacing");

        table.setAutoHeaderRefreshDisabled(false);
        table.setFocus(false);


        // Create table columns
        for (int c = 0; c < getDisplayColumns().length; c++) {
            table.addColumn(new IndexedColumn(c),
                    new TextHeader(getDisplayColumns()[c]));
        }

        rows.add(getDisplayColumns()); // Header row

        logger.fine("updating rows:" + dataRows.size());
        dataProvider.setList(dataRows);
        dataProvider.addDataDisplay(table);

//        getContentPanel().setSize("52%","20%");
        getContentPanel().add(table);


//        new CsvTableFactory().updateList(1, rows.toArray(new String[rows.size()][]),dataProvider.getList());



        SearchCriteria searchCriteria = new SearchCriteria(SearchCriteria.Criteria.AND);
        searchCriteria.append(new SearchTerm(PropertyKey.ASSET_ID, SearchTerm.Operator.EQUALTO, getEventAssetId()));

        String[][] repository = new String[][]{{getExternalRepositoryId(), "RW_EXTERNAL"}};
        reposService.search(repository, searchCriteria, searchResults);

        return getContentPanel();
    }

    private class IndexedColumn extends Column<List<EventMessageCell>, SafeHtml> { // Column<List<SafeHtml>, SafeHtml>
        private final int index;
        public IndexedColumn(int index) {
            // For use with String:
            // super(new TextCell());

            super(new SafeHtmlCell() {
                @Override
                public Set<String> getConsumedEvents() {
//                    return super.getConsumedEvents();
                    HashSet<String> events = new HashSet<String>();
                    events.add(ClickEvent.getType().getName());
                    return events;
                }
            });
            this.index = index;
        }
        @Override
        public SafeHtml getValue(List<EventMessageCell> object) {
            return object.get(this.index).getCellValue();
        }

        /**
         * Handle a browser event that took place within the column.
         *
         * @param context the cellValue context
         * @param elem    the parent Element
         * @param object  the base object to be updated
         * @param event   the native browser event
         */
        @Override
        public void onBrowserEvent(Cell.Context context, Element elem, List<EventMessageCell> object, NativeEvent event) {
            super.onBrowserEvent(context, elem, object, event);
//            Window.alert(ClickEvent.getType().getName() + " " + this.index);
            if (this.index==0 && ClickEvent.getType().getName().equals(event.getType()) ) {
                AssetNode an = object.get(this.index).getAn();
                int rowNumber = object.get(this.index).getRowNumber();

//                Window.alert(an.getLocation() + ">>" + elem.getTagName()  + " asset: " + an.getAssetId() +  " row num:" + rowNumber);
                if (an!=null) {
                    switch (getAssetClickEvent()) {
                        case IN_CONTEXT: eventBus.fireEvent(new InContextAssetClickedEvent(an,rowNumber));
                            break;
                        case OUT_OF_CONTEXT: eventBus.fireEvent(new OutOfContextAssetClickedEvent(an,rowNumber));
                            break;
                    }
                }


            }
        }


    }

    private void aggregateMessage(final AssetNode an, final String assetType, final List<String[]> rows, final String[] columnNames) throws RepositoryConfigException {

            logger.fine("entering aggregateMessage: an displayName:" + an.getDisplayName() + " an type:" + an.getType() + " search type:" + assetType + " mimeType:" + an.getMimeType() + " hasContent:" + an.isContentAvailable() +  " csv:" + (an.getCsv()!=null));
            if (assetType.equals(an.getType()) && "text/csv".equals(an.getMimeType()) && an.isContentAvailable() && an.getCsv()==null) {
                reposService.getAssetTxtContent(an, new AsyncCallback<AssetNode>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        logger.warning("getAssetTxtContent failed: " + caught.toString());
                    }

                    @Override
                    public void onSuccess(final AssetNode resultNode) {
                        logger.fine("retrieved content for:" + resultNode.getAssetId());
                        String[][] csv = resultNode.getCsv();
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

                        logger.fine("indexed column size:" + colIdxMap.size());

//                String[][] aggregateList = new String[][];


                        String[] extractedRowValues = new String[columnNames.length];
                        String[][] extractedRowValuesTemp = new String[1][];

                        String previousSection = "";
                        // Extract values
                        for (int rowIdx=1 /* skip header */; rowIdx < rowLen; rowIdx++) {

                            if (!previousSection.equals(resultNode.getDisplayName())) {
                                Anchor anchor = new Anchor(); // result.getDisplayName(),"javascript:void(0)"
                                String colorStr = "";

                                if (resultNode.getColor()!=null && !"".equals(resultNode.getColor())) {
                                    colorStr = "style=\"color:" + resultNode.getColor() + "\"";
                                }

                                SafeHtmlBuilder nodeSafeHtml =  new SafeHtmlBuilder();
                                nodeSafeHtml.appendHtmlConstant("<span "+ colorStr + " >"
                                        + resultNode.getDisplayName() + "</span>");
                                anchor.setHref("javascript:void(0)");
                                anchor.setHTML(nodeSafeHtml.toSafeHtml());


                                /* This option is not suitable for the csv translation table method.
                                anchor.addClickHandler(new ClickHandler() {

                                    public void onClick(ClickEvent event) {
                                        Window.alert("clicked!");
                                        eventBus.fireEvent(new AssetClickedEvent(resultNode)); // Need to use AssetNode
                                    }
                                });
                                */
//                                anchor.setText(resultNode.getDisplayName() + " -- " + anchor.getHref());

                                extractedRowValues[0] = "$htmlConstant" +  anchor.getElement().getString();
                            } else {
                                extractedRowValues[0] = "";
                            }
                            previousSection = resultNode.getDisplayName();

                            for (int col=FIXED_HEADER_LAST_IDX; col < columnNames.length; col++) {
                                if (colIdxMap.containsKey(columnNames[col])) {
                                    int colIdx = colIdxMap.get(columnNames[col]);
                                    extractedRowValues[col]=csv[rowIdx][colIdx];
                                }
                            }

                            /* Add row only if not empty value -- this may cause the row order to break
                            for (int col=FIXED_HEADER_LAST_IDX; col < columnNames.length; col++) {
                                String val = extractedRowValues[col];
                                if (val!=null && !"".equals(val)) {
                                ...
                                break;
                                }
                            }
                            */

                            rows.add(extractedRowValues);
                            extractedRowValuesTemp[0] = extractedRowValues;
                            // Set row marker which is to be used for highlighting the clicked row
//                            logger.info("rowIndex:" + rowIdx );
                            updateList(START_ROW_ZERO, rowIdx, extractedRowValuesTemp, dataProvider.getList(), an);
                            logger.fine("total rows:" + rows.size());


                        }



                    }

                });
            }

            if (an.getChildren().size() == 1 && "HASCHILDREN".equals(an.getChildren().get(0).getDisplayName())) {
                reposService.getImmediateChildren(an,0, new AsyncCallback<List<AssetNode>>() {
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

    /**
     *
     * @param startRow
     * @param csv
     * @param rows The aggregate list.
     * @param an
     */
    private void updateList(int startRow, int rowNumber, String [][]csv, List<List<EventMessageCell>> rows, AssetNode an) {

        int rowLen = csv.length;
        int colLen = csv[0].length;

        for (int r = startRow; r < rowLen; r++) {
//			        List<String> textRow = Arrays.asList(csv[r]);
            if (csv[r]!=null) {
                int textRowSz =  csv[r].length;  //textRow.size();
                if (textRowSz>0) {
                    List<EventMessageCell> htmlRow = new ArrayList<EventMessageCell>(textRowSz);
                    for (int cx=0; cx<textRowSz; cx++) {
//                        SafeHtmlBuilder shb = new SafeHtmlBuilder();
                        String val =  csv[r][cx];    //textRow.get(cx);
//                                logger.info("val LB: " + val);
                        /* This will only attach the asset node object where the text is non-empty
                        if (!"".equals(csv[r][0])) {
                            htmlRow.add(new EventMessageCell(an,csvFactory.makeSafeHtml(val).toSafeHtml()));
                        } else {
                            htmlRow.add(new EventMessageCell(csvFactory.makeSafeHtml(val).toSafeHtml()));
                        }
                        */
                        rowNumber = (startRow==START_ROW_ZERO)?rowNumber-1:rowNumber;  // r is always zero?
//                        logger.info("EventMessageCell rowNumber: " + rowNumber);
                        htmlRow.add(new EventMessageCell(an,csvFactory.makeSafeHtml(val, false),rowNumber));

                    }
                    rows.add(htmlRow);
                }
            }

        }
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

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public String getExternalRepositoryId() {
        return externalRepositoryId;
    }

    public void setExternalRepositoryId(String externalRepositoryId) {
        this.externalRepositoryId = externalRepositoryId;
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

    public SplitLayoutPanel getContentPanel() {
        return contentPanel;
    }

    public void setContentPanel(SplitLayoutPanel contentPanel) {
        this.contentPanel = contentPanel;
    }
    public DataGrid<List<EventMessageCell>> getTable() {
        return table;
    }


    public ASSET_CLICK_EVENT getAssetClickEvent() {
        return assetClickEvent;
    }

    public void setAssetClickEvent(ASSET_CLICK_EVENT assetClickEvent) {
        this.assetClickEvent = assetClickEvent;
    }

}
