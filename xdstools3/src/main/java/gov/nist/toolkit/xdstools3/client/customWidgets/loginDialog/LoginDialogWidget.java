package gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourcePasswordField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.RPCUtils.OpenEndpointTabCallback;
import gov.nist.toolkit.xdstools3.client.RPCUtils.OpenSettingsTabCallback;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.GenericCancelButton;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.LoginButton;

public class LoginDialogWidget extends Window {

    //	protected TextItem userName;
    //	protected TextItem password;
    protected DynamicForm form;
    protected LoginButton login;
    protected GenericCancelButton cancel;
    protected DataSource dataSource;

    private String protectedTab = "";

    public LoginDialogWidget(String _protectedTab) {
        protectedTab = _protectedTab;

        // Create elements
        setTitle("Login");
        setWidth(280); setHeight(140);
        setShowResizeBar(false);
        setAutoCenter(true);

        // Set DataSource (link to backend)
        dataSource = new DataSource();
        dataSource.setID("login");
        DataSourceTextField firstNameField = new DataSourceTextField("username", "Username", 50, true);
        DataSourcePasswordField passwordField = new DataSourcePasswordField("password", "Password", 50, true);
        dataSource.setFields(firstNameField, passwordField);


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

        cancel = new GenericCancelButton();
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
        // call to server to log the user
        LoginService intf = GWT.create(LoginService.class);

        // The callback depends on which tab we want to open
        AsyncCallback callback;
        if (protectedTab == "ADMIN") {callback = new OpenSettingsTabCallback();}
        else {callback = new OpenEndpointTabCallback();} // ENDPOINTS
        intf.logMeIn("", "");
        close();
    }

}
