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

public class RetrieveDocumentsTab extends GenericCloseableTab {

    private static final String header="Retrieve Documents";
    private GenericTextItemWithTooltipWidget docUID;
    private EndpointWidget repositories;
    private Button runBtn;

    public RetrieveDocumentsTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        Label l1=createSubtitle1("1. Enter Document UID");
        DynamicForm docUIDForm = new DynamicForm();
        docUID = new GenericTextItemWithTooltipWidget();
        docUID.setTitle("Document UID");
        docUID.setWidth(400);
        docUIDForm.setFields(docUID);

        Label l2=createSubtitle1("2. Select Repository");
        repositories = new EndpointWidget();
//        repositories.isEndpointValueSelected()

        Label l3=createSubtitle1("3. Select SAML and TLS options");
        TLSAndSAMLForm tlsAndSAMLForm=new TLSAndSAMLForm();

        runBtn=new Button("Run");
        runBtn.disable();

        vStack.addMembers(l1,docUIDForm,l2, repositories,l3,tlsAndSAMLForm,runBtn);

        bindUI();

        return vStack;
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getInstance().getRetrieveDocumentTabCode();
    }

    private void bindUI() {
        docUID.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent blurEvent) {
                if (!docUID.getValueAsString().isEmpty() && repositories.isEndpointValueSelected()) {
                    runBtn.enable();
                } else {
                    runBtn.disable();
                }
            }
        });
        repositories.addSelectionChangedHandler(new SelectionChangedHandler() {
            @Override
            public void onSelectionChanged(SelectionEvent selectionEvent) {
                if (docUID.getValue() != null && repositories.isEndpointValueSelected()) {
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
