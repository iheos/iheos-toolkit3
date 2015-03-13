package gov.nist.hit.ds.xdstools3.client.tabs.QRSCombinedTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.*;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableToolTab;
import gov.nist.hit.ds.xdstools3.client.tabs.QRSCombinedTab.data.QRSDataFactory;

/**
 * Created by dazais on 3/9/2015.
 */
public class QueryRetrieveSubmitCombinedTab extends GenericCloseableToolTab {
    private static final String header = "Query - Retrieve - Submit";


    public QueryRetrieveSubmitCombinedTab(){
        super(header);
    }


    @Override
    protected Widget createContents() {

        // Transaction parameters selection grids and layout
        final ListGrid profileGrid = createSelectionListGrid("Profile", "profile");
        final ListGrid fromGrid = createSelectionListGrid("From", "from");
        final ListGrid toGrid = createSelectionListGrid("To", "to");
        final ListGrid transactionGrid = createSelectionListGrid("Transaction", "transaction");

        HLayout gridLayout = new HLayout();
        gridLayout.addMembers(profileGrid, fromGrid, toGrid, transactionGrid);



        // Sections

        final SectionStack sectionStack = new SectionStack();
        sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
        sectionStack.setWidth(1000);
        sectionStack.setHeight(300);

        SectionStackSection section1 = new SectionStackSection("Select query parameters");
        section1.setExpanded(true);
        section1.setCanCollapse(true);
        section1.addItem(gridLayout);
        sectionStack.addSection(section1);

        SectionStackSection section2 = new SectionStackSection("HTML Flow");
        section2.setExpanded(true);
        section2.setCanCollapse(true);
        //section2.addItem(htmlFlow);
        sectionStack.addSection(section2);



        // SectionStack container
        HStack sections = new HStack();
        sections.addMember(sectionStack);

        //DynamicForm form = new DynamicForm();
        //form.setFields(selectItemMultipleGrid);

        VStack vStack = new VStack();
        vStack.addMember(sections);
        return vStack;
    }

    /** Creates a custom ListGrid with only one column that will be used as a
     * permanently expanded selection box.
     * @param title
     * @param fieldName Must match the record's name in the matching Data class.
     * @return
     */
    private ListGrid createSelectionListGrid(String title, String fieldName){
        ListGrid grid = new ListGrid();
        grid.setWidth("25%");
        grid.setShowAllRecords(true);
        grid.setLeaveScrollbarGap(false);
        grid.setRecords(QRSDataFactory.getRecord(fieldName));
        ListGridField field = new ListGridField(fieldName, title);
        grid.setFields(field);
        return grid;
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getQRSCombinedTabCode();
    }
}