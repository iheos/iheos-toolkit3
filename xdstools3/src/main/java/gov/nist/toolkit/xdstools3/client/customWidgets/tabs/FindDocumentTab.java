package gov.nist.toolkit.xdstools3.client.customWidgets.tabs;

import gov.nist.toolkit.xdstools3.client.customWidgets.FindDocumentForm;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.AllEndpointsWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.validationOutput.ValidationSummaryWidget;

import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;


public class FindDocumentTab extends CloseableTabWidget {
	String header = "Find Documents";

	public FindDocumentTab() { 
		super("Find Documents");

		// Set header and subtitles
		setHeader(header);

		// Contents
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