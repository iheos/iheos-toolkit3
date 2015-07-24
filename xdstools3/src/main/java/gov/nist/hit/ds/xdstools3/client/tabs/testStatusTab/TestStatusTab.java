package gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableToolTab;

import java.util.logging.Logger;

public class TestStatusTab extends GenericCloseableToolTab {
    static final Logger logger = Logger.getLogger(TestStatusTab.class.getName());
    //private final static PreConnectathonTabServiceAsync toolkitService = GWT
    //        .create(PreConnectathonTabService.class);


    private static String header = "Tests Overview";


    public TestStatusTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack = new VStack();
        vStack.setWidth(1100);

        vStack.addMembers(new TestStatusWidget());
        return vStack;
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getTestsOverviewTabCode();
    }

}
