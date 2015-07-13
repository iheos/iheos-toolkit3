package gov.nist.hit.ds.xdstools3.client.tabs.queryRetrieveTabs;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.hit.ds.xdstools3.client.customWidgets.TLSAndSAML.TLSAndSAMLForm;
import gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.select.EndpointWidget;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableToolTab;

public class FindFoldersTab extends GenericCloseableToolTab {
    private static final String header="Find Folders";
    private PatientIDWidget patientIDWidget;
    private EndpointWidget sites;
    private Button runBtn;

    public FindFoldersTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        Label l1=createSubtitle1("1. Enter Patient ID");
        patientIDWidget = new PatientIDWidget();
        patientIDWidget.setWidth(400);

        Label l2=createSubtitle1("2. Select site");
        sites = new EndpointWidget();

        Label l3=createSubtitle1("3. Select SAML and TLS options");
        TLSAndSAMLForm tlsAndSAMLForm=new TLSAndSAMLForm();

        runBtn=new Button("Run");
        runBtn.disable();

        vStack.addMembers(l1,patientIDWidget,l2, sites,l3,tlsAndSAMLForm,runBtn);

        bindUI();

        return vStack;
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getFindFoldersCode();
    }

    private void bindUI() {
        patientIDWidget.getField("patientID").addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent blurEvent) {
                if (patientIDWidget.isPidValueEntered() && sites.isEndpointValueSelected()) {
                    runBtn.enable();
                } else {
                    runBtn.disable();
                }
            }
        });
        sites.addSelectionChangedHandler(new SelectionChangedHandler() {
            @Override
            public void onSelectionChanged(SelectionEvent selectionEvent) {
                if (patientIDWidget.isPidValueEntered() && sites.isEndpointValueSelected()) {
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
