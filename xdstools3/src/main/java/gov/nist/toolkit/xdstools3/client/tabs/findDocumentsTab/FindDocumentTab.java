package gov.nist.toolkit.xdstools3.client.tabs.findDocumentsTab;

import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.validationOutput.ValidationSummaryWidget;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;


public class FindDocumentTab extends GenericCloseableTab {
    static String header = "Find Documents";

    public FindDocumentTab() {
        super(header);

        // Set tab header
        setHeader(header);

        VStack stack = createContents();

        // calls a custom function that sets the contents and keeps the titles
        setContents(stack);
    }

    /**
     * Creates and sets the contents of the FindDocuments Tab
     */
    private VStack createContents(){
        // create components
        EndpointWidget endpoints = new EndpointWidget();
        FindDocumentForm findDocs = new FindDocumentForm();
        ValidationSummaryWidget output = new ValidationSummaryWidget();

        // Add to layout
        VStack findDocsPanel = new VStack();
        findDocsPanel.addMembers(findDocs, endpoints, output);

        return findDocsPanel;
    }


}  