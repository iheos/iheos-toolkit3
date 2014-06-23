package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IconButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.toolbar.RibbonBar;
import com.smartgwt.client.widgets.toolbar.RibbonGroup;
import gov.nist.toolkit.xdstools3.client.InterfaceClientServer;
import gov.nist.toolkit.xdstools3.client.InterfaceClientServerAsync;
import gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog.LoginDialogWidget;

import java.util.LinkedHashMap;

public class Toolbar extends RibbonBar {
    private SimpleEventBus bus;

    public Toolbar(SimpleEventBus _bus) {
        bus = _bus;

        setMembersMargin(10);
        setAlign(Alignment.CENTER);

        // Menu group: Session
        RibbonGroup sessionGroup = createRibbonGroup("Session");

        SelectItem listBox = new SelectItem();
        listBox.setShowTitle(false);
        listBox.setShowOptionsFromDataSource(false);
        listBox.setWidth("150");
        //listBox.setHeight("35");
        listBox.setDefaultToFirstOption(true);
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put("US1", "NA2014");
        valueMap.put("EU1", "EURO2011");
        valueMap.put("EU2", "EURO2012");
        valueMap.put("US2", "NwHIN");
        listBox.setValueMap(valueMap);
        // This is a workaround for the  LinkedHashMap, which normally accepts unique elements only, to display the
        // flags although several are redundant.
        LinkedHashMap<String, String> valueIcons = new LinkedHashMap<String, String>();
        valueIcons.put("US1", "US");
        valueIcons.put("EU1", "EU");
        valueIcons.put("US2", "US");
        valueIcons.put("EU2", "EU");
        listBox.setValueIcons(valueIcons);

        // sets flag icons
        listBox.setImageURLPrefix("icons/flags/16/");
        listBox.setImageURLSuffix(".png");





        ComboBoxItem cbItem = new ComboBoxItem();
        cbItem.setShowTitle(false);
        cbItem.setDefaultToFirstOption(true);
        //cbItem.setHeight(35);
        cbItem.setWidth(200);
        cbItem.setType("comboBox");
        cbItem.setValueMap("Select test session", "Test session 1", "Add new test session...");

        // create form
        DynamicForm form = new DynamicForm();
        form.setFields(listBox, cbItem);
        form.setPadding(5);


        sessionGroup.addControls(form);

        LayoutSpacer spacer = new LayoutSpacer();
        spacer.setWidth("360");


        // Menu group: Site / Actors
        IconButton endpointButton = getIconButton("View / Configure Endpoints", "icons/user_24x24.png", true);

        // Menu group: Admin
        // Behavior: Clicking on any of the buttons in the admin group opens a dialog to allow the user to log in as admin,
        // IF not logged in yet. Then follows to the link initially requested.
        IconButton adminButton = getIconButton("Admin Settings", "icons/glyphicons/glyphicons_136_cogwheel.png", true) ;

        // ---- Listeners ----

//        listBox.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                int selectedIndex = listBox.getSelectedIndex();
//                String env = listBox.getItemText(selectedIndex);
//                if (env.equals("")) {
//                    //
//                }
//            }
//        });

        adminButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                //if (!LoginManager.getInstance().isLoggedAsAdmin()) missing rpc call and user management
                showLoginWindow();
            }

            // Opens login dialog, which then displays the Admin Settings tab if login is successful
            private void showLoginWindow() {
                // TODO The Login window is missing check of credentials in backend
                LoginDialogWidget dialog = new LoginDialogWidget(bus);
                dialog.show();
            }
        });

        // Add menu groups to menu bar
        this.addMembers(sessionGroup, spacer, endpointButton, adminButton);
        draw();
    }


    /**
     * Calls to the backend for session parameters
     */
    protected void retrieveSessionParameters(){
        String[] environments;
        String[] sessions;

        InterfaceClientServerAsync intf = GWT.create(InterfaceClientServer.class);
        AsyncCallback<String[]> envCallback = new AsyncCallback<String[]>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }
            @Override
            public void onSuccess(String[] result) {
                // setEnvironments(result);
            }
        };
        AsyncCallback<String[]> sessionCallback = new AsyncCallback<String[]>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
            }
            @Override
            public void onSuccess(String[] result) {
                // setSessions(result);
            }
        };
        intf.retrieveEnvironments(envCallback);
        intf.retrieveTestSessions(sessionCallback);
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
