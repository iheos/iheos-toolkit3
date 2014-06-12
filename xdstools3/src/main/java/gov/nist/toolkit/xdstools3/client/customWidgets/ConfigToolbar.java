package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IconButton;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.menu.IconMenuButton;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.toolbar.RibbonBar;
import com.smartgwt.client.widgets.toolbar.RibbonGroup;
import gov.nist.toolkit.xdstools3.client.InterfaceClientServer;
import gov.nist.toolkit.xdstools3.client.InterfaceClientServerAsync;
import gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog.LoginDialogWidget;

import java.util.LinkedHashMap;

public class ConfigToolbar extends RibbonBar {
    private SimpleEventBus bus;

    public ConfigToolbar(SimpleEventBus _bus) {
        bus = _bus;

        setMembersMargin(2);
        setBorder("0px");
        setAlign(Alignment.CENTER);

        // Menu group: Session
        RibbonGroup sessionGroup = createRibbonGroup("Session");

        final DynamicForm form = new DynamicForm();
        SelectItem listBox = new SelectItem();
        listBox.setShowTitle(false);
        // listBox.setValueField("EmployeeId");
        listBox.setDisplayField("Environment");
        listBox.setWidth("290px");
        listBox.setDefaultToFirstOption(true);
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put("Select Environment", "XX");
        valueMap.put("NA2014", "US");
        valueMap.put("EURO2011", "EU");
        valueMap.put("EURO2012","EU");
        valueMap.put("NwHIN", "US");
        // TODO set flag icons
        // listBox.setImageURLPrefix("flags/16/");
        // listBox.setImageURLSuffix(".png");
        listBox.setValueMap(valueMap);
        form.setFields(listBox);

//        listBox.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                int selectedIndex = listBox.getSelectedIndex();
//                String env = listBox.getItemText(selectedIndex);
//                if (env.equals("")) {
//                    //
//                }
//            }
//        });



        //	WidgetCanvas widgetCanvas_2 = new WidgetCanvas(listBox);
        ListBox listBox_1 = new ListBox();
        listBox_1.setWidth("290px");
        listBox_1.addItem("Select test session");
        listBox_1.addItem("Test session 1");
        listBox_1.addItem("Add new test session...");
        listBox_1.setVisibleItemCount(1);
        WidgetCanvas widgetCanvas_3 = new WidgetCanvas(listBox_1);
        sessionGroup.addControls(form, widgetCanvas_3);

        // Menu group: Site / Actors
        RibbonGroup actorsGroup = createRibbonGroup("Endpoints");
        IconButton configEndpoints = getIconButton("View / Configure", "icon_gear.png"); configEndpoints.setWidth("80px");
        actorsGroup.addControls(configEndpoints);

        // Menu group: Admin
        // Behavior: Clicking on any of the buttons in the admin group opens a dialog to allow the user to log in as admin,
        // IF not logged in yet. Then follows to the link initially requested.
        RibbonGroup adminGroup = createRibbonGroup("Admin Panel");
        adminGroup.setWidth("450px");
        IconButton button = getIconButton("Settings", "icon_gear.png") ;
        button.addClickHandler(new ClickHandler() {
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
        adminGroup.addControl(button);

        // Add menu groups to menu bar
        this.addMembers(sessionGroup, actorsGroup, adminGroup);
        this.setHeight(sessionGroup.getHeight());
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
    private IconButton getIconButton(String title, String iconName) {
        IconButton button = new IconButton(title);
        button.setTitle(title);
        if (iconName == null) iconName = "defaulticon";
        button.setIcon(iconName);
        return button;
    }

    /**
     * Creates a drop-down menu with an icon.
     * @param title
     * @param iconName
     * @param menu
     * @param vertical
     * @return
     */
    private IconMenuButton getIconMenuButton(String title, String iconName, Menu menu, boolean vertical) {
        IconMenuButton button = new IconMenuButton(title);
        button.setTitle(title);
        if (iconName == null) iconName = "defaulticon";
        button.setIcon(iconName);
        if (menu != null) button.setMenu(menu);
        if (vertical == true) button.setOrientation("vertical");
        return button;
    }

    private RibbonGroup createRibbonGroup(String title){
        RibbonGroup group = new RibbonGroup();
        group.setTitle(title);
        group.setNumRows(1);
        return group;
    }
}
