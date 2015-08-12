package gov.nist.hit.ds.xdstools3.client;


import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import gov.nist.hit.ds.xdstools3.client.activitiesAndPlaces.TabPlace;
import gov.nist.hit.ds.xdstools3.client.customWidgets.dialogs.PopupMessageV3;
import gov.nist.hit.ds.xdstools3.client.customWidgets.toolbar.Toolbar;
import gov.nist.hit.ds.xdstools3.client.customWidgets.toolbar.ToolbarService;
import gov.nist.hit.ds.xdstools3.client.customWidgets.toolbar.ToolbarServiceAsync;
import gov.nist.hit.ds.xdstools3.client.manager.Manager;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.EndpointConfigTab;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericTab;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericTabSet;
import gov.nist.hit.ds.xdstools3.client.tabs.HelpTab;
import gov.nist.hit.ds.xdstools3.client.tabs.MPQTab.MPQTab;
import gov.nist.hit.ds.xdstools3.client.tabs.MessageValidatorTab;
import gov.nist.hit.ds.xdstools3.client.tabs.QRSCombinedTab.QueryRetrieveSubmitCombinedTab;
import gov.nist.hit.ds.xdstools3.client.tabs.adminSettingsTab.AdminSettingsTab;
import gov.nist.hit.ds.xdstools3.client.tabs.connectathonTabs.FolderValidationTab;
import gov.nist.hit.ds.xdstools3.client.tabs.connectathonTabs.LifecycleValidationTab;
import gov.nist.hit.ds.xdstools3.client.tabs.connectathonTabs.RegisterAndQueryTab;
import gov.nist.hit.ds.xdstools3.client.tabs.connectathonTabs.SourcesStoresDocumentValidationTab;
import gov.nist.hit.ds.xdstools3.client.tabs.connectathonTabs.SubmitRetrieveTab;
import gov.nist.hit.ds.xdstools3.client.tabs.contactTab.ContactTab;
import gov.nist.hit.ds.xdstools3.client.tabs.docEntryEditorTab.DocEntryEditorTab;
import gov.nist.hit.ds.xdstools3.client.tabs.findDocumentsTab.FindDocumentTab;
import gov.nist.hit.ds.xdstools3.client.tabs.homeTab.HomeTab;
import gov.nist.hit.ds.xdstools3.client.tabs.logBrowserTab.LogBrowserTab;
import gov.nist.hit.ds.xdstools3.client.tabs.mhdTabs.MHDValidatorTab;
import gov.nist.hit.ds.xdstools3.client.tabs.mhdTabs.MhdToXdsConverterTab;
import gov.nist.hit.ds.xdstools3.client.tabs.preConnectathonTestsTab.PreConnectathonTestsTab;
import gov.nist.hit.ds.xdstools3.client.tabs.queryRetrieveTabs.FindFoldersTab;
import gov.nist.hit.ds.xdstools3.client.tabs.queryRetrieveTabs.GetDocumentsTab;
import gov.nist.hit.ds.xdstools3.client.tabs.queryRetrieveTabs.GetFolderAndContentsTab;
import gov.nist.hit.ds.xdstools3.client.tabs.queryRetrieveTabs.GetFoldersTab;
import gov.nist.hit.ds.xdstools3.client.tabs.queryRetrieveTabs.GetRelatedDocumentsTab;
import gov.nist.hit.ds.xdstools3.client.tabs.queryRetrieveTabs.GetSubmissionSetAndContentsTab;
import gov.nist.hit.ds.xdstools3.client.tabs.queryRetrieveTabs.RetrieveDocumentsTab;
import gov.nist.hit.ds.xdstools3.client.tabs.submitTestDataTab.SubmitTestDataTab;
import gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.V2HomeTab;
import gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.DocRetrieveTab;
import gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.FindDocumentsByRefIdTab;
import gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.FindDocumentsTab;
import gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.GetRelatedTab;
import gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.InspectorTab;
import gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.MPQFindDocumentsTab;
import gov.nist.hit.ds.xdstools3.client.util.eventBus.CloseAllTabsEvent;
import gov.nist.hit.ds.xdstools3.client.util.eventBus.CloseOtherTabsEvent;
import gov.nist.hit.ds.xdstools3.client.util.eventBus.CloseTabEvent;
import gov.nist.hit.ds.xdstools3.client.util.eventBus.OpenTabEvent;
import gov.nist.hit.ds.xdstools3.client.util.eventBus.OpenTabEventHandler;
import gov.nist.hit.ds.xdstools3.client.util.injection.Xdstools3GinInjector;
import gov.nist.toolkit.xdstools2.client.event.tabContainer.V2TabOpenedEvent;
import gov.nist.toolkit.xdstools2.client.tabs.TabLauncher;

import java.util.logging.Logger;


/**
 * Main class of the application which is the Activity for Activity-Places design, the view and a bit of a controller.
 */
// AcceptsOneWidget was added to avoid creating an entire MVP architecture (fuse Activity and View in one single class)
public class Xdstools3ActivityView extends AbstractActivity implements AcceptsOneWidget {
    private ToolbarServiceAsync propertiesService = GWT.create(ToolbarService.class);
    private Logger logger= Logger.getLogger(this.getClass().getName());

    private GenericTabSet topTabSet;

    private HLayout container;
    private String tabId;
    private HTMLFlow header= new HTMLFlow();
    private String toolkitEvent=null;
    private String toolkitVersion=null;

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
        topTabSet.setWidth(1200);
        Tab homeTab = new HomeTab("Home");
        topTabSet.addTab(homeTab);
        tabsetStack.setZIndex(-1);


        // Main layout
        VLayout mainLayout = new VLayout();
        setHeader();
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

    private void setHeader() {
        header.setContents(getHeaderHtmlContent());
        header.redraw();
    }

    /**
     * binds the UI Actions
     */
    private void bindUI() {
        propertiesService.getToolkitEvent(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                new PopupMessageV3("Error loading Connectathon event name from properties. See log for more information.");
                logger.warning("Error loading Connectathon event name from properties. " + throwable.getMessage());
            }

            @Override
            public void onSuccess(String s) {
                toolkitEvent = s;
                setHeader();
            }
        });
        propertiesService.getToolkitVersion(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                new PopupMessageV3("Error loading app version property. See log for more information.");
                logger.warning("Error loading app version property. "+throwable.getMessage());
            }

            @Override
            public void onSuccess(String s) {
                toolkitVersion=s;
                setHeader();
            }
        });
        // Add listener for Open Tab eventBus. The tabs called must be defined in function "openTab".
        Manager.EVENT_BUS.addHandler(OpenTabEvent.TYPE, new OpenTabEventHandler(){
            public void onEvent(OpenTabEvent event) {
                if (event.getV2TabOpenedEvent()==null) {
                    openTab(event.getTabName());
                } else {
                    openTab(event.getV2TabOpenedEvent());
                }
            }
        });


        topTabSet.addTabSelectedHandler(new TabSelectedHandler() {
            @Override
            public void onTabSelected(TabSelectedEvent tabSelectedEvent) {
                String tabName;
                tabName=((GenericTab) tabSelectedEvent.getTab()).getTabName();
//                Window.alert("*** tab selection handler: " + tabName);
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

    public Tab openTab(V2TabOpenedEvent event) {
        Tab tab = null;
        String tabName = event.getTitle();


        /* *************** begin v2 ************* */

        if (tabName.equals(TabNamesManager.getInstance().getV2HomeTabCode())) {
            tab = new V2HomeTab();
        }
        else if (tabName.equals(TabLauncher.findDocumentsTabLabel)) {
           tab = new FindDocumentsTab(event);
            // Use PlaceTab to launch non-event based v2 tabs (make sure the v2 xdstools2 addTab event propagation is turned off)
            // tab = new FindDocumentsEmbeddedPlaceTab();
        }
        else if (tabName.equals("Inspector")) { // Note: this v2 tab is a dependent tab
            tab = new InspectorTab(event);
        }
        else if (tabName.equals(TabLauncher.findDocumentsByRefIdTabLabel)) {
            tab = new FindDocumentsByRefIdTab(event);
        }
        else if (tabName.equals(TabLauncher.mpqFindDocumentsTabLabel) || "MPQFindDocuments".equals(tabName)) { // Note: this v2 tab link text in v2 Home differs from the actual tab display label
            tab = new MPQFindDocumentsTab(event);
        }
        else if (tabName.equals(TabLauncher.getDocumentsTabLabel)) {
            tab = new gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.GetDocumentsTab(event);
        }
        else if (tabName.equals(TabLauncher.getRelatedTabLabel)) {
            tab = new GetRelatedTab(event);
        }
        else if (tabName.equals(TabLauncher.findFoldersTabLabel)) {
            tab = new gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.FindFoldersTab(event);
        }
        else if (tabName.equals(TabLauncher.getFoldersTabLabel)) {
            tab = new gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.GetFoldersTab(event);
        }
        else if (tabName.equals(TabLauncher.getFolderAndContentsTabLabel)) {
            tab = new gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.GetFolderAndContentsTab(event);
        }
        else if (tabName.equals(TabLauncher.getSubmissionSetTabLabel) || "SubmissionSetAndContents".equals(tabName)) { // Note: this v2 tab link text in v2 Home differs from the actual tab display label
            tab = new gov.nist.hit.ds.xdstools3.client.tabs.v2Wrapper.qandr.GetSubmissionSetAndContentsTab(event);
        }
        else if (tabName.equals(TabLauncher.documentRetrieveTabLabel) || "RetrieveDoc".equals(tabName)) { // Note: this v2 tab link text in v2 Home differs from the actual tab display label
            tab = new DocRetrieveTab(event);
        }

        /* *************** end v2 ************* */

        if (tab!=null) {
            addTab(tab);
        }

        return tab;

    }

    /**
     * Opens a given tab defined by its name. Updates the display to add this new tab and to bring it into focus.
     * This function must be updated when new tabs are added to the application, as well as its related classes.
     *
     * @param tabName the name of the tab to open.
     * @see gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager , OpenTabEvent
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
            tab = new MhdToXdsConverterTab();
        }
        else if(tabName.equals(TabNamesManager.getInstance().getHelpTabCode())){
            tab = new HelpTab();
        }
        else if(tabName.equals(TabNamesManager.getInstance().getContactTabCode())){
            tab = new ContactTab();
        }
        else if(tabName.equals(TabNamesManager.getInstance().getQRSCombinedTabCode())){
            tab = new QueryRetrieveSubmitCombinedTab();
        }
        else {

            // Check if it is a V2 tab
            V2TabOpenedEvent v2TabOpenedEvent = new V2TabOpenedEvent(null,tabName,-1);
            if (openTab(v2TabOpenedEvent)==null) {

                // unknown tab
                topTabSet.selectTab(0); // todo we can create a 404
                return;
            }
        }
        addTab(tab);


    }

    protected void addTab(Tab tab) {
        // update set of tabs
        if (tab != null) {
            // tests if the tab is already open
//            Window.alert("In addTab(): 2 title:" + tab.getTitle() + " id:" + tab.getID() + " name:" + tab.getName());
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
                "<div id='apptitle-wrapper'>" +
                "<div id='apptitle'>Document Sharing Toolkit</div>" +
                "<div id='appevent'>"+ toolkitEvent +"</div>" +
                "<div id='appsubtitle'>Testing platform for healthcare interoperability</div>" +
                "<div id='appversion'>"+ toolkitVersion +"</div>" +
                "</div>" +   /* end apptitle-wrapper */
                "</header>" +
                "<nav class='navbar'>" +
                "<div class='app-padding navbar-inner'>" +
                "<ul>" +
                "<li><a href='#'>HOME</a></li>" +
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
                "<li><a href='#'>MHD TOOLS</a>" +
                "<ul>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getMHDValidatorTabCode()+"'>MHD Validator</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getMhdtoXdsConverterTabCode()+"'>MHD to XDS Converter</a></li>" +
                "</ul>" +
                "</li>" +
                "<li><a href='#'>TOOLS</a>" +
                "<ul>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getMessageValidatorTabCode()+"'>Message Validator</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getDocumentMetadataEditorTabCode()+"'>Document Metadata Editor</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getPreConnectathonTestsTabCode()+"'>Pre-Connectathon Tests</a></li>" +
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
                // In order to obtain blue icons for that section, use this online tool: http://fa2png.io/ and color #0000aa (shade of blue).
                "<div style='float:right;'>" +
               // "<li><a class='right-side-button' href='#'><img class='icon-link' src='images/icons/glyphicons/16px-blue/glyphicons-download-16px.png'/> Download</a></li>" +
               // "<li><a class='right-side-button' href='#TabPlace:"+ TabNamesManager.getInstance().getHelpTabCode()+"'><img class='icon-link' src='images/icons/glyphicons/16px-blue/glyphicons-help-16px.png'/> Help</a></li>" +
                "<li><a href='#TabPlace:"+ TabNamesManager.getInstance().getContactTabCode()+"'><i class='fa fa-envelope'></i>&nbsp; CONTACT US</a></li>" +
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
