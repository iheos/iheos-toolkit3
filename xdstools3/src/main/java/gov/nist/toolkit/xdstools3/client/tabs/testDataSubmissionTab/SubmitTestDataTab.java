package gov.nist.toolkit.xdstools3.client.tabs.testDataSubmissionTab;

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
import gov.nist.toolkit.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSAndSAMLForm;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.select.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.tabs.mhdTabs.MHDTabsServices;
import gov.nist.toolkit.xdstools3.client.tabs.mhdTabs.MHDTabsServicesAsync;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

import java.util.LinkedHashMap;

public class SubmitTestDataTab extends GenericCloseableTab {
    private final static String header = "Test Data Submission";

    private final static TestDataSubmissionServicesAsync testDataSubmissionServices = GWT
            .create(TestDataSubmissionServices.class);

    private SelectItem testDataType;
    private SelectItem testDataSetSelectItem;
    private PatientIDWidget pid;
    private EndpointWidget docRepository;
    private TLSAndSAMLForm tlsAndSAMLForm;
    private Button runBtn;
    private String selectedDataTestSet;

    public SubmitTestDataTab() {
        super(header);
        bindUI();
    }

    @Override
    protected Widget createContents() {
        VStack container=new VStack();

        DynamicForm form=new DynamicForm();

        HeaderItem l1 = new HeaderItem();
        l1.setDefaultValue("1. Select Test Data Type");
        testDataType = new SelectItem();
        testDataType.setWidth(400);
        testDataType.setEmptyDisplayValue("Select test data type...");
        testDataType.setTitle("Test Data Type");
        loadTestDataType();
        testDataType.setDefaultValue("reg");

        HeaderItem l2=new HeaderItem();
        l2.setDefaultValue("2. Select a Test Data Set");
        testDataSetSelectItem = new SelectItem();
        testDataSetSelectItem.setWidth(400);
        testDataSetSelectItem.setTitle("Test Data Set");
        testDataSetSelectItem.setEmptyDisplayValue("Select test data set...");

        Label l3=createSubtitle1("3. Enter Patient ID");
        pid = new PatientIDWidget();

        Label l4 = createSubtitle1("4. Select a Document Repository");
        docRepository = new EndpointWidget();

        Label l5=createSubtitle1("5. Select SAML and TLS options");
        tlsAndSAMLForm = new TLSAndSAMLForm();

        runBtn = new Button("Submit");
        runBtn.disable();

        form.setFields(l1, testDataType,l2, testDataSetSelectItem);

        container.addMembers(form,l3, pid,l4, /*docRepository,*/l5, tlsAndSAMLForm, runBtn);
        return container;
    }

    private void bindUI(){
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
        pid.addFocusChangedHandler(new FocusChangedHandler() {
            @Override
            public void onFocusChanged(FocusChangedEvent focusChangedEvent) {
                if(pid.isPidValueEntered() && docRepository.isEndpointValueSelected() && selectedDataTestSet!=null && !selectedDataTestSet.isEmpty()){
                    runBtn.enable();
                }
            }
        });
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

    private void loadTestDataType() {
        LinkedHashMap<String,String> testTypeMap=new LinkedHashMap<String,String>();
        testTypeMap.put("reg","Registry");
        testTypeMap.put("rep","Repository");
        testTypeMap.put("mhd","MHD");
        testDataType.setValueMap(testTypeMap);
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getInstance().getTestDataSubmissionTabCode();
    }
}
