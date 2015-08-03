package gov.nist.hit.ds.xdstools3.client.tabs.testStatusTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
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

        // create a VStack to store the entire contents of the tab
        VStack contents = new VStack();
        contents.setWidth(1185);

        // create a stack with collapsible sections and populate it with the different widgets
        /*
        final SectionStack tabSections = new SectionStack();
        tabSections.setVisibilityMode(VisibilityMode.MULTIPLE);

        SectionStackSection sitesSection = new SectionStackSection("Sites");
        sitesSection.setExpanded(true);
        sitesSection.setCanCollapse(true);

        SectionStackSection testCategoriesSection = new SectionStackSection("Test Categories");
        testCategoriesSection.setExpanded(true);
        testCategoriesSection.setCanCollapse(true);

        SectionStackSection testStatusSection = new SectionStackSection("Test Status");
        testStatusSection.setExpanded(true);
        testStatusSection.setCanCollapse(true);
        testStatusSection.addItem(new TestStatusWidget());

        // Add the SectionsStack to the contents panel
        tabSections.setSections(sitesSection, testCategoriesSection, new TestStatusWidget());
        contents.addMember(tabSections);
        */

        contents.addMember(new TestStatusWidget());
        setPane(contents);
    }



    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getTestsOverviewTabCode();
    }

}
