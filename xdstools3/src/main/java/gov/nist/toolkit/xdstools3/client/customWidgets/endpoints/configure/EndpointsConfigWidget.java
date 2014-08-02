package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 *
 * Created by dazais on 5/21/2014.
 */
public class EndpointsConfigWidget extends HLayout {
    private ListGrid endpointGrid;
    private boolean endpointValueSelected = false;

    public EndpointsConfigWidget() {
        endpointGrid = createEndpointGrid();
        addMember(endpointGrid);
    }

    public ListGrid createEndpointGrid() {

        // Load all endpoints
//        List<String> results = new ArrayList<String>();
//        File[] files = new File("/path/to/the/directory").listFiles();
//
//        for (File file : files) {
//            if (file.isFile()) {
//                results.add(file.getName());
//            }
//        }

        // Create the ListGrid linked to the DataSource
        final ListGrid grid = new ListGrid(){

            //TODO may need to call super for default behavior. This should move to the nested grid.
            @Override
            public boolean canEditCell(int rowNum, int colNum) {
                ListGridRecord record = this.getRecord(rowNum);
                String colName = this.getFieldName(colNum);

                if (colName == "Non-TLS Endpoints" && (record.getAttribute("Transaction Type") == "repositoryUniqueID")
                    || (record.getAttribute("Transaction Type") == "homeCommunityId")) return false;
                else return true;
            }

            @Override
            protected Canvas getExpansionComponent(final ListGridRecord record) {

                final TransactionGrid nestedGrid = new TransactionGrid();
                nestedGrid.fetchRelatedData(record, EndpointConfigDSNew.getInstance());


                VLayout layout = new VLayout(5);
                layout.setPadding(5);
                layout.addMember(nestedGrid);

                HLayout hLayout = new HLayout(10);
                hLayout.setAlign(Alignment.CENTER);

                IButton saveButton = new IButton("Save");
                saveButton.setTop(250);
                saveButton.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        nestedGrid.saveAllEdits();
                        nestedGrid.exportAsXML();
                    }
                });
                hLayout.addMember(saveButton);

                IButton newButton = new IButton("Add");
                newButton.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        nestedGrid.startEditingNew();
                    }
                });
                hLayout.addMember(newButton);

//                    IButton discardButton = new IButton("Discard");
//                    discardButton.addClickHandler(new ClickHandler() {
//                        public void onClick(ClickEvent event) {
//                            nestedGrid.discardAllEdits();
//                        }
//                    });
//                    hLayout.addMember(discardButton);

//                    IButton closeButton = new IButton("Close");
//                    closeButton.addClickHandler(new ClickHandler() {
//                        public void onClick(ClickEvent event) {
//                            grid.collapseRecord(record);
//                        }
//                });
//                hLayout.addMember(closeButton);
//

                layout.addMember(hLayout);

                return layout;
            }
        };

        grid.setHeight100();
        grid.setWidth(800);
        grid.setAlternateRecordStyles(true);
        grid.setDataSource(EndpointConfigDSNew.getInstance());
        grid.setAutoFetchData(true);
        grid.setDrawAheadRatio(4);
        grid.setCanExpandRecords(true);
        grid.setCanExpandMultipleRecords(true);
       //  grid.setExpansionMode(ExpansionMode.RELATED);
      //  grid.setDetailDS(TransactionDS.getInstance());
        grid.setCanEdit(true);
        grid.setModalEditing(true);
        grid.setEditEvent(ListGridEditEvent.CLICK);
        grid.setListEndEditAction(RowEndEditAction.NEXT);
        grid.setAutoSaveEdits(false);
        draw();




//
//        // Group by type of transaction
//        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//        map.put("transaction", "Transaction Type");
//        grid.getField("Transaction Type").setGroupValueFunction(new GroupValueFunction() {
//            public Object getGroupValue(Object value, ListGridRecord record, ListGridField field, String fieldName, ListGrid grid) {
//                String transaction = (String) value;
//                    if ((transaction.equals("Patient Identity Feed")) || (transaction.equals("Register")) || (transaction.equals("Stored Query"))
//                            || (transaction.equals("Update")) || (transaction.equals("Multi-Patient Query"))) {
//                        return "Document Registry";
//                    } else {
//                        return ""; // TODO more to add
//                    }
//            }
//        });
//        grid.getField("transaction.name").setGroupingMode("transaction");
//

//       ListGridField transactionTypeField = new ListGridField("Transaction Type");
//        transactionTypeField.setWidth(150);
//        grid.setFields(transactionTypeField);

//        grid.addRecordClickHandler(new RecordClickHandler() {
//            @Override
//            public void onRecordClick(RecordClickEvent event) {
//                ListGridRecord record = grid.getSelectedRecord();
//                if (record != null) {
//                    grid.fetchRelatedData(record, ItemSupplyDS.getInstance());
//                }
//            }
//        });

        return grid;
    }



}
