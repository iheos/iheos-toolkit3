package gov.nist.toolkit.xdstools3.client.tabs.homeTab;


import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.tabs.GenericTab;

public class HomeTab extends GenericTab {

    public HomeTab(String s) {
        super(s);
    }

    @Override
    protected VStack createContents(){
        HomeBox queriesAndRetrieves = new HomeBox("Queries and Retrieves");
        queriesAndRetrieves.setIcon("icons/glyphicons/glyphicons_027_search.png");
        queriesAndRetrieves.addItem("Find Documents");
        queriesAndRetrieves.addItem("MPQ Find Documents");
        queriesAndRetrieves.addItem("Get Documents");
        queriesAndRetrieves.addItem("Get Related Documents");
        queriesAndRetrieves.addItem("Retrieve Document");
        queriesAndRetrieves.addItem("Find Folders");
        queriesAndRetrieves.addItem("Get Folders");
        queriesAndRetrieves.addItem("Get Folder and Contents");
        queriesAndRetrieves.addItem("Get Submission Set and Contents");

        HomeBox tools = new HomeBox("Tools");
        tools.setIcon("icons/glyphicons/glyphicons_280_settings.png");
        tools.addItem("Message Validator");
        tools.addItem("Document Metadata Editor");
        tools.addItem("Pre-Connectathon Tests");
        tools.addItem("v2 Tab Example");

        HomeBox sendTestData = new HomeBox("Send Test Data");
        sendTestData.setIcon("icons/glyphicons/glyphicons_123_message_out.png");

        HomeBox simulators = new HomeBox("Simulators");
        simulators.setIcon("icons/glyphicons/glyphicons_086_display.png");

        HStack hstack1 = new HStack();
        HStack hstack2 = new HStack();
        hstack1.addMembers(queriesAndRetrieves, new LayoutSpacer(), tools);
        hstack2.addMembers(sendTestData, new LayoutSpacer(), simulators);
        VStack vstack = new VStack();
        LayoutSpacer spacer = new LayoutSpacer();
        spacer.setHeight(30);
        vstack.addMembers(hstack1, spacer, hstack2);
        return vstack;
    }


}
