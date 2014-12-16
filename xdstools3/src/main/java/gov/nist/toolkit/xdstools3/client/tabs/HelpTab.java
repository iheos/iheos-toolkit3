package gov.nist.toolkit.xdstools3.client.tabs;


import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;

public class HelpTab extends GenericCloseableTab {
	static String header = "Help";

	public HelpTab() {
		super(header);
	}

	protected VStack createContents(){
        VStack vstack = new VStack();
        HTMLPane panel = new HTMLPane();
        vstack.addMember(panel);
        return vstack ;
	}

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getHelpTabCode();
    }


}
