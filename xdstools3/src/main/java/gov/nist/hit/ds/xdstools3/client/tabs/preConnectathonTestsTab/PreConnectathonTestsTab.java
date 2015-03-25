package gov.nist.hit.ds.xdstools3.client.tabs.preConnectathonTestsTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.customWidgets.PatientIDWidget;
import gov.nist.hit.ds.xdstools3.client.customWidgets.TLSAndSAML.TLSAndSAMLForm;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableToolTab;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PreConnectathonTestsTab extends GenericCloseableToolTab {
    static final Logger logger = Logger.getLogger(PreConnectathonTestsTab.class.getName());
    private final static PreConnectathonTabServiceAsync toolkitService = GWT
            .create(PreConnectathonTabService.class);


    private static String header = "Pre-Connectathon Tests";

    private DynamicForm form;
    private HTMLPane htmlReadme;
    private SelectItem selectActor;
    private SelectItem selectTest;
    private SelectItem selectSection;

    private VStack sectionPanel;


    private String selectedActor;
    private LinkedHashMap<String, String> actorMap;
    private HTMLFlow beforeReadme;
    private HTMLFlow afterReadme;

    public PreConnectathonTestsTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack = new VStack();
        form = new DynamicForm();

        selectActor = new SelectItem();
        selectActor.setTitle("Select Actor");
        selectActor.setName("actorItem");
        selectActor.setEmptyDisplayValue("Select author...");
        selectActor.setWidth(400);
        loadActorsMap();
        selectActor.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                selectedActor = (String) changeEvent.getValue();
                selectTest.clearValue();
                if (selectedActor == null)
                    return;
                if ("".equals(selectedActor))
                    return;
                loadTestsForActor(selectedActor);
            }
        });

        selectTest = new SelectItem();
        selectTest.setTitle("Select test");
        selectTest.setName("testItem");
        selectTest.setEmptyDisplayValue("Select actor first...");
        selectTest.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                loadTestReadme((String) changeEvent.getValue());
            }
        });
        selectTest.setWidth(400);

        beforeReadme = new HTMLFlow("<hr/>");
        htmlReadme = new HTMLPane();
        htmlReadme.setHeight(300);
        htmlReadme.setShowEdges(true);
        htmlReadme.setContents("<div style='padding-left:20px'>" +
                "<h3>README</h3>" +
                "</div>");
        afterReadme = new HTMLFlow("<hr/>");
        afterReadme = new HTMLFlow("<hr/>");

        form.setFields(selectActor, selectTest);
        form.setCellPadding(10);

        selectSection = new SelectItem();
        selectSection.setTitle("Select section");
        selectSection.setName("sectionItem");
        selectSection.setEmptyDisplayValue("Select section...");
        selectSection.setWidth(400);
        selectSection.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {

               /* viewTestPlan.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        // TODO
                    }
                });*/

            }
        });
        DynamicForm f=new DynamicForm();
        IButton viewTestPlan = new IButton("View this section's testplan");
        viewTestPlan.setWidth(200);
        f.setFields(selectSection);
        f.setCellPadding(10);

        sectionPanel = new VStack();
        sectionPanel.addMembers(f,viewTestPlan);

        PatientIDWidget patientID = new PatientIDWidget();
        TLSAndSAMLForm tLSAndSAMLForm = new TLSAndSAMLForm();

        vStack.addMembers(form, beforeReadme, htmlReadme, afterReadme,sectionPanel,patientID,tLSAndSAMLForm);
        return vStack;
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getPreConnectathonTestsTabCode();
    }

    private void loadActorsMap() {
        toolkitService.getCollectionNames("actorcollections",new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.warning(caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                actorMap = new LinkedHashMap<String,String>();
                actorMap.putAll(result);
                selectActor.setValueMap(actorMap);
            }
        });
    }

    private void loadTestsForActor(String selectedActor) {
        toolkitService.getCollection("actorcollections",selectedActor,new AsyncCallback<Map<String, String>>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.warning(caught.getMessage());
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                final LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
                map.putAll(result);
                if (map.isEmpty()){
                    selectTest.setEmptyDisplayValue(" -- NO DATA -- ");
                }else{
                    selectTest.setEmptyDisplayValue("Select Test...");
                }
                selectTest.setValueMap(map);
                form.getField("testItem").setValueMap(map);
            }
        });

    }

    void loadSectionNames(String selectedTest) {
        // TODO
    }

    private void loadTestReadme(String selectedTest){
        toolkitService.getTestReadme(selectedTest, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.warning(throwable.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                String readme = result;
                // FIXME line break character replacement does not work
                readme=readme.replaceAll("\\n", "<br/>");
                setReadmeHTMLPanel(readme);
            }
        });
    }

    private void setReadmeHTMLPanel(String readme){
        String readMe="<div style='padding-left:20px'>"+
                "<h3>README</h3>";
        for (String s:readme.split("\\n")){
            readMe+=s+"<br/>";
        }
        readMe+="</div>";
        htmlReadme.setContents(readMe);
    }

    private void setReadmeVisible(boolean visible){
        beforeReadme.setVisible(visible);
        htmlReadme.setVisible(visible);
        afterReadme.setVisible(visible);
    }
}
