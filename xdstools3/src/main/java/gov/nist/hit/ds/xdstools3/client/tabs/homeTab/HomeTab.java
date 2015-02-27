package gov.nist.hit.ds.xdstools3.client.tabs.homeTab;


import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericTab;

public class HomeTab extends GenericTab {

    public HomeTab(String s) {
        super(s);
        setPane(createContents());
    }

    protected HStack createContents(){

        HomeBox queriesAndRetrieves = new HomeBox("Queries and Retrieves");
        queriesAndRetrieves.setIcon("icons/glyphicons/glyphicons_027_search.png");
        queriesAndRetrieves.addItem("Find Documents", true);
        queriesAndRetrieves.addItem("MPQ Find Documents", true);
        queriesAndRetrieves.addItem("Get Documents", true);
        queriesAndRetrieves.addItem("Get Related Documents", true);
        queriesAndRetrieves.addItem("Retrieve Document", true);
        queriesAndRetrieves.addItem("Find Folders", true);
        queriesAndRetrieves.addItem("Get Folders", true);
        queriesAndRetrieves.addItem("Get Folder and Contents", true);
        queriesAndRetrieves.addItem("Get Submission Set and Contents", true);


        HomeBox tools = new HomeBox("Tools");
        tools.setIcon("icons/glyphicons/glyphicons_280_settings.png");
        tools.addItem("Message Validator", true);
        tools.addItem("Document Metadata Editor", false);
        tools.addItem("Pre-Connectathon Tests", true);

        HomeBox sendTestData = new HomeBox("Send Test Data");
        sendTestData.setIcon("icons/glyphicons/glyphicons_123_message_out.png");
        sendTestData.addItem("Submit Test Data", true);

        HomeBox simulators = new HomeBox("Simulators");
        simulators.setIcon("icons/glyphicons/glyphicons_086_display.png");

        HomeBox mhdTools = new HomeBox("MHD Tools");
        mhdTools.setIcon("icons/glyphicons/glyphicons_280_settings.png");
        mhdTools.addItem("MHD Validator", false);
        mhdTools.addItem("MHD to XDS Converter", true);

        HomeBox connectathonTools = new HomeBox("Connectathon Tools");
        connectathonTools.setIcon("icons/glyphicons/glyphicons_280_settings.png");
        // Connectathon Validations
        connectathonTools.addItem("XDS.b Doc Source Stores Document", true);
        // Registry Validations
        connectathonTools.addItem("XDS.b Registry Do This First", true);
        connectathonTools.addItem("XDS.b Lifecycle", true);
        connectathonTools.addItem("XDS.b Registry Folder Handling", true);
        // Repository Validations
        connectathonTools.addItem("XDS.b Repository Do This First", true);

        VStack vstack1 = new VStack();
        VStack vstack2 = new VStack();
        vstack1.addMembers(mhdTools, queriesAndRetrieves, connectathonTools);
        vstack2.addMembers(tools, sendTestData, simulators);

        HStack hstack = new HStack();
        hstack.addMembers(vstack1, vstack2);
        hstack.setAlign(Alignment.CENTER);
        hstack.setLayoutTopMargin(20);
        return hstack;
    }

    protected String setTabName() {
        return TabNamesManager.getInstance().getHomeTabCode();
    }


}
