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

public class MHDValidatorTab extends GenericCloseableTab {
    private final static MHDTabsServicesAsync mhdToolkitService = GWT
            .create(MHDTabsServices.class);

    private static String header="MHD Validator";

    final protected Toolkit2ServiceAsync toolkitService =
            GWT.create(Toolkit2Service.class);
    private String selectedMessageType;
    private Button runBtn;
//    private DynamicForm uploadForm;

    private Logger logger=Logger.getLogger(MHDValidatorTab.class.getName());
    private SelectItem messageTypeSelect;
    private HTMLPane validationResultsPanel;
    private FileUpload fileUploadItem;
    private String uploadFilename;
    private FormPanel uploadForm;

    public MHDValidatorTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        DynamicForm form = new DynamicForm();

        HeaderItem l1=new HeaderItem();
        l1.setDefaultValue("1. Select a message type");
        messageTypeSelect = new SelectItem();
        messageTypeSelect.setTitle("Message type");
        messageTypeSelect.setName("messageTypeItem");
        messageTypeSelect.setEmptyDisplayValue("Select message type...");
        messageTypeSelect.setWidth(400);
        loadMessageTypesMap();

        HeaderItem l2=new HeaderItem();
        l2.setDefaultValue("2. Upload file to validate");
        uploadForm = new FormPanel();
        uploadForm.setMethod(FormPanel.METHOD_POST);
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setAction("fileUploadServlet");
        fileUploadItem = new FileUpload();
        fileUploadItem.setTitle("File to validate");
        fileUploadItem.setName("uploadItem");
        fileUploadItem.setWidth("400");
//        fileUploadItem.setWidth(400);
        uploadForm.add(fileUploadItem);

        runBtn = new Button("Run");
        runBtn.disable();

        validationResultsPanel = new HTMLPane();

        form.setFields(l1, messageTypeSelect, l2);

        vStack.addMember(form);
        vStack.addMember(uploadForm);
        vStack.addMembers(runBtn,validationResultsPanel);

        bindUI();

        return vStack;
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getInstance().getMHDValidatorTabCode();
    }

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
        runBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                uploadForm.submit();
            }
        });
        uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                toolkitService.getLastFilename(new AsyncCallback<String>() {
                    public void onFailure(Throwable caught) {
                        new PopupMessageV3(caught.getMessage());
                    }
                    public void onSuccess(String result) {
                        uploadFilename = result;
                        validate();
                    }
                });
            }
        });
    }

    private void validate() {
        mhdToolkitService.validateMHDMessage(selectedMessageType, uploadFilename, new AsyncCallback<String>() {
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

    private void loadMessageTypesMap() {
        LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
        map.put("sbmt", "Submit");
        map.put("sbmt_resp","Submit Response");
        messageTypeSelect.setValueMap(map);
    }

    private void displayValidationResults(String result) {
        validationResultsPanel.setContents(result);
        if (!validationResultsPanel.isVisible()){
            validationResultsPanel.setVisible(true);
        }
    }

}
