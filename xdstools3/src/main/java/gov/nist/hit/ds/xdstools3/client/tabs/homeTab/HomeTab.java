package gov.nist.hit.ds.xdstools3.client.tabs.homeTab;


import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.Xdstools3ActivityView;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericTab;
import gov.nist.hit.ds.xdstools3.client.util.injection.Xdstools3GinInjector;
import gov.nist.toolkit.xdstools2.client.Xdstools2;
import gov.nist.toolkit.xdstools2.client.tabs.TabLauncher;

public class HomeTab extends GenericTab {

    private static final Xdstools3GinInjector injector=Xdstools3GinInjector.injector;
    static Xdstools3ActivityView xdstools3ActivityView=injector.getXdstools3();
    static Xdstools2 xdstools2 = injector.getXdstools2();

    public HomeTab(String s) {
        super(s);
        setPane(createContents());

    }

    /**
     * Usage: homebox.addItem(String Tab_Title, boolean isUnderConstruction)
     * @return links to be displayed on the Home Tab
     */
    protected HStack createContents(){

        HomeBox queriesAndRetrieves = new HomeBox("Queries and Retrieves");
       /* queriesAndRetrieves.setIcon("icons/glyphicons/glyphicons_027_search.png");
        queriesAndRetrieves.addItem("Find Documents", true);
        queriesAndRetrieves.addItem("MPQ Find Documents", true);
        queriesAndRetrieves.addItem("Get Documents", true);
        queriesAndRetrieves.addItem("Get Related Documents", true);
        queriesAndRetrieves.addItem("Retrieve Document", true);
        queriesAndRetrieves.addItem("Find Folders", true);
        queriesAndRetrieves.addItem("Get Folders", true);
        queriesAndRetrieves.addItem("Get Folder and Contents", true);
        queriesAndRetrieves.addItem("Get Submission Set and Contents", true); */
        queriesAndRetrieves.addItem(TabLauncher.findDocumentsTabLabel, false);
        queriesAndRetrieves.addItem(TabLauncher.findDocumentsByRefIdTabLabel, false);
        queriesAndRetrieves.addItem(TabLauncher.mpqFindDocumentsTabLabel, false);
        queriesAndRetrieves.addItem(TabLauncher.getDocumentsTabLabel, false);
        queriesAndRetrieves.addItem(TabLauncher.getRelatedTabLabel, false);
        queriesAndRetrieves.addItem(TabLauncher.findFoldersTabLabel, false);
        queriesAndRetrieves.addItem(TabLauncher.getFoldersTabLabel, false);
        queriesAndRetrieves.addItem(TabLauncher.getFolderAndContentsTabLabel, false);
        queriesAndRetrieves.addItem(TabLauncher.getSubmissionSetTabLabel, false);
        queriesAndRetrieves.addItem(TabLauncher.documentRetrieveTabLabel, false);
        queriesAndRetrieves.addItem("Query - Retrieve - Submit", true);

        HomeBox tools = new HomeBox("Tools");
        tools.setIcon("icons/glyphicons/glyphicons_280_settings.png");
        tools.addItem("V2 Home", false);
        tools.addItem("V2 Dynamic Tab", false);
        tools.addItem("Message Validator", true);
        tools.addItem("Document Metadata Editor", false);
        tools.addItem("Pre-Connectathon Tests", true);
        tools.addItem("Tests Overview", true);

        HomeBox sendTestData = new HomeBox("Send Test Data");
        sendTestData.setIcon("icons/glyphicons/glyphicons_123_message_out.png");
        sendTestData.addItem("Submit Test Data", true);

        HomeBox simulators = new HomeBox("Simulators");
        simulators.setIcon("icons/glyphicons/glyphicons_086_display.png");

        HomeBox mhdTools = new HomeBox("MHD Tools");
        mhdTools.setIcon("icons/glyphicons/glyphicons_280_settings.png");
        mhdTools.addItem("MHD Validator", false);
        //mhdTools.addItem("MHD to XDS Converter", true);

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
