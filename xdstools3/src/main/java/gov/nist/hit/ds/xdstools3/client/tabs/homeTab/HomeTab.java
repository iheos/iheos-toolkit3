package gov.nist.hit.ds.xdstools3.client.tabs.homeTab;


import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.Xdstools3ActivityView;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.resources.Resources;
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

        HomeBox preConnectathonTools = new HomeBox("Pre-Connectathon Tools");
        preConnectathonTools.setIcon(Resources.INSTANCE.getSettingsIcon().getSafeUri().asString());
        preConnectathonTools.addItem("Pre-Connectathon Tests", 3, true);

        HomeBox connectathonTools = new HomeBox("Connectathon Tools");
        connectathonTools.setIcon(Resources.INSTANCE.getSettingsIcon().getSafeUri().asString());
        connectathonTools.addItem("Tests Overview", 3, true);
        // Connectathon Validations
        connectathonTools.addItem("XDS.b Doc Source Stores Document", 3,true);
        // Registry Validations
        connectathonTools.addItem("XDS.b Registry Do This First", 3, true);
        connectathonTools.addItem("XDS.b Lifecycle", 3, true);
        connectathonTools.addItem("XDS.b Registry Folder Handling", 3, true);
        // Repository Validations
        connectathonTools.addItem("XDS.b Repository Do This First", 3, true);

        HomeBox queriesAndRetrieves = new HomeBox("Queries and Retrieves");
        queriesAndRetrieves.setIcon(Resources.INSTANCE.getTransferIcon().getSafeUri().asString());
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
        queriesAndRetrieves.addItem(TabLauncher.findDocumentsTabLabel, 2, false);
        queriesAndRetrieves.addItem(TabLauncher.findDocumentsByRefIdTabLabel, 2, false);
        queriesAndRetrieves.addItem(TabLauncher.mpqFindDocumentsTabLabel, 2, false);
        queriesAndRetrieves.addItem(TabLauncher.getDocumentsTabLabel, 2, false);
        queriesAndRetrieves.addItem(TabLauncher.getRelatedTabLabel, 2, false);
        queriesAndRetrieves.addItem(TabLauncher.findFoldersTabLabel, 2, false);
        queriesAndRetrieves.addItem(TabLauncher.getFoldersTabLabel, 2, false);
        queriesAndRetrieves.addItem(TabLauncher.getFolderAndContentsTabLabel, 2, false);
        queriesAndRetrieves.addItem(TabLauncher.getSubmissionSetTabLabel, 2, false);
        queriesAndRetrieves.addItem(TabLauncher.documentRetrieveTabLabel, 2, false);

        HomeBox tempDev = new HomeBox("Temporary Tabs & Long-Term Development");
        tempDev.setIcon(Resources.INSTANCE.getNotesIcon().getSafeUri().asString());
        tempDev.addItem("V2 Home", 2, false);
        tempDev.addItem("V2 Dynamic Tab", 2, false);
        tempDev.addItem("Endpoint Selection Test", 3, true);
        tempDev.addItem("Query - Retrieve - Submit", 3, true);

        HomeBox sendTestData = new HomeBox("Send Test Data");
        sendTestData.setIcon(Resources.INSTANCE.getExportIcon().getSafeUri().asString());
        sendTestData.addItem("Submit Test Data", 3, true);

        HomeBox generateTestData = new HomeBox("Generate Test Data");
        generateTestData.setIcon(Resources.INSTANCE.getEditIcon().getSafeUri().asString());
        generateTestData.addItem("XDS Document Entry Editor", 3, false);

        //HomeBox simulators = new HomeBox("Simulators");
        // simulators.setIcon(Resources.INSTANCE.getDisplayIcon().getSafeUri().asString());

        HomeBox mhdTools = new HomeBox("MHD Tools");
        mhdTools.setIcon(Resources.INSTANCE.getIpadIcon().getSafeUri().asString());
        mhdTools.addItem("MHD Validator", 3, false);
        //mhdTools.addItem("MHD to XDS Converter", 3, true);


        VStack vstack1 = new VStack();
        VStack vstack2 = new VStack();
        vstack1.addMembers(preConnectathonTools, connectathonTools, generateTestData, sendTestData);
        vstack2.addMembers(queriesAndRetrieves, mhdTools, tempDev);

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
