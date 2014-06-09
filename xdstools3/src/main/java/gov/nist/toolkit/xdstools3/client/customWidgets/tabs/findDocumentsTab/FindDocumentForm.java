package gov.nist.toolkit.xdstools3.client.customWidgets.tabs.findDocumentsTab;

import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import gov.nist.toolkit.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.SAMLComboBox;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSCheckbox;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.RunButton;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.LinkItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;

public class FindDocumentForm extends DynamicForm {

	public FindDocumentForm(){
		setPadding(10);
		setCellPadding(10);
		setOverflow(Overflow.AUTO);
		setNumCols(3);

		createInputs();
	}

	/**
	 * Creates the fields
	 */
	public void createInputs() {
        // A spacer item to use where needed
        SpacerItem space = new SpacerItem();

		PatientIDWidget pid = new PatientIDWidget();

		// External link to Patient ID Generator
        LinkItem linkItem = new LinkItem("pidGenerator");
        linkItem.setLinkTitle("Patient ID generator");
        linkItem.setStartRow(false);
        linkItem.setShowTitle(false);
        linkItem.setHeight(15);
        linkItem.setVAlign(VerticalAlignment.CENTER);
        linkItem.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
               Window.open("http://ihexds.nist.gov:12080/xdstools/pidallocate", "", "");
            }
        });

        // TLS and SAML
        TLSCheckbox tls = new TLSCheckbox(); tls.setEndRow(true);
        SAMLComboBox saml = new SAMLComboBox(); saml.setEndRow(true);
		
		CheckboxItem includeOnDemand = new CheckboxItem("includeOnDemand");  
		includeOnDemand.setTitle("Include On-Demand document entries");
		
		final RunButton runButton = new RunButton();
		setFields(new FormItem[] {pid, linkItem, tls, space, saml, includeOnDemand, runButton});
		
		// Event handlers
		 pid.addChangeHandler(new ChangeHandler() {  
	            public void onChange(ChangeEvent event) {  
	            	runButton.setDisabled(false);  // re-enable button Run when required fields are completed
	            }  
	        });
	}

}
