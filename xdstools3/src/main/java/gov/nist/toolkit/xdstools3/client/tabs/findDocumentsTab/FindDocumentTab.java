package gov.nist.toolkit.xdstools3.client.tabs.findDocumentsTab;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.customWidgets.validationOutput.ValidationSummaryWidget;


public class FindDocumentTab extends GenericCloseableTab {
	static String header = "Find Documents";

	public FindDocumentTab() { 
		super(header);

        // Set tab header
        setHeader(header);

		createContents();
	}

    /**
     * Creates and sets the contents of the FindDocuments Tab
     */
	private void createContents(){
				EndpointWidget endpoints = new EndpointWidget();
				FindDocumentForm findDocs = new FindDocumentForm();
				ValidationSummaryWidget output = new ValidationSummaryWidget();
				
				HLayout upperPanel = new HLayout();
				upperPanel.addMembers(findDocs); // should also add endpoints
				//endpoints.setWidth("20%");

				VLayout findDocsPanel = new VLayout(); 
				findDocsPanel.setWidth100();
				findDocsPanel.setHeight100(); 
				findDocsPanel.addMembers(upperPanel, output);

                // dimensions
				upperPanel.setHeight("50%");
				output.setHeight("*");

                // calls a custom function that sets the contents and keeps the titles
				setContents(findDocsPanel);
	}


}  