package gov.nist.toolkit.xdstools3.client.customWidgets.loginDialog;

import com.smartgwt.client.types.Alignment;
import gov.nist.toolkit.xdstools3.client.customWidgets.forms.GenericForm;

/**
 * Holds the formatting for a form used in the AdminSettings tab.
 *
 * Created by dazais on 10/30/2014.
 */
public class AdminSettingsForm extends GenericForm {

    public AdminSettingsForm(){
        setWidth(600);
        setLayoutAlign(Alignment.CENTER);
        setNumCols(2);
        setColWidths(150);
    }

}
