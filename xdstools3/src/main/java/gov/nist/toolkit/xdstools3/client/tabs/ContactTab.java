package gov.nist.toolkit.xdstools3.client.tabs;


import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;
import gov.nist.toolkit.xdstools3.client.resources.Resources;

public class ContactTab extends GenericCloseableTab {
	static String header = "Contact us";

	public ContactTab() {
		super(header);
	}

	protected VStack createContents(){
        // create containers
        VStack vstack = new VStack();
        HTMLPane panel = new HTMLPane();

        // load page contents from file
        panel.setContents(Resources.INSTANCE.getContactPageContents().getText());

        vstack.addMember(panel);
        return vstack ;
	}

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getContactTabCode();
    }


}
