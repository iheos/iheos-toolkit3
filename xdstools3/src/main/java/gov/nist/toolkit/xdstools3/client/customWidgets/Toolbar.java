package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.smartgwt.client.widgets.IconButton;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
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

        setMembersMargin(2);
        setStyleName("navbar-inner");

        // Menu group: Session
        RibbonGroup sessionGroup = createRibbonGroup("Session");


        final DynamicForm form = new DynamicForm();
        SelectItem listBox = new SelectItem();
        listBox.setShowTitle(false);
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
        IconButton endpointButton = getIconButton("View / Configure Endpoints", "glyphicons_136_cogwheel.png", true);
        endpointButton.setStyleName("glyphiconNav");

        // Menu group: Admin
        // Behavior: Clicking on any of the buttons in the admin group opens a dialog to allow the user to log in as admin,
        // IF not logged in yet. Then follows to the link initially requested.
        IconButton adminButton = getIconButton("Admin Settings", "glyphicons_136_cogwheel.png", true) ;


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
        this.addMembers(sessionGroup, endpointButton, adminButton);
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
    private IconButton getIconButton(String title, String iconName, boolean vertical) {
        IconButton button = new IconButton(title);
        button.setIcon("icons/glyphicons/" + iconName);
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
