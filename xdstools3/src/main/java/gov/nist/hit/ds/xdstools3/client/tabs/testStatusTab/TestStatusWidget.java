package gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.*;
import gov.nist.hit.ds.xdstools3.client.exceptions.NoServletSessionException;
import gov.nist.hit.ds.xdstools3.client.resources.Resources;
import gov.nist.hit.ds.xdstools3.client.tabs.submitTestDataTab.TestDataSubmissionServices;
import gov.nist.hit.ds.xdstools3.client.tabs.submitTestDataTab.TestDataSubmissionServicesAsync;
import gov.nist.toolkit.results.client.Result;

import java.util.Map;


/**
 * Created by Diane Azais local on 7/23/2015.
 */
public class TestStatusWidget extends VStack {

    // RPC Services declaration
    private final static TestStatusTabServiceAsync testStatusTabService = GWT.create(TestStatusTabService.class);


    public TestStatusWidget(){
        setWidth100();
        setHeight100();

        SectionStack testStatusStack = new SectionStack();
        testStatusStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        testStatusStack.setWidth100();
        testStatusStack.setHeight(300);

        // Tests statistics header bar
        String tab = "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"; // This is a workaround for GWT not accepting tabulations
        String title = "<b><font color='#4d4d4d'>Tests Run: <font color='blue'>15</font> / 26" + tab + "Failed: " +
                "<font color='red'>4</font> / 26" + tab + "Passed: <font color='green'>11</font> / 26</b></font>";
        SectionStackSection statsSection = new SectionStackSection(title);
        statsSection.setCanCollapse(false);
        statsSection.setExpanded(true);

        // Run - Reset - Reload Button bar
        ImgButton deleteButton = createLargeIconButton(Resources.INSTANCE.getRemoveIcon().getSafeUri().asString(), "Delete all test results");
        ImgButton reloadButton = createLargeIconButton(Resources.INSTANCE.getRefreshIcon().getSafeUri().asString(), "Reload results");
        ImgButton runButton = createLargeIconButton(Resources.INSTANCE.getPlayIcon().getSafeUri().asString(), "Run all tests (overwrites existing results)");

        SectionStackSection buttonSection = new SectionStackSection("");
        buttonSection.setControls(runButton, reloadButton, deleteButton);
        buttonSection.setCanCollapse(false);
        buttonSection.setExpanded(true);

        // Create main tests grid
        ListGrid grid = new ListGrid(){

            // This section creates the commands widget to be added to each test row
            @Override
            protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {

                String fieldName = this.getFieldName(colNum);

                if (fieldName.equals("commands")) {
                    HLayout commandsCanvas = new HLayout(3);
                    commandsCanvas.setHeight(22);
                    commandsCanvas.setWidth100();
                    commandsCanvas.setMembersMargin(5);
                    commandsCanvas.setAlign(Alignment.LEFT);


                    ImgButton runImg = createSmallIcon(Resources.INSTANCE.getPlayIcon().getSafeUri().asString(), "Run test (overwrites existing results)");

                    ImgButton deleteImg = createSmallIcon(Resources.INSTANCE.getRemoveIcon().getSafeUri().asString(), "Delete test results");

                    IButton testPlanButton =  createSmallButton("Test Plan", "Display the test plan in a new tab", 60);
                    testPlanButton.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent clickEvent) {
                            Window.open("", "", "");
                        }
                    });
                    IButton logButton =  createSmallButton("Log", "Display the log file in a new tab ", 40);
                    logButton.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent clickEvent) {
                            Window.open("", "", "");
                        }
                    });
                    IButton testDescrButton =  createSmallButton("Test Description", "Display the full test description in a new tab ", 100);
                    testDescrButton.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent clickEvent) {
                            Window.open("", "", "");
                        }
                    });
                    commandsCanvas.addMembers(runImg, deleteImg, testPlanButton, logButton, testDescrButton);
                    return commandsCanvas;

                } else if (fieldName.equals("testStatus")) {
                    ImgButton statusButton = createLargeIconButton(Resources.INSTANCE.getBlueRoundIcon().getSafeUri().asString(), "Partially run");
                    statusButton.setWidth(20);
                    statusButton.setHeight(20);
                    return statusButton;

                } else { return null; }
            }

            @Override
            protected Canvas getExpansionComponent(final ListGridRecord record) {

                final ListGrid grid = this;

                VLayout expComponent = new VLayout(5);
                expComponent.setPadding(5);

                final ListGrid testSectionGrid = new ListGrid(){
                    @Override
                    protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {

                        String fieldName = this.getFieldName(colNum);

                        if (fieldName.equals("commands2")) {
                            HLayout commandsCanvas = new HLayout(3);
                            commandsCanvas.setHeight(22);
                            commandsCanvas.setWidth100();
                            commandsCanvas.setMembersMargin(5);
                            commandsCanvas.setAlign(Alignment.LEFT);


                            ImgButton runImg = createSmallIcon(Resources.INSTANCE.getPlayIcon().getSafeUri().asString(), "Run test section (overwrites existing results)");
                            ImgButton deleteImg = createSmallIcon(Resources.INSTANCE.getRemoveIcon().getSafeUri().asString(), "Delete results for this test section");
                            IButton testPlanButton =  createSmallButton("Test Plan", "Display the test plan in a new tab", 60);
                            testPlanButton.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent clickEvent) {
                                    Window.open("", "", "");
                                }
                            });
                            IButton logButton =  createSmallButton("Log", "Display the log file in a new tab ", 40);
                            logButton.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent clickEvent) {
                                    Window.open("", "", "");
                                }
                            });
                            commandsCanvas.addMembers(runImg, deleteImg, testPlanButton, logButton);
                            return commandsCanvas;

                        } else if (fieldName.equals("testStatus2")) {
                            ImgButton statusButton = createLargeIconButton(Resources.INSTANCE.getBlueRoundIcon().getSafeUri().asString(), "Partially run");
                            statusButton.setWidth(20);
                            statusButton.setHeight(20);
                            return statusButton;

                        } else { return null; }
                    }
                };
                testSectionGrid.setWidth100();
                testSectionGrid.setHeight(100);
                testSectionGrid.setShowRecordComponents(true);
                testSectionGrid.setShowRecordComponentsByCell(true);

                ListGridField testReference = new ListGridField("testReference2", "Test ID");
                testReference.setWidth(70);
                ListGridField testDescription = new ListGridField("testDescription2", "Description");
                ListGridField commands = new ListGridField("commands2", "Commands");
                commands.setWidth(280);
                ListGridField time = new ListGridField("time2", "Time");
                time.setWidth(90);
                ListGridField testStatus = new ListGridField("testStatus2", "Status");
                testStatus.setWidth(40);
                testStatus.setAlign(Alignment.CENTER);
                ListGridField sectionNumber = new ListGridField("sectionNumber2", "Section Number");
                sectionNumber.setHidden(true);
                ListGridField testNumber = new ListGridField("testNumber2", "Test Number");
                testNumber.setHidden(true);
                testSectionGrid.setFields(testReference, testDescription, commands, time, testStatus, sectionNumber, testNumber);

                FakeData bogusDataGenerator = new FakeData();
                if (record.getAttributeAsString("testNumber").equals("11012")) {
                    testSectionGrid.addData(bogusDataGenerator.createSubRecord("11012", "a"));
                    testSectionGrid.addData(bogusDataGenerator.createSubRecord("11012", "b"));
                }

                expComponent.addMember(testSectionGrid);
                return expComponent;
            }

            };

        // Define characteristics of main grid
        grid.setShowAllRecords(true);
        grid.setLeaveScrollbarGap(false);
        grid.setShowRecordComponents(true);
        grid.setShowRecordComponentsByCell(true);
        grid.setCanExpandRecords(true);
        ListGridField testNumber = new ListGridField("testNumber", "Test ID");
        testNumber.setWidth(70);
        ListGridField testDescription = new ListGridField("testDescription", "Description");
        ListGridField commands = new ListGridField("commands", "Commands");
        commands.setWidth(280);
        ListGridField time = new ListGridField("time", "Time");
        time.setWidth(90);
        ListGridField testStatus = new ListGridField("testStatus", "Status");
        testStatus.setWidth(40);
        testStatus.setAlign(Alignment.CENTER);
        ListGridField sectionNumber = new ListGridField("sectionNumber", "Section Number");
        sectionNumber.setHidden(true);
        ListGridField testReference = new ListGridField("testReference", "Test Number");
        testReference.setHidden(true);

        grid.setFields(testNumber, testDescription, commands, time, testStatus, sectionNumber, testReference);

        // Add components
        buttonSection.addItem(grid);
        testStatusStack.setSections(statsSection, buttonSection);
        addMember(testStatusStack);
        grid.draw();

        // Populate the grid
       // FakeData bogusDataGenerator = new FakeData();
       // grid.addData(bogusDataGenerator.createRecord("11011", ""));
        //grid.addData(bogusDataGenerator.createRecord("11012", ""));

        try {
            testStatusTabService.retrieveAllTests(new AsyncCallback<Map<String, Result>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(Map<String, Result> testTestCollection) {

                }
            });
        } catch (NoServletSessionException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Creates large clickable icons to use in the commands in header bar, applicable to all tests
     * @param iconSrc  Source of the icon, use GWT Resources to obtain
     * @param tooltip Tooltip to display
     * @return a clickable icon button 24x24
     */
    private ImgButton createLargeIconButton(String iconSrc, String tooltip){
        ImgButton button = new ImgButton();
        button.setLayoutAlign(Alignment.CENTER);
        button.setWidth(24);
        button.setHeight(24);
        button.setMargin(2);
        button.setShowRollOver(false);
        button.setShowDown(true);
        button.setTooltip(tooltip);
        button.setSrc(iconSrc);
        return button;
    }

    /**
     * Creates a small clickable icon to use in the commands row for each test
     * @param iconSrc Source of the icon, use GWT Resources to obtain
     * @param tooltip Tooltip to display
     * @return a clickable icon button 16x16
     */
    private ImgButton createSmallIcon(String iconSrc, String tooltip){
        ImgButton icon = new ImgButton();
        icon.setLayoutAlign(Alignment.CENTER);
        icon.setWidth(16);
        icon.setHeight(16);
        icon.setShowRollOver(false);
        icon.setShowDown(true);
        icon.setTooltip(tooltip);
        icon.setSrc(iconSrc);
        return icon;
    }

    private IButton createSmallButton(String title, String tooltip, int width) {
        IButton button = new IButton();
        button.setLayoutAlign(Alignment.CENTER);
        button.setHeight(16);
        button.setWidth(width);
        button.setShowRollOver(false);
        button.setShowDown(true);
        button.setTooltip(tooltip);
        button.setTitle(title);
        return button;
    }

    }
