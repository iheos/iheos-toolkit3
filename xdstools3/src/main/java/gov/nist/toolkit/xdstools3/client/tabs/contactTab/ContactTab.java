package gov.nist.toolkit.xdstools3.client.tabs.contactTab;


import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.design.Formatter;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;
import gov.nist.toolkit.xdstools3.client.resources.Resources;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;

public class ContactTab extends GenericCloseableTab {
	static String header = "Contact us";

	public ContactTab() {
		super(header);

        VStack mainStack = new VStack();
        Label tabHeader = Formatter.createTabHeaderWithoutLeftPadding(header);
        mainStack.addMembers(tabHeader, createHTMLPanel());
        mainStack.setLayoutLeftMargin(50); mainStack.setLayoutRightMargin(50);
        mainStack.setLayoutTopMargin(20); mainStack.setLayoutBottomMargin(20);
        setPane(mainStack);
	}

    /**
     * Load contents and create HTML panel
     * @return the panel that contains the instructions to the user on the points of contact
     * */
	protected HTMLFlow createHTMLPanel(){
        HTMLFlow htmlFlow = new HTMLFlow();
        htmlFlow.setContents(Resources.INSTANCE.getContactPageContents().getText());
        return htmlFlow;
	}


    /**
     * Creates a panel that contains the points of contact displayed as a table
     * @return
     */
   //TODO this was a test and should ultimately be reintegrated or removed. It does not work as is.
    //TODO I am keeping it until I figure out what the best solution is. -Diane
    /*
    protected HStack createContactPanel(){
        HStack hstack = new HStack();
        ListGrid grid = new ListGrid();
        ListGridField questionTypeField = new ListGridField("questionType", "For questions related to...");
        ListGridField contactField = new ListGridField("contact", "Contact");
        grid.setCanSelectCells(false);

        // create data
        HashMap<String, String> questionTypeMap = new HashMap<String, String>();
        questionTypeMap.put("1","The testing process \nThe XDS specification");
        questionTypeField.setValueMap(questionTypeMap);
        HashMap<String, String> contactMap = new HashMap<String, String>();
        contactMap.put("1","The testing process \nThe XDS specification");
        contactField.setValueMap(contactMap);
        grid.setFields(questionTypeField, contactField);
        grid.setWidth(500);

        // add to hstack
        hstack.addMember(grid);
        return hstack;
    }*/

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getContactTabCode();
    }


}
