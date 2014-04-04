package gov.nist.toolkit.xdstools3.client.events;

import java.util.ArrayList;

import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;

/**
 * Checks whether a form field was filled out and makes a "run", "submit" or "validate" button disabled or enabled.
 * @author dazais
 *
 */
public class FieldFilledOutHandler implements ChangeHandler {
	ArrayList<TextItem> fieldtab;

	public FieldFilledOutHandler(ArrayList<TextItem> _fieldtab){
		fieldtab=_fieldtab;
	}

	@Override
	public void onChange(ChangeEvent event) {
		for (TextItem field:fieldtab){
			if (!field._getValue().equals("")) {
				
		}
		}
		
	}
	
	

}
