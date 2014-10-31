package gov.nist.toolkit.xdstools3.client.tabs.testDataSubmissionTab;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSAndSAMLForm;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.select.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

/**
 * Created by onh2 on 10/30/2014.
 */
public class SubmitTestDataTab extends GenericCloseableTab {
    private final static String header = "Test Data Submission";

    public SubmitTestDataTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack container=new VStack();

        DynamicForm form=new DynamicForm();

        HeaderItem l1 = new HeaderItem();
        l1.setDefaultValue("1. Select Test Data Type");
        SelectItem testDataType = new SelectItem();
        testDataType.setWidth(400);
        testDataType.setEmptyDisplayValue("Select test data type...");
        testDataType.setTitle("Test Data Type");
        loadTestDataType();

        HeaderItem l2=new HeaderItem();
        l2.setDefaultValue("2. Select a Test Data Set");
        SelectItem testDataSetSelectItem = new SelectItem();
        testDataSetSelectItem.setWidth(400);
        testDataSetSelectItem.setTitle("Test Data Set");
        testDataSetSelectItem.setEmptyDisplayValue("Select test data set...");

        Label l3=createSubtitle1("3. Enter Patient ID");
        PatientIDWidget pid = new PatientIDWidget();
        pid.setWidth(400);

        Label l4 = createSubtitle1("4. Select a Document Repository");
        EndpointWidget docRepository = new EndpointWidget();


        Label l5=createSubtitle1("5. Select SAML and TLS options");
        TLSAndSAMLForm tlsAndSAMLForm = new TLSAndSAMLForm();

        Button runBtn = new Button("Submit");
        runBtn.disable();

        form.setFields(l1,testDataType,l2,testDataSetSelectItem);

        container.addMembers(form,l3,pid,l4,docRepository,l5,tlsAndSAMLForm,runBtn);
        return container;
    }

    private void loadTestDataType() {

    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getInstance().getTestDataSubmissionTabCode();
    }
}
