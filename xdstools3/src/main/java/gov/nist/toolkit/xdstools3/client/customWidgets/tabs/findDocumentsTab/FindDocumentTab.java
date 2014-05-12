package gov.nist.toolkit.xdstools3.client.customWidgets.tabs.findDocumentsTab;

import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.AllEndpointsWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.tabs.CloseableTabWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.validationOutput.ValidationSummaryWidget;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;


public class FindDocumentTab extends CloseableTabWidget {
	static String header = "Find Documents";

	public FindDocumentTab() { 
		super(header);

        // Set tab header
		setHeader(header);

        // set page title
        setTitle(header);

		setContents();
	}  
	
	private void setContents(){
				AllEndpointsWidget endpoints = new AllEndpointsWidget();
				FindDocumentForm findDocs = new FindDocumentForm();
				ValidationSummaryWidget output = new ValidationSummaryWidget();
				
				HLayout upperPanel = new HLayout();
				upperPanel.addMembers(endpoints, findDocs);
				endpoints.setWidth("20%");

				VLayout findDocsPanel = new VLayout(); 
				findDocsPanel.setWidth100();
				findDocsPanel.setHeight100(); 
				findDocsPanel.addMembers(upperPanel, output);
				upperPanel.setHeight("50%");
				output.setHeight("*");
				setPane(findDocsPanel);
	}


}  