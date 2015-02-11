package gov.nist.hit.ds.xdstools3.client.customWidgets.TLSAndSAML;

import com.google.gwt.user.client.ui.ListBox;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;

/**
 * A simple SAML drop-down widget. It is set to be alone on a form line, by default.
 */
public class SAMLComboBox extends ComboBoxItem {

    public SAMLComboBox() {
        setValueMap("SAML off", "SAML on", "Do we have SAML in IHE?", "How many versions?");
        setValue("SAML off");
        setShowTitle(false);
        setEndRow(true);

    }
}
