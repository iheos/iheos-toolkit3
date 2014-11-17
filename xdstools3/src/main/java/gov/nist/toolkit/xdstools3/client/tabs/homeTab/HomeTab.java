package gov.nist.toolkit.xdstools3.client.tabs.homeTab;


import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.tabs.GenericTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

public class HomeTab extends GenericTab {

    public HomeTab(String s) {
        super(s);
        hideHeaderTitle();
    }

    @Override
    protected HStack createContents(){
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
        sendTestData.addItem("Submit Test Data");

        HomeBox simulators = new HomeBox("Simulators");
        simulators.setIcon("icons/glyphicons/glyphicons_086_display.png");

        HomeBox mhdTools = new HomeBox("MHD Tools");
        mhdTools.setIcon("icons/glyphicons/glyphicons_280_settings.png");
        // Mhd Validations
        mhdTools.addItem("MHD Validator");
        mhdTools.addItem("MHD to XDS Converter");

        HomeBox connectathonTools = new HomeBox("Connectathon Tools");
        connectathonTools.setIcon("icons/glyphicons/glyphicons_280_settings.png");
        // Connectathon Validations
        connectathonTools.addItem("XDS.b Doc Source Stores Document");
        // Registry Validations
        connectathonTools.addItem("XDS.b Registry Do This First");
        connectathonTools.addItem("XDS.b Lifecycle");
        connectathonTools.addItem("XDS.b Registry Folder Handling");
        // Repository Validations
        connectathonTools.addItem("XDS.b Repository Do This First");

        VStack hstack1 = new VStack();
        VStack hstack2 = new VStack();
        hstack1.addMembers(queriesAndRetrieves,sendTestData,mhdTools);
        hstack2.addMembers(tools, simulators,connectathonTools);

        HStack vstack = new HStack();
        LayoutSpacer spacer = new LayoutSpacer();
        spacer.setHeight(30);
        vstack.addMembers(hstack1, hstack2);
        return vstack;
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getInstance().getHomeTabCode();
    }


}
