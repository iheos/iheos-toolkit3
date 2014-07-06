package gov.nist.toolkit.xdstools3.client.customWidgets.buttons;

import com.smartgwt.client.widgets.form.fields.ButtonItem;

/**
 * Creates a TextItem to hold a Patient ID, to be used in a form. The PID input is required.
 * The button is disabled until a ChangeEvent occurs in the required form item(s). 
 * @author dazais
 *
 */
public class GenericRunButton extends ButtonItem {
	
	public GenericRunButton(){
		setTitle("Run");
		setDisabled(true);  
	}
	
	

}
