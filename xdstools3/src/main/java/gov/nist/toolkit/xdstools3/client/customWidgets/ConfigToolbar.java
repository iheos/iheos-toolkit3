package gov.nist.toolkit.xdstools3.client.customWidgets;

import gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog.LoginDialogWidget;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.ListBox;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IconButton;
import com.smartgwt.client.widgets.WidgetCanvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.menu.IconMenuButton;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.toolbar.RibbonBar;
import com.smartgwt.client.widgets.toolbar.RibbonGroup;

public class ConfigToolbar extends RibbonBar {
	private SimpleEventBus bus;

	public ConfigToolbar(SimpleEventBus _bus) {
		bus = _bus;

		setMembersMargin(2); 
		setBorder("0px");
		setAlign(Alignment.CENTER);

		// Menu group: Session
		RibbonGroup sessionGroup = createRibbonGroup("Session");

		ListBox listBox = new ListBox();
		listBox.setWidth("290px");
		listBox.addItem("Select environment");
		listBox.addItem("Env1");
		listBox.addItem("Env2");
		listBox.setVisibleItemCount(1);
		WidgetCanvas widgetCanvas_2 = new WidgetCanvas(listBox);
		ListBox listBox_1 = new ListBox();
		listBox_1.setWidth("290px");
		listBox_1.addItem("Select test session");
		listBox_1.addItem("Test session 1");
		listBox_1.addItem("Add new test session...");
		listBox_1.setVisibleItemCount(1);
		WidgetCanvas widgetCanvas_3 = new WidgetCanvas(listBox_1);
		sessionGroup.addControls(widgetCanvas_2, widgetCanvas_3); 

		// Menu group: Test config
//		RibbonGroup testconfigGroup = createRibbonGroup("Test Configuration");
//		CheckboxItem tls = new CheckboxItem("TLS");
//		tls.setTitle("TLS");
//		tls.setWidth("40px");
//		DynamicForm tlsform = new DynamicForm();
//		tlsform.setFields(tls);
//		ListBox listBox_saml = new ListBox();
//		listBox_saml.addItem("SAML off");
//		listBox_saml.addItem("DIRECT SAML");
//		listBox_saml.setVisibleItemCount(1);
//		WidgetCanvas widgetCanvas_6 = new WidgetCanvas(listBox_saml);
//		testconfigGroup.addControls(widgetCanvas_6, tlsform);

		//       Menu configMenu = new Menu();
		//       configMenu.addItem(new MenuItem("Endpoint Configuration", "icon_gear.png", "Ctrl+D"));  
		//       configMenu.addItem(new MenuItem("List of Endpoints", "icon_gear.png", "Ctrl+P"));
		//		IconMenuButton endpoints = getIconMenuButton("Endpoints","icon_gear.png", configMenu, true);

		// Menu group: Site / Actors
		RibbonGroup actorsGroup = createRibbonGroup("Endpoints");  
		IconButton configEndpoints = getIconButton("Configure", "icon_gear.png"); configEndpoints.setWidth("80px");
		IconButton listEndpoints = getIconButton("View", "icon_gear.png"); listEndpoints.setWidth("80px");
		actorsGroup.addControls(configEndpoints, listEndpoints);

		// Menu group: Admin 
		// Behavior: Clicking on any of the buttons in the admin group opens a dialog to allow the user to log in as admin,
		// IF not logged in yet. Then follows to the link initially requested.
		RibbonGroup adminGroup = createRibbonGroup("Admin Panel");
        adminGroup.setWidth("350px");
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
		this.setWidth100();
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
