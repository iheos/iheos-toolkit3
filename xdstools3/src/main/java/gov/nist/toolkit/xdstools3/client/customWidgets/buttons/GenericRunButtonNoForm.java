package gov.nist.toolkit.xdstools3.client.customWidgets.buttons;

import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Creates a generic button, disabled by default, to become re-enabled when all required fields of a tab are filled out.
 * @author dazais
 *
 */
public class GenericRunButtonNoForm extends HLayout {
	Button run ;
	
	public GenericRunButtonNoForm(){
		run = new Button("Run");
        run.setDisabled(true);
		addChild(run);
		}


}
