package gov.nist.toolkit.xdstools3.client.tabs;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.select.EndpointDS;

public class MessageValidatorTab extends GenericCloseableTab {
    String title = "Message Validator";


    public MessageValidatorTab() {

        // Teb name
        super("Message Validator");

        // Set title and subtitles
        setHeader(title);

        createEndpointGrid();

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


}