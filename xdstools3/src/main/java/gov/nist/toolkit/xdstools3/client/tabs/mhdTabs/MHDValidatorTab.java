package gov.nist.toolkit.xdstools3.client.tabs.mhdTabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools2.client.Toolkit2Service;
import gov.nist.toolkit.xdstools2.client.Toolkit2ServiceAsync;
import gov.nist.toolkit.xdstools2.client.adapter2v3.PopupMessageV3;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * This class is the UI implementation for MHD Validation tab.
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
    private HTMLPane validationResultsPanel;
    private Button runBtn;

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

        // Message type elements
        HeaderItem l1=new HeaderItem();
        l1.setDefaultValue("1. Select a message type");
        messageTypeSelect = new SelectItem();
        messageTypeSelect.setTitle("Message type");
        messageTypeSelect.setType("comboBox");
        messageTypeSelect.setName("messageTypeItem");
        messageTypeSelect.setEmptyDisplayValue("Select message type...");
        messageTypeSelect.setWidth(400);
        loadMessageTypesMap();

        // Uploader elements
        HeaderItem l2=new HeaderItem();
        l2.setDefaultValue("2. Upload file to validate");
        uploadForm = new FormPanel();
        uploadForm.setMethod(FormPanel.METHOD_POST);
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setAction("fileUploadServlet");
        fileUploadItem = new FileUpload();
        fileUploadItem.setTitle("File to validate");
        fileUploadItem.setName("upload1FormElement");
        fileUploadItem.setWidth("400px");
        uploadForm.add(fileUploadItem);

        // Run button
        runBtn = new Button("Run");
        runBtn.disable();

        // Validation result elements
        validationResultsPanel = new HTMLPane();

        form.setFields(l1, messageTypeSelect, l2);
        form.setCellPadding(10);

        vStack.addMember(form);
        vStack.addMember(uploadForm);
        vStack.addMembers(runBtn,validationResultsPanel);

        bindUI();

        return vStack;
    }

    /**
     * Abstract method implementation to set the tab name
     * @return
     */
    @Override
    protected String setTabName() {
        return TabNamesUtil.getInstance().getMHDValidatorTabCode();
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
                        runBtn.enable();
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
     * Method that calls RPC method for validation
     */
    private void validate() {
        mhdToolkitService.validateMHDMessage(selectedMessageType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.warning(caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                displayValidationResults(result);
            }
        });
    }

    /**
     * Method loading possible values for message type.
     */
    private void loadMessageTypesMap() {
        LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
        map.put("sbmt", "Submit");
        map.put("sbmt_resp","Submit Response");
        messageTypeSelect.setValueMap(map);
    }

    /**
     * Method to display validation results
     *
     * @param result Validation result from RPC validation
     */
    private void displayValidationResults(String result) {
        validationResultsPanel.setContents(result);
        if (!validationResultsPanel.isVisible()){
            validationResultsPanel.setVisible(true);
        }
    }

}
