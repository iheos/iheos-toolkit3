package gov.nist.hit.ds.logBrowser.client.widgets;


import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import gov.nist.hit.ds.logBrowser.client.event.NewTxMessageEvent;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryService;
import gov.nist.hit.ds.repository.simple.search.client.RepositoryServiceAsync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TransactionMonitorWidget extends Composite {
	/**
	 *
	 * @author Sunil.Bhaskarla
	 */
	private static Logger logger = Logger.getLogger(TransactionMonitorWidget.class.getName());

	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);


    private SimpleEventBus eventBus;

    private Boolean showTxDetail;

    private Boolean listenerEnabled;

    SplitLayoutPanel southPanel = new SplitLayoutPanel(2);
    SplitLayoutPanel txMonitorMainSplitPanel = new SplitLayoutPanel(3);

    VerticalPanel contentPanel = new VerticalPanel();
    ScrollPanel centerPanel = new ScrollPanel();
    //ListDataProvider<List<String>> dataProvider  = new ListDataProvider<List<String>>();
    ListDataProvider<TxDetailRow> dataProvider  = new ListDataProvider<TxDetailRow>();
    /*
    Sample 2-way Exchange pattern txDetail:
    20140326160812,"RESPONSE", "","500","localhost","localhost:8080","localhost:8001^ProxyRuleMappingName: localcap","text/html","","65","0"

     */
    final String[] columns = {"Timestamp","Status","Artifact","Message From","Proxy","Forwarded To","Path","ContentType","Method","Length","Response Time"};
    MessageViewerWidget requestViewerWidget = new MessageViewerWidget(eventBus, "Request", null);
    MessageViewerWidget responseViewerWidget = new MessageViewerWidget(eventBus, "Response", null);
    Map<Integer, String> txRowParentId = new HashMap<Integer, String>();
    Map<Integer, List<AssetNode>> txRowAssetNode = new HashMap<Integer, List<AssetNode>>();
    int txRowIdx = 0;

    final AsyncCallback<AssetNode> contentSetup = new AsyncCallback<AssetNode>() {
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
    final AsyncCallback<List<AssetNode>> updateHandler = new AsyncCallback<List<AssetNode>> () {

        @Override
        public void onFailure(Throwable caught) {
            logger.warning(caught.toString());
        }

        @Override
        public void onSuccess(List<AssetNode> anList) {
            popTx(anList);
        }
    };

    public void popTx(List<AssetNode> anList) {
        if (anList==null) {
            logger.info("Null assetNode: bad tx message?");
        } else {
            logger.info("Got a message? " + anList.isEmpty());
            logger.info("sz:"+anList.size());

            if (!anList.isEmpty() && anList.size()>0) {
                AssetNode an = anList.get(1); // The header has got to exist
                if (an!=null) {
                    if (an.getCsv() !=null) {
                        String[] csvData = an.getCsv()[0];
                        logger.info(csvData.toString());
                        if (appendData(txRowIdx,csvData)) {
                            //if (!txRowParentId.containsKey(new Integer(txRowIdx))) {
                                txRowParentId.put(new Integer(txRowIdx), an.getParentId());
                                txRowAssetNode.put(new Integer(txRowIdx), anList);
                            //}

                            if ((requestViewerWidget.getIoHeaderId() == null || responseViewerWidget.getIoHeaderId() == null)
                                    || (an.getParentId().equals(requestViewerWidget.getIoHeaderId()) && an.getParentId().equals(responseViewerWidget.getIoHeaderId()))) {
                                requestViewerWidget.setIoHeaderId(an.getParentId());
                                responseViewerWidget.setIoHeaderId(an.getParentId());
                                reposService.getAssetTxtContent(an, contentSetup);

                                if (anList.size()==3) { // Body may not exist. For example as in a GET request
                                    reposService.getAssetTxtContent(anList.get(2), contentSetup);
                                }
                            }

                            // Manage the event with the parent loc for immediate indexing
                            if (getListenerEnabled()) {
                                eventBus.fireEvent(new NewTxMessageEvent(txRowIdx,anList.get(0)));
                            }
                            txRowIdx++;

                        }

                    }

                }
            }

        }

        if (getListenerEnabled()) {
            try {
                reposService.getTxUpdates("",updateHandler);
            } catch (Exception ex) {
                logger.warning(ex.getMessage());
            }
        }

        return;
    }
    /**
     *
     * @param eventBus
     * @param enableListener
     * @param showTxDetail
     */
    public TransactionMonitorWidget(SimpleEventBus eventBus, Boolean enableListener, Boolean showTxDetail)  {
        setEventBus(eventBus);
        setShowTxDetail(showTxDetail);
        setListenerEnabled(enableListener);

        // All composites must call initWidget() in their constructors.
	     initWidget(setupMonitor());

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                int containerWidth =  txMonitorMainSplitPanel.getParent().getElement().getClientWidth(); // Window.getClientWidth())
                int containerHeight = txMonitorMainSplitPanel.getParent().getElement().getClientHeight(); // Window.getClientHeight()

                try {

                    txMonitorMainSplitPanel.setWidgetSize(southPanel, .4 * containerHeight);
                    if (getShowTxDetail()) {
                        southPanel.setWidgetSize(requestViewerWidget, .5 * containerWidth);
                    }

                } catch (Exception ex) {
                    logger.warning("Window resize failed:" + ex.toString());
                }
            }
        });

        if (getListenerEnabled()) {
        /* live connection */
            // TODO: gracefully deattach existing connection on exit -- only an issue when attached to a PTP queue
            try {
                reposService.getTxUpdates("",updateHandler);
            } catch (Exception ex) {
                logger.warning(ex.toString());
            }
        }

    }




    protected Boolean appendData(int keyIdx, String[] csvRow) {

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

            TxDetailRow txRow = new TxDetailRow();
            txRow.setCsvData(Arrays.asList(csvRow));
            txRow.setKey(keyIdx);

           List<TxDetailRow> rowList = dataProvider.getList();
           return rowList.add(txRow);


        } catch (Exception ex) {
            logger.warning(ex.toString());
        }
        return false;
    }



    private Widget setupMonitor() {
        requestViewerWidget.getElement().getStyle()
                .setProperty("border", "none");
        responseViewerWidget.getElement().getStyle()
                .setProperty("border", "none");

        long containerWidth = Window.getClientWidth(); // This must be in sync with the widget original load size, make constant
        long containerHeight = Math.round(.5 * Window.getClientHeight());

        //=  txMonitorMainSplitPanel.getParent().getElement().getClientWidth(); // Window.getClientWidth())
        // = txMonitorMainSplitPanel.getParent().getElement().getClientHeight(); // Window.getClientHeight()

        southPanel.addWest(requestViewerWidget,Math.round(.5 * containerWidth));
        southPanel.add(responseViewerWidget);



        if (getShowTxDetail()) {
            txMonitorMainSplitPanel.addSouth(southPanel, Math.round(.4 * containerHeight)); // 500

        }


        txMonitorMainSplitPanel.add(setupTable(centerPanel));

        return txMonitorMainSplitPanel;

    }

    public void showTxDetail() {

        southPanel.setVisible(true);
    }

    public void clear() {

        requestViewerWidget.setIoHeaderId(null);
        requestViewerWidget.setHeaderContent(new HTML(""));
        requestViewerWidget.setMessageContent(new HTML(""));

        responseViewerWidget.setIoHeaderId(null);
        responseViewerWidget.setHeaderContent(new HTML(""));
        responseViewerWidget.setMessageContent(new HTML(""));

        getTxRowParentId().clear();
        getTxRowAssetNode().clear();

        List<TxDetailRow> rowList = dataProvider.getList();
        rowList.clear();

    }

    protected class TxDetailRow  {

        int key;
        List<String> csvData = null;

        public List<String> getCsvData() {
            return csvData;
        }

        public void setCsvData(List<String> csvData) {
            this.csvData = csvData;
        }


        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }


    }
    private static final ProvidesKey<TxDetailRow> KEY_PROVIDER = new ProvidesKey<TxDetailRow>() {
        @Override
        public Object getKey(TxDetailRow item) {
            return item == null ? null : item.getKey();
        }
    };


    protected ScrollPanel setupTable(ScrollPanel sp) {
        //StyleInjector.inject(".txColHidden {background-color:black;visibility:hidden;display:none}");
        //CellTable<List<String>> txTable = new CellTable<List<String>>();

        CellTable<TxDetailRow> txTable = new CellTable<TxDetailRow>(KEY_PROVIDER);
        txTable.setWidth("100%", true);

        //List<List<String>> rows = new ArrayList<List<String>>();
        List<TxDetailRow> rows = new ArrayList<TxDetailRow>();

        int colLen = columns.length;

        // Create table columns
        for (int c = 0; c < colLen; c++) {
            txTable.addColumn(new IndexedColumn(c) ,new TextHeader(columns[c]));
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
                if (selected != null) {
                    setMessageViewer(selected.getKey());
                }
            }
        });


        sp.add(txTable);

        return sp;

    }

    public void setMessageViewer(int selectedIndex) {

                if (selectedIndex<=txRowParentId.size()) {
                    List<AssetNode> anList = txRowAssetNode.get(new Integer(selectedIndex));
                    int anLen = anList.size();
                    if (anLen>0) {
                        AssetNode an = anList.get(0);

                        //Window.alert("setup for" + selectedIndex + an.getParentId() + an.getType());


                        requestViewerWidget.setIoHeaderId(an.getParentId());
                        responseViewerWidget.setIoHeaderId(an.getParentId());

                        reposService.getAssetTxtContent(an, contentSetup);

                        if (anList.size()==2) { // Body exists
                            reposService.getAssetTxtContent(anList.get(1), contentSetup);
                        }

                        if (txRowIdx==1)
                            return;

                        // Find matching pair if it exists
                        int matchingPairIdx = -1;
                        for (int cx=0; cx<txRowIdx; cx++) {
                            if ((an!=null && an.getParentId()!=null) && txRowParentId!=null) {
                                if (cx!=selectedIndex && an.getParentId().equals(txRowParentId.get(new Integer(cx)))) {
                                    matchingPairIdx = cx;
                                    break;
                                }
                            }
                        }

                        if (matchingPairIdx>-1) {
                            //Window.alert("match pair"+matchingPairIdx);
                            List<AssetNode> anPair = txRowAssetNode.get(new Integer(matchingPairIdx));

                            if (anPair.size()>0) {
                                reposService.getAssetTxtContent(anPair.get(0), contentSetup);
                            }

                            if (anPair.size()==2) { // Body exists
                                reposService.getAssetTxtContent(anPair.get(1), contentSetup);
                            }
                        } else {
                            // No matching pair
                            if ((an!=null && an.getType()!=null) && an.getType().indexOf("REQUEST")>-1) {
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

                if (this.index==4) {

                    AssetNode an = txRowAssetNode.get(o.getKey()).get(0);

                    shb.appendHtmlConstant("<span title='" + an.getProps() + "'>");
                    shb.appendEscaped(o.getCsvData().get(this.index));
                    shb.appendHtmlConstant("</span>");

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

    public SimpleEventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(SimpleEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public Boolean getShowTxDetail() {
        return showTxDetail;
    }

    public void setShowTxDetail(Boolean showTxDetail) {
        this.showTxDetail = showTxDetail;
    }
    public Boolean getListenerEnabled() {
        return listenerEnabled;
    }

    public void setListenerEnabled(Boolean listenerEnabled) {
        this.listenerEnabled = listenerEnabled;
    }

    public Map<Integer, List<AssetNode>> getTxRowAssetNode() {
        return txRowAssetNode;
    }

    public void setTxRowAssetNode(Map<Integer, List<AssetNode>> txRowAssetNode) {
        this.txRowAssetNode = txRowAssetNode;
    }

    public Map<Integer, String> getTxRowParentId() {
        return txRowParentId;
    }

    public void setTxRowParentId(Map<Integer, String> txRowParentId) {
        this.txRowParentId = txRowParentId;
    }

}
