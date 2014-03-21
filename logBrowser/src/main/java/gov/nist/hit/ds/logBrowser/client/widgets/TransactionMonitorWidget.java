package gov.nist.hit.ds.logBrowser.client.widgets;


import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
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
	public VerticalPanel topPanel;
	DialogBox db = new DialogBox();

	final public RepositoryServiceAsync reposService = GWT.create(RepositoryService.class);
	private SimpleEventBus eventBus;

    SplitLayoutPanel mainSplitPanel = new SplitLayoutPanel(3);
    VerticalPanel contentPanel = new VerticalPanel();
    ScrollPanel centerPanel = new ScrollPanel();
    //ListDataProvider<List<String>> dataProvider  = new ListDataProvider<List<String>>();
    ListDataProvider<TxDetailRow> dataProvider  = new ListDataProvider<TxDetailRow>();

    final String[] columns = {"Timestamp","Type","Path","Status","Sender","Receiver","ContentType","Method","Length","ResponseTime"};
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
            if (anList==null) {
                logger.info("Null assetNode: bad tx message?");
            } else {
                logger.info("Got a message? " + anList.isEmpty());

                if (!anList.isEmpty() && anList.size()>0) {
                    AssetNode an = anList.get(0); // Header
                    if (an!=null) {
                        if (an.getCsv() !=null) {
                            String[] csvData = an.getCsv()[0];
                            logger.info(csvData.toString());
                            if (appendData(txRowIdx,csvData)) {
                                txRowParentId.put(new Integer(txRowIdx), an.getParentId());
                                txRowAssetNode.put(new Integer(txRowIdx++), anList);

                                    if ((requestViewerWidget.getIoHeaderId() == null || responseViewerWidget.getIoHeaderId() == null)
                                            || (an.getParentId().equals(requestViewerWidget.getIoHeaderId()) && an.getParentId().equals(responseViewerWidget.getIoHeaderId()))) {
                                        requestViewerWidget.setIoHeaderId(an.getParentId());
                                        responseViewerWidget.setIoHeaderId(an.getParentId());
                                        reposService.getAssetTxtContent(an, contentSetup);

                                        if (anList.size()==2) { // Body exists
                                            reposService.getAssetTxtContent(anList.get(1), contentSetup);
                                        }
                                    }
                                 eventBus.fireEvent(new NewTxMessageEvent(txRowIdx));
                            }

                        }

                    }
                }

            }

            try {
                reposService.getTxUpdates("",updateHandler);
            } catch (Exception ex) {
                logger.warning(ex.getMessage());
            }

        }
    };

    public TransactionMonitorWidget(SimpleEventBus eventBus)  {
	    this.eventBus = eventBus;

       /* test
        appendData(new String[]{"Timestamp","Type","Path","Status","Sender","Receiver","ContentType","Method","Length1","ResponseTime"});
        appendData(new String[]{"Timestamp2","Type","Path","Status","Sender","Receiver","ContentType","Method","Length1","ResponseTime"});
        */

        // All composites must call initWidget() in their constructors.
	     initWidget(setupMainPanel());


        /* live connection */
        // TODO: gracefully deattach existing connection on exit
        try {
            reposService.getTxUpdates("",updateHandler);
        } catch (Exception ex) {
            logger.warning(ex.toString());
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
            //Window.alert(columns.length + " : " +  ex.toString());
            logger.warning(ex.toString());
        }
        return false;
    }

    protected SplitLayoutPanel setupMainPanel() {

        requestViewerWidget.getElement().getStyle()
                .setProperty("border", "none");
        responseViewerWidget.getElement().getStyle()
                .setProperty("border", "none");


        SplitLayoutPanel southPanel = new SplitLayoutPanel(2);
        southPanel.addWest(requestViewerWidget,Math.round(.5 * Window.getClientWidth())); // Math.round(.15 * Window.getClientWidth())
        southPanel.add(responseViewerWidget);

        mainSplitPanel.addSouth(southPanel,Math.round(.7 * Window.getClientHeight())); // 500
        mainSplitPanel.add(setupTable(centerPanel));


        return mainSplitPanel;
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
                            if (cx!=selectedIndex && an.getParentId().equals(txRowParentId.get(new Integer(cx)))) {
                                matchingPairIdx = cx;
                                break;
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

    class IndexedColumn extends Column<TxDetailRow, String> {
        private final int index;
        public IndexedColumn(int index) {
            super(new TextCell());
            this.index = index;
        }
        @Override
        public String getValue(TxDetailRow object) {

            try {
                return object.getCsvData().get(this.index);

             // return object.get(this.index);
            } catch (Exception ex) {
                return "";
            }
        }
    }



}
