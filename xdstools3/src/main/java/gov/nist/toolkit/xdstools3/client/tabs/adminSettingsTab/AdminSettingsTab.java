package gov.nist.toolkit.xdstools3.client.tabs.adminSettingsTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.SectionItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.MatchesFieldValidator;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.customWidgets.dialogs.PopupMessageV3;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;

/**
 * This tabs allows to edit the Administrator Settings / Toolkit Settings available through the back-end from a
 * properties file. This tab is being opened when the user logs in as administrator, through the Login Dialog.
 */
public class AdminSettingsTab extends GenericCloseableTab {
	private static String title = "Administrator Settings";
    private TextItem host, port, tls_port, cache, environment, gazelle;
    protected AdminSettingsServiceAsync rpcService = GWT.create(AdminSettingsService.class);


	public AdminSettingsTab() {
        super(title);
        populateFields();
    }



    @Override
    protected Widget createContents() {

        // ------  First form to hold most settings -----
        final AdminSettingsForm form = new AdminSettingsForm();

        host = createField("host", "Toolkit host", "transport-testing.nist.gov", 400);
        port = createField("port", "Toolkit port", "12080", 400);
        tls_port = createField("tls_port", "Toolkit TLS Port", "12081", 400);
        cache = createField("cache", "External cache", "Ex.: C:\\Workspace\\external_cache", 400);
        environment = createField("environment", "Default environment	", "Ex.: MU2014, NA2014...", 400);
        gazelle = createField("gazelle", "Gazelle config URL", "", 400);

        // Build form sections
        SectionItem section1 = new SectionItem();
        section1.setDefaultValue("General");
        section1.setSectionExpanded(true);
        section1.setItemIds("host", "port", "tls_port", "cache", "environment");

        final SectionItem section2 = new SectionItem();
        section2.setDefaultValue("Connectathon");
        section2.setSectionExpanded(true);
        section2.setItemIds("gazelle");

        form.setFields(section1, host, port, tls_port, cache, environment, section2, gazelle);



        // ------  Second form for password -----
        // Allows to implement client-side validation checks on the data entered in the password fields
        // without affecting the rest of the page (e.g. all fields must be filled out in that form,
        // but not necessarily in the other form).
        final AdminSettingsForm formPassword = new AdminSettingsForm();

        // Form items
        final PasswordItem oldPassword = createPasswordField("oldpassword", "Current password", 30);
        oldPassword.setRequired(true);
        final PasswordItem newPassword = createPasswordField("newpassword", "New password", 30);
        newPassword.setRequired(true);
        final PasswordItem newPasswordCheck = createPasswordField("newpasswordcheck", "Confirm new password", 30);
        newPasswordCheck.setRequired(true);

        // validator that checks that the "new password" fields match each other
        MatchesFieldValidator matchesValidator = new MatchesFieldValidator();
        matchesValidator.setOtherField("newpassword");
        matchesValidator.setErrorMessage("Passwords do not match");
        newPasswordCheck.setValidators(matchesValidator);

        // Form sections
        SectionItem section3 = new SectionItem();
        section3.setDefaultValue("Change administrator password");
        section3.setSectionExpanded(false);
        section3.setItemIds("oldpassword", "newpassword", "newpasswordcheck");

        formPassword.setFields(section3, oldPassword, newPassword, newPasswordCheck);

        // -------- Enclosing layout -------
        final VLayout layout = new VLayout(10);

        //------- Save Button --------
        final IButton saveButton = new IButton("Save");
        saveButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                String[] settings = {(String) host.getValue(), (String) port.getValue(), (String) tls_port.getValue(),
                        (String) cache.getValue(), (String) environment.getValue(), (String) gazelle.getValue()};

                // if password fields are null, save only the other form
                if (oldPassword.getValue() == null
                        && newPassword.getValue() == null
                        && newPasswordCheck.getValue() == null) {
                    saveAdminSettings(settings);
                } else {
                    if (formPassword.validate()) {
                            saveAdminSettings(settings);
                            saveAdminPassword((String) oldPassword.getValue(), (String) newPassword.getValue());
                        }
                    }
                }
        });


        // -------- Enclosing layout -------
        layout.addMembers(form, formPassword, saveButton);
        saveButton.setLayoutAlign(Alignment.CENTER);
        return layout;
    }



    /**
     * Populates the form fields by retrieving the Toolkit settings from the back-end.
     */
    protected void populateFields() {
        // FIXME why does this function trigger two updates of the admin settings? cf console log

       // -------- Populate forms with data from Toolkit configuration -------
        // Returns the following parameters in that order: host, port, tls_port, cache, environment, gazelleURL.
        AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
            public void onFailure(Throwable caught) {
                // logger.debug(getClassName() + ": " +"populateForms");
                new PopupMessageV3("Retrieving administrator settings from the server failed.");
            }

            @Override
            public void onSuccess(String[] result) {
                if (result.length != 6) new PopupMessageV3("Retrieving administrator settings from the server failed.");
                else {
                    host.setDefaultValue(result[0]);
                    port.setDefaultValue(result[1]);
                    tls_port.setDefaultValue(result[2]);
                    cache.setDefaultValue(result[3]);
                    environment.setDefaultValue(result[4]);
                    gazelle.setDefaultValue(result[5]);
                }
            }
        };
        rpcService.retrieveAdminSettings(callback);
    }

    /** Propagates changes in the admin settings to the back-end where they are ultimately saved to file
     *
     * @param settings the array of settings
     */
    protected void saveAdminSettings(String[] settings){

        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                // logger.debug(getClassName() + ": " +"saveAdminSettings");
                new PopupMessageV3("Saving administrator settings to server failed.");
            }

            @Override
            public void onSuccess(Void result) {
                // TODO close the tab. Maybe have both a "save" button and a "save and close" one?
                // TODO This is not urgent as the user can do it manually for now.
            }
        };
        rpcService.saveAdminSettings(settings, callback);
    }

    protected void saveAdminPassword(String oldPassword, String newPassword){

        AsyncCallback<Void> callback = new AsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                // logger.debug(getClassName() + ": " +"saveAdminSettings");
                new PopupMessageV3("Saving administrator settings to the server failed.");
            }

            @Override
            public void onSuccess(Void result) {
                // TODO close the tab. Maybe have both a "save" button and a "save and close" one?
                // TODO This is not urgent as the user can do it manually for now.
            }
        };
        rpcService.saveAdminPassword(oldPassword, newPassword, callback);
    }


    /**
     * Creates a form standard field based on the SmartGWT TextItem class.
     * @param name Reference of the field, in the code.
     * @param title Field title, displayed on the interface.
     * @param hint hint to help fill out the field, configured to be displayed inside the field itself
     * @param width desired width of the field, in pixels
     * @return
     */
    private TextItem createField(String name, String title, String hint, int width) {
        TextItem item = new TextItem();
        item.setName(name);
        item.setTitle(title);
        item.setHint(hint);
        item.setShowHintInField(true);
        item.setWidth(width);
        return item;
    }

    /**
     * Creates a form password field based on the SmartGWT PasswordItem class. The field's contents is hidden.
     * @param name the reference of the field, in the code
     * @param title the title of the field, displayed on the interface
     * @param length the length, in characters, of the expected password
     * @return a PasswordItem object, pre-configured
     */
    private PasswordItem createPasswordField(String name, String title, int length){
        PasswordItem item = new PasswordItem(name, title);
        item.setLength(length);
        return item;
    }


    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getAdminTabCode();
    }
}
