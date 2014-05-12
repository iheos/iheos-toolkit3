package gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML;

import com.smartgwt.client.widgets.form.fields.CheckboxItem;

/**
 * A simple TLS checkbox widget.
 * TLS is checked by default since it is more commonly used than no TLS.
 */
public class TLSCheckbox extends CheckboxItem {

    public TLSCheckbox() {
        setTitle("TLS");
        setWidth("40px");
        setValue(true);
    }
}
