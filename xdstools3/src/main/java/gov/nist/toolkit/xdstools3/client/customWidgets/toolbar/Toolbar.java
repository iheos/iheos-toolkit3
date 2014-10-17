package gov.nist.toolkit.xdstools3.client.customWidgets.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IconButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SelectOtherItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.toolbar.RibbonBar;
import com.smartgwt.client.widgets.toolbar.RibbonGroup;
import gov.nist.toolkit.xdstools2.client.adapter2v3.PopupMessageV3;
import gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog.LoginDialogWidget;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.OpenTabEvent;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;
import gov.nist.toolkit.xdstools3.client.util.Util;

// TODO put each session and env in a separate ribbonbar
public class Toolbar extends RibbonBar {
   // static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Toolbar.class);
    private SelectOtherItem sessionsComboBox;
    private SelectItem listBox;
    private ToolbarServiceAsync rpcService = GWT.create(ToolbarService.class);

    public Toolbar() {

        setMembersMargin(20);
        setAlign(Alignment.CENTER);

        // Menu group: Session
        RibbonGroup sessionGroup = createRibbonGroup("Session");

        listBox = new SelectItem();
        listBox.setShowTitle(false);
        listBox.setShowOptionsFromDataSource(false);
        listBox.setWidth("150");
        listBox.setDefaultToFirstOption(true);

        // Retrieve Environment data from server
        loadEnvironmentNamesFromServer();

//        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
//        valueMap.put("US1", "NA2014");
//        valueMap.put("EU1", "EURO2011");
//        valueMap.put("EU2", "EURO2012");
//        valueMap.put("US2", "NwHIN");
//        listBox.setValueMap(valueMap);
//        // This is a workaround for the  LinkedHashMap, which normally accepts unique elements only, to display the
//        // flags although several are redundant.
//        LinkedHashMap<String, String> valueIcons = new LinkedHashMap<String, String>();
//        valueIcons.put("US1", "US");
//        valueIcons.put("EU1", "EU");
//        valueIcons.put("US2", "US");
//        valueIcons.put("EU2", "EU");
//        listBox.setValueIcons(valueIcons);

        // sets flag icons
        listBox.setImageURLPrefix("icons/flags/16/");
        listBox.setImageURLSuffix(".png");

        // create sessions drop-down
        sessionsComboBox = new SelectOtherItem();
        sessionsComboBox.setShowTitle(false);
        sessionsComboBox.setWidth(603);
        sessionsComboBox.setOtherTitle("Enter a new session name...");
        sessionsComboBox.setTitle("Session");

        // load session data from server
        loadSessionNamesFromServer();

        // create form
        DynamicForm form = new DynamicForm();
        form.setFields(listBox, sessionsComboBox);
        form.setCellPadding(10);

        sessionGroup.addControls(form);

        // Menu group: Site / Actors
        IconButton endpointButton = getIconButton("View / Configure Endpoints", "icons/user_24x24.png", true);

        // Menu group: Admin
        // Behavior: Clicking on any of the buttons in the admin group opens a dialog to allow the user to log in as admin,
        // IF not logged in yet. Then follows to the link initially requested.
        IconButton adminButton = getIconButton("Admin Settings", "icons/glyphicons/glyphicons_136_cogwheel.png", true);



        // ---------------------- Listeners ----------------------

        /**
         * Handles user selection of session
         */
        sessionsComboBox.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent changedEvent) {

                AsyncCallback<String[]> addSessionNameCallback = new AsyncCallback<String[]>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        //logger.debug(getClassName() + ": " +" listBox.addChangedHandler");
                        new PopupMessageV3(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(String[] result) {
                        sessionsComboBox.setValueMap(result);
                    }
                };
                    rpcService.addTestSession((String)changedEvent.getValue(), addSessionNameCallback);
             }
        });



        endpointButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // Display the Endpoint Settings tab
                Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getEndpointsTabCode()));
            }});

        adminButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // if not logged in
                if (!Util.getInstance().getLoggedAsAdminStatus()) {
                    // ask user to log in
                    LoginDialogWidget dialog = new LoginDialogWidget(TabNamesUtil.getInstance().getAdminTabCode());
                    dialog.show();
                } else {
                    // Display the Admin Settings tab if logged in
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getAdminTabCode()));
                }
            }
        });



        // Add menu groups to menu bar
        this.addMembers(sessionGroup, endpointButton, adminButton);
        draw();
    }


    /**
     * Calls to the backend for session parameters
     */
    protected void loadSessionNamesFromServer(){

        AsyncCallback<String[]> sessionCallback = new AsyncCallback<String[]>() {
            public void onFailure(Throwable caught) {
                //logger.debug(getClassName() + ": " +"loadSessionNamesFromServer");
                new PopupMessageV3(caught.getMessage());
            }
            @Override
            public void onSuccess(String[] result) {
                sessionsComboBox.setValueMap(result);
            }
        };
        rpcService.retrieveTestSessions(sessionCallback);
    }



    /**
     * Calls to the backend to get environments
     */
    protected void loadEnvironmentNamesFromServer() {

        AsyncCallback<String[]> envCallback = new AsyncCallback<String[]>() {
            public void onFailure(Throwable caught) {
               // logger.debug(getClassName() + ": " +"loadEnvironmentNamesFromServer");
                new PopupMessageV3(caught.getMessage());
            }
            @Override
            public void onSuccess(String[] result) {
                listBox.setValueMap(result);
            }
        };
        rpcService.retrieveEnvironments(envCallback);
    }



    /**
     * Creates an icon button.
     * @param title
     * @param iconName
     * @return
     */
    private IconButton getIconButton(String title, String iconName, boolean vertical) {
        IconButton button = new IconButton(title);
        button.setIcon(iconName);
        if (vertical) button.setOrientation("vertical");
        return button;
    }

    private RibbonGroup createRibbonGroup(String title){
        RibbonGroup group = new RibbonGroup();
        group.setTitle(title);
        group.setNumRows(1);
        return group;
    }
}
