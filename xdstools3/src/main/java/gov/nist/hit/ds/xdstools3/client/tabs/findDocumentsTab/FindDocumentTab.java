package gov.nist.hit.ds.xdstools3.client.tabs.findDocumentsTab;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.hit.ds.xdstools3.client.customWidgets.TLSAndSAML.SAMLComboBox;
import gov.nist.hit.ds.xdstools3.client.customWidgets.TLSAndSAML.TLSCheckbox;
import gov.nist.hit.ds.xdstools3.client.customWidgets.buttons.GenericRunButtonNoForm;
import gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.select.EndpointWidget;
import gov.nist.hit.ds.xdstools3.client.customWidgets.validationOutput.ValidationSummaryWidget;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableToolTab;


public class FindDocumentTab extends GenericCloseableToolTab {
    static String header = "Find Documents";

    // Global class fields
    PatientIDWidget pid;
    EndpointWidget endpoints;
    GenericRunButtonNoForm runButton;



    public FindDocumentTab() {
        super(header);
    }

    /**
     * Creates and sets the contents of the FindDocuments Tab
     */
    @Override
    protected VStack createContents(){
        pid = new PatientIDWidget();
        endpoints = new EndpointWidget();
        runButton = new GenericRunButtonNoForm();


        // layout
        final VStack findDocsPanel = new VStack();

        // create components
        Label l1 = createSubtitle1("Step 1: Enter Patient ID");

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

        ValidationSummaryWidget output = new ValidationSummaryWidget();

        // Add to layout
        findDocsPanel.addMembers(l1, pid, l2, options, l3, endpoints, runButton, output); //new CodedTermEditableGridWidget()



        // -------------- Add listeners --------------

        // Enable Run button if all fields were filled out
        pid.addItemChangedHandler(new ItemChangedHandler() {
            @Override
            public void onItemChanged(ItemChangedEvent itemChangedEvent) {
                checkFieldsAndEnableRunButton();
            }
        });

        // Enable Run button if all fields were filled out
        endpoints.addSelectionChangedHandler(new SelectionChangedHandler() {
            @Override
            public void onSelectionChanged(SelectionEvent selectionEvent) {
                checkFieldsAndEnableRunButton();
            }
        });

        // Runs the query if all fields are filled out
        runButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                    runQuery();
            }
        });
        return findDocsPanel;
    }

    /**
     * Utility function that checks if all fields are filled out before enabling the Run button
     */
    private void checkFieldsAndEnableRunButton() {
        if (pid.isPidValueEntered() && endpoints.isEndpointValueSelected()) {
            runButton.enable();
        }
        if (!pid.isPidValueEntered() || !endpoints.isEndpointValueSelected()) {
            runButton.disable();
        }
    }

    private void runQuery(){

    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getFindDocumentsTabCode();
    }

}
