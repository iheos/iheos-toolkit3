package gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;

/**
 * Created by dazais on 6/20/2014.
 */
public class TLSAndSAMLForm extends DynamicForm {

    public TLSAndSAMLForm(){
        TLSCheckbox tls = new TLSCheckbox(); tls.setEndRow(true);
        SAMLComboBox saml = new SAMLComboBox(); saml.setEndRow(true);
        SpacerItem space = new SpacerItem();
        setFields(new FormItem[]{tls, space, saml});
        setCellPadding(10);
    }
}
