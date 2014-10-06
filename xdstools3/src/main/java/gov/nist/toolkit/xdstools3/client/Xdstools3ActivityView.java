package gov.nist.toolkit.xdstools3.client;


import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.ui.*;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
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

import java.util.logging.Logger;


// TabContainer was added for v2-v3 integration purposes
public class Xdstools3ActivityView extends AbstractActivity implements TabContainer, AcceptsOneWidget {

    private GenericTabSet topTabSet;

    private HLayout container;
    private String tabId;
    private String currentPlace=TabNamesUtil.getHomeTabCode();


    public void run() {
        // Toolbar
        Toolbar configBar = new Toolbar();

        // Tabs
        topTabSet = new GenericTabSet();
        Tab homeTab = new HomeTab("Home");
        topTabSet.addTab(homeTab);

        // Main layout
        VLayout mainLayout = new VLayout();
        HTMLFlow header = new HTMLFlow();
        header.setContents("<header id='appheader'>" +
                "<div id='apptitle'>Document Sharing Test Tools</div>" +
                "<div id='appversion'>Version 3.0.1</div>" +
                "<div id='appsubtitle'>IHE USA Chicago Connectathon Jan. 2014</div>" +
                "</header>" +
                "<nav class='navbar-inner'>" +
                "<ul>" +
                "<li><a href='#'>Home</a></li>" +
                "<li><a href='#'>Queries & Retrieves</a>" +
                "<ul>" +
                "<li><a href='#'>Find Document</a></li>" +
                "<li><a href='#'>Get Documents</a></li>" +
                "</ul>" +
                "</li>" +
                "</ul>" +
                "</nav>;");
        HTMLFlow footer = new HTMLFlow();
        footer.setContents("<footer>\n" +
                "        <ul>\n" +
                "            <li>\n" +
                "                <a href=\"http://www.nist.gov\">NIST homepage</a>\n" +
                "            </li>\n" +
                "            <li>\n" +
                "                <a href=\"#\">NIST Displamer</a>\n" +
                "            </li>\n" +
                "        </ul>\n" +
                "    </footer>");
        mainLayout.addMembers(header,configBar, topTabSet,footer);
        mainLayout.setStyleName("mainLayout");
        topTabSet.setStyleName("tabSetLayout");

        // Attach the contents to the RootLayoutPanel
        container = new HLayout();
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

        topTabSet.addTabSelectedHandler(new TabSelectedHandler() {
            @Override
            public void onTabSelected(TabSelectedEvent tabSelectedEvent) {
                String tabName;
                if(tabSelectedEvent.getTab() instanceof v2TabExample){
                    tabName=((v2TabExample) tabSelectedEvent.getTab()).getTabName();
                }else{
                    tabName=((GenericTab) tabSelectedEvent.getTab()).getTabName();
                }
                Xdstools3GinInjector.injector.getPlaceController().goTo(new TabPlace(tabName));
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
        if (tabName.equals(TabNamesUtil.getInstance().getHomeTabCode())){
            tab = new HomeTab("Home");
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getAdminTabCode())) {
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
        else if (tabName.equals(TabNamesUtil.getInstance().getGetDocumentsTabCode())) {
            tab = new GetDocumentsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getFindFoldersCode())) {
            tab = new FindFoldersTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getGetFoldersTabCode())) {
            tab = new GetFoldersTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getRetrieveDocumentTabCode())) {
            tab = new RetrieveDocumentsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getGetFoldersAndContentsCode())) {
            tab = new GetFolderAndContentsTab();
        }
        else if (tabName.equals(TabNamesUtil.getInstance().getGetSubmissionSetAndContentsTabCode())) {
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

        else{
            System.out.println("Unknown tab");
            topTabSet.selectTab(0); // todo we can create a 404
            currentPlace=TabNamesUtil.getHomeTabCode();
        }

        // update set of tabs
        if (tab != null) {
            boolean found=false;
            for (Tab t:topTabSet.getTabs())
                if (t.getTitle().equals(tab.getTitle())) {
                    topTabSet.selectTab(t);
                    found=true;
                    break;
                }
            // Remove second part of the condition if you want to reopen a closed tab on browser history back navigation
            if(found==false && TabNamesUtil.getHomeTabCode().equals(currentPlace)){
                topTabSet.addTab(tab);
                topTabSet.selectTab(tab);
            }
            if(tab instanceof GenericTab)
                currentPlace=((GenericTab) tab).getTabName();
            else if (tab instanceof v2TabExample)
                currentPlace=((v2TabExample) tab).getTabName();
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

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, com.google.gwt.event.shared.EventBus eventBus) {
        if(tabId!=null ) {
            System.out.println("eventBus ? null: "+(Util.EVENT_BUS==null));
            Util.EVENT_BUS.fireEvent(new OpenTabEvent(tabId));
        }
    }

    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    public HLayout getDisplay(){
        return container;
    }

    @Override
    public void setWidget(IsWidget w) {
        container.addMember(w.asWidget());
    }
}
