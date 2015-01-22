package gov.nist.hit.ds.repository.ui.client.widgets;


import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextButtonCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.EventBus;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryService;
import gov.nist.hit.ds.repository.rpc.search.client.RepositoryServiceAsync;
import gov.nist.hit.ds.repository.rpc.search.client.exception.RepositoryConfigException;
import gov.nist.hit.ds.repository.shared.ValidationLevel;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.ui.client.event.ListenerStatusEvent;
import gov.nist.hit.ds.repository.ui.client.event.NewTxMessageEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class TransactionMonitorAdvancedWidget extends Composite {
    public static final int CELLTABLE_PAGE_SIZE = 512;
    /**
	 *
	 * @author Sunil.Bhaskarla
	 */
	private static Logger logger = Logger.getLogger(TransactionMonitorAdvancedWidget.class.getName());

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
//    private CellTable<TxDetailRow> txTable = new CellTable<TxDetailRow>(CELLTABLE_PAGE_SIZE,KEY_PROVIDER);
//    private ListDataProvider<TxDetailRow> dataProvider  = new ListDataProvider<TxDetailRow>();

    private DataGrid<TxMessageBundle> txTable = new DataGrid<TxMessageBundle>(CELLTABLE_PAGE_SIZE,TX_MESSAGE_BUNDLE_PROVIDES_KEY);
    private ListDataProvider<TxMessageBundle> dataProvider  = new ListDataProvider<TxMessageBundle>();


    Set<TxMessageBundle> itemsToExpandToggle = new TreeSet<TxMessageBundle>();

    public static final int MESSAGE_LEFT_MARGIN = 132;
    private SimplePager pager = new SimplePager();


    private Boolean autoShowFirstMessage = false;

    private ValidationLevel validationLevel = ValidationLevel.ERROR; // default

    /*
    Sample 2-way Exchange pattern txDetail:
    20140326160812,"RESPONSE", "","500","localhost","localhost:8080","localhost:8001^ProxyRuleMappingName: localcap","text/html","","65","0"

     */
    private final String COLUMN_HEADER_PROXY = "Proxy";
    private final String COLUMN_HEADER_RESPONSE_TIME_MS = "Response Time";
    private final String COLUMN_HEADER_SEARCH_HIT_IND = " ";
    private final String COLUMN_HEADER_ROW_MESSAGE_FROM = "Message From";
    private final String COLUMN_HEADER_ROW_FORWARDED_TO = "Forwarded To";
    private final String COLUMN_HEADER_PATH = "Path";
    private final String COLUMN_HEADER_VALIDATION = "Validation";
    private final String COLUMN_HEADER_CONTENT_TYPE = "Content Type";

    final String[] columns = {COLUMN_HEADER_SEARCH_HIT_IND,"Timestamp","Status","Artifact",COLUMN_HEADER_ROW_MESSAGE_FROM,COLUMN_HEADER_PROXY,COLUMN_HEADER_ROW_FORWARDED_TO,COLUMN_HEADER_PATH,COLUMN_HEADER_CONTENT_TYPE,"Method","Length",COLUMN_HEADER_RESPONSE_TIME_MS,COLUMN_HEADER_VALIDATION};
    private MessageViewerWidget requestViewerWidget;
    private MessageViewerWidget responseViewerWidget;
    //Map<Integer, String> txRowParentId = new HashMap<Integer, String>();
    //Map<Integer, Map<String,AssetNode>> txRowAssetNode = new HashMap<Integer, Map<String,AssetNode>>();
    //int txRowIdx = 0;


    private String filterLocation;
    private String jmsHostAddress;
    private List<String> validatorNames = new ArrayList<String>();

    private final AsyncCallback<AssetNode> contentSetup = new AsyncCallback<AssetNode>() {
        public void onFailure(Throwable arg0) {
            logger.warning(arg0.toString());
        }

        public void onSuccess(AssetNode an) {
            logger.info("in content load" + an.getType());
            if (an.getTxtContent()!=null) {
//                HTML txtContent = new HTML("<pre>" + an.getTxtContent() + "</pre>");
                if ("reqHdrType".equals(an.getType())) {
                    requestViewerWidget.setHeaderAssetNode(an);
                } else if ("reqBodyType".equals(an.getType())) {
                    requestViewerWidget.setMessageAssetNode(an);
                } else if ("resHdrType".equals(an.getType())) {
                    responseViewerWidget.setHeaderAssetNode(an);
                } else if ("resBodyType".equals(an.getType())) {
                    responseViewerWidget.setMessageAssetNode(an);
                }
            }

        }

    };
    private final AsyncCallback<Map<String,AssetNode>> updateHandler = new AsyncCallback<Map<String,AssetNode>> () {

        @Override
        public void onFailure(Throwable caught) {
            setListening(false);
            eventBus.fireEvent(new ListenerStatusEvent(getListening()));
            //Window.alert(caught.toString());
            logger.finest(caught.toString()); // TODO: may want to set to warning level when running in live/production/test environments

            if (getListenerEnabled()) {
                Timer timer = new Timer() {
                    public void run() {
                        logger.finest("attempting to activate listener");
                        activateListener();
                    }
                };
                timer.schedule(1000*3);
            }

        }

        @Override
        public void onSuccess(Map<String,AssetNode> anMap) {
            if (getListenerEnabled()) {
                activateListener();
            }
            logger.finest("good connection");
            if (getJmsHostAddress()==null || "".equals(jmsHostAddress)) {
                if (anMap.get("parentLoc")!=null)
                    setJmsHostAddress(anMap.get("parentLoc").getExtendedProps().get("jmsHostAddress"));
            }
            popTx(anMap,null);
            getTxTable().redraw();
            eventBus.fireEvent(new ListenerStatusEvent(getListening()));

        }
    };

    public Boolean popTx(Map<String,AssetNode> anMap, Map<String,AssetNode> anMapRelated) {
        Boolean filteredMessage = false;
        if (anMap==null) {
            logger.finest("Null assetNode: timeout or bad tx message?");
            return Boolean.FALSE;
//            if (dialogBox.isShowing()) {
//                dialogBox.hide();
//            }
        } else {
            logger.info("entering popTx " + ((getListenerEnabled()) ? "Live" : "Bypass)"));
            logger.fine("Got an empty message? " + anMap.isEmpty());
            logger.fine("sz:"+anMap.size());

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
//                            txTable.redraw();
                            if (anMapRelated!=null) {
                                appendData(anMapRelated);
//                                txTable.redraw();
                            }


                            // old: Manage the event with the parent loc for immediate indexing


                            //if (!txRowParentId.containsKey(new Integer(txRowIdx))) {
                                //txRowParentId.put(new Integer(txRowIdx), an.getParentId());
                                //txRowAssetNode.put(new Integer(txRowIdx), anMap);
                            //}

                            if (getAutoShowFirstMessage()
                                    || (an.getParentId().equals(requestViewerWidget.getIoHeaderId()) && an.getParentId().equals(responseViewerWidget.getIoHeaderId()))) {
                                requestViewerWidget.setIoHeaderId(an.getParentId());
                                requestViewerWidget.setRepId(an.getRepId());
                                requestViewerWidget.setRepositorySrc(an.getReposSrc());
                                responseViewerWidget.setIoHeaderId(an.getParentId());
                                reposService.getAssetTxtContent(an, contentSetup);

                                if (anMap.get("body")!=null) { // Body may not exist. For example as in a GET request
                                    reposService.getAssetTxtContent(anMap.get("body"), contentSetup);
                                }
                            }

                        }

                    } else {
                        logger.warning("no csv!?" + an.getParentId());
                    }

                } else {
                    logger.warning("aN was null");
                }
            } else {
                logger.warning("empty map!?");
            }


            if (getListenerEnabled() && !getFilterEnabled()) {
                logger.fine("firing event: NewTxMessageEvent");
                eventBus.fireEvent(new NewTxMessageEvent(dataProvider.getList().size()/*zero based idx*/,anMap));
            } else if ((getFilterEnabled() &&  filteredMessage)) {
                // raise BackendFilteredMessage
            }

        }

        if (!getListenerEnabled()) {
            txTable.redraw();
        }


        logger.fine("leaving popTx " + ((getListenerEnabled()) ? "Live" : "Bypass)"));
        return Boolean.TRUE;
    }
    /**
     *
     * @param eventBus
     * @param enableListener
     * @param showTxDetail
     */
    public TransactionMonitorAdvancedWidget(EventBus eventBus, Boolean enableListener, Boolean enableFilter, Boolean showTxDetail)  {
        setEventBus(eventBus);
        setShowTxDetail(showTxDetail);
        setListenerEnabled(enableListener);
        setFilterEnabled(enableFilter);

        requestViewerWidget = new MessageViewerWidget(eventBus, "Request", null);
        responseViewerWidget = new MessageViewerWidget(eventBus, "Response", null);


        // All composites must call initWidget() in their constructors.
	     initWidget(setupMonitor());


        if (getListenerEnabled() && (!getFilterEnabled() || (getFilterEnabled() && getFilterLocation()!=null))) {
            activateListener();
        }


        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {

                resizeMessageDetailArea();

            }
        });
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
        logger.fine("entering resizeMessageDetailArea");
        try {
            if (txMonitorMainSplitPanel.getParent()!=null) {
//                logger.info("resizing...");
                long containerWidth =  txMonitorMainSplitPanel.getParent().getElement().getClientWidth(); // Window.getClientWidth())
                long containerHeight = txMonitorMainSplitPanel.getParent().getElement().getClientHeight(); // Window.getClientHeight()

                long messageDetailViewerHeight = getMessageDetailHeight(containerHeight);
                txMonitorMainSplitPanel.setWidgetSize(southPanel, messageDetailViewerHeight);
                southPanel.setWidgetSize(requestViewerWidget, Math.round(.5 * containerWidth));
            } else {
                logger.fine("parent is " + (txMonitorMainSplitPanel.getParent()==null) + " or no detail showing:  detail: " + getShowTxDetail());
            }

        } catch (Throwable t) {
            logger.warning("Window resize failed:" + t.toString());
//            t.printStackTrace();
        }

        try {
//            getTxTable().onResize();
//            getTxTable().redraw();
        } catch (Throwable t) {
            ;
        }
        logger.fine("leaving resizeMessageDetailArea");
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
        logger.fine("contains filtered message? " + (anMap.get("header").getExtendedProps().containsKey("searchHit")));
        try {

            String parentId = anMap.get("header").getParentId();
            TxMessageBundle txMessageBundle = findTxMessageBundle(parentId);
            Boolean newBundle = false;

            List<TxMessageBundle> rowList = dataProvider.getList();
            TxDetailRow txRow = new TxDetailRow();
            txRow.setAnMap(anMap);

            if (txMessageBundle==null) {
                logger.fine("preparing new message bundle");
                newBundle = true;
                txMessageBundle = new TxMessageBundle();
                txMessageBundle.setKey(rowList.size());

                txMessageBundle.setParentId(parentId);
                txMessageBundle.setPath(txRow.getCsvData().get(7));
                txMessageBundle.setProxy(txRow.getCsvData().get(5));
                txMessageBundle.setTimestamp(txRow.getCsvData().get(1));
                logger.fine("preparation complete of new message bundle, header type is: <" + anMap.get("header").getType() + ">");

            }


            String anType = anMap.get("header").getType();
            if (anType!=null)
                if (anType.indexOf("REQUEST")>-1) {
                    txMessageBundle.getMessageDetailMap().put("request",txRow);
                } else if (anType.indexOf("RESPONSE")>-1) {
                    txMessageBundle.getMessageDetailMap().put("response",txRow);
                }


            if (newBundle) {
                logger.info("adding new message bundle :" + anType);
                rowList.add(txMessageBundle);
            }


        } catch (Exception ex) {
//            Window.alert(ex.toString());
            logger.warning(ex.toString());
            return false;
        } finally {
            if (dataProvider!=null && dataProvider.getList()!=null) {
                dataProvider.flush();
                dataProvider.refresh();
//                txTable.redraw();
            }
        }
        // logger.warning("Append failed!!" + an.getParentId());
        return true;
    }



    private Widget setupMonitor() {

        try {
            reposService.getJmsHostAddress(new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    setJmsHostAddress(null);
                }

                @Override
                public void onSuccess(String result) {
                    setJmsHostAddress(result);
                }
            });

        } catch (RepositoryConfigException rce) {
            logger.warning(rce.toString());
            // Need to troubleshoot, manually
        }

        try {
            reposService.getValidatorNames(new AsyncCallback<List<String>>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(List<String> result) {
                    validatorNames = result;
                }
            });
        } catch (Throwable t) {
            logger.warning(t.toString());
        }

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
        requestViewerWidget.setRepId(null);
        requestViewerWidget.setRepositorySrc(null);


        requestViewerWidget.setHeaderContent(new HTML(""));
        requestViewerWidget.setMessageContent(new HTML(""));

        responseViewerWidget.setIoHeaderId(null);
        responseViewerWidget.setHeaderContent(new HTML(""));
        responseViewerWidget.setMessageContent(new HTML(""));

        responseViewerWidget.setRepId(null);
        responseViewerWidget.setRepositorySrc(null);


//        getTxRowParentId().clear();
//        getTxRowAssetNode().clear();

        List<TxMessageBundle> rowList = dataProvider.getList();
        rowList.clear();

        //txRowIdx = 0; // Reset the index counter
    }

    public class TxTableBuilder extends AbstractCellTableBuilder<TxMessageBundle> {

        String evenRowStyle;
        String oddRowStyle;
        String rowStyle;
        String selectedRowStyle;

        StringBuilder trClasses;

        public TxTableBuilder(AbstractCellTable<TxMessageBundle> cellTable) {
            super(cellTable);
            com.google.gwt.user.cellview.client.AbstractCellTable.Style style = txTable.getResources().style();
            rowStyle =  " txDataGridEvenRow"; // style.evenRow();
            selectedRowStyle =  " txDataGridSelectedRow";  // " " + style.selectedRow();
            evenRowStyle = " txDataGridEvenRow"; // " " + style.evenRow();
            oddRowStyle = " txDataGridOddRow";   // " " + style.oddRow();
        }

        @Override
        protected void buildRowImpl(TxMessageBundle rowValue, int absRowIndex) {
            logger.fine("entering buildRowImpl");
            buildStandardRowImpl(rowValue, absRowIndex);
            if (itemsToExpandToggle.contains(rowValue)){
                for(String key : rowValue.getMessageDetailMap().keySet()) {
                    logger.fine("building message detail row: " + key);
                    buildMessageRowImpl(rowValue, key, absRowIndex);
                }
            }
            logger.fine("leaving buildRowImpl");
        }

        protected void buildStandardRowImpl(TxMessageBundle rowValue, int absRowIndex) {
            try {
                logger.fine("entering buildStandardRowImpl");
                determineStyle(rowValue,absRowIndex);
                // Start Row
                TableRowBuilder row = startRow();

                row.className(trClasses.toString());

                // Render Ind cell
                TableCellBuilder indCellTd = row.startTD();
//                HasCell<TxMessageBundle, SafeHtml> indCell = new IndicatorCell();
                this.renderCell(indCellTd, createContext(0), buildMainIndColumn(), rowValue);
                indCellTd.endTD();

                // Render timestamp cell
                TableCellBuilder timestampCell = row.startTD();
                this.renderCell(timestampCell, createContext(1), txTable.getColumn(1), rowValue);
                timestampCell.endTD();

                // Render Status cell
                TableCellBuilder statusCell = row.startTD();
                //this.renderCell(statusCell, createContext(2), dataGrid.getColumn(2), rowValue);
                statusCell.endTD();

                // Render Artifact cell
                TableCellBuilder artifactCell = row.startTD();
//            if(!itemsToExpandToggle.contains(rowValue))
//                this.renderCell(artifactCell, createContext(3), dataGrid.getColumn(3), rowValue);
                artifactCell.endTD();

                // Render MessageFrom cell
                TableCellBuilder messageFromCell = row.startTD();
                if(!itemsToExpandToggle.contains(rowValue))
                    this.renderCell(messageFromCell, createContext(4), txTable.getColumn(4), rowValue);
                messageFromCell.endTD();

                // Render Proxy cell
                TableCellBuilder proxyCell = row.startTD();
                if(!itemsToExpandToggle.contains(rowValue))
                    this.renderCell(proxyCell, createContext(5), txTable.getColumn(5), rowValue);
                proxyCell.endTD();

                // Render forwardedTo cell
                TableCellBuilder forwardedToCell = row.startTD();
                if(!itemsToExpandToggle.contains(rowValue))
                    this.renderCell(forwardedToCell, createContext(6), txTable.getColumn(6), rowValue);
                forwardedToCell.endTD();

                // path
                TableCellBuilder pathCell = row.startTD();
                if(!itemsToExpandToggle.contains(rowValue))
                    this.renderCell(pathCell, createContext(7), txTable.getColumn(7), rowValue);
                pathCell.endTD();


                // Render contentType cell
                TableCellBuilder contentTypeCell = row.startTD();
                contentTypeCell.endTD();

                // Render method cell
                TableCellBuilder methodCell = row.startTD();
                methodCell.endTD();

                // Render length cell
                TableCellBuilder lengthCell = row.startTD();
                lengthCell.endTD();

                // Render responseTime cell
                TableCellBuilder responseCell = row.startTD();
                responseCell.align("center");
                if(!itemsToExpandToggle.contains(rowValue))
                    this.renderCell(responseCell, createContext(11), new MessageDetailCell("response",11), rowValue);
                responseCell.endTD();

                // Render validation cell
                TableCellBuilder validationCell = row.startTD();
                validationCell.endTD();


                // End Row
                row.endTR();

            } catch (Throwable t ) {

                logger.warning("builder standard failed: " + t.toString());
                t.printStackTrace();
            }
            logger.fine("leaving buildStandard");
        }

         private class MessageDetailCell implements HasCell<TxMessageBundle, SafeHtml> {
            //            private EditTextCell cell = new EditTextCell();
            private SafeHtmlCell cell = new SafeHtmlCell();
            private String messageKey;
            private int index;

//            TagResources tagResources = GWT.create(TagResources.class);
//            IconCellDecorator<String> decoratorCell = new IconCellDecorator<String>(tagResources.delete(), cell);

            public MessageDetailCell(String messageKey, int index){
                setIndex(index);
                setMessageKey(messageKey);
            }

            @Override
            public Cell<SafeHtml> getCell() {
                return cell;
            }

            @Override
            public FieldUpdater<TxMessageBundle, SafeHtml> getFieldUpdater() {

                return new FieldUpdater<TxMessageBundle, SafeHtml>() {
                    @Override
                    public void update(int index, TxMessageBundle object, SafeHtml value) {
                        if (itemsToExpandToggle.contains(object)){
                            itemsToExpandToggle.remove(object);
                        } else {
                            itemsToExpandToggle.add(object);
                        }
                        txTable.redraw();
                    }
                };
            }


            @Override
            public SafeHtml getValue(TxMessageBundle o) {
                SafeHtmlBuilder shb = new SafeHtmlBuilder();

                if (o.getMessageDetailMap().get(getMessageKey())!=null
                        && o.getMessageDetailMap().get(getMessageKey())!=null
                        && o.getMessageDetailMap().get(getMessageKey()).getCsvData()!=null
                        && index < o.getMessageDetailMap().get(getMessageKey()).getCsvData().size()) {
                    if (COLUMN_HEADER_SEARCH_HIT_IND.equals(columns[this.index])) {
                        if (!getListenerEnabled() && "yes".equals(o.getMessageDetailMap().get(getMessageKey()).getAnMap().get("header").getExtendedProps().get("frontendSearchHit"))) {
                            shb.appendHtmlConstant("<span style=\"width:32px;height:32px;\"><img height=8 width=8 src='" + GWT.getModuleBaseForStaticFiles() + "images/bullet_ball_glass_green.png'/></span>");
                        }
                    } else  if (COLUMN_HEADER_RESPONSE_TIME_MS.equals(columns[index])) {
                        return formatResponseTime(o.getMessageDetailMap().get(getMessageKey()).getCsvData().get(index));
                    } else if (COLUMN_HEADER_PATH.equals(columns[index])) {

                        String path = o.getMessageDetailMap().get(getMessageKey()).getCsvData().get(index);
                        shb.appendHtmlConstant("<span title='" + ((path!=null)?path:"")  + "'>");
                        shb.appendEscaped(path);
                        shb.appendHtmlConstant("</span>");

                    } else if (COLUMN_HEADER_VALIDATION.equals(columns[index])) {

                        String validationDetail = o.getMessageDetailMap().get(getMessageKey()).getAnMap().get("header").getExtendedProps().get("validationDetail");

                        if ("validationResponse".equals(validationDetail)) {
                            shb.appendHtmlConstant(o.getMessageDetailMap().get(getMessageKey()).getCsvData().get(index));
                        } else {
                            shb.appendHtmlConstant("<span title='" + ((validationDetail!=null)?validationDetail:"")  + "'>");
                            shb.appendEscaped(o.getMessageDetailMap().get(getMessageKey()).getCsvData().get(index));
                            shb.appendHtmlConstant("</span>");
                        }

                    }  /* else if (COLUMN_HEADER_ROW_FORWARDED_TO.equals(columns[index])) {
                        shb.appendEscaped(o.getMessageDetailMap().get(getMessageKey()).getAnMap().get("header").getExtendedProps().get("toIp"));
                    } */ else {

                        try {
//                            String val = o.getMessageDetailMap().get(getMessageKey()).getCsvData().get(index);
//                            if (val!=null || (val!=null && !"null".equals(val)))
                                shb.appendEscaped(o.getMessageDetailMap().get(getMessageKey()).getCsvData().get(index));
//                            else
//                                shb.appendEscaped("");
                        } catch (Throwable t) {
                            logger.info(t.toString());
                        }

                    }
                }
                return shb.toSafeHtml();

            }



            public String getMessageKey() {
                return messageKey;
            }

            public void setMessageKey(String messageKey) {
                this.messageKey = messageKey;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }
        }

        private Column<TxMessageBundle,String> buildMainIndColumn() {
            Column<TxMessageBundle,String> col = new Column<TxMessageBundle, String>(new TextButtonCell()) {


                @Override
                public String getValue(TxMessageBundle o) {
                    if (itemsToExpandToggle.contains(o)) {
                        return "<<";
                    } else
                        return ">>";
                }
            };

            col.setFieldUpdater( new FieldUpdater<TxMessageBundle, String>() {
                @Override
                public void update(int index, TxMessageBundle object, String value) {
                    if (itemsToExpandToggle.contains(object)){
                        itemsToExpandToggle.remove(object);
                    } else {
                        itemsToExpandToggle.add(object);
                    }
                    txTable.redraw();
                }
            });

            return col;
        }




        protected void buildMessageRowImpl(TxMessageBundle rowValue, String messageKey, int absRowIndex){
            logger.fine("entering buildMessageRowImpl");
            try {
                // Start Row
                TableRowBuilder row = startRow();

                row.className(trClasses.toString());

                // Render Ind cell
                TableCellBuilder indCell = row.startTD();
                HasCell<TxMessageBundle, SafeHtml> cell = new MessageDetailCell(messageKey,0);
                this.renderCell(indCell, createContext(0), cell, rowValue);
                indCell.endTD();

                // Render timestamp cell
                TableCellBuilder timestampCell = row.startTD();
//                this.renderCell(timestampCell, createContext(1), txTable.getColumn(1), rowValue); /* Having this cell blank will create a grouping effect by the standard row */
                timestampCell.endTD();

                // Render Status cell
                TableCellBuilder statusCellTD = row.startTD();
//                statusCellTD.align("right");
                HasCell<TxMessageBundle, SafeHtml> statusCell = new MessageDetailCell(messageKey,2);
                this.renderCell(statusCellTD, createContext(2), statusCell, rowValue);
                statusCellTD.endTD();

                // Render Artifact cell
                TableCellBuilder artifactCellTd = row.startTD();
                HasCell<TxMessageBundle, SafeHtml> artifactCell = new MessageDetailCell(messageKey,3);
                this.renderCell(statusCellTD, createContext(3), artifactCell, rowValue);
                artifactCellTd.endTD();

                // Render MessageFrom cell
                TableCellBuilder messageFromCell = row.startTD();
//                if(!itemsToExpandToggle.contains(rowValue))
                    this.renderCell(messageFromCell, createContext(4), new MessageDetailCell(messageKey,4), rowValue);
                messageFromCell.endTD();

                // Render Proxy cell
                TableCellBuilder proxyCell = row.startTD();
//                proxyCell.align("right");
//                if(!itemsToExpandToggle.contains(rowValue))
                    this.renderCell(proxyCell, createContext(5), new MessageDetailCell(messageKey,5), rowValue);
                proxyCell.endTD();

                // Render forwardedTo cell
                TableCellBuilder forwardedToCell = row.startTD();
//                if(!itemsToExpandToggle.contains(rowValue))
                    this.renderCell(forwardedToCell, createContext(6), new MessageDetailCell(messageKey,6), rowValue);
                forwardedToCell.endTD();

                // path
                TableCellBuilder pathCell = row.startTD();
//                if(!itemsToExpandToggle.contains(rowValue))
                    this.renderCell(pathCell, createContext(7), new MessageDetailCell(messageKey,7), rowValue);
                pathCell.endTD();


                // Render contentType cell
                TableCellBuilder contentTypeCell = row.startTD();
                this.renderCell(contentTypeCell, createContext(8), new MessageDetailCell(messageKey,8), rowValue);
                contentTypeCell.endTD();

                // Render method cell
                TableCellBuilder methodCell = row.startTD();
                this.renderCell(methodCell, createContext(9), new MessageDetailCell(messageKey,9), rowValue);
                methodCell.endTD();

                // Render length cell
                TableCellBuilder lengthCell = row.startTD();
                lengthCell.align("right");
                this.renderCell(lengthCell, createContext(10), new MessageDetailCell(messageKey,10), rowValue);
                lengthCell.endTD();

                // Render responseTime cell
                TableCellBuilder responseCell = row.startTD();
                responseCell.align("center");
                this.renderCell(responseCell, createContext(11), new MessageDetailCell(messageKey,11), rowValue);
                responseCell.endTD();

                TableCellBuilder validationCell = row.startTD();
                this.renderCell(validationCell, createContext(12), new MessageDetailCell(messageKey,12), rowValue);
                validationCell.endTD();


                // End Row
                row.endTR();

            } catch (Throwable t) {
                logger.warning("builder message row failed: " + t.toString());
            }
            logger.fine("leaving buildMessageRowImpl");
        }

        private void determineStyle(TxMessageBundle rowValue, int absRowIndex){
            SelectionModel<? super TxMessageBundle> selectionModel = txTable.getSelectionModel();
            boolean isSelected =
                    (selectionModel == null || rowValue == null) ? false : selectionModel
                            .isSelected(rowValue);
            boolean isEven = absRowIndex % 2 == 0;
            trClasses = new StringBuilder(rowStyle);
            if (isEven) {
                trClasses.append(evenRowStyle);
            } else {
                trClasses.append(oddRowStyle);
            }
            if (isSelected) {
                trClasses.append(selectedRowStyle);
            }
        }

    }


    private class TxMessageBundle implements IsSerializable, Comparable<TxMessageBundle>  {
        private int key;
        private String timestamp;
        private String proxy;
        private String path;
        private String parentId;

        private String responseTime;

        //        private List<TxDetailRow> messages;
        private Map<String,TxDetailRow> messageDetailMap =  new LinkedHashMap<String, TxDetailRow>();


        public List<String> getCsvData() {

            if (getMessageDetailMap().get("request")!=null
                    && getMessageDetailMap().get("request").getAnMap()!=null
                    && getMessageDetailMap().get("request").getAnMap().get("header")!=null) {
                AssetNode an =  getMessageDetailMap().get("request").getAnMap().get("header"); // A header has got to exist
                if (an!=null) {
                    if (an.getCsv() !=null) {
                        String[] csvData = an.getCsv()[0];
                        return Arrays.asList(csvData);

                    }
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

        public Map<String, TxDetailRow> getMessageDetailMap() {
            return messageDetailMap;
        }

        public void setMessageDetailMap(Map<String, TxDetailRow> messageDetailMap) {
            this.messageDetailMap = messageDetailMap;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getProxy() {
            return proxy;
        }

        public void setProxy(String proxy) {
            this.proxy = proxy;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }


        public String getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(String responseTime) {
            this.responseTime = responseTime;
        }

        @Override
        public int compareTo(TxMessageBundle o) {
                return getParentId().compareTo(o.getParentId());
        }
    }

    private static final ProvidesKey<TxMessageBundle> TX_MESSAGE_BUNDLE_PROVIDES_KEY = new ProvidesKey<TxMessageBundle>() {
        @Override
        public Object getKey(TxMessageBundle item) {
            return item == null ? null : item.getKey();
        }
    };
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
            return new ArrayList<String>();
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


    private Widget setupTable(ScrollPanel sp) {
        //StyleInjector.inject(".txColHidden {background-color:black;visibility:hidden;display:none}");
        //CellTable<List<String>> txTable = new CellTable<List<String>>();

        try {
//            txTable.setEmptyTableWidget(new Label("No Data to Display"));

            TxTableBuilder txTableBuilder = new TxTableBuilder(txTable);
            txTable.setTableBuilder(txTableBuilder);

            //txTable.setWidth("100%", true);
            txTable.setWidth("100%");
            txTable.setHeight("100%");
            txTable.setSkipRowHoverCheck(true);
            txTable.setSkipRowHoverFloatElementCheck(true);
            txTable.setSkipRowHoverStyleUpdate(true);
            txTable.setStyleName("txDataGridNoTableSpacing");
            txTable.getElement().getStyle().setProperty("wordWrap","break-word");


            //List<List<String>> rows = new ArrayList<List<String>>();
            //List<TxDetailRow> rows = new ArrayList<TxDetailRow>();
            List<TxMessageBundle> rows = new ArrayList<TxMessageBundle>();

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
            final SingleSelectionModel<TxMessageBundle> selectionModel = new SingleSelectionModel<TxMessageBundle>();
            txTable.setSelectionModel(selectionModel);

            selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                public void onSelectionChange(SelectionChangeEvent event) {

        //                TxDetailRow selected = selectionModel.getSelectedObject();
                    TxMessageBundle selected = selectionModel.getSelectedObject();
                    if (selected != null ) {  // && getShowTxDetail()
                        //setMessageViewer(selected.getKey());
                        setMessageViewer(selected);
                    }
                }
            });

//            txTable.redraw();
            //sp.add(txTable);


            final PopupPanel menu = new PopupPanel(true);



            txTable.addDomHandler(new ContextMenuHandler() {
                @Override
                public void onContextMenu(ContextMenuEvent event) {
                    event.preventDefault();
                    event.stopPropagation();

                    if (requestViewerWidget.getIoHeaderId()==null)
                        return;

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


                    for (final String valName : validatorNames) {
                        Anchor validatorAnchor = new Anchor("Validate " + valName);
                        validatorAnchor.addClickHandler(new ClickHandler() {
                            @Override
                            public void onClick(ClickEvent event) {
                                if (requestViewerWidget.getIoHeaderId() != null) {
                                    AssetNode transaction = new AssetNode();
                                    transaction.setRepId(requestViewerWidget.getRepId());
                                    transaction.setReposSrc(requestViewerWidget.getRepositorySrc());
                                    transaction.setAssetId(requestViewerWidget.getIoHeaderId());
//                                    Window.alert(transaction.getAssetId() + " src:" + transaction.getReposSrc());

                                    AssetNode request = new AssetNode();
                                    request.setDisplayName("Request");
                                    request.setType("tran-request");
                                    request.addChild(requestViewerWidget.getHeaderAssetNode());
                                    request.addChild(requestViewerWidget.getMessageAssetNode());

                                    AssetNode response = new AssetNode();
                                    response.setDisplayName("Response"); // Carefully check this usage in the map below
                                    response.setType("tran-response");
                                    response.addChild(responseViewerWidget.getHeaderAssetNode());
                                    response.addChild(responseViewerWidget.getMessageAssetNode());

                                    transaction.addChild(request);
                                    transaction.addChild(response);

                                    setValidationResponseResult("request", "Processing...", "");
                                    setValidationResponseResult("response", "Processing...", "");
                                    getTxTable().redraw();

                                    try {
                                        reposService.validateMessage(valName, getValidationLevel(), transaction, new AsyncCallback<Map<String, AssetNode>>() {
                                            @Override
                                            public void onFailure(Throwable caught) {
                                                String msg = caught.toString();
                                                Window.alert(msg);
                                                setValidationResponseResult("request", "RPC Fail", msg);
                                                setValidationResponseResult("response", "RPC Fail", msg);
                                                getTxTable().redraw();
                                            }

                                            @Override
                                            public void onSuccess(Map<String, AssetNode> result) {
//                                                Window.alert((result==null)?"null":""+result.size()  +  " req rs:" + result.get("Request").getExtendedProps().get("result") );
                                                try {
                                                    if (result!=null && result.size()==0) {
                                                        Window.alert("No validation response was received.");
                                                        setValidationResponseResult("request", "No data", "Empty set");
                                                        setValidationResponseResult("response", "No data", "Empty set");
                                                    }
                                                    if (result!=null) {
//                                                    Window.alert(result.get("resType").getExtendedProps().get("result"));
                                                        if (result.get("Request")!=null) {
                                                            AssetNode an = result.get("Request");
                                                            setValidationResponseResult("request", an);
                                                        }
                                                        if (result.get("Response")!=null) {
                                                            AssetNode an = result.get("Response");
                                                            setValidationResponseResult("response", an);
                                                        }
                                                    }
                                                } catch (Throwable t) {
                                                    t.printStackTrace();
                                                    setValidationResponseResult("request", "Exception/UI", t.toString());
                                                    setValidationResponseResult("response", "Exception/UI", t.toString());
                                                }
                                                getTxTable().redraw();
                                            }
                                        });

                                    } catch (Throwable t) {
                                        logger.warning(t.toString());
                                        setValidationResponseResult("request", "Exception", t.toString());
                                        setValidationResponseResult("response", "Exception", t.toString());

                                    }

                                }
                            }
                        });
                        menuItemPanel.add(validatorAnchor);
                    }

                    menu.setWidget(menuItemPanel);
                    menu.setPopupPosition(event.getNativeEvent().getClientX(), event.getNativeEvent().getClientY());
                    menu.show();


                }
            }, ContextMenuEvent.getType());


            getPager().getElement().getStyle().setMarginTop(0, Style.Unit.PX);
            //getPager().setHeight("50%");
            getPager().getElement().getStyle().setVerticalAlign(Style.VerticalAlign.MIDDLE);
            getPager().setDisplay(txTable);
        //    return sp;
        return txTable;
        } catch (Throwable t ) {
            logger.warning(t.toString());
        }
        return  null;
    }

    private void setValidationResponseResult(String rowStr, AssetNode resultNode) {
        if (resultNode==null)
            return;

        String resultStr = resultNode.getExtendedProps().get("result");

        TxMessageBundle txMessageBundle = findTxMessageBundle(requestViewerWidget.getIoHeaderId());
        AssetNode an = txMessageBundle.getMessageDetailMap().get(rowStr).getAnMap().get("header");

        if (an!=null) {
            if (an.getCsv() !=null) {
                String[][] csvData = an.getCsv();

                if (resultStr!=null) {
                    SafeHtmlBuilder resultShb = new SafeHtmlBuilder();

                    resultShb.appendHtmlConstant("<a href='"
                            + GWT.getHostPageBaseURL()
                            + GWT.getModuleName()
                            + ".html?reposSrc=RW_EXTERNAL&reposId=" + resultNode.getRepId() + "&assetId=" + resultNode.getAssetId()
                            + "' target='_blank'>" // Open link in new tab
                    );
                    resultShb.appendEscaped(resultStr);
                    resultShb.appendHtmlConstant("</a>&nbsp;");

                    csvData[0][12] =  resultShb.toSafeHtml().asString();

                    an.getExtendedProps().put("validationDetail","validationResponse"); // This will use the html constant text generated above, otherwise the text will be HTML escaped
                    an.setCsv(csvData);
                }

            }
        }


    }

    private void setValidationResponseResult(String rowStr, String resultStr, String validationDetail) {
        TxMessageBundle txMessageBundle = findTxMessageBundle(requestViewerWidget.getIoHeaderId());
        AssetNode an = txMessageBundle.getMessageDetailMap().get(rowStr).getAnMap().get("header");

        if (an!=null) {
            if (an.getCsv() !=null) {
                String[][] csvData = an.getCsv();

                if (resultStr!=null) {
                    SafeHtmlBuilder resultShb = new SafeHtmlBuilder();

                    resultShb.appendEscaped(resultStr);

                    csvData[0][12] =  resultShb.toSafeHtml().asString();
                    an.setCsv(csvData);

                }

                if (validationDetail!=null) {

                    SafeHtmlBuilder shb = new SafeHtmlBuilder();
                    shb.appendEscaped(validationDetail);

                    an.getExtendedProps().put("validationDetail",shb.toSafeHtml().asString());
                }
            }
        }
    }

    public TxMessageBundle findTxMessageBundle(String parentId) {
        List<TxMessageBundle> rowList = dataProvider.getList();

        if (parentId!=null) {
            for (TxMessageBundle row : rowList) {
                if (parentId.equals(row.getParentId())) {
                    return row;  // rowList.indexOf(row).;
                }
            }
        }

        return null;
    }

    public Boolean isTxMessageBundleLoaded(String parentId) {
        return (findTxMessageBundle(parentId)!=null);
    }

    public Map<String,AssetNode> getAnMap(String parentId, String messageKey) {
        try {
            TxMessageBundle txMessageBundle = findTxMessageBundle(parentId);
            if (txMessageBundle!=null) {
                return txMessageBundle.getMessageDetailMap().get(messageKey).getAnMap();
            }

        } catch (Throwable t) {
            logger.info(t.toString());
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

    public void setMessageViewer(TxMessageBundle row) {

                if (dataProvider.getList().size()>0) {
                    //Map<String,AssetNode> anMap = txRowAssetNode.get(new Integer(selectedIndex));
                  if (row.getMessageDetailMap().containsKey("request")) {
                    Map<String,AssetNode> anMap =  row.getMessageDetailMap().get("request").getAnMap();
                    int anLen = anMap.size();
                    if (anLen>0) {
                        AssetNode an = anMap.get("header");

                        //Window.alert("setup for" + selectedIndex + an.getParentId() + an.getType());


                        requestViewerWidget.setIoHeaderId(an.getParentId());
                        requestViewerWidget.setRepId(an.getRepId());
                        requestViewerWidget.setRepositorySrc(an.getReposSrc());

                        responseViewerWidget.setIoHeaderId(an.getParentId());
                        responseViewerWidget.setRepId(an.getRepId());
                        responseViewerWidget.setRepositorySrc(an.getReposSrc());


                        reposService.getAssetTxtContent(an, contentSetup);

                        if (anMap.get("body")!=null) { // Body exists
                            reposService.getAssetTxtContent(anMap.get("body"), contentSetup);
                        }

//                        List<TxDetailRow> rowList = dataProvider.getList();
//                        TxDetailRow row = rowList.get(selectedIndex);
//                        row.getCsvData().set(0,"X");
//
//                        if (dataProvider.getList().size()==1)
//                            return;

                        // Find matching pair if it exists
                        //int matchingPairIdx = findMatchingPairIdx(an,selectedIndex);
//                        int matchingPairIdx = findMatchingPairIdx(an);

                        if (!row.getMessageDetailMap().containsKey("response")) return;
//                        Map<String,AssetNode> matchingPair = null;
                        Map<String,AssetNode> matchingPair = row.getMessageDetailMap().get("response").getAnMap(); //  row.getMessages().get(1).getAnMap();
//
//                        if (matchingPairIdx>-1 && dataProvider.getList().size()>0) {
//                            matchingPair = dataProvider.getList().get(matchingPairIdx).getAnMap();
//                        }

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
                } else {
                      logger.info("no request found in bundle!");
                  }

                }

    }

    /*
    class IndexedTxColumn extends  Column<TxMessageBundle, SafeHtml> {
        private final int index;
        public IndexedTxColumn(int index) {
            super(new SafeHtmlCell());
            this.index = index;
        }

    }
    */

    private SafeHtml formatResponseTime(String responseTime) {
        SafeHtmlBuilder shb = new SafeHtmlBuilder();
        if ("0".equals(responseTime)) { // Reformat 0 ms to less than 1 ms
            shb.appendEscaped("<1");
        } else  {
            shb.appendEscaped(""+responseTime);
        }

        if (!"".equals(responseTime)) {
            shb.appendHtmlConstant("<span style=\"font-size:9px\">&nbsp;ms</span>");
        }
        return  shb.toSafeHtml();
    }
    //  class to return main parent-transaction level detail
    // move the column getValue index retrieval to MessageDetail
    class IndexedColumn extends Column<TxMessageBundle, SafeHtml> {
        private final int index;
        public IndexedColumn(int index) {
            super(new SafeHtmlCell());
            this.index = index;
        }

        /*
        @Override
        public SafeHtml getValue(TxMessageBundle o) {
            SafeHtmlBuilder shb = new SafeHtmlBuilder();
                shb.appendHtmlConstant("<span>Static text test</span>");
            return shb.toSafeHtml();


        }
*/

        /**


        @Override
        public void onBrowserEvent(Cell.Context context, Element elem, TxMessageBundle object, NativeEvent event) {
            super.onBrowserEvent(context, elem, object, event);

            Window.alert(event.getType());
            if (ContextMenuEvent.getType().getName().equals(event.getType())) {
                final PopupPanel menu = new PopupPanel(true);

                VerticalPanel menuItemPanel = new VerticalPanel();

                Anchor validateSoapAction = new Anchor("Validate SoapAction");
                validateSoapAction.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {

                    }
                });

                menuItemPanel.add(validateSoapAction);
                menuItemPanel.add(new Anchor("Validate Header"));

                menu.setWidget(menuItemPanel);
                menu.setPopupPosition(event.getClientX(), event.getClientY());
                menu.show();

            }

        }

         */

        @Override
        public SafeHtml getValue(TxMessageBundle o) {
            SafeHtmlBuilder shb = new SafeHtmlBuilder();
            try {
                if (o.getMessageDetailMap().get("request")==null) return shb.toSafeHtml();

                AssetNode headerMsg =  o.getMessageDetailMap().get("request").getAnMap().get("header"); //  txRowAssetNode.get(o.getKey()).get("header"); // headerMsg

                if (COLUMN_HEADER_PROXY.equals(columns[this.index])) {

                    shb.appendHtmlConstant("<span title='" + headerMsg.getExtendedProps().get("proxyDetail")  + "'>"); // .getProps()
                    shb.appendEscaped(o.getCsvData().get(this.index));
                    shb.appendHtmlConstant("</span>");

                } else if (COLUMN_HEADER_ROW_MESSAGE_FROM.equals(columns[this.index])) {

                    shb.appendHtmlConstant("<span title='" + headerMsg.getExtendedProps().get("fromIp")  + "'>"); // .getProps()
                    shb.appendEscaped(o.getCsvData().get(this.index));
                    shb.appendHtmlConstant("</span>");

                } else if (COLUMN_HEADER_ROW_FORWARDED_TO.equals(columns[this.index])) {

                    shb.appendHtmlConstant("<span title='" + o.getCsvData().get(this.index)  + "'>"); // .getProps()
                    shb.appendEscaped(headerMsg.getExtendedProps().get("toIp"));
                    shb.appendHtmlConstant("</span>");

                } else if (COLUMN_HEADER_PATH.equals(columns[this.index])) {

                    shb.appendHtmlConstant("<span title='" + o.getCsvData().get(this.index)  + "'>"); // .getProps()
                    shb.appendEscaped(o.getCsvData().get(this.index));
                    shb.appendHtmlConstant("</span>");


                } else if (COLUMN_HEADER_RESPONSE_TIME_MS.equals(columns[this.index])) {
                    if (o.getCsvData()!=null) {
                        if ("0".equals(o.getCsvData().get(this.index))) { // Reformat 0 ms to less than 1 ms
                            shb.appendEscaped("<1");
                        } else  {
                            shb.appendEscaped(""+o.getCsvData().get(this.index));
                        }

                        if (!"".equals(o.getCsvData().get(this.index))) {
                            shb.appendHtmlConstant("<span style=\"font-size:9px\">&nbsp;ms</span>");
                        }
                    }
                } else if (COLUMN_HEADER_SEARCH_HIT_IND.equals(columns[this.index])) {
                    if (!getListenerEnabled() && "yes".equals(headerMsg.getExtendedProps().get("frontendSearchHit"))) {
//                        String criteria = headerMsg.getExtendedProps().get("frontendSearchHitCriteria");

                        shb.appendHtmlConstant("<span style=\"width:32px;height:32px;\"><img height=8 width=8 src='" + GWT.getModuleBaseForStaticFiles() + "images/bullet_ball_glass_green.png'/></span>");

                        /*
                        if (criteria!=null) {
                            shb.appendEscaped(criteria);
                        } */


                    } else
                        shb.appendHtmlConstant("<span style=\"width:32px;height:32px;\">&nbsp;</span>");
                }  else if (COLUMN_HEADER_VALIDATION.equals(columns[index])) {
                    String validationDetail = headerMsg.getExtendedProps().get("validationDetail");
                    shb.appendHtmlConstant("<span title='" + ((validationDetail!=null)?validationDetail:"")  + "'>");
                    shb.appendEscaped(o.getCsvData().get(this.index));
                    shb.appendHtmlConstant("</span>");
                }  else {
                        shb.appendEscaped(o.getCsvData().get(this.index));
                }


            } catch (Throwable t) {

                logger.warning("getValue failed: " +t.toString() + " o parentId:" + o.getParentId() + " o messageDetailMap sz:" + o.getMessageDetailMap().size());
//                t.printStackTrace();
                shb.appendEscaped(" ");
             }

            return shb.toSafeHtml();
        }



        /*
         // For use with String type, for example: Column<TxDetailRow, String>:
        //  super(new TextCell());

        @Override
        public String getValue(TxDetailRow object) {

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

    public DataGrid<TxMessageBundle> getTxTable() {
        return txTable;
    }

    public void setTxTable(DataGrid<TxMessageBundle> txTable) {
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

    public String getJmsHostAddress() {
        return jmsHostAddress;
    }

    public void setJmsHostAddress(String jmsHostAddress) {
        this.jmsHostAddress = jmsHostAddress;
    }

    public ValidationLevel getValidationLevel() {
        return validationLevel;
    }

    public void setValidationLevel(ValidationLevel validationLevel) {
        this.validationLevel = validationLevel;
    }
}
