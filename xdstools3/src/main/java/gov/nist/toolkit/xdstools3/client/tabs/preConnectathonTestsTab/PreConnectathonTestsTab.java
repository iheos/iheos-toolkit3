package gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSAndSAMLForm;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by onh2 on 8/5/2014.
 */
public class PreConnectathonTestsTab extends GenericCloseableTab {
    static final Logger logger = Logger.getLogger(PreConnectathonTestsTab.class.getName());
    private final static ToolkitServiceAsync toolkitService = GWT
            .create(ToolkitService.class);
    private static String header = "Pre-Connectathon Tests";

    private DynamicForm form;
    private HTMLPane htmlReadme;

    public PreConnectathonTestsTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack = new VStack();
        form = new DynamicForm();

        SelectItem selectActor = new SelectItem();
        selectActor.setTitle("Select Actor");
        LinkedHashMap<String, String> actorsMap = loadActorsMap();
        selectActor.setValueMap(actorsMap);
        selectActor.setWidth(400);

        SelectItem selectTest = new SelectItem();
        selectTest.setTitle("Select Test");
        LinkedHashMap<String, String> testsMap = loadTestsMap();
        selectTest.setValueMap(testsMap);
        selectTest.setWidth(400);

        HTMLFlow beforeReadme = new HTMLFlow("<hr/>");
        htmlReadme = new HTMLPane();
        htmlReadme.setShowEdges(true);
        htmlReadme.setContents("<div style='padding-left:20px'>" +
                "<h3>README</h3>" +
                "Initialize XDS.b for Registry testing<br/>" +
                "<br/>" +
                "This test data is suitable for Stored Query testing only. It uses Register.b to load the Registry.<br/>" +
                "The Repository references in metadata are fake." +
                "</div>");
        HTMLFlow afterReadme = new HTMLFlow("<hr/>");

        form.setFields(selectActor, selectTest);

        PatientIDWidget patientID = new PatientIDWidget();
        TLSAndSAMLForm tLSAndSAMLForm = new TLSAndSAMLForm();

        vStack.addMembers(form, beforeReadme, htmlReadme, afterReadme, patientID, tLSAndSAMLForm);
        return vStack;
    }

    private LinkedHashMap<String, String> loadTestsMap() {
        final LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        return map;
    }

    private LinkedHashMap<String, String> loadActorsMap() {
        final LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        if (toolkitService == null)
            logger.info("SERVICE NULL");
        toolkitService.getCollectionNames("actorcollections", new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.warning(caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                if (result != null) {
                    map.putAll(result);
                }
            }
        });
        return map;
    }

//    private void loadTestReadme(){
//        result.replaceAll("\\n","<br/>");
//    }
}
