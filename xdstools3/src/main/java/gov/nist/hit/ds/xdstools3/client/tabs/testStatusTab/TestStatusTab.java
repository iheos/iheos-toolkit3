package gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableToolTab;

import java.util.logging.Logger;

public class TestStatusTab extends GenericCloseableTab {
    static final Logger logger = Logger.getLogger(TestStatusTab.class.getName());


    private static String header = "Tests Overview";


    public TestStatusTab() {
        super(header);

        VStack contents = new VStack();
        contents.setWidth(1100);
        contents.addMembers(new TestStatusWidget());
        setPane(contents);
    }



    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getTestsOverviewTabCode();
    }

}
