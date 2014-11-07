package gov.nist.toolkit.xdstools3.client.tabs.queryRetrieveTabs;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.GenericTextItemWithTooltipWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSAndSAMLForm;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.select.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

public class GetSubmissionSetAndContentsTab extends GenericCloseableTab {

    private static final String header="Get Submission Set And Contents";

    private GenericTextItemWithTooltipWidget subSetUUID;
    private EndpointWidget sites;
    private Button runBtn;

    public GetSubmissionSetAndContentsTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        Label l1=createSubtitle1("1. Enter Submission Set UUID or UID");
        DynamicForm subSetUUIDForm = new DynamicForm();
        subSetUUID = new GenericTextItemWithTooltipWidget();
        subSetUUID.setTitle("Submission Set UUID or UID");
        subSetUUID.setWidth(400);
        subSetUUIDForm.setFields(subSetUUID);

        Label l2=createSubtitle1("2. Select site");
        sites = new EndpointWidget();
//        sites.isEndpointValueSelected()

        Label l3=createSubtitle1("3. Select SAML and TLS options");
        TLSAndSAMLForm tlsAndSAMLForm=new TLSAndSAMLForm();

        runBtn=new Button("Run");
        runBtn.disable();

        vStack.addMembers(l1,subSetUUIDForm,l2, sites,l3,tlsAndSAMLForm,runBtn);

        bindUI();

        return vStack;
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getInstance().getGetSubmissionSetAndContentsTabCode();
    }

    private void bindUI() {
        subSetUUID.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent blurEvent) {
                if (!subSetUUID.getValueAsString().isEmpty() && sites.isEndpointValueSelected()) {
                    runBtn.enable();
                } else {
                    runBtn.disable();
                }
            }
        });
        sites.addSelectionChangedHandler(new SelectionChangedHandler() {
            @Override
            public void onSelectionChanged(SelectionEvent selectionEvent) {
                if (subSetUUID.getValue() != null && sites.isEndpointValueSelected()) {
                    runBtn.enable();
                } else {
                    runBtn.disable();
                }
            }
        });
        runBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                // TODO
            }
        });
    }
}
