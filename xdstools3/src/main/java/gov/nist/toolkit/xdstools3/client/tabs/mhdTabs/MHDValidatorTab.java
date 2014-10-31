package gov.nist.toolkit.xdstools3.client.tabs.mhdTabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FileItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class MHDValidatorTab extends GenericCloseableTab {
    private final static MHDTabsServicesAsync mhdToolkitService = GWT
            .create(MHDTabsServices.class);

    private static String header="MHD Validator";
    private String selectedMessageType;
    private Button runBtn;
//    private DynamicForm uploadForm;

    private Logger logger=Logger.getLogger(MHDValidatorTab.class.getName());
    private SelectItem messageTypeSelect;
    private HTMLPane validationResultsPanel;
    private FileItem fileUploadItem;

    public MHDValidatorTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        DynamicForm form = new DynamicForm();
        form.setMethod(FormMethod.POST);
        form.setEncoding(Encoding.MULTIPART);
        form.setAction("fileUploadServlet");

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
        fileUploadItem = new FileItem();
//        fileUploadItem = new UploadItem(); // or ...WithTooltip
        fileUploadItem.setTitle("File to validate");
        fileUploadItem.setName("uploadItem");
        fileUploadItem.setWidth(400);

        runBtn = new Button("Run");
        runBtn.disable();

        form.setFields(l1,messageTypeSelect,l2,fileUploadItem);

        validationResultsPanel = new HTMLPane();


        vStack.addMembers(form,runBtn,validationResultsPanel);

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
                    if(fileUploadItem.getDisplayValue()!=null && !fileUploadItem.getDisplayValue().isEmpty()){
                        runBtn.enable();
                    }
                }
            }
        });
        fileUploadItem.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                if(fileUploadItem.getDisplayValue()!=null && !fileUploadItem.getDisplayValue().isEmpty()){
                    if(selectedMessageType!=null && !selectedMessageType.isEmpty()){
                        runBtn.enable();
                    }
                }
            }
        });
        runBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
//                uploadForm.submit();
                String somethingAboutTheFileUploaded = new String();
                mhdToolkitService.validateMHDMessage(selectedMessageType, somethingAboutTheFileUploaded, new AsyncCallback<String>() {
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
