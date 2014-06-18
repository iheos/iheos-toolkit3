package gov.nist.toolkit.xdstools3.client.tabs.MPQTab;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.SAMLComboBox;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSCheckbox;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.GenericRunButton;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.forms.GenericForm;

/**
 * Created by dazais on 5/14/2014.
 */
public class MPQCanvas extends VLayout {

    public MPQCanvas(){
        // Actors widget, TLS and SAML options
        VStack up = createLayout();
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

    public VStack createLayout(){
        // Actors
        // TODO needs an actors widget with only Registries
        Label label1 = new Label("Step 1: Select Actors");
        label1.setStyleName("h5");
        EndpointWidget endpoints = new EndpointWidget();
        VStack actorsLayout = new VStack();
        actorsLayout.addMembers(label1, endpoints);
       // actorsLayout.setAlign(Alignment.CENTER);

        // TLS / SAML
        Label label2 = new Label("Step 2: Select TLS and SAML options");
        TLSCheckbox tls = new TLSCheckbox();
        SAMLComboBox saml = new SAMLComboBox();
        GenericForm formTls = new GenericForm();
        formTls.setFields(tls, saml);
        VStack tlsLayout = new VStack();
        tlsLayout.addMembers(label2, formTls);

        // layout
        VStack upper = new VStack();
        upper.addMembers(actorsLayout, tlsLayout);
        upper.setWidth100();
       // upper.setAlign(Alignment.CENTER);
        return upper;
    }

    public VLayout createLowerLayout(){
        Label label3 = new Label("Step 3: Enter PID, OR select one or more Code Values");

        // PID
        PatientIDWidget pid = new PatientIDWidget();
        GenericForm formPid = new GenericForm();
        formPid.setFields(pid);


        // Create the Codes grids
        HealthcareCodesWidget codeGridsWidget = new HealthcareCodesWidget();

        // Run button
        final GenericRunButton runButton = new GenericRunButton();
        DynamicForm runButtonForm = new DynamicForm();
        runButtonForm.setFields(runButton);

        // layout of the 3rd step - lower part of screen
        VLayout lower = new VLayout();
        lower.setWidth100();
        lower.addMembers(label3, formPid, codeGridsWidget, runButtonForm);

        return lower;
    }
}
