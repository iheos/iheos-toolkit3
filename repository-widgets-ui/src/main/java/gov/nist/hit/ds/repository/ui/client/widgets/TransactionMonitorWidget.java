package gov.nist.hit.ds.repository.ui.client.widgets;


import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.EventBus;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryService;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryServiceAsync;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.ui.client.event.NewTxMessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TransactionMonitorWidget extends Composite {
    public static final int CELLTABLE_PAGE_SIZE = 100;
    /**
	 *
	 * @author Sunil.Bhaskarla
	 */
	private static Logger logger = Logger.getLogger(TransactionMonitorWidget.class.getName());

	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);


    private EventBus eventBus;

    private Boolean showTxDetail;

    private Boolean listenerEnabled;
    private Boolean listening;
    private Boolean filterEnabled;

    private SplitLayoutPanel southPanel = new SplitLayoutPanel(2);
    private SplitLayoutPanel txMonitorMainSplitPanel = new SplitLayoutPanel(3);

    //VerticalPanel contentPanel = new VerticalPanel();
    private ScrollPanel centerPanel = new ScrollPanel();
    DialogBox dialogBox = new DialogBox();

    //ListDataProvider<List<String>> dataProvider  = new ListDataProvider<List<String>>();
    private CellTable<TxDetailRow> txTable = new CellTable<TxDetailRow>(CELLTABLE_PAGE_SIZE,KEY_PROVIDER);
    private ListDataProvider<TxDetailRow> dataProvider  = new ListDataProvider<TxDetailRow>();

    public static final int MESSAGE_LEFT_MARGIN = 132;
    private SimplePager pager = new SimplePager();


    private Boolean autoShowFirstMessage = false;

    /*
    Sample 2-way Exchange pattern txDetail:
    20140326160812,"RESPONSE", "","500","localhost","localhost:8080","localhost:8001^ProxyRuleMappingName: localcap","text/html","","65","0"

     */
    private final String COLUMN_HEADER_PROXY = "Proxy";
    private final String COLUMN_HEADER_RESPONSE_TIME_MS = "Response Time";
    private final String COLUMN_HEADER_IND_OR_FLAG = " ";
    private final String COLUMN_HEADER_ROW_MESSAGE_FROM = "Message From";
    private final String COLUMN_HEADER_ROW_FORWARDED_TO = "Forwarded To";

    final String[] columns = {COLUMN_HEADER_IND_OR_FLAG,"Timestamp","Status","Artifact",COLUMN_HEADER_ROW_MESSAGE_FROM,COLUMN_HEADER_PROXY,COLUMN_HEADER_ROW_FORWARDED_TO,"Path","ContentType","Method","Length",COLUMN_HEADER_RESPONSE_TIME_MS};
    private MessageViewerWidget requestViewerWidget = new MessageViewerWidget(eventBus, "Request", null);
    private MessageViewerWidget responseViewerWidget = new MessageViewerWidget(eventBus, "Response", null);
    //Map<Integer, String> txRowParentId = new HashMap<Integer, String>();
    //Map<Integer, Map<String,AssetNode>> txRowAssetNode = new HashMap<Integer, Map<String,AssetNode>>();
    //int txRowIdx = 0;


    private String filterLocation;

    private final AsyncCallback<AssetNode> contentSetup = new AsyncCallback<AssetNode>() {
        public void onFailure(Throwable arg0) {
            logger.warning(arg0.toString());
        }

        public void onSuccess(AssetNode an) {
            logger.info("in content load" + an.getType()); 
            if (an.getTxtContent()!=null) {
                HTML txtContent = new HTML("<pre>" + an.getTxtContent() + "</pre>");
                if ("reqHdrType".equals(an.getType())) {
                    requestViewerWidget.setHeaderContent(txtContent);
                } else if ("reqBodyType".equals(an.getType())) {
                    requestViewerWidget.setMessageContent(txtContent);
                } else if ("resHdrType".equals(an.getType())) {
                    responseViewerWidget.setHeaderContent(txtContent);
                } else if ("resBodyType".equals(an.getType())) {
                    responseViewerWidget.setMessageContent(txtContent);
                }
            }

        }

    };
    private final AsyncCallback<Map<String,AssetNode>> updateHandler = new AsyncCallback<Map<String,AssetNode>> () {

        @Override
        public void onFailure(Throwable caught) {
            //Window.alert(caught.toString());
            logger.warning(caught.toString());
        }

        @Override
        public void onSuccess(Map<String,AssetNode> anMap) {
            popTx(anMap,null);
        }
    };

    public Boolean popTx(Map<String,AssetNode> anMap, Map<String,AssetNode> anMapRelated) {
        logger.info("entering popTx " + ((getListenerEnabled())?"Live":"Bypass)"));
        Boolean filteredMessage = false;
        if (anMap==null) {
            logger.finest("Null assetNode: timeout or bad tx message?");
            if (dialogBox.isShowing()) {
                dialogBox.hide();
            }
        } else {
            logger.info("Got an empty message? " + anMap.isEmpty());
            logger.info("sz:"+anMap.size());

            if (!anMap.isEmpty() && anMap.size()>0) {
                AssetNode an = anMap.get("header"); // A header has got to exist
                if (an!=null) {
                    if (an.getCsv() !=null) {
                        //String[] csvData = an.getCsv()[0];
                        // logger.info(csvData.toString());
                        if (an.getExtendedProps().containsKey("searchHit")) {
                            logger.info("this message has been filtered (back-end filter)!");
                            filteredMessage = true;
                            appendData(anMap);
                        } else {
                            appendData(anMap);
                            appendData(anMapRelated);

                            // old: Manage the event with the parent loc for immediate indexing


                            //if (!txRowParentId.containsKey(new Integer(txRowIdx))) {
                                //txRowParentId.put(new Integer(txRowIdx), an.getParentId());
                                //txRowAssetNode.put(new Integer(txRowIdx), anMap);
                            //}

                            if (getAutoShowFirstMessage()
                                    || (an.getParentId().equals(requestViewerWidget.getIoHeaderId()) && an.getParentId().equals(responseViewerWidget.getIoHeaderId()))) {
                                requestViewerWidget.setIoHeaderId(an.getParentId());
                                responseViewerWidget.setIoHeaderId(an.getParentId());
                                reposService.getAssetTxtContent(an, contentSetup);

                                if (anMap.get("body")!=null) { // Body may not exist. For example as in a GET request
                                    reposService.getAssetTxtContent(anMap.get("body"), contentSetup);
                                }
                            }

                        }

                    } else {
                        Window.alert("no csv!?"  + an.getParentId());
                    }

                }
            } else {
                Window.alert("empty map!?");
            }


            if (getListenerEnabled() && !getFilterEnabled()) {
                logger.info("firing event: NewTxMessageEvent");
                eventBus.fireEvent(new NewTxMessageEvent(dataProvider.getList().size()/*zero based idx*/,anMap));
            } else if ((getFilterEnabled() &&  filteredMessage)) {
                // raise BackendFilteredMessage
            }

        }

        if (getListenerEnabled()) {
            activateListener();
        }

        logger.info("leaving popTx " + ((getListenerEnabled())?"Live":"Bypass)"));
        return Boolean.TRUE;
    }
    /**
     *
     * @param eventBus
     * @param enableListener
     * @param showTxDetail
     */
    public TransactionMonitorWidget(EventBus eventBus, Boolean enableListener, Boolean enableFilter, Boolean showTxDetail)  {
        setEventBus(eventBus);
        setShowTxDetail(showTxDetail);
        setListenerEnabled(enableListener);
        setFilterEnabled(enableFilter);

        // All composites must call initWidget() in their constructors.
	     initWidget(setupMonitor());

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {

                resizeMessageDetailArea();
            }
        });

        if (getListenerEnabled() && (!getFilterEnabled() || (getFilterEnabled() && getFilterLocation()!=null))) {
            activateListener();
        }
    }

    private boolean activateListener() {

        /* live connection */
            // TODO: gracefully detach existing connection on exit -- only an issue when attached to a PTP queue
            try {
                reposService.getTxUpdates("", getFilterLocation(), updateHandler);
                setListening(true);
            } catch (Throwable t) {
                logger.warning(t.toString());
                setListening(false);
            }
        return getListening();
    }

    private void resizeMessageDetailArea() {
        try {
            long containerWidth =  txMonitorMainSplitPanel.getParent().getElement().getClientWidth(); // Window.getClientWidth())
            long containerHeight = txMonitorMainSplitPanel.getParent().getElement().getClientHeight(); // Window.getClientHeight()

            long messageDetailViewerHeight = getMessageDetailHeight(containerHeight);
            txMonitorMainSplitPanel.setWidgetSize(southPanel, messageDetailViewerHeight);
            southPanel.setWidgetSize(requestViewerWidget, Math.round(.5 * containerWidth));


        } catch (Exception ex) {
            logger.warning("Window resize failed:" + ex.toString());
        }
    }

    private long getMessageDetailHeight(long containerHeight) {
        long messageDetailViewerHeight = Math.round(.4 * containerHeight);
        if (!getShowTxDetail()) {
            messageDetailViewerHeight = 0;
        }
        return messageDetailViewerHeight;
    }


    private Boolean appendData(Map<String,AssetNode> anMap) { // int keyIdx, String[] csvRow
        if (anMap==null) {
            logger.fine("empty anMap");
            return false;
        }
        logger.info("entering appendData" + anMap.get("header").getRelativePath());
        logger.info("contains filtered message? " + (anMap.get("header").getExtendedProps().containsKey("searchHit")));
        try {
//            int colLen = columns.length;
//            String []csvRowNew = new String[colLen];
//
//            for (int cx=0; c<colLen; cx++) { // truncate excess data not applicable for table display, NOTE: extra data is not anticipated at this time.
//                csvRowNew[cx] = csvRow[cx];
//            }


            // List<List<String>> rowList = dataProvider.getList();

//            List<String> row = Arrays.asList(csvRowNew);
//            rowList.add(row);
// ----

//            TxDetailRow txRow = new TxDetailRow();
//            txRow.setCsvData(Arrays.asList(csvRow));
//            txRow.setKey(keyIdx);

            TxDetailRow txRow = new TxDetailRow();
            txRow.setAnMap(anMap);


           List<TxDetailRow> rowList = dataProvider.getList();
            txRow.setKey(rowList.size()-1);

            int matchingPairIdx = findMatchingPairIdx(anMap.get("header"));

            // if ("resHdrType".equals(anMap.get("header").getExtendedProps().get("type"))) {
            if (matchingPairIdx>-1) {
                if (matchingPairIdx+1 < rowList.size()) {
                     matchingPairIdx++; // Bundle message pair
                } else {
                    matchingPairIdx = -1; // Just append
                }
            }


           boolean state = false;
                if (matchingPairIdx>-1) {
                    try {
                        rowList.add(matchingPairIdx,txRow);
                        state = true;
                    } catch (Throwable t) {
                        Window.alert(t.toString());
                        state = false;
                    }
                } else {
                    state = rowList.add(txRow);
                }

            return  state;


        } catch (Exception ex) {
            Window.alert(ex.toString());
            logger.warning(ex.toString());
        } finally {
            if (dataProvider!=null && dataProvider.getList()!=null) {
                dataProvider.flush();
            }
        }
        // logger.warning("Append failed!!" + an.getParentId());
        return false;
    }



    private Widget setupMonitor() {
        requestViewerWidget.getElement().getStyle()
                .setProperty("border", "none");
        responseViewerWidget.getElement().getStyle()
                .setProperty("border", "none");

        long containerWidth = Window.getClientWidth(); // This must be in sync with the widget original load size, make constant
        long containerHeight = Integer.parseInt("" + Math.round(.5 * Window.getClientHeight()));

        //=  txMonitorMainSplitPanel.getParent().getElement().getClientWidth(); // Window.getClientWidth())
        // = txMonitorMainSplitPanel.getParent().getElement().getClientHeight(); // Window.getClientHeight()

        southPanel.addWest(requestViewerWidget,Integer.parseInt(""+Math.round(.5 * containerWidth)));
        southPanel.add(responseViewerWidget);


        long messageDetailViewerHeight = getMessageDetailHeight(containerHeight);
        if (!getShowTxDetail()) {
            messageDetailViewerHeight = 0;
        }
        txMonitorMainSplitPanel.addSouth(southPanel, messageDetailViewerHeight); // 500 Math.round(.4 * containerHeight)

        txMonitorMainSplitPanel.add(setupTable(centerPanel));

        return txMonitorMainSplitPanel;

    }


    public void clear() {

        requestViewerWidget.setIoHeaderId(null);
        requestViewerWidget.setHeaderContent(new HTML(""));
        requestViewerWidget.setMessageContent(new HTML(""));

        responseViewerWidget.setIoHeaderId(null);
        responseViewerWidget.setHeaderContent(new HTML(""));
        responseViewerWidget.setMessageContent(new HTML(""));

//        getTxRowParentId().clear();
//        getTxRowAssetNode().clear();

        List<TxDetailRow> rowList = dataProvider.getList();
        rowList.clear();

        //txRowIdx = 0; // Reset the index counter
    }

    private class TxDetailRow  {

        private int key;
        private Map<String,AssetNode> anMap = null;

        public List<String> getCsvData() {

            AssetNode an = getAnMap().get("header"); // A header has got to exist
            if (an!=null) {
                if (an.getCsv() !=null) {
                    String[] csvData = an.getCsv()[0];
                    return Arrays.asList(csvData);

                }
            }
            return null;
        }


        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }
        public Map<String, AssetNode> getAnMap() {
            return anMap;
        }

        public void setAnMap(Map<String, AssetNode> anMap) {
            this.anMap = anMap;
        }


    }
    private static final ProvidesKey<TxDetailRow> KEY_PROVIDER = new ProvidesKey<TxDetailRow>() {
        @Override
        public Object getKey(TxDetailRow item) {
            return item == null ? null : item.getKey();
        }
    };


    private ScrollPanel setupTable(ScrollPanel sp) {
        //StyleInjector.inject(".txColHidden {background-color:black;visibility:hidden;display:none}");
        //CellTable<List<String>> txTable = new CellTable<List<String>>();

        txTable.setWidth("100%", true);
        txTable.setHeight("100%");

        //List<List<String>> rows = new ArrayList<List<String>>();
        List<TxDetailRow> rows = new ArrayList<TxDetailRow>();

        int colLen = columns.length;

        // Create table columns
        for (int c = 0; c < colLen; c++) {
            txTable.addColumn(new IndexedColumn(c) ,new TextHeader(columns[c]));
            if (c==0) {
                txTable.setColumnWidth(txTable.getColumn(0), "50px");
            }
        }


        //txTable.addColumnStyleName(3,"txColHidden");
        //txTable.removeColumn(columns.length-1);
        //txTable.setColumnWidth(1 ,-1, Style.Unit.PX);

        //txTable.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        dataProvider.setList(rows);

        dataProvider.addDataDisplay(txTable);

        // Add a selection model to handle user selection.
        final SingleSelectionModel<TxDetailRow> selectionModel = new SingleSelectionModel<TxDetailRow>();
        txTable.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {

                TxDetailRow selected = selectionModel.getSelectedObject();
                if (selected != null ) {  // && getShowTxDetail()
                    //setMessageViewer(selected.getKey());
                    setMessageViewer(selected);
                }
            }
        });


        sp.add(txTable);


        getPager().getElement().getStyle().setMarginTop(0, Style.Unit.PX);
        //getPager().setHeight("50%");
        getPager().getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
        getPager().setDisplay(txTable);
        return sp;

    }

    public int findMatchingPairIdx(AssetNode an) {
        List<TxDetailRow> rowList = dataProvider.getList();

        if (an!=null) {
            String parentId = an.getParentId();
                for (TxDetailRow row : rowList) {
                    if (!an.equals(row.anMap.get("header")) && parentId.equals(row.getAnMap().get("header").getParentId())) {
                        return rowList.indexOf(row);
                    }
                }
        }

        return -1;
    }

    public Map<String,AssetNode> findMatchingPair(AssetNode an) {
        List<TxDetailRow> rowList = dataProvider.getList();

        if (an!=null) {
            String parentId = an.getParentId();
            for (TxDetailRow row : rowList) {
                if (!an.equals(row.anMap.get("header")) && parentId.equals(row.getAnMap().get("header").getParentId())) {
                    return  row.getAnMap();  // rowList.indexOf(row).;
                }
            }
        }

        return null;
    }


    private int findIdx(AssetNode an) {
        List<TxDetailRow> rowList = dataProvider.getList();

        if (an!=null) {
            String parentId = an.getParentId();
            for (TxDetailRow row : rowList) {
                if (an.equals(row.anMap.get("header")) && parentId.equals(row.getAnMap().get("header").getParentId())) {
                    return rowList.indexOf(row);
                }
            }
        }

        return -1;
    }

    public Map<String,AssetNode> getRowMap(int idx) {
        List<TxDetailRow> rowList = dataProvider.getList();

        if (idx>-1) {
            return rowList.get(idx).getAnMap();
        }
        return null;
    }

    /*
    private int findMatchingPairIdx(AssetNode an, int selectedIndex) {
        int matchingPairIdx = -1;
        for (int cx=0; cx<txRowIdx; cx++) {
            if ((an!=null && an.getParentId()!=null) && txRowParentId!=null) {
                if (cx!=selectedIndex && an.getParentId().equals(txRowParentId.get(cx))) {
                    matchingPairIdx = cx;
                    break;
                }
            }
        }
        return matchingPairIdx;
    }
    */

    public void setMessageViewer(TxDetailRow row) {

                if (dataProvider.getList().size()>0) {
                    //Map<String,AssetNode> anMap = txRowAssetNode.get(new Integer(selectedIndex));
                    Map<String,AssetNode> anMap = row.getAnMap();
                    int anLen = anMap.size();
                    if (anLen>0) {
                        AssetNode an = anMap.get("header");

                        //Window.alert("setup for" + selectedIndex + an.getParentId() + an.getType());


                        requestViewerWidget.setIoHeaderId(an.getParentId());
                        responseViewerWidget.setIoHeaderId(an.getParentId());

                        reposService.getAssetTxtContent(an, contentSetup);

                        if (anMap.get("body")!=null) { // Body exists
                            reposService.getAssetTxtContent(anMap.get("body"), contentSetup);
                        }

//                        List<TxDetailRow> rowList = dataProvider.getList();
//                        TxDetailRow row = rowList.get(selectedIndex);
//                        row.getCsvData().set(0,"X");

                        if (dataProvider.getList().size()==1)
                            return;

                        // Find matching pair if it exists
                        //int matchingPairIdx = findMatchingPairIdx(an,selectedIndex);
                        int matchingPairIdx = findMatchingPairIdx(an);

                        Map<String,AssetNode> matchingPair = null;

                        if (matchingPairIdx>-1 && dataProvider.getList().size()>0) {
                            matchingPair = dataProvider.getList().get(matchingPairIdx).getAnMap();
                        }

                        if (matchingPair!=null) {
                            if (matchingPair.get("header")!=null) {
                                reposService.getAssetTxtContent(matchingPair.get("header"), contentSetup);
                            }

                            if (matchingPair.get("body")!=null) { // Body exists
                                reposService.getAssetTxtContent(matchingPair.get("body"), contentSetup);
                            }
                        } else {
                            // No matching pair
                            if (an!=null && an.getType()!=null) {
                                if (an.getType().indexOf("REQUEST")>-1) {
                                    responseViewerWidget.setIoHeaderId(an.getParentId());
                                    responseViewerWidget.setHeaderContent(new HTML(""));
                                    responseViewerWidget.setMessageContent(new HTML(""));
                                } else {
                                    requestViewerWidget.setIoHeaderId(an.getParentId());
                                    requestViewerWidget.setHeaderContent(new HTML(""));
                                    requestViewerWidget.setMessageContent(new HTML(""));
                                }
                            }
                        }
                    }

                }

    }

    class IndexedColumn extends Column<TxDetailRow, SafeHtml> {
        private final int index;
        public IndexedColumn(int index) {
            super(new SafeHtmlCell());
            this.index = index;
        }

        @Override
        public SafeHtml getValue(TxDetailRow o) {

            try {
                SafeHtmlBuilder shb = new SafeHtmlBuilder();
                AssetNode headerMsg =  o.getAnMap().get("header"); //  txRowAssetNode.get(o.getKey()).get("header"); // headerMsg

                if (COLUMN_HEADER_PROXY.equals(columns[this.index])) {

                    shb.appendHtmlConstant("<span title='" + headerMsg.getExtendedProps().get("proxyDetail")  + "'>"); // .getProps()
                    shb.appendEscaped(o.getCsvData().get(this.index));
                    shb.appendHtmlConstant("</span>");

                } else if (COLUMN_HEADER_ROW_MESSAGE_FROM.equals(columns[this.index])) {

                    shb.appendHtmlConstant("<span title='" + headerMsg.getExtendedProps().get("fromIp")  + "'>"); // .getProps()
                    shb.appendEscaped(o.getCsvData().get(this.index));
                    shb.appendHtmlConstant("</span>");

                } else if (COLUMN_HEADER_ROW_FORWARDED_TO.equals(columns[this.index])) {

                    shb.appendHtmlConstant("<span title='" + headerMsg.getExtendedProps().get("toIp")  + "'>"); // .getProps()
                    shb.appendEscaped(o.getCsvData().get(this.index));
                    shb.appendHtmlConstant("</span>");

                } else if (COLUMN_HEADER_RESPONSE_TIME_MS.equals(columns[this.index])) {
                    if ("0".equals(o.getCsvData().get(this.index))) { // Reformat 0 ms to less than 1 ms
                        shb.appendEscaped("<1");
                    } else  {
                        shb.appendEscaped(""+o.getCsvData().get(this.index));
                    }

                    if (!"".equals(o.getCsvData().get(this.index))) {
                        shb.appendHtmlConstant("<span style=\"font-size:9px\">&nbsp;ms</span>");
                    }
                } else if (COLUMN_HEADER_IND_OR_FLAG.equals(columns[this.index])) {
                    if (!getListenerEnabled() && "yes".equals(headerMsg.getExtendedProps().get("frontendSearchHit"))) {
                        shb.appendHtmlConstant("<span style=\"width:32px;height:32px;\"><img height=8 width=8 src='" + GWT.getModuleBaseForStaticFiles() + "images/bullet_ball_glass_green.png'/></span>");
                    } else
                        shb.appendHtmlConstant("<span style=\"width:32px;height:32px;\">&nbsp;</span>");
                } else {
                    shb.appendEscaped(o.getCsvData().get(this.index));
                }

                return shb.toSafeHtml();

            } catch (Exception ex) {
                return new SafeHtmlBuilder().toSafeHtml();
            }
        }

        /*
         // For use with String type, for example: Column<TxDetailRow, String>:
        //  super(new TextCell());

        @Override
        public String getBooleanValue(TxDetailRow object) {

            try {
                return object.getCsvData().get(this.index);

             // Straight String[]: return object.get(this.index);
            } catch (Exception ex) {
                return "";
            }
        }
        */
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Boolean getShowTxDetail() {
        return showTxDetail;
    }

    public void setShowTxDetail(Boolean showTxDetail) {
        this.showTxDetail = showTxDetail;
        resizeMessageDetailArea();
    }
    public Boolean getListenerEnabled() {
        return listenerEnabled;
    }

    public void setListenerEnabled(Boolean listenerEnabled) {
        this.listenerEnabled = listenerEnabled;
    }


    public Boolean getFilterEnabled() {
        return filterEnabled;
    }

    public void setFilterEnabled(Boolean filterEnabled) {
        this.filterEnabled = filterEnabled;
    }


    public SimplePager getPager() {
        return pager;
    }


    public Boolean getAutoShowFirstMessage() {
        return false;
        // TODO: enable this after the row selection highlight method is implemented
        // return  (autoShowFirstMessage && (requestViewerWidget.getIoHeaderId() == null || responseViewerWidget.getIoHeaderId() == null));
    }

    public Boolean getListening() {
        return listening;
    }

    public void setListening(Boolean listening) {
        this.listening = listening;
    }

    public void setAutoShowFirstMessage(Boolean autoShowFirstMessage) {
        this.autoShowFirstMessage = autoShowFirstMessage;
    }

    public CellTable<TxDetailRow> getTxTable() {
        return txTable;
    }

    public void setTxTable(CellTable<TxDetailRow> txTable) {
        this.txTable = txTable;
    }

    public String getFilterLocation() {
        return filterLocation;
    }

    public void setFilterLocation(String filterLocation) {
    /*
    initial status
    1) no filter
        - setting filter location via apply method should activate the filter
    2) filter change
        - apply method should automatically apply in the next listener (***after time out***) from the updateHandler
    */

        this.filterLocation = filterLocation;

        if (!getListening()) {
            setFilterEnabled(true);
            activateListener();
        } else {
            HorizontalPanel hpDialog = new HorizontalPanel();

            HTML txt = new HTML("<span style='color:red;font-size:large;font-style:bold'>Please wait...</span>");
            hpDialog.add(txt);

            dialogBox.clear();
            dialogBox.setWidget(hpDialog);

            dialogBox.setPopupPosition(Integer.parseInt(""+Math.round(Window.getClientWidth() * .5)), Integer.parseInt(""+Math.round(Window.getClientHeight() * .5)));
            dialogBox.show();
        }
    }
}
