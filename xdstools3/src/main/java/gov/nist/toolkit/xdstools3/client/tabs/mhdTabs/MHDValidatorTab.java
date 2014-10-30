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
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MHDValidatorTab extends GenericCloseableTab {
    private final static MHDTabsServicesAsync mhdToolkitService = GWT
            .create(MHDTabsServices.class);

    private static String header="MHD Validator";
    private String selectedMessageType;
    private Button runBtn;
    private DynamicForm uploadForm;

    private Logger logger=Logger.getLogger(MHDValidatorTab.class.getName());
    private SelectItem messageTypeSelect;
    private HTMLPane validationResultsPanel;

    public MHDValidatorTab() {
        super(header);
    }

    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        DynamicForm messageTypeForm = new DynamicForm();
        Label l1=createSubtitle1("1. Select a message type");
        messageTypeSelect = new SelectItem();
        messageTypeSelect.setTitle("Message type");
        messageTypeSelect.setName("messageTypeItem");
        messageTypeSelect.setEmptyDisplayValue("Select message type...");
        messageTypeSelect.setWidth(400);
        LinkedHashMap<String,String> messageMap=new LinkedHashMap<String,String>();
        Map<String,String> map= new HashMap<String,String>();
        map.put("sbmt", "Submit");
        map.put("sbmt_resp","Submit Response");
        messageMap.putAll(map);
        messageTypeSelect.setValueMap(messageMap);
        messageTypeForm.setFields(messageTypeSelect);

        uploadForm = new DynamicForm();
        uploadForm.setMethod(FormMethod.POST);
        uploadForm.setEncoding(Encoding.MULTIPART);
        uploadForm.setAction("fileUploadServlet");
        Label l2=createSubtitle1("2. Upload file to validate");
        UploadItem fileUploadItem = new UploadItem(); // or UploadItemWithTooltip
        fileUploadItem.setTitle("File to validate");
        fileUploadItem.setName("uploadItem");
        fileUploadItem.setWidth(400);
        uploadForm.setFields(fileUploadItem);

        runBtn = new Button("Run");

        validationResultsPanel = new HTMLPane();


        vStack.addMembers(l1, messageTypeForm, l2, uploadForm, new LayoutSpacer(), runBtn, validationResultsPanel);

        bindUI();

        return vStack;
    }

    @Override
    protected String setTabName() {
        return TabNamesUtil.getMHDValidatorTabCode();
    }

    private void bindUI(){
        messageTypeSelect.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                selectedMessageType = (String) changeEvent.getValue();
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

//    private void loadMessageTypesMap() {
//        // TODO
//        LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
//        map.put("sbmt", "Submit");
//        map.put("sbmt_resp","Submit Response");
//        messageTypeSelect.setValueMap(map);
//        Logger.getLogger(this.getClass().getName()).info("map loaded "+messageTypeSelect.getValues());
//        for(String s:messageTypeSelect.getValues()){
//            Logger.getLogger(this.getClass().getName()).info(s);
//        }
//    }

    private void displayValidationResults(String result) {
        validationResultsPanel.setContents(result);
        if (!validationResultsPanel.isVisible()){
            validationResultsPanel.setVisible(true);
        }
    }

//    /*make a javascript funtion and html to received result
//        here sniped function :*/
//    public void uploadComplete(String status) {
//        String[] result=status.split("@");
//        if(result[0].equals("sucess")){
//            //TODO
//        }//else SC.say(result[0] + " upload file " + result[1]);
//
//    }
//    // Solution from http://www.mrsondao.com/TopicDetail.aspx?TopicId=2
//    private native void initComplete(MHDValidatorTab upload) /*-{
//        $wnd.uploadComplete = function(fileName) {
//            upload.@gov.nist.toolkit.xdstools3.client.tabs.mhdTabs.MHDValidatorTab::uploadComplete(Ljava/lang/String;)(fileName);
//        };
//    }-*/;
////    You see reponseStatus in FileUploadServlet will call uploadComplete in client, end snip code below declare uploadComplete function on client!
}
