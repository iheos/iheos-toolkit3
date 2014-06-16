package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 *
 * Created by dazais on 5/21/2014.
 */
public class EndpointWidget extends HLayout {
    ListGrid endpointGrid;

    public EndpointWidget()
    {
        //formatting
        //setAutoHeight();
        // setmemebersmargin 15

        endpointGrid = createEndpointGrid();
        addMember(endpointGrid);
    }

    public ListGrid createEndpointGrid() {
        // Create the DataSource
        RestDataSource dataSource = new EndpointDS();

        // Create the ListGrid linked to the DataSource
        final ListGrid grid = new ListGrid();
        grid.setHeight(200);
        grid.setWidth(500);
        grid.setTitle("Endpoints");
        grid.setDataSource(dataSource);
        grid.setAutoFetchData(true);
        grid.setListEndEditAction(RowEndEditAction.NEXT);

        // Configure sorting
        grid.setSortField(1);
        grid.setSortDirection(SortDirection.DESCENDING);
        grid.setGroupStartOpen(GroupStartOpen.ALL);
        grid.setGroupByField("type");

        // Set ListGrid fields
        ListGridField typeField = new ListGridField("type", "Type");
        ListGridField nameField = new ListGridField("name", "Name");
        grid.setFields(typeField, nameField);
        return grid;
    }

//    public IButton createAddButton() {
//        // Create "Add" button
//        IButton updateButton = new IButton("Add message");
//        updateButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//                Record message = new ListGridRecord();
//                message.setAttribute("value", "...");
//                grid.addData(message);
//            }
//        });
//        return updateButton;
//    }

}
