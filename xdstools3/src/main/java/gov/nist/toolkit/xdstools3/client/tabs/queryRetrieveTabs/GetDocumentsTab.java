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

public class GetDocumentsTab extends GenericCloseableTab {

    private static final String header="Get Documents";
    private GenericTextItemWithTooltipWidget docEntryUUID;
    private EndpointWidget sites;
    private Button runBtn;

    public GetDocumentsTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        Label l1=createSubtitle1("1. Enter Document Entry UUID or UID");
        DynamicForm docEntryUUIDForm = new DynamicForm();
        docEntryUUID = new GenericTextItemWithTooltipWidget();
        docEntryUUID.setTitle("Document Entry UUID or UID");
        docEntryUUID.setWidth(400);
        docEntryUUIDForm.setFields(docEntryUUID);

        Label l2=createSubtitle1("2. Select site");
        sites = new EndpointWidget();

        Label l3=createSubtitle1("3. Select SAML and TLS options");
        TLSAndSAMLForm tlsAndSAMLForm=new TLSAndSAMLForm();

        runBtn=new Button("Run");
        runBtn.disable();

        vStack.addMembers(l1,docEntryUUIDForm,l2, sites,l3,tlsAndSAMLForm,runBtn);

        bindUI();

        return vStack;
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getGetDocumentsTabCode();
    }

    private void bindUI() {
        docEntryUUID.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent blurEvent) {
                if (!docEntryUUID.getValueAsString().isEmpty() && sites.isEndpointValueSelected()){
                    runBtn.enable();
                }else{
                    runBtn.disable();
                }
            }
        });
        sites.addSelectionChangedHandler(new SelectionChangedHandler() {
            @Override
            public void onSelectionChanged(SelectionEvent selectionEvent) {
                if (docEntryUUID.getValue() != null && sites.isEndpointValueSelected()) {
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
