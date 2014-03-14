package gov.nist.toolkit.xdstools3.client.customWidgets;

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

	public void createInputs() {
		PatientIDWidget pid = new PatientIDWidget();

		// Link to Patient ID Generator
        LinkItem linkItem = new LinkItem("link");  //URL 
        linkItem.setLinkTitle("Patient ID generator");
        linkItem.setStartRow(false);
        linkItem.setShowTitle(false);
        linkItem.setHeight(15);
        linkItem.setVAlign(VerticalAlignment.CENTER);
		
		CheckboxItem includeOnDemand = new CheckboxItem("includeOnDemand");  
		includeOnDemand.setTitle("Include on-demand document entries"); 
		
		final RunWidget runButton = new RunWidget();
		setFields(new FormItem[] {pid, linkItem, includeOnDemand, runButton});
		
		// Event handlers
		 pid.addChangeHandler(new ChangeHandler() {  
	            public void onChange(ChangeEvent event) {  
	            	runButton.setDisabled(false);  // re-enable button Run when required fields are completed
	            }  
	        });
	}
	
	


}
