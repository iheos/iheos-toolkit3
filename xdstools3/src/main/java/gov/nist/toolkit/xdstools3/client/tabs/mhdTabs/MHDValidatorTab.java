package gov.nist.toolkit.xdstools3.client.tabs.mhdTabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.ui.client.widgets.EventAggregatorWidget;
import gov.nist.toolkit.xdstools3.client.exceptions.ToolkitServerError;
import gov.nist.toolkit.xdstools3.client.manager.Manager;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * This class is the UI implementation for the MHD Validator tab.
 * @author oherrmann
 */
public class MHDValidatorTab extends GenericCloseableTab {
    private Logger logger=Logger.getLogger(MHDValidatorTab.class.getName());
    // RPC services declaration
    private final static MHDTabsServicesAsync mhdToolkitService = GWT
            .create(MHDTabsServices.class);

    // tab's title and heander
    private static String header="MHD Validator";

    // UI components
    private FormPanel uploadForm;
    private SelectItem messageTypeSelect;
    private FileUpload fileUploadItem;
    private VStack validationResultsPanel;
    private Button runBtn;
    private EventAggregatorWidget eventMessageAggregatorWidget;

    // Variables
    private String selectedMessageType;

    /**
     * Default constuctor
     */
    public MHDValidatorTab() {
        super(header);
    }

    /**
     * Abstract method implementation that build the UI.
     *
     * @return tab UI as a Widget
     */
    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        DynamicForm form = new DynamicForm();

        // Message type transactions
        HeaderItem l1=new HeaderItem();
        l1.setDefaultValue("1. Select the type of MHD message to validate:");
        messageTypeSelect = new SelectItem();
        messageTypeSelect.setShowTitle(false);
        messageTypeSelect.setType("comboBox");
        messageTypeSelect.setName("messageTypeItem");
        messageTypeSelect.setEmptyDisplayValue("Select message type...");
        messageTypeSelect.setWidth(400);
        loadMessageTypesMap();

        // Uploader transactions
        HeaderItem l2=new HeaderItem();
        l2.setDefaultValue("2. Upload file to validate");
        uploadForm = new FormPanel();
        uploadForm.setHeight("40px");
        uploadForm.setMethod(FormPanel.METHOD_POST);
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setAction("fileUploadServlet");
        fileUploadItem = new FileUpload();

        fileUploadItem.setName("upload1FormElement");
        fileUploadItem.setWidth("400px");
        uploadForm.add(fileUploadItem);

        // Run button
        runBtn = new Button("Run");
        runBtn.disable();

        // Validation result transactions
        validationResultsPanel = new VStack();

        form.setFields(l1, messageTypeSelect, l2);
        form.setCellPadding(10);

        vStack.addMember(form);
        vStack.addMember(uploadForm);
        vStack.addMember(runBtn);

        // Event summary widget parameters
        String id = "f721daed-d17c-4109-b2ad-c1e4a8293281"; // "052c21b6-18c2-48cf-a3a7-f371d6dd6caf";
        String type = "validators";
        String[] displayColumns = new String[]{"ID","STATUS","MSG"};

        // Event summary widget
        validationResultsPanel.addMember(setupEventMessagesWidget(EventAggregatorWidget.ASSET_CLICK_EVENT.OUT_OF_CONTEXT, "Sim", id, type, displayColumns));
        vStack.addMember(validationResultsPanel);

        bindUI();

        return vStack;
    }

    /**
     * Abstract method implementation to set the tab name
     * @return
     */
    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getMHDValidatorTabCode();
    }

    /**
     * Method that binds the tab widgets together and their functionality
     */
    private void bindUI(){
        messageTypeSelect.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                selectedMessageType = (String) changeEvent.getValue();
                if (selectedMessageType!=null){
                    if(fileUploadItem.getFilename()!=null && !fileUploadItem.getFilename().isEmpty()){
                        runBtn.enable();
                    }
                }
            }
        });
        fileUploadItem.addChangeHandler(new com.google.gwt.event.dom.client.ChangeHandler() {
            @Override
            public void onChange(com.google.gwt.event.dom.client.ChangeEvent event) {
                if (fileUploadItem.getFilename() != null && !fileUploadItem.getFilename().isEmpty()) {
                    if (selectedMessageType != null && !selectedMessageType.isEmpty()) {
                        if(fileUploadItem.getFilename().endsWith(".xml") || fileUploadItem.getFilename().endsWith(".json")) {
                            runBtn.enable();
                        }else{
                            runBtn.disable();
                            SC.warn("Invalid file format - must be xml or json");
                        }
                    }
                }
            }
        });
        // Click handler on run button
        runBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                // submit the form information to the UploadServlet (for file upload)
                uploadForm.submit();
            }
        });
        uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                // call for validation
                validate();
            }
        });
    }

    /**
     * Calls the MHD validation service
     */
    private void validate() {
        try {
            mhdToolkitService.validateMHDMessage(selectedMessageType, new AsyncCallback<AssetNode>() {
                @Override
                public void onFailure(Throwable caught) {
                    logger.warning(caught.getMessage());
                }

                @Override
                public void onSuccess(AssetNode assetNode) {
                    displayValidationResults(assetNode);
                }
            });
        } catch (ToolkitServerError toolkitServerError) {
            toolkitServerError.printStackTrace();
            // TODO display error messge to user
        }
    }

    /**
     * Method loading possible values for message type.
     */
    private void loadMessageTypesMap() {
        LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
        map.put("sbmt", "Submit");
        map.put("sbmt_resp","Submit Response - not implemented yet");
        messageTypeSelect.setValueMap(map);
        messageTypeSelect.setDefaultValue("sbmt");
        selectedMessageType=map.get("sbmt");
    }

    /**
     * Method to display validation results
     *
     * @param assetNode Validation result from RPC validation
     */
    private void displayValidationResults(AssetNode assetNode) {
        if (!validationResultsPanel.isVisible()){
            validationResultsPanel.setVisible(true);
        }
        eventMessageAggregatorWidget.setEventAssetNode(assetNode);
        validationResultsPanel.redraw();
    }


        /**
         * Initializes the Event Message Widget to be populated with the validation result
         */
    protected Widget setupEventMessagesWidget(EventAggregatorWidget.ASSET_CLICK_EVENT assetClickEvent, String externalRepositoryId, String eventAssetId, String type, String[] displayColumns) {

        try {
            // Initialize the widget
            eventMessageAggregatorWidget = new EventAggregatorWidget(Manager.EVENT_BUS, assetClickEvent, externalRepositoryId,eventAssetId,type,displayColumns);
            eventMessageAggregatorWidget.setSize("990px", "375px");
            return eventMessageAggregatorWidget;

        } catch (Throwable t) {
            Window.alert("EventAggregatorWidget instance could not be created: " + t.toString());
        }
        return null;
    }

}
