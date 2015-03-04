package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VStack;

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
public class EndpointsConfigWidget extends VStack {
    private ListGrid endpointGrid;
    private HLayout buttonBar;

    public EndpointsConfigWidget() {
        endpointGrid = createEndpointGrid();
        buttonBar = createButtonBar();
        addMembers(endpointGrid, buttonBar);
    }

    //TODO: Save to file, Delete
    private ListGrid createEndpointGrid() {

        // Create the ListGrid linked to the DataSource
        final ListGrid grid = new ListGrid(){

            @Override
            protected Canvas getExpansionComponent(final ListGridRecord record) {

                // create the nested forms
                final TransactionWidget nestedForm = new TransactionWidget();

               // nestedForm.fetchRelatedData(record, EndpointConfigDS.getInstance());

                return nestedForm;

            }
        };

        // More formatting
        grid.setMinWidth(800);
        grid.setWidth100();
        grid.setShowAllRecords(true);
        grid.setBodyOverflow(Overflow.VISIBLE);
        grid.setOverflow(Overflow.VISIBLE);
        grid.setLeaveScrollbarGap(false);
        grid.setDataSource(EndpointConfigDS.getInstance());
        grid.setAutoFetchData(true);
        grid.setCanExpandRecords(true);
        grid.setCanExpandMultipleRecords(true);

        return grid;
    }

    /**
     * Creates the button bar for Save, Add, etc.
     * @return
     */
    private HLayout createButtonBar(){
        HLayout buttonsLayout = new HLayout(10);
        buttonsLayout.setAlign(Alignment.CENTER);
        buttonsLayout.setLayoutMargin(10);

        IButton saveButton = new IButton("Save all");
        saveButton.setTop(250);
        saveButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                // FIXME this dooesnt work
                //nestedGrid.saveAllEdits();
                //nestedGrid.exportAsXML();
            }
        });
        buttonsLayout.addMember(saveButton);

        IButton newButton = new IButton("Add site");
        newButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                //TODO add a site
            }
        });
        buttonsLayout.addMember(newButton);

        IButton discardButton = new IButton("Discard changes");
        discardButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                //nestedGrid.discardAllEdits();
            }
        });
        buttonsLayout.addMember(discardButton);


        return buttonsLayout;
    }

}
