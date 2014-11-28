package gov.nist.toolkit.xdstools3.client.customWidgets.dialogs.loginDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourcePasswordField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.GenericCancelButton;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.LoginButton;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.OpenTabEvent;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;
import gov.nist.toolkit.xdstools3.client.util.Util;

public class LoginDialogWidget extends Window {
    private static int POPUP_WIDTH = 295;
    private static int POPUP_HEIGHT = 165;


    //	protected TextItem userName;
    //	protected TextItem password;
    protected DynamicForm form;
    protected LoginButton login;
    protected GenericCancelButton cancel;
    protected DataSource dataSource;

    private String protectedTab = "";
    private VLayout vlayout;

    public LoginDialogWidget(String _protectedTab) {
        protectedTab = _protectedTab;

        // Create transactions
        setTitle("Login");
        setWidth(POPUP_WIDTH); setHeight(POPUP_HEIGHT);
        setShowResizeBar(false);
        setAutoCenter(true);
        setMembersMargin(10);
        setLayoutMargin(10);


        // Set DataSource (link to backend)
        dataSource = new DataSource();
        dataSource.setID("login");
        DataSourceTextField firstNameField = new DataSourceTextField("username", "Username", 50, true);
        DataSourcePasswordField passwordField = new DataSourcePasswordField("password", "Password", 50, true);
        dataSource.setFields(firstNameField, passwordField);


        vlayout = new VLayout();
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
                logMeIn((String)form.getField("username").getValue(), (String)form.getField("password").getValue());
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
        buttonLayout.setLayoutMargin(10);
        return buttonLayout;
    }

    /**
     * Perform login scripts.
     * If the call to server fails, an error message is added to the existing login dialog.
     * If the call succeeds, the login itself can have succeeded or failed. The status of the login attempt
     * is relayed from the server using a Boolean.
     */
    protected void logMeIn(String username, String password){
        // call to server to log the user
        LoginServiceAsync service = GWT.create(LoginService.class);

        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                // If the call to the server fails
                // logger.debug(getClassName() + ": " + "logMeIn");
                Label errLabel = createErrorLabel("Could not contact the server for authentification. Please contact the system administrator.");
                vlayout.addMember(errLabel, 1);
                vlayout.redraw();
                setAutoSize(true);
            }
            @Override
            public void onSuccess(Boolean loggedIn) {
                if (loggedIn){
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getAdminTabCode()));
                    // TODO display a "you are logged in as admin" text somewhere in a menu bar
                    close(); // close the popup

                } else {
                    // call to server went through but login failed
                    Label errLabel = createErrorLabel("Incorrect password or username. Nice try though.");
                    vlayout.addMember(errLabel);
                    vlayout.redraw();
                    setAutoSize(true);
                }
            }
        };
        service.logMeIn(username, password, callback);
    }

    private Label createErrorLabel(String msg){
        Label errorMsg = new Label();
        errorMsg.setWidth(POPUP_WIDTH - 20);
        errorMsg.setHeight(45);
        errorMsg.setLayoutAlign(Alignment.CENTER);
        errorMsg.setPadding(10);
        errorMsg.setContents("<span style='color: "+ "red" + "'>" + msg + "</span>");
        return errorMsg;
    }

}
