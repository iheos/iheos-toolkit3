package gov.nist.toolkit.xdstools3.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import gov.nist.toolkit.xdstools2.client.TabContainer;
import gov.nist.toolkit.xdstools2.client.tabs.EnvironmentState;
import gov.nist.toolkit.xdstools2.client.tabs.QueryState;
import gov.nist.toolkit.xdstools2.client.tabs.TestSessionState;
import gov.nist.toolkit.xdstools3.client.customWidgets.toolbar.Toolbar;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.OpenTabEvent;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.OpenTabEventHandler;
import gov.nist.toolkit.xdstools3.client.tabs.*;
import gov.nist.toolkit.xdstools3.client.tabs.MPQTab.MPQTab;
import gov.nist.toolkit.xdstools3.client.tabs.connectathonTabs.*;
import gov.nist.toolkit.xdstools3.client.tabs.docEntryEditorTab.DocEntryEditorTab;
import gov.nist.toolkit.xdstools3.client.tabs.findDocumentsTab.FindDocumentTab;
import gov.nist.toolkit.xdstools3.client.tabs.homeTab.HomeTab;
import gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab.PreConnectathonTestsTab;
import gov.nist.toolkit.xdstools3.client.tabs.queryRetrieveTabs.*;
import gov.nist.toolkit.xdstools3.client.tabs.v2.v2TabExample;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;
import gov.nist.toolkit.xdstools3.client.util.Util;

// TabContainer was added for v2-v3 integration purposes
public class Xdstools3 implements EntryPoint, TabContainer {

    private GenericTabSet topTabSet;


    public void onModuleLoad() {

        // Toolbar
        Toolbar configBar = new Toolbar();

        // Tabs
        topTabSet = new GenericTabSet();
        Tab homeTab = new HomeTab("Home");
        topTabSet.addTab(homeTab);

        // Main layout
        VLayout mainLayout = new VLayout();
        mainLayout.addMembers(configBar, topTabSet);
        mainLayout.setStyleName("mainLayout");

        // Attach the contents to the RootLayoutPanel
        HLayout container = new HLayout();
        container.setAlign(Alignment.CENTER);
        container.setWidth100();
        container.setHeight100();
        container.addMembers(new LayoutSpacer(), mainLayout, new LayoutSpacer());
        mainLayout.setWidth(900); // width has to be set here after use of LayoutSpacers, not in CSS, else it will not work.

        container.draw();

        // Smartgwt Console - useful for development, mainly tracking RPC calls
        // SC.showConsole();

        // Add listener for Open Tab eventBusUtils. The tabs called must be defined in function "openTab".
        Util.EVENT_BUS.addHandler(OpenTabEvent.TYPE, new OpenTabEventHandler(){
            public void onEvent(OpenTabEvent event) {
                openTab(event.getTabName());
            }
        });

    }

    /**
     * Opens a given tab defined by its name. Updates the display to add this new tab and to bring it into focus.
     * This function must be updated when new tabs are added to the application, as well as its related classes.
     *
     * @param tabName the name of the tab to open.
     * @see TabNamesUtil, OpenTabEvent
     */
    public void openTab(String tabName) {
        Tab tab = null;

        // ---------- v3 tabs --------
        if (tabName.equals(TabNamesUtil.getInstance().getAdminTabCode())) {
            tab = new SettingsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getEndpointsTabCode())) {
            tab = new EndpointConfigTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getFindDocumentsTabCode())) {
            tab = new FindDocumentTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getMpqFindDocumentsTabCode())) {
            tab = new MPQTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getMessageValidatorTabCode())) {
            tab = new MessageValidatorTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getPreConnectathonTestsTabCode())) {
            tab = new PreConnectathonTestsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getDocumentMetadataEditorTabCode())) {
            tab = new DocEntryEditorTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getGetDocumentsCode())) {
            tab = new GetDocumentsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getFindFoldersCode())) {
            tab = new FindFoldersTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getGetFoldersCode())) {
            tab = new GetFoldersTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getRetrieveDocumentCode())) {
            tab = new RetrieveDocumentsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getGetFoldersAndContentsCode())) {
            tab = new GetFolderAndContentsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getGetSubmissionSetAndContentsCode())) {
            tab = new GetSubmissionSetAndContentsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getGetRelatedDocumentsCode())) {
            tab = new GetRelatedDocumentsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getSourceStoresDocumentValidationCode())) {
            tab = new SourcesStoresDocumentValidationTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getRegisterAndQueryTabCode())) {
            tab = new RegisterAndQueryTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getLifecycleValidationTabCode())) {
            tab = new LifecycleValidationTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getFolderValidationTabCode())) {
            tab = new FolderValidationTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getSubmitRetrieveTabCode())) {
            tab = new SubmitRetrieveTab();
        }

        // ---------- legacy v2 tabs --------
        else if (tabName.equals(TabNamesUtil.getInstance().getv2TabCode())) {
            tab = new v2TabExample(this);
        }

        // update set of tabs
        if (tab != null) {
            topTabSet.addTab(tab);
            topTabSet.selectTab(tab);
        }
    }


    // ------- Added for v2-v3 integration purposes -----
    @Override
    public void addTab(VerticalPanel w, String title, boolean select) {

    }

    @Override
    public TabPanel getTabPanel() {
        return null;
    }

    @Override
    public QueryState getQueryState() {
        return null;
    }

    @Override
    public EnvironmentState getEnvironmentState() {
        return null;
    }

    @Override
    public TestSessionState getTestSessionState() {
        return null;
    }
}
