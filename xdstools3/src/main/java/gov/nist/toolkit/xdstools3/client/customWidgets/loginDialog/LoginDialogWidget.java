package gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog;

import gov.nist.toolkit.xdstools3.client.InterfaceClientServer;
import gov.nist.toolkit.xdstools3.client.InterfaceClientServerAsync;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.CancelButton;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.LoginButton;
import gov.nist.toolkit.xdstools3.client.events.OpenTabEvent;
import gov.nist.toolkit.xdstools3.client.events.demo.PingEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourcePasswordField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class LoginDialogWidget extends Window {

	//	protected TextItem userName;
	//	protected TextItem password;
	protected DynamicForm form;
	protected LoginButton login;
	protected CancelButton cancel;
	protected DataSource dataSource;
	private SimpleEventBus bus;

	public LoginDialogWidget(SimpleEventBus _bus) {
		bus = _bus;

		// Create elements
		setTitle("Login");
		setWidth(280); setHeight(140);
		setShowResizeBar(false);
		setAutoCenter(true);

		// Set DataSource (link to backend)
		dataSource = new DataSource();
        dataSource.setID("toolkit_config");
        DataSourceTextField firstNameField = new DataSourceTextField("username", "Username", 50, true);
        DataSourcePasswordField passwordField = new DataSourcePasswordField("password", "Password", 50, true);
        dataSource.setFields(firstNameField, passwordField);
      //  dataSource.setDataURL("data/datasources/toolkit_config.xml"); not used


		VLayout vlayout = new VLayout();
		vlayout.setHeight100();
		vlayout.addMembers(createLoginForm(), createButtons());
		vlayout.setAlign(Alignment.CENTER);
		vlayout.setAlign(VerticalAlignment.CENTER);
		addItem(vlayout);
	}

	/**
	 * Creates the form that asks for username and password
	 */
	private DynamicForm createLoginForm() {
		form = new DynamicForm();
		form.setDataSource(dataSource);
		form.setUseAllDataSourceFields(true);
		form.setAutoFocus(true); form.setAutoFocusOnError(true);
		form.setHeight(80);
		form.setCellPadding(5);
		form.setAlign(Alignment.CENTER);
		return form;
	}

	/**
	 * Creates "Cancel" and "Login" buttons
	 * @return The HLayout containing the buttons
	 */
	private HLayout createButtons(){
		login = new LoginButton();
		login.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {  
				if (form.validate()){
                logMeIn();
			}
		}});

		cancel = new CancelButton();
		cancel.addClickHandler(new ClickHandler() {  
			public void onClick(ClickEvent event) {
                close();
			}  
		});  


		HLayout buttonLayout = new HLayout();
		buttonLayout.addMembers(cancel, login);
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setMembersMargin(10);
		return buttonLayout;
	}

    /**
     * Perform login operations
     */
	protected void logMeIn(){

		// test of client-server calls
		InterfaceClientServerAsync intf = (InterfaceClientServerAsync) GWT.create(InterfaceClientServer.class);
		AsyncCallback callback = new AsyncCallback() {
			public void onFailure(Throwable caught) {
				SC.say("Failed to communicate with the server.");
			}

			@Override
			public void onSuccess(Object result) {
				SC.say("Communication with the server successful!");
			}
		};

		intf.logMeIn("", "", callback);

        // Display the Admin Settings tab if login was successful
        bus.fireEvent(new OpenTabEvent("ADMIN"));
	}




}
