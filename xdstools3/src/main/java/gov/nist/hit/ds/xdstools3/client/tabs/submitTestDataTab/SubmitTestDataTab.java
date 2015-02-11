package gov.nist.hit.ds.xdstools3.client.tabs.submitTestDataTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.FocusChangedEvent;
import com.smartgwt.client.widgets.events.FocusChangedHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableToolTab;
import gov.nist.hit.ds.xdstools3.client.customWidgets.TLSAndSAML.TLSAndSAMLForm;
import gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.select.EndpointWidget;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Tab implementation to submit test data, handle Repository Test data,
 * Registry Test Data and MHD Test Data.
 */
public class SubmitTestDataTab extends GenericCloseableToolTab {
    private Logger logger=Logger.getLogger(SubmitTestDataTab.class.getName());
    // RPC Services declaration
    private final static TestDataSubmissionServicesAsync testDataSubmissionServices = GWT
            .create(TestDataSubmissionServices.class);

    // tab title and header
    private final static String header = "Submit Test Data";

    // Tab components
    private SelectItem testDataType;
    private SelectItem testDataSetSelectItem;
    private PatientIDWidget pid;
    private EndpointWidget docRepository;
    private TLSAndSAMLForm tlsAndSAMLForm;
    private Button runBtn;

    // Variables
    private String selectedDataTestSet;

    /**
     * Default constructor
     */
    public SubmitTestDataTab() {
        super(header);
        bindUI();
    }

    /**
     * Abstract method implementation to build the tab UI
     *
     * @return tab UI as a Widget
     */
    @Override
    protected Widget createContents() {
        VStack container=new VStack();

        DynamicForm form=new DynamicForm();

        // Test data type transactions
        HeaderItem l1 = new HeaderItem();
        l1.setDefaultValue("1. Select Test Data Type");
        testDataType = new SelectItem();
        testDataType.setType("comboBox");
        testDataType.setWidth(400);
        testDataType.setEmptyDisplayValue("Select test data type...");
        testDataType.setTitle("Test Data Type");
        loadTestDataType();
        // select registry as default value
        testDataType.setDefaultValue("reg");

        // Test data set transactions
        HeaderItem l2=new HeaderItem();
        l2.setDefaultValue("2. Select a Test Data Set");
        testDataSetSelectItem = new SelectItem();
        // display the SelectItem as a comboBox if not using this the comboBox might not dropdown
        testDataSetSelectItem.setType("comboBox");
        testDataSetSelectItem.setWidth(400);
        testDataSetSelectItem.setTitle("Test Data Set");
        testDataSetSelectItem.setEmptyDisplayValue("Select test data set...");
        loadTestDataSet();

        // Patient ID transactions
        Label l3=createSubtitle1("3. Enter Patient ID");
        pid = new PatientIDWidget();

        // Document repository transactions
        Label l4 = createSubtitle1("4. Select a Document Repository");
        docRepository = new EndpointWidget();

        // TLS and SAML transactions
        Label l5=createSubtitle1("5. Select SAML and TLS options");
        tlsAndSAMLForm = new TLSAndSAMLForm();

        runBtn = new Button("Submit");
        runBtn.disable();

        form.setFields(l1, testDataType,l2, testDataSetSelectItem);
        form.setCellPadding(15);

        container.addMembers(form,l3, pid,l4, docRepository,l5, tlsAndSAMLForm, runBtn);
        return container;
    }

    /**
     * Method to load Test Data Set using RPC to call upon a distant method
     */
    private void loadTestDataSet() {
        testDataSubmissionServices.retrieveTestDataSet("",new AsyncCallback<Map<String,String>>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.warning("Error: "+caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String,String> result) {
                LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
                map.putAll(result);
                testDataSetSelectItem.setValueMap(map);
            }
        });
    }

    /**
     * Method that binds the tab widgets together
     */
    private void bindUI(){
        // set variable when a test data set is selected and enable run button if all field are filled
        testDataSetSelectItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent changedEvent) {
                selectedDataTestSet= (String) changedEvent.getValue();
                if(selectedDataTestSet!=null && !selectedDataTestSet.isEmpty()){
                    if (pid.isPidValueEntered() && docRepository.isEndpointValueSelected()){
                        runBtn.enable();
                    }
                }
            }
        });
        // enable run button when patient id is filled if all field are filled
        pid.addFocusChangedHandler(new FocusChangedHandler() {
            @Override
            public void onFocusChanged(FocusChangedEvent focusChangedEvent) {
                if(pid.isPidValueEntered() && docRepository.isEndpointValueSelected() && selectedDataTestSet!=null && !selectedDataTestSet.isEmpty()){
                    runBtn.enable();
                }
            }
        });
        // enable run button when a document repository is selected if all other fields are filled
        docRepository.addSelectionChangedHandler(new SelectionChangedHandler() {
            @Override
            public void onSelectionChanged(SelectionEvent selectionEvent) {
                if(pid.isPidValueEntered() && docRepository.isEndpointValueSelected() && selectedDataTestSet!=null && !selectedDataTestSet.isEmpty()){
                    runBtn.enable();
                }
            }
        });
        runBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                String selectedTestDataType = testDataType.getValueAsString();
                String pidValue=pid.getValue();
                testDataSubmissionServices.submitTestData(selectedTestDataType, selectedDataTestSet, pidValue, "repository", tlsAndSAMLForm.isTLSChecked(),tlsAndSAMLForm.getSAMLValue(),new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        // TODO error meessage
                    }

                    @Override
                    public void onSuccess(String result) {

                    }
                });
            }
        });
    }

    /**
     * Method that set possible test data types
     */
    private void loadTestDataType() {
        LinkedHashMap<String,String> testTypeMap=new LinkedHashMap<String,String>();
        testTypeMap.put("reg","Registry");
        testTypeMap.put("rep","Repository");
        testTypeMap.put("mhd","MHD");
        testDataType.setValueMap(testTypeMap);
    }

    /**
     * Abstract method implementation to set the tab name (defined in {@link gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager}).
     *
     * @return tab name
     */
    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getTestDataSubmissionTabCode();
    }
}
