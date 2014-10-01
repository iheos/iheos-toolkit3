package gov.nist.toolkit.xdstools3.client.tabs.findDocumentsTab;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.SAMLComboBox;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSCheckbox;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.GenericRunButtonNoForm;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.select.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.validationOutput.ValidationSummaryWidget;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;


public class FindDocumentTab extends GenericCloseableTab {
    static String header = "Find Documents";
    private boolean runButtonState = false; // the Run button is disabled until all required fields are filled

    public FindDocumentTab() {
        super(header);
    }

    /**
     * Creates and sets the contents of the FindDocuments Tab
     */
    @Override
    protected VStack createContents(){
        // layout
        final VStack findDocsPanel = new VStack();

        // create components
        Label l1 = createSubtitle1("Step 1: Enter Patient ID");
        final PatientIDWidget pid = new PatientIDWidget();

        Label l2 = createSubtitle1("Step 2: Select TLS / SAML / On-Demand options");
        TLSCheckbox tls = new TLSCheckbox(); tls.setEndRow(true);
        SAMLComboBox saml = new SAMLComboBox(); saml.setEndRow(true);
        CheckboxItem includeOnDemand = new CheckboxItem("includeOnDemand");
        includeOnDemand.setTitle("Include On-Demand document entries");
        SpacerItem space = new SpacerItem();
        DynamicForm options = new DynamicForm();
        options.setFields(tls, space, saml, includeOnDemand);
        options.setCellPadding(10);

        Label l3 = createSubtitle1("Step 3: Select Endpoint");
        final EndpointWidget endpoints = new EndpointWidget();

        ValidationSummaryWidget output = new ValidationSummaryWidget();
        final GenericRunButtonNoForm runButton = new GenericRunButtonNoForm();

        // Add to layout
        findDocsPanel.addMembers(l1, pid, l2, options, l3, endpoints, runButton, output);

        // Add listeners
        runButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                if (pid.isPidValueEntered() && endpoints.isEndpointValueSelected()){
                   // runButton.setDisabled(false);
                    runQuery();
                }

                // if yes, pass value to validation
//                                                    String newValue = (event.getNewValue() == null ? null : event.getNewValue().toString());
//                                                    disabledForm.getItem(FORM_FIELD_NAME).setValue(newValue);
            }
        });

        // get changes from pid and endpoints


        return findDocsPanel;
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getFindDocumentsTabCode();
    }

    // FIXME Unused is that normal?
    private void updateRunButtonState(){
        runButtonState = !runButtonState;
    }

    private void runQuery(){

    }

}
