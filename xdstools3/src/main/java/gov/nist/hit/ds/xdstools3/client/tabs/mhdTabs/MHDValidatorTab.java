package gov.nist.hit.ds.xdstools3.client.tabs.mhdTabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.xdstools3.client.customWidgets.buttons.GenericRunButtonNoForm;
import gov.nist.hit.ds.xdstools3.client.customWidgets.forms.GenericForm;
import gov.nist.hit.ds.xdstools3.client.exceptions.ToolkitServerError;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;
import gov.nist.hit.ds.xdstools3.client.resources.Resources;
import gov.nist.hit.ds.xdstools3.client.tabs.GenericCloseableToolTab;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * This class is the UI implementation for the MHD Validator tab.
 * @author oherrmann
 */
public class MHDValidatorTab extends GenericCloseableToolTab {
    private Logger logger = Logger.getLogger(MHDValidatorTab.class.getName());

    // RPC services declaration
    private final static MHDTabsServicesAsync mhdToolkitService = GWT
            .create(MHDTabsServices.class);

    private static String header="MHD Validator";

    // UI components
    private FormPanel uploadForm;
    private SelectItem messageTypeSelect;
    private FileUpload fileUploadItem;
    private Button runBtn;
    private VLayout container;
    // Variables
    private String selectedMessageType;

    /**
     * Default constuctor
     */
    public MHDValidatorTab() {
        super(header);
        getContentsPanel().setWidth(440);
    }

    /**
     * Abstract method implementation that build the UI.
     *
     * @return tab UI as a Widget
     */
    @Override
    protected Widget createContents() {
        container = new VLayout();

        // Message type transactions
        HeaderItem messageTypeLabel = new HeaderItem();
        messageTypeLabel.setDefaultValue("1. Select the type of MHD message to validate");
        messageTypeSelect = new SelectItem();
        messageTypeSelect.setShowTitle(false);
        messageTypeSelect.setType("comboBox");
        messageTypeSelect.setName("messageTypeItem");
        messageTypeSelect.setEmptyDisplayValue("Select message type...");
        messageTypeSelect.setWidth(400);
        loadMessageTypesMap();

        // Uploader
        HeaderItem uploadLabel=new HeaderItem();
        uploadLabel.setDefaultValue("2. Upload file");
        uploadForm = new FormPanel();
        uploadForm.setHeight("40px");
        uploadForm.setMethod(FormPanel.METHOD_POST);
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setAction("fileUploadServlet");
        uploadForm.setStyleName("gwt-form-margin");
        fileUploadItem = new FileUpload();
        fileUploadItem.setName("upload1FormElement");
        fileUploadItem.setWidth("400px");
        uploadForm.add(fileUploadItem);

        // ------- Create the first form ------
        GenericForm form = new GenericForm();
        form.setFields(messageTypeLabel, messageTypeSelect, uploadLabel);

        // Run button
        runBtn = new GenericRunButtonNoForm();

        // Create Help Link and populates it with text from resources
        setHelpButton(getHelpPanel(), Resources.INSTANCE.getMHDValidatorHelpContents().getText());


        // Layout
        container.addMembers(form);
        container.addMember(uploadForm);
        container.addMember(runBtn);
        container.addMember(waitPanel);
        container.hideMember(waitPanel);

        bindUI();

        return container;
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
                if (selectedMessageType != null) {
                    if (fileUploadItem.getFilename() != null && !fileUploadItem.getFilename().isEmpty()) {
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
                        if (fileUploadItem.getFilename().endsWith(".xml") || fileUploadItem.getFilename().endsWith(".json")) {
                            runBtn.enable();
                        } else {
                            runBtn.disable();
                            SC.warn("Invalid file format. The uploaded file must be XML or JSON.");
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
//                waitPanel.show();
                container.showMember(waitPanel);
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
            // TODO display error message to user
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
        selectedMessageType="sbmt";
    }


}
