package gov.nist.toolkit.xdstools3.client.customWidgets.tabs.MPQTab;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.toolkit.xdstools3.client.RESTClient.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.SAMLComboBox;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSCheckbox;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.RunButton;
import gov.nist.toolkit.xdstools3.client.customWidgets.forms.FormattedDynamicForm;

/**
 * Created by dazais on 5/14/2014.
 */
public class MPQCanvas extends VLayout {

    public MPQCanvas(){
        // Actors widget, TLS and SAML options
        HLayout up = createUpperLayout();
        up.setMembersMargin(10);

        // PID and Codes
        VLayout down = createLowerLayout();
        down.setMembersMargin(10);

       this.addMembers(up, down);
        setLayoutMargin(60);

//        // Event handlers
//        pid.addChangeHandler(new ChangeHandler() {
//            public void onChange(ChangeEvent event) {
//                runButton.setDisabled(false);  // re-enable button Run when required fields are completed
//            }
//        });
    }

    public HLayout createUpperLayout(){
        // Actors
        // TODO needs an actors widget with only Registries
        Label label1 = new Label("Step 1: Select Actors");
        EndpointWidget endpoints = new EndpointWidget();
        VLayout actorsLayout = new VLayout();
        actorsLayout.addMembers(label1); // should also add endpoints
        actorsLayout.setAlign(Alignment.CENTER);

        // middle spacer
        LayoutSpacer spacer = new LayoutSpacer();
        spacer.setWidth("100");

        // TLS / SAML
        Label label2 = new Label("Step 2: Select TLS and SAML options");
        TLSCheckbox tls = new TLSCheckbox();
        SAMLComboBox saml = new SAMLComboBox();
        FormattedDynamicForm formTls = new FormattedDynamicForm();
        formTls.setFields(tls, saml);
        VLayout tlsLayout = new VLayout();
        tlsLayout.addMembers(label2, formTls);

        // layout
        HLayout upper = new HLayout();
        upper.addMembers(actorsLayout, spacer, tlsLayout);
        upper.setWidth100();
        upper.setAlign(Alignment.CENTER);
        return upper;
    }

    public VLayout createLowerLayout(){
        Label label3 = new Label("Step 3: Enter PID, OR select one or more Code Values");

        // PID
        PatientIDWidget pid = new PatientIDWidget();
        FormattedDynamicForm formPid = new FormattedDynamicForm();
        formPid.setFields(pid);


        // Create the Codes grids
        HealthcareCodesWidget codeGridsWidget = new HealthcareCodesWidget();

        // Run button
        final RunButton runButton = new RunButton();
        DynamicForm runButtonForm = new DynamicForm();
        runButtonForm.setFields(runButton);

        // layout of the 3rd step - lower part of screen
        VLayout lower = new VLayout();
        lower.setWidth100();
        lower.addMembers(label3, formPid, codeGridsWidget, runButtonForm);

        return lower;
    }
}
