package gov.nist.hit.ds.xdstools3.client.customWidgets.TLSAndSAML;

import com.smartgwt.client.widgets.form.fields.CheckboxItem;

/**
 * A simple TLS checkbox widget.
 * TLS is checked by default since it is more commonly used than no TLS. It is also set to be alone on a form line, by default.
 */
public class TLSCheckbox extends CheckboxItem {

    public TLSCheckbox() {
        setTitle("TLS");
        setValue(true);
       setEndRow(true);
        setStartRow(true);
    }
}
