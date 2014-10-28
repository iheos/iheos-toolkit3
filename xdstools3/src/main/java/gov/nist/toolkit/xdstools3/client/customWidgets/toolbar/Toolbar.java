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

import java.util.ArrayList;
import java.util.LinkedHashMap;

// TODO put each session and env in a separate ribbonbar
public class Toolbar extends RibbonBar {
    //static Logger logger = Logger.getLogger(Toolbar.class);
    private SelectOtherItem sessionsComboBox;
    private SelectItem envListBox;
    private ToolbarServiceAsync rpcService = GWT.create(ToolbarService.class);


    public Toolbar() {

        setMembersMargin(20);
        setAlign(Alignment.CENTER);

        // Menu group: Session
        RibbonGroup sessionGroup = createRibbonGroup("Session");

        envListBox = new SelectItem();
        envListBox.setShowTitle(false);
        envListBox.setShowOptionsFromDataSource(false);
        envListBox.setWidth("150");
        envListBox.setDefaultToFirstOption(true);

        // Retrieve Environment data from server
        loadEnvironmentNamesFromServer();

        // sets path for flag icons
        envListBox.setImageURLPrefix("icons/flags/16/");
        envListBox.setImageURLSuffix(".png");

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
        form.setFields(envListBox, sessionsComboBox);
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
         * Handles user selection on Environments drop-down
         */
        envListBox.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent changedEvent) {

                AsyncCallback<Void> envIsSelectedCallback = new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        //logger.debug(getClassName() + ": " + " envListBox.addChangedHandler");
                        new PopupMessageV3(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Void result) {
                        // do nothing
                    }
                };
                rpcService.setEnvironment((String)changedEvent.getValue(), envIsSelectedCallback);
            }
        });



        /**
         * Handles user selection on Session drop-down and adding new sessions
         */
        sessionsComboBox.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent changedEvent) {

                AsyncCallback<String[]> addSessionNameCallback = new AsyncCallback<String[]>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        //logger.debug(getClassName() + ": " + " sessionsComboBox.addChangedHandler");
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

        /**
         * Open Endpoint Tab
         */
        endpointButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // Display the Endpoint Settings tab
                Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getEndpointsTabCode()));
            }});

        /**
         * Open Configuration / Admin Tab
         */
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
               // logger.debug(getClassName() + ": " +"loadSessionNamesFromServer");
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
                //logger.debug(getClassName() + ": " +"loadEnvironmentNamesFromServer");
                new PopupMessageV3("Failed to retrieve the list of available environments from the server. " +
                        "The location of the configuration files for this application may have been misconfigured.");
            }
            @Override
            public void onSuccess(String[] result) {
                // Processes the list of environments for display
                ArrayList<LinkedHashMap<String, String>> envsProcessedForDisplay = createEnvironmentMap(result);
                envListBox.setValueMap(envsProcessedForDisplay.get(0));
                envListBox.setValueIcons(envsProcessedForDisplay.get(1));
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


    /**
     * Creates a map of environments and matching regions to display the correct flag in the drop-down.
     * Supports only US and EU environments!
     * The key is the region code ("US" or "EU") to which are added incremental numbers because the key must be unique.
     *
     * This is a workaround to build a Map that will be accepted by the SmartGWT dropdown and retain all of its
     * built-in functionality.
     *s
     * @param envs the list of environments
     * @return two LinkedHashMap<String, String> of environments and regions, stored into an ArrayList. Ex.: < EU1, EURO2012 > and < EU1, EU >.
     */
    private ArrayList<LinkedHashMap<String, String>> createEnvironmentMap(String[] envs){
        String key = "";
        int counterEU = 0;
        int counterUS = 0;
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>(); /* Not specifying types here does not work at GWT compile */
        LinkedHashMap<String, String> iconsMap = new LinkedHashMap<String, String>();

        for (String envName : envs) {
            if (envName.contains("EURO")) {
                counterEU++;
                key = "EU" + Integer.toString(counterEU);
                valueMap.put(key, envName);
                iconsMap.put(key, "EU");
            } else {
                counterUS++;
                key = "US" + Integer.toString(counterUS);
                valueMap.put(key, envName);
                iconsMap.put(key, "US");
            }
        }

        // build an arraylist that is only used to return the results
        ArrayList<LinkedHashMap<String, String>> envResults = new ArrayList<LinkedHashMap<String, String>>();
        envResults.add(valueMap);
        envResults.add(iconsMap);
        return envResults;
    }

}
