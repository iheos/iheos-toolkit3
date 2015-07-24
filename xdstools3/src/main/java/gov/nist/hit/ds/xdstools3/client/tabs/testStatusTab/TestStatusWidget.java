package gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.ListGridComponent;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import gov.nist.hit.ds.xdstools3.client.resources.Resources;
import gov.nist.hit.ds.xdstools3.client.tabs.QRSCombinedTab.data.QRSDataFactory;

/**
 * Created by Diane Azais local on 7/23/2015.
 */
public class TestStatusWidget extends SectionStack {

    public TestStatusWidget(){

        setWidth100();
        setHeight(230);

        // Tests statistics header bar
        String title = "<b>Tests Run: 15/26" + "&nbsp&nbsp&nbsp&nbsp" + "Failed: " + "4/26" + "&nbsp&nbsp&nbsp&nbsp" + "Passed: 11/26 (42%)</b>";
        SectionStackSection statsSection = new SectionStackSection(title);
        statsSection.setCanCollapse(false);
        statsSection.setExpanded(true);

        // Run - Reset - Reload Button bar
        ImgButton deleteButton = createLargeIconButton(Resources.INSTANCE.getRemoveIcon().getSafeUri().asString(), "Delete all test results");
        ImgButton reloadButton = createLargeIconButton(Resources.INSTANCE.getRefreshIcon().getSafeUri().asString(), "Reload results");
        ImgButton runButton = createLargeIconButton(Resources.INSTANCE.getPlayIcon().getSafeUri().asString(), "Run all tests");

        SectionStackSection buttonSection = new SectionStackSection("");
        buttonSection.setControls(runButton, reloadButton, deleteButton);
        buttonSection.setCanCollapse(false);
        buttonSection.setExpanded(true);

        // Create main tests grid
        ListGrid grid = new ListGrid();
        grid.setShowAllRecords(true);
        grid.setLeaveScrollbarGap(false);
        ListGridField testNumber = new ListGridField("testNumber", "Test Number");
        testNumber.setWidth(100);
        ListGridField testDescription = new ListGridField("testDescription", "Description");
        ListGridField time = new ListGridField("time", "Time");
        time.setWidth(100);
        ListGridField testStatus = new ListGridField("testStatus", "Status");
        testStatus.setWidth(70);
        grid.setFields(testNumber, testDescription, time, testStatus);

        // Add components
        buttonSection.setItems(grid);
        setSections(statsSection, buttonSection);

        // Prepare components for display
        grid.draw();
    }

    private ImgButton createLargeIconButton(String iconSrc, String tooltip){
        ImgButton button = new ImgButton();
        button.setWidth(24);
        button.setHeight(24);
        button.setMargin(2);
        button.setShowRollOver(false);
        button.setShowDown(true);
        button.setTooltip(tooltip);
        button.setSrc(iconSrc);
        return button;
    }

}
