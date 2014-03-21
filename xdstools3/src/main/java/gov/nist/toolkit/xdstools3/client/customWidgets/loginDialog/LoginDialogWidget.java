package gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog;

import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.CancelButton;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.LoginButton;

import java.awt.event.KeyListener;

import com.google.gwt.thirdparty.streamhtmlparser.util.EntityResolver.Status;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class LoginDialogWidget extends Window {

	protected TextItem userName;
	protected TextItem password;
	protected Button reset;
	protected Button login;
	protected Status status;
	protected KeyListener keyListener;

	public LoginDialogWidget() {
		
		setTitle("Login");
		setWidth(300); setHeight(150);
		setShowResizeBar(false);
		setAutoCenter(true);
		
		VLayout vlayout = new VLayout();
		vlayout.addMembers(createLoginForm(), createButtons());
		vlayout.setAlign(VerticalAlignment.CENTER);
		vlayout.setAlign(Alignment.CENTER);
		
		addItem(vlayout);
	}

	/**
	 * Creates the form that asks for username and password
	 */
	private DynamicForm createLoginForm() {
		DynamicForm form = new DynamicForm();
		
		userName = new TextItem();
		userName.setTitle("Username");
		//userName.addKaddKeyListener(keyListener);

		password = new PasswordItem();
		password.setTitle("Password");
		//password.addKeyListener(keyListener);
		
		form.setFields(new FormItem[] {userName, password});
		form.focusInItem(userName);
		return form;
	}
	
	/**
	 * Creates "Cancel" and "Login" buttons
	 * @return The HLayout containing the buttons
	 */
	private HLayout createButtons(){
		LoginButton login = new LoginButton();
		CancelButton cancel = new CancelButton();
		HLayout buttonLayout = new HLayout();
		buttonLayout.addMembers(cancel, login);
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setMembersMargin(10);
		return buttonLayout;
	}


	protected void onSubmit() {
	
	}

	protected boolean hasValue(TextItem field) {
		return field.getValue() != null;
	}

	protected void validateLogin() {
		//login.setEnabled(hasValue(userName) && hasValue(password));
	}


}
