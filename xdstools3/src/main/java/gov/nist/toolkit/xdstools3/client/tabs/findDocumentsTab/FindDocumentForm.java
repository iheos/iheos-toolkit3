package gov.nist.toolkit.xdstools3.client.tabs.findDocumentsTab;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import gov.nist.toolkit.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.SAMLComboBox;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSCheckbox;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.RunButton;

public class FindDocumentForm extends DynamicForm {

	public FindDocumentForm(){
		setCellPadding(15);
		setNumCols(3);

		createInputs();
	}

	/**
	 * Creates fields
	 */
	public void createInputs() {
        // A spacer item to use where needed
        SpacerItem space = new SpacerItem();

		PatientIDWidget pid = new PatientIDWidget();

        // TLS and SAML
        TLSCheckbox tls = new TLSCheckbox(); tls.setEndRow(true);
        SAMLComboBox saml = new SAMLComboBox(); saml.setEndRow(true);
		
		CheckboxItem includeOnDemand = new CheckboxItem("includeOnDemand");  
		includeOnDemand.setTitle("Include On-Demand document entries");
		
		final RunButton runButton = new RunButton();
		setFields(new FormItem[] {pid, tls, space, saml, includeOnDemand, runButton});
		
		// Event handlers
		 pid.addChangeHandler(new ChangeHandler() {  
	            public void onChange(ChangeEvent event) {  
	            	runButton.setDisabled(false);  // re-enable button Run when required fields are completed
	            }  
	        });
	}

}
