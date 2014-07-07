package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.configure;

import com.smartgwt.client.types.ExpansionMode;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;

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

        // Create the ListGrid linked to the DataSource
        final ListGrid grid = new ListGrid();
//        {
//            /**
//             * Creates the sub-Listgrid in each expanded ListGridRecord
//             * @param record any record
//             * @return a Canvas containing the detail that matches the expanded record
//             */
//            @Override
//            protected Canvas getExpansionComponent(ListGridRecord record) {
//                Canvas c = new Canvas();
//                c.setContents(record.getAttribute("expandedContent"));
//                return c;
//            }
//
//        };
        grid.setHeight(500);
        grid.setWidth(800);
        grid.setTitle("Configure Endpoints");
        grid.setDataSource(EndpointConfigDS.getInstance());
        grid.setAutoFetchData(true);
        grid.setDrawAheadRatio(4);
        grid.setCanExpandRecords(true);
        grid.setCanExpandMultipleRecords(true);
        grid.setExpansionMode(ExpansionMode.RELATED);
        grid.setDetailDS(ItemSupplyDS.getInstance());
        grid.setCanEdit(true);
        grid.setModalEditing(true);
        grid.setEditEvent(ListGridEditEvent.CLICK);
        grid.setListEndEditAction(RowEndEditAction.NEXT);
        grid.setAutoSaveEdits(false);


        ListGridField itemNameField = new ListGridField("categoryName");
        grid.setFields(itemNameField);

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
