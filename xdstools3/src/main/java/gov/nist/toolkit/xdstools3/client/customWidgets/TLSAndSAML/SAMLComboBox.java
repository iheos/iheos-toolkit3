package gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML;

import com.google.gwt.user.client.ui.ListBox;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;

/**
 * A simple SAML drop-down widget
 */
public class SAMLComboBox extends ComboBoxItem {

    public SAMLComboBox() {
        setValueMap("SAML off", "SAML on", "Do we have SAML in IHE?", "How many versions?");
        setValue("SAML off");
        setShowTitle(false);

    }
}
