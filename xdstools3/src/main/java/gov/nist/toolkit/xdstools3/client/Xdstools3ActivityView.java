package gov.nist.toolkit.xdstools3.client;


import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.ui.client.event.asset.OutOfContextAssetClickedEvent;
import gov.nist.hit.ds.repository.ui.client.event.asset.OutOfContextAssetClickedEventHandler;
import gov.nist.toolkit.xdstools3.client.activitiesAndPlaces.TabPlace;
import gov.nist.toolkit.xdstools3.client.customWidgets.toolbar.Toolbar;
import gov.nist.toolkit.xdstools3.client.manager.Manager;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;
import gov.nist.toolkit.xdstools3.client.tabs.*;
import gov.nist.toolkit.xdstools3.client.tabs.MPQTab.MPQTab;
import gov.nist.toolkit.xdstools3.client.tabs.adminSettingsTab.AdminSettingsTab;
import gov.nist.toolkit.xdstools3.client.tabs.connectathonTabs.FolderValidationTab;
import gov.nist.toolkit.xdstools3.client.tabs.connectathonTabs.LifecycleValidationTab;
import gov.nist.toolkit.xdstools3.client.tabs.connectathonTabs.RegisterAndQueryTab;
import gov.nist.toolkit.xdstools3.client.tabs.connectathonTabs.SourcesStoresDocumentValidationTab;
import gov.nist.toolkit.xdstools3.client.tabs.connectathonTabs.SubmitRetrieveTab;
import gov.nist.toolkit.xdstools3.client.tabs.docEntryEditorTab.DocEntryEditorTab;
import gov.nist.toolkit.xdstools3.client.tabs.findDocumentsTab.FindDocumentTab;
import gov.nist.toolkit.xdstools3.client.tabs.homeTab.HomeTab;
import gov.nist.toolkit.xdstools3.client.tabs.logBrowserTab.LogBrowserTab;
import gov.nist.toolkit.xdstools3.client.tabs.mhdTabs.MHDValidatorTab;
import gov.nist.toolkit.xdstools3.client.tabs.mhdTabs.MhdToXdsConverterTab2;
import gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab.PreConnectathonTestsTab;
import gov.nist.toolkit.xdstools3.client.tabs.queryRetrieveTabs.*;
import gov.nist.toolkit.xdstools3.client.tabs.submitTestDataTab.SubmitTestDataTab;
import gov.nist.toolkit.xdstools3.client.util.eventBus.*;
import gov.nist.toolkit.xdstools3.client.util.injection.Xdstools3GinInjector;

/**
 * Main class of the application which is the Activity for Activity-Places design, the view and a bit of a controller.
 */
// TabContainer was added for v2-v3 integration purposes, AcceptsOneWidget to avoid an entire MVP architecture (fuse Activity and View in one single class)
public class Xdstools3ActivityView extends AbstractActivity implements AcceptsOneWidget {

    private GenericTabSet topTabSet;

    private HLayout container;
    private String tabId;

    /**
     * Method that starts the UI
     */
    public void run() {
        // Toolbar
        Toolbar configBar = new Toolbar();
        configBar.setStyleName("app-padding");


        // Tabs
        topTabSet = new GenericTabSet();
        HLayout tabsetStack = new HLayout();
        tabsetStack.setLayoutBottomMargin(27);
        tabsetStack.addMembers(new LayoutSpacer(), topTabSet,new LayoutSpacer());
        topTabSet.setWidth(1024);
        Tab homeTab = new HomeTab("Home");
        topTabSet.addTab(homeTab);
        tabsetStack.setZIndex(-1);


        // Main layout
        VLayout mainLayout = new VLayout();
        HTMLFlow header = new HTMLFlow();
        header.setContents(getHeaderHtmlContent());
        HTMLFlow footer = new HTMLFlow();
        footer.setContents(getFooterHtmlContent());

        mainLayout.addMembers(header, configBar, tabsetStack, footer);
        mainLayout.setStyleName("mainLayout");

        // Attach the contents to the RootLayoutPanel
        container = new HLayout();
        container.setAlign(Alignment.CENTER);
        container.setWidth100();
        container.setHeight100();
        container.addMembers(mainLayout);
        container.draw();

        // Smartgwt Console - useful for development, mainly tracking RPC calls
        //SC.showConsole();

        bindUI();
    }

    /**
     * binds the UI Actions
     */
    private void bindUI() {
        // Add listener for Open Tab eventBus. The tabs called must be defined in function "openTab".
        Manager.EVENT_BUS.addHandler(OpenTabEvent.TYPE, new OpenTabEventHandler(){
            public void onEvent(OpenTabEvent event) {
                openTab(event.getTabName());
            }
        });

        topTabSet.addTabSelectedHandler(new TabSelectedHandler() {
            @Override
            public void onTabSelected(TabSelectedEvent tabSelectedEvent) {
                String tabName;
                tabName=((GenericTab) tabSelectedEvent.getTab()).getTabName();
                Xdstools3GinInjector.injector.getPlaceController().goTo(new TabPlace(tabName));
            }
        });
        //---------- Close Tabs from context menu ---------
        //------ TODO Could be move to GenericTabSet ------
        Manager.EVENT_BUS.addHandler(CloseTabEvent.TYPE,new CloseTabEvent.CloseTabEventHandler() {
            @Override
            public void onCloseTabEvent(CloseTabEvent event) {
                final Tab t=event.getTab();
                SC.confirm("Are you sure you want to close the tab: '" + t.getTitle() + "'?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean response) {
                        if(response != null && response){
                            topTabSet.removeTab(t);
                        }
                    }
                });
            }
        });
        Manager.EVENT_BUS.addHandler(CloseOtherTabsEvent.TYPE,new CloseOtherTabsEvent.CloseOtherTabsEventHandler() {
            @Override
            public void onCloseOtherTabsEvent(CloseOtherTabsEvent event) {
                final Tab tab=event.getTab();
                SC.confirm("Are you sure you want to close all tab except from: '" + tab.getTitle() + "'?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean response) {
                        if(response != null && response){
                            for (Tab t:topTabSet.getTabs()){
                                if (!(t.getTitle().equals("Home") || t.getTitle().equals(tab.getTitle()))){
                                    topTabSet.removeTab(t);
                                }
                            }
                        }
                    }
                });
            }
        });
        Manager.EVENT_BUS.addHandler(CloseAllTabsEvent.TYPE,new CloseAllTabsEvent.CloseAllTabsEventHandler() {
            @Override
            public void onCloseAllTabsEvent(CloseAllTabsEvent event) {
                SC.confirm("Are you sure you want to close your tabs?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean response) {
                        if(response != null && response){
                            for (Tab t:topTabSet.getTabs()){
                                if (!(t.getTitle().equals("Home"))){
                                    topTabSet.removeTab(t);
                                }
                            }
                        }
                    }
                });
            }
        });
        //--------------------------------------------------
        // Add handler for log browser events
        try {
            // This handler is specific to the widget launch from the MHD Validator tab
            Manager.EVENT_BUS.addHandler(OutOfContextAssetClickedEvent.TYPE, new OutOfContextAssetClickedEventHandler() {
                public void onAssetClick(OutOfContextAssetClickedEvent event) {
                    try {
                        final AssetNode target = event.getValue();

                        if ("text/csv".equals(target.getMimeType())) {
                            String rowNumberToHighlightStr = "" + event.getRowNumber();

                            target.getExtendedProps().put("rowNumberToHighlight", rowNumberToHighlightStr);

                            Tab lbContextTab = new LogBrowserTab(target);
                            topTabSet.addTab(lbContextTab);
                            topTabSet.selectTab(lbContextTab);

                        }

                    } catch (Throwable t) {
                        t.printStackTrace();
                        Window.alert(t.toString());
                    }

                }
            });

        } catch (Throwable t) {
            t.printStackTrace();
            Window.alert("Event setup failed. " + t.toString());
        }
        //--------------------------------------------------
    }


    /**
     * Opens a given tab defined by its name. Updates the display to add this new tab and to bring it into focus.
     * This function must be updated when new tabs are added to the application, as well as its related classes.
     *
     * @param tabName the name of the tab to open.
     * @see gov.nist.toolkit.xdstools3.client.manager.TabNamesManager , OpenTabEvent
     */
    public void openTab(String tabName) {
        Tab tab = null;

        // ---------- v3 tabs --------
        if (tabName.equals(TabNamesManager.getInstance().getHomeTabCode())){
            tab = new HomeTab("Home");
        }
        else if (tabName.equals(TabNamesManager.getInstance().getAdminTabCode())) {
            tab = new AdminSettingsTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getEndpointsTabCode())) {
            tab = new EndpointConfigTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getFindDocumentsTabCode())) {
            tab = new FindDocumentTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getMpqFindDocumentsTabCode())) {
            tab = new MPQTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getMessageValidatorTabCode())) {
            tab = new MessageValidatorTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getPreConnectathonTestsTabCode())) {
            tab = new PreConnectathonTestsTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getDocumentMetadataEditorTabCode())) {
            tab = new DocEntryEditorTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getGetDocumentsTabCode())) {
            tab = new GetDocumentsTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getFindFoldersCode())) {
            tab = new FindFoldersTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getGetFoldersTabCode())) {
            tab = new GetFoldersTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getRetrieveDocumentTabCode())) {
            tab = new RetrieveDocumentsTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getGetFoldersAndContentsCode())) {
            tab = new GetFolderAndContentsTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getGetSubmissionSetAndContentsTabCode())) {
            tab = new GetSubmissionSetAndContentsTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getGetRelatedDocumentsCode())) {
            tab = new GetRelatedDocumentsTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getSourceStoresDocumentValidationCode())) {
            tab = new SourcesStoresDocumentValidationTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getRegisterAndQueryTabCode())) {
            tab = new RegisterAndQueryTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getLifecycleValidationTabCode())) {
            tab = new LifecycleValidationTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getFolderValidationTabCode())) {
            tab = new FolderValidationTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getSubmitRetrieveTabCode())) {
            tab = new SubmitRetrieveTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getMHDValidatorTabCode())) {
            tab = new MHDValidatorTab();
        }
        else if(tabName.equals(TabNamesManager.getInstance().getTestDataSubmissionTabCode())){
            tab = new SubmitTestDataTab();
        }
        else if (tabName.equals(TabNamesManager.getInstance().getLogBrowserTabCode())) {
            tab = new LogBrowserTab();
        }
        else if(tabName.equals(TabNamesManager.getInstance().getMhdtoXdsConverterTabCode())){
            tab = new MhdToXdsConverterTab2();
        }
        else if(tabName.equals(TabNamesManager.getInstance().getHelpTabCode())){
            tab = new HelpTab();
        }
        else{
            // unknown tab
            topTabSet.selectTab(0); // todo we can create a 404
        }

        // update set of tabs
        if (tab != null) {
            // tests if the tab is already open
            Tab t=topTabSet.findTab(tab);
            if(t!=null){
                // select and display the tab when already open
                topTabSet.selectTab(t);
            }else{
                // open a new tab in tabset, select and display it.
                topTabSet.addTab(tab);
                topTabSet.selectTab(tab);
            }
        }
    }

    /**
     * Implementation of abstract activity method that starts an activity.
     * This method is called each time an activity must be loaded, which means
     * each time Place change.
     *
     * @param acceptsOneWidget
     * @param eventBus
     */
    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, com.google.gwt.event.shared.EventBus eventBus) {
        if(tabId!=null ) {
            // Open required tab
            Manager.EVENT_BUS.fireEvent(new OpenTabEvent(tabId));
        }
    }

    /**
     * Method to set open Tab's ID (or to open)
     * @param tabId
     */
    public void setTabId(String tabId) {
        this.tabId = tabId;
    }

    /**
     * Implementation of AcceptsOneWidget method to set a widget in the Activity (AcceptsOneWidget) main container.
     * (Kind of an ActivityDisplayer)
     *
     * @param w widget to load in the activity
     */
    @Override
    public void setWidget(IsWidget w) {
        container.addMember(w.asWidget());
    }
    /**
     * Method that create application header html content
     * @return app header html
     */
    private String getHeaderHtmlContent() {
        return "<header id='appheader'>" +
                "<div id='apptitle'>Document Sharing Test Tools</div>" +
                "<div id='appversion'>Version 3.0.1</div>" +
                "<div id='appsubtitle'>IHE USA Chicago Connectathon Jan. 2014</div>" +
                "</header>" +
                "<nav class='navbar'>" +
                "<div class='app-padding navbar-inner'>" +
                "<ul>" +
                "<li><a href='#'>Home</a></li>" +
               /* "<li><a href='#'>Queries & Retrieves</a>" +
                "<ul>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getFindDocumentsTabCode()+"'>Find Document</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getMpqFindDocumentsTabCode()+"'>MPQ Find Documents</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getGetDocumentsTabCode()+"'>Get Documents</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getGetRelatedDocumentsCode()+"'>Get Related Documents</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getRetrieveDocumentTabCode()+"'>Retrieve Document</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getFindFoldersCode()+"'>Find Folders</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getGetFoldersTabCode()+"'>Get Folders</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getGetFoldersAndContentsCode()+"'>Get Folders and Contents</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getGetSubmissionSetAndContentsTabCode()+"'>Get Submission Set and Contents</a></li>" +
                "</ul>" +
                "</li>" + */
                "<li><a href='#'>Tools</a>" +
                "<ul>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getMessageValidatorTabCode()+"'>Message Validator</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getDocumentMetadataEditorTabCode()+"'>Document Metadata Editor</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getPreConnectathonTestsTabCode()+"'>Pre-Connectathon Tests</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getv2TabCode()+"'>v2 Tab Example</a></li>" +
                "</ul>" +
                "</li>" +
               /* "<li><a href='#'>Send Test Data</a>" +
                "<ul>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getv2TabCode()+"'>v2 Tab Example</a></li>" +
                "</ul>" +
                "</li>" +
                "<li><a href='#'>Simulators</a>" +
                "<ul>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getv2TabCode()+"'>v2 Tab Example</a></li>" +
                "</ul>" +
                "</li>" +
                "<li><a href='#'>Connectathon Tools</a>" +
                "<ul>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getSourceStoresDocumentValidationCode()+"'>XDS.b Doc Source Stores Document</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getRegisterAndQueryTabCode()+"'>XDS.b Register and Query</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getLifecycleValidationTabCode()+"'>XDS.b Lifecycle Validation</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getFolderValidationTabCode()+"'>XDS.b Registry Folder Validation</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getSubmitRetrieveTabCode()+"'>XDS.b Submit/Retrieve</a></li>" +
                "</ul>" +
                "</li>" + */
                "<div style='float:right;'>" +
                "<li><a class='right-side-button' href='#'><img class='icon-link' src='images/icons/glyphicons/download-icon.png'/> Download</a></li>" +
                "<li><a class='right-side-button' href='#TabPlace:"+ TabNamesManager.getInstance().getHelpTabCode()+"'><img class='icon-link' src='images/icons/glyphicons/help-icon.png'/> Help</a></li>" +
                "</div>" +
                "<ul>" +
                "</div>" +
                "</nav>";
    }

    /**
     * Method that create application footer html content
     * @return app footer html
     */
    private String getFooterHtmlContent() {
        return "<footer>" +
                "    <ul>" +
                "         <li>" +
                "            <a href=\"http://www.nist.gov\">NIST homepage</a>" +
                "         </li>" +
                "         <li>" +
                "            <a href=\"http://www.nist.gov/public_affairs/disclaimer.cfm\">NIST disclaimer</a>" +
                "         </li>" +
                "    </ul>" +
                "</footer>";
    }

}
