package gov.nist.toolkit.xdstools3.client.customWidgets.healthcareCodes;

import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Created by dazais on 5/15/2014.
 */
public class HealthcareCodeWidget extends VStack {
    private ListGrid codesGrid;
    private ListGrid selectedCodesGrid;


    public HealthcareCodeWidget() {

        // Main list of codes and the summary of codes selected by the user
        createCodesGrids();

       // LayoutSpacer spacer = new LayoutSpacer();
       // spacer.setWidth("*");

        // Add both grids to the HLayout and set formatting
        addMembers(selectedCodesGrid, codesGrid);
        selectedCodesGrid.setAlign(Alignment.CENTER);
        setAlign(Alignment.CENTER);

    }

    /**
     * Builds the main list of codes and the summary of the codes currently selected by the user
     * @see ListGrid in SmartGWT documentation
     */
    public void createCodesGrids() {
        // Create the DataSource
        RestDataSource ds = new HealthcareCodeDS();


        // Create the ListGrid linked to the DataSource + additional list of codes selected by the user
        codesGrid = new ListGrid();
        selectedCodesGrid = new ListGrid();

        // formatting for all codes
        codesGrid.setWidth(500);
        codesGrid.setHeight(224);
        codesGrid.setShowAllRecords(true);
        codesGrid.setSelectionType(SelectionStyle.MULTIPLE);
        codesGrid.setDataSource(ds);
        codesGrid.setAutoFetchData(true);

        // Formatting for selected codes
        selectedCodesGrid.setWidth(250);
        selectedCodesGrid.setHeight(100);
        selectedCodesGrid.setShowAllRecords(true);

        // Set Code fields
        ListGridField codeField = new ListGridField("code", "Code");
        ListGridField descrField = new ListGridField("description", "Description");
        codeField.setWidth(100);
        codesGrid.setFields(codeField, descrField);

        // Set Selected Codes fields
        ListGridField selectedCodesField = new ListGridField("code", "Selected Codes"); // TODO to change
        selectedCodesGrid.setFields(selectedCodesField);

        // Configure sorting
        codesGrid.setSortField(1);
        codesGrid.setSortDirection(SortDirection.DESCENDING);

        // listeners



}

}
