package gov.nist.toolkit.xdstools3.client.tabs.MPQTab;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSAndSAMLForm;
import gov.nist.toolkit.xdstools3.client.customWidgets.buttons.GenericRunButtonNoForm;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.smartgwt.select.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.healthcareCodes.HealthcareCodeWidget;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

/**
 * Created by dazais on 5/14/2014.
 */
public class MPQTab extends GenericCloseableTab {
    static String header = "Multi-Patient Find Documents (MPQ)";

    public MPQTab() {
        super(header);
    }

    @Override
    protected VStack createContents(){
        // layout
        final VStack mpqPanel = new VStack();

        // PID
        Label label1 = createSubtitle1("Step 1: Enter PID, OR select one or more Code Values");
        PatientIDWidget pid = new PatientIDWidget();
        // Create the Code grids
        HealthcareCodeWidget codeGridsWidget = new HealthcareCodeWidget();
        HStack pidAndCodes = new HStack();
        pidAndCodes.addMembers(pid, codeGridsWidget);

        // Actors
        // TODO needs an actors widget with only Registries
        Label label2 = createSubtitle1("Step 2: Select Actors");
        EndpointWidget endpoints = new EndpointWidget();

        // TLS / SAML
        Label label3 = createSubtitle1("Step 3: Select TLS and SAML options");
        TLSAndSAMLForm tlsSaml = new TLSAndSAMLForm();

        // Run button
        final GenericRunButtonNoForm runButton = new GenericRunButtonNoForm();

        // Add to layout
        mpqPanel.addMembers(label1, pid, codeGridsWidget, label2, endpoints, label3, tlsSaml, runButton);

        // Add listeners

        return mpqPanel;
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getInstance().getMpqFindDocumentsTabCode();
    }

}
