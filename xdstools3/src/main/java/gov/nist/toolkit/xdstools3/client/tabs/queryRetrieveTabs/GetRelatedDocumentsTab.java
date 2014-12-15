package gov.nist.toolkit.xdstools3.client.tabs.queryRetrieveTabs;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.customWidgets.GenericTextItemWithTooltipWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.TLSAndSAML.TLSAndSAMLForm;
import gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.smartgwt.select.EndpointWidget;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;

import java.util.LinkedHashMap;

public class GetRelatedDocumentsTab extends GenericCloseableTab {
    private static final String header="Get Related Documents";

    private GenericTextItemWithTooltipWidget docEntryUUID;
    private ComboBoxItem associationTypes;
    private EndpointWidget sites;
    private Button runBtn;

    public GetRelatedDocumentsTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        Label l1=createSubtitle1("1. Enter Document Entry UUID");
        DynamicForm docEntryUUIDForm = new DynamicForm();
        docEntryUUID = new GenericTextItemWithTooltipWidget();
        docEntryUUID.setTitle("Document Entry UUID");
        docEntryUUID.setWidth(400);
        docEntryUUIDForm.setFields(docEntryUUID);
        docEntryUUIDForm.setCellPadding(10);

        Label l2=createSubtitle1("2. Select Association Type");
        DynamicForm associationForm = new DynamicForm();
        associationTypes = new ComboBoxItem();
        associationTypes.setTitle("Association Type");
        associationTypes.setName("associationItem");
        associationTypes.setEmptyDisplayValue("Select Association Type...");
        associationTypes.setWidth(400);
        associationForm.setFields(associationTypes);
        associationForm.setCellPadding(10);
        loadAssociationTypesMap();


        Label l3=createSubtitle1("3. Select Site");
        sites = new EndpointWidget();
//        sites.isEndpointValueSelected()

        Label l4=createSubtitle1("4. Select SAML and TLS options");
        TLSAndSAMLForm tlsAndSAMLForm=new TLSAndSAMLForm();

        runBtn=new Button("Run");
        runBtn.disable();

        vStack.addMembers(l1,docEntryUUIDForm,l2,associationForm,l3, sites,l4,tlsAndSAMLForm,runBtn);

        bindUI();

        return vStack;
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getGetRelatedDocumentsCode();
    }

    private void loadAssociationTypesMap() {
        LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
        map.put("RPLC","RPLC (full name)");
        map.put("APND","APND (full name)");
        map.put("XFRM","XFRM (full name)");
        map.put("XFRM_RPLC","XFRM_RPLC (full name)");
        map.put("signs","signs");
        associationTypes.setValueMap(map);
    }

    private void bindUI() {
        docEntryUUID.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent blurEvent) {
                if (!docEntryUUID.getValueAsString().isEmpty() && sites.isEndpointValueSelected() && associationTypes.getValue()!=null) {
                    runBtn.enable();
                } else {
                    runBtn.disable();
                }
            }
        });
        associationTypes.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent changedEvent) {
                if (!docEntryUUID.getValueAsString().isEmpty() && sites.isEndpointValueSelected() && associationTypes.getValue()!=null) {
                    runBtn.enable();
                } else {
                    runBtn.disable();
                }
            }
        });
        sites.addSelectionChangedHandler(new SelectionChangedHandler() {
            @Override
            public void onSelectionChanged(SelectionEvent selectionEvent) {
                if (docEntryUUID.getValue() != null && sites.isEndpointValueSelected() && associationTypes.getValue()!=null) {
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
