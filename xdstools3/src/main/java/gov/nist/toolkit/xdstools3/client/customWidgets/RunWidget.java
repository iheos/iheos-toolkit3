package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.ButtonItem;

/**
 * Creates a TextItem to hold a Patient ID, to be used in a form. The PID input is required.
 * The button is disabled until a ChangeEvent occurs in the required form item(s). 
 * @author dazais
 *
 */
public class RunWidget extends ButtonItem {
	
	public RunWidget(){
		setTitle("Run");
		setDisabled(true);  
	}
	
	

}
