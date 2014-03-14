package gov.nist.toolkit.xdstools3.client.customWidgets;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * Creates a TextItem to hold a Patient ID, to be used in a form. The PID input is required.
 * @author dazais
 *
 */
public class RunWidgetNoForm extends HLayout {
	Button run ;
	
	public RunWidgetNoForm(){
		run = new Button("Run");
		run.setAlign(Alignment.CENTER);
		addChild(run);
		}


}
