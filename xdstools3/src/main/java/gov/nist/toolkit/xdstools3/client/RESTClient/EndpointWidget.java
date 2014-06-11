package gov.nist.toolkit.xdstools3.client.RESTClient;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.CloseableTabWidget;

/**
 *
 * Created by dazais on 5/21/2014.
 */
public class EndpointWidget extends CloseableTabWidget {
    static String header = "header";

    public EndpointWidget() {
        super(header);
        setContents(createLayout());
    }

    public VLayout createLayout() {

        VLayout layout = new VLayout(15);
        layout.setAutoHeight();

        // Create the DataSource
        RestDataSource dataSource = new EndpointDS();

        // Create the ListGrid linked to the DataSource
        final ListGrid grid = new ListGrid();
        grid.setHeight(300);
        grid.setWidth(500);
        grid.setTitle("Messages");
        grid.setDataSource(dataSource);
        grid.setAutoFetchData(true);
        grid.setCanEdit(true);
        grid.setCanRemoveRecords(true);
        grid.setListEndEditAction(RowEndEditAction.NEXT);

        ListGridField idField = new ListGridField("id", "Id", 40);
        idField.setAlign(Alignment.LEFT);
        ListGridField messageField = new ListGridField("value", "Message");
        grid.setFields(idField, messageField);

        layout.addMember(grid);

        IButton updateButton = new IButton("Add message");
        updateButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
            public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
                Record message = new ListGridRecord();
                message.setAttribute("value", "...");
                grid.addData(message);
            }
        });

        HLayout hLayout = new HLayout(15);
        hLayout.addMember(updateButton);
        layout.addMember(hLayout);

        return layout;
    }
}
