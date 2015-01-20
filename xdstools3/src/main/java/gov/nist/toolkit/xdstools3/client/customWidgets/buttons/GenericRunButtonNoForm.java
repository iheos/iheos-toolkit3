package gov.nist.toolkit.xdstools3.client.customWidgets.buttons;

import com.smartgwt.client.widgets.Button;

/**
 * Creates a generic button, disabled by default, to become re-enabled when all required fields of a tab are filled out.
 * @author dazais
 *
 */
public class GenericRunButtonNoForm extends Button {
	
	public GenericRunButtonNoForm(){
		setTitle("Run");
		setMargin(10);
		setHeight(42);
        setDisabled(true);
		}
}
