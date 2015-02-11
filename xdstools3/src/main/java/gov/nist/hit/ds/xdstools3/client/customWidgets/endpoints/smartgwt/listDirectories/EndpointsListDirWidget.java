package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.listDirectories;

import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 *
 * Created by dazais on 5/21/2014.
 */
public class EndpointsListDirWidget extends HLayout {
    private ListGrid endpointGrid;
    private boolean endpointValueSelected = false;

    public EndpointsListDirWidget() {
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
        final ListGrid grid = new ListGrid() {

            //TODO may need to call super for default behavior. This should move to the nested grid.
            @Override
            public boolean canEditCell(int rowNum, int colNum) {
                ListGridRecord record = this.getRecord(rowNum);
                String colName = this.getFieldName(colNum);

                if (colName == "Non-TLS Endpoints" && (record.getAttribute("Transaction Type") == "repositoryUniqueID")
                        || (record.getAttribute("Transaction Type") == "homeCommunityId")) return false;
                else return true;
            }
        };
        grid.setHeight100();
        grid.setWidth(800);
        grid.setAlternateRecordStyles(true);
       // grid.setDataSource(EndpointListDirDS.getInstance());
       // grid.setAutoFetchData(true);
       // grid.setDrawAheadRatio(4);
        grid.setCanExpandRecords(true);
        grid.setCanExpandMultipleRecords(true);
       //  grid.setExpansionMode(ExpansionMode.RELATED);
      //  grid.setDetailDS(TransactionDS.getInstance());
        grid.setCanEdit(true);
        grid.setModalEditing(true);
        grid.setEditEvent(ListGridEditEvent.CLICK);
        grid.setListEndEditAction(RowEndEditAction.NEXT);
        grid.setAutoSaveEdits(false);

        // load filenames
//        List<String> results = new ArrayList<String>();
//        File[] files = new File("/resources/datasources/endpointsTestDir").listFiles();
//
//        for (File file : files) {
//            if (file.isFile()) {
//                results.add(file.getName());
//            }
//        }

        ListGridField field = new ListGridField("Endpoint Name");
        grid.setFields(field);



        draw();

        return grid;
    }



    }
