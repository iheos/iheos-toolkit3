package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

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

import java.util.HashMap;

/**
 * Creates grids to display and edit the Sites.
 * @see com.smartgwt.client.widgets.grid.ListGrid
 * @see SmartGWT nested ListGrid mechanism
 * @see EndpointsConfigWidget
 *
 *
 * Created by dazais on 5/21/2014.
 */
@SuppressWarnings("JavadocReference")
public class EndpointsConfigWidget extends HLayout {
    private ListGrid endpointGrid;

    public EndpointsConfigWidget() {
        endpointGrid = createEndpointGrid();
        addMember(endpointGrid);
    }

    //TODO: Save to file, Delete
    public ListGrid createEndpointGrid() {

        // Create the ListGrid linked to the DataSource
        final ListGrid grid = new ListGrid(){

            //TODO not sure what this is
            //@Override
/*            public boolean canEditCell(int rowNum, int colNum) {
                ListGridRecord record = this.getRecord(rowNum);
                String colName = this.getFieldName(colNum);

                if (colName == "Non-TLS Endpoints" && (record.getAttribute("Transaction Type") == "repositoryUniqueID")
                    || (record.getAttribute("Transaction Type") == "homeCommunityId")) return false;
                else return true;
            }*/

            @Override
            protected Canvas getExpansionComponent(final ListGridRecord record) {

                // create the nested grids
                final TransactionGrid nestedGrid = new TransactionGrid();
                nestedGrid.fetchRelatedData(record, EndpointConfigDS.getInstance());


                // Layout
                VLayout layout = new VLayout(5);
                layout.setPadding(5);
                layout.addMember(nestedGrid);
                layout.setAlign(Alignment.CENTER);

                HLayout hLayout = new HLayout(10);
                hLayout.setAlign(Alignment.CENTER);


                // Buttons
                IButton saveButton = new IButton("Save");
                saveButton.setTop(250);
                saveButton.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        // FIXME this dooesnt work
                        nestedGrid.saveAllEdits();
                        //nestedGrid.exportAsXML();
                    }
                });
                hLayout.addMember(saveButton);

                IButton newButton = new IButton("Add");
                newButton.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        String currentSiteName = endpointGrid.getRecord(endpointGrid.getFocusRow()).getAttribute("endpointName");
                       // nestedGrid.addData(createListGridRecord(currentSiteName, "actorCode", "actorType", "reg.b", "transactionName", "tls", "notls", "repositoryUniqueID"));
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("siteName", currentSiteName);
                        nestedGrid.startEditingNew(map);
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

        // More formatting
        grid.setHeight100();
        grid.setMinWidth(800);
        grid.setWidth100();
        grid.setAlternateRecordStyles(true);
        grid.setDataSource(EndpointConfigDS.getInstance());
        grid.setAutoFetchData(true);
        grid.setCanExpandRecords(true);
        grid.setCanExpandMultipleRecords(true);
        grid.setCanEdit(true);
        grid.setModalEditing(true);
        grid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
        grid.setListEndEditAction(RowEndEditAction.NEXT);
        grid.setAutoSaveEdits(false);

        return grid;
    }

}
