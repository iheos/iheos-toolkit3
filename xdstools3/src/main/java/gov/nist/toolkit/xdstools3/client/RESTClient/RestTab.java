package gov.nist.toolkit.xdstools3.client.RESTClient;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.GenericCloseableTab;

/**
* Created by dazais on 5/21/2014.
*/
public class RestTab extends GenericCloseableTab {
    static String header = "header";

    public RestTab() {
        super(header);
        setContents(createLayout());
    }

        public ListGridRecord createRecord(String countryCode, String countryName) {
            ListGridRecord record = new ListGridRecord();
            record.setAttribute("country_code", countryName);
            record.setAttribute("country_name", countryCode);
            return record;
        }

    public VLayout createLayout() {

        VLayout layout = new VLayout(15);
        layout.setAutoHeight();

        //overrides here are for illustration purposes only

//        RestDataSource countryDS = new RestDataSource() {
//            @Override
//            protected Object transformRequest(DSRequest dsRequest) {
//                return super.transformRequest(dsRequest);
//            }
//            @Override
//            protected void transformResponse(DSResponse response, DSRequest request, Object data) {
//                super.transformResponse(response, request, data);
//            }
//        };
       //DataSource countryDS = new DataSource();
//        DataSourceTextField countryCode = new DataSourceTextField("country_code", "Country Code");
//        countryDS.setDataFormat(DSDataFormat.JSON);
//       countryDS.setFetchDataURL("http://localhost:8888/Xdstools3/rest/jsonServices/loadItems");

        // These lines are not required for this sample to work, but they demonstrate how you can configure RestDataSource
        // with OperationBindings in order to control settings such as whether to use the GET, POST or PUT HTTP methods,
        // and whether to send data as URL parameters vs as posted JSON or XML messages.
//        OperationBinding fetch = new OperationBinding();
//        fetch.setOperationType(DSOperationType.FETCH);
//        fetch.setDataProtocol(DSProtocol.POSTPARAMS);
//        fetch.setDataURL("http://localhost:8888/Xdstools3/rest/jsonServices/loadItems");
//        OperationBinding add = new OperationBinding();
//        add.setOperationType(DSOperationType.ADD);
//        add.setDataProtocol(DSProtocol.POSTMESSAGE);
//        add.setDataURL("http://localhost:8888/Xdstools3/rest/jsonServices/add");
//        OperationBinding update = new OperationBinding();
//        update.setOperationType(DSOperationType.UPDATE);
//        update.setDataProtocol(DSProtocol.POSTMESSAGE);
//        OperationBinding remove = new OperationBinding();
//        remove.setOperationType(DSOperationType.REMOVE);
//        remove.setDataProtocol(DSProtocol.POSTMESSAGE);
//        countryDS.setOperationBindings(fetch, add, update, remove);

//        countryCode.setPrimaryKey(true);
//        countryCode.setCanEdit(false);
//        DataSourceTextField countryName = new DataSourceTextField("country_name", "Country");
//        countryDS.setFields(countryCode, countryName);
//
//
//        final ListGrid countryGrid = new ListGrid();
//        countryGrid.setWidth(500);
//        countryGrid.setHeight(224);
//        countryGrid.setDataSource(countryDS);
//        countryGrid.setEmptyCellValue("--");
//
//
//        ListGridField codeField = new ListGridField("country_code");
//        ListGridField nameField = new ListGridField("country_name");
//        countryGrid.setFields(codeField, nameField);
//        countryGrid.setSortField(0);
//        countryGrid.setDataPageSize(50);
//        countryGrid.setAutoFetchData(true);
//
//        layout.addMember(countryGrid);
//
//        HLayout hLayout = new HLayout(15);
//
//        final IButton addButton = new IButton("Add new Country");
//        addButton.setWidth(150);
//        addButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                countryGrid.addData(createRecord("A1", "Test"));
//            }
//        });
//        hLayout.addMember(addButton);
//
//        final IButton updateButton = new IButton("Update Country (US)");
//        updateButton.setWidth(150);
//        updateButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                countryGrid.updateData(createRecord("US", "Edited Value"));
//            }
//        });
//        hLayout.addMember(updateButton);
//
//        final IButton removeButton = new IButton("Remove Country (UK)");
//        removeButton.setWidth(150);
//        removeButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                ListGridRecord record = new ListGridRecord();
//                record.setAttribute("countryCode", "UK");
//                countryGrid.removeData(record);
//            }
//        });

        RestDataSource dataSource = new EndpointDS();
        dataSource.setID("endpointDS");
        final ListGrid messageGrid = new ListGrid();
        messageGrid.setHeight(300);
        messageGrid.setWidth(500);
        messageGrid.setTitle("Messages");
        messageGrid.setDataSource(dataSource);
        messageGrid.setAutoFetchData(true);
        messageGrid.setCanEdit(true);
        messageGrid.setCanRemoveRecords(true);
        messageGrid.setListEndEditAction(RowEndEditAction.NEXT);

        ListGridField idField = new ListGridField("id", "Id", 40);
        idField.setAlign(Alignment.LEFT);
        ListGridField messageField = new ListGridField("value", "Message");
        messageGrid.setFields(idField, messageField);

        layout.addMember(messageGrid);

        HLayout hLayout = new HLayout(15);

        IButton updateButton = new IButton("Add message");
        updateButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Record message = new ListGridRecord();
                message.setAttribute("value", "...");
                messageGrid.addData(message);
            }
        });



        hLayout.addMember(updateButton);

        layout.addMember(hLayout);


return layout;
    }

}
