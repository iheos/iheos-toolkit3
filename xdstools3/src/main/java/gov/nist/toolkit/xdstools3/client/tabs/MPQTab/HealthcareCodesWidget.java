package gov.nist.toolkit.xdstools3.client.tabs.MPQTab;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Created by dazais on 5/15/2014.
 */
public class HealthcareCodesWidget extends VStack {
    private ListGrid codesGrid;
    private ListGrid selectedCodesGrid;


    public HealthcareCodesWidget() {

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
        codesGrid = new ListGrid();
        selectedCodesGrid = new ListGrid();

        // create the grid fields
        ListGridField selectedCountriesField = new ListGridField("code", "Selected Codes");
        selectedCodesGrid.setFields(selectedCountriesField);

        ListGridField codesField = new ListGridField("code", "Code Values");
       // ListGridField codeTypeField = new ListGridField("codeType", "Selected Codes"); // the Type of Code is only useful for sorting
        codesGrid.setFields(codesField);
        codesGrid.setGroupStartOpen(GroupStartOpen.ALL);
        codesGrid.setGroupByField("code");

        // formatting
        codesGrid.setWidth(500);
        codesGrid.setHeight(224);
        codesGrid.setShowAllRecords(true);
        codesGrid.setSelectionType(SelectionStyle.MULTIPLE);
        selectedCodesGrid.setWidth(250);
        selectedCodesGrid.setHeight(100);
        selectedCodesGrid.setShowAllRecords(true);

}

}
