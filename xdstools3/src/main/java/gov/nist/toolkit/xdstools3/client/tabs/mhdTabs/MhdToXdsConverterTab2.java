package gov.nist.toolkit.xdstools3.client.tabs.mhdTabs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VStack;
import gov.nist.hit.ds.repository.shared.data.AssetNode;
import gov.nist.hit.ds.repository.ui.client.widgets.EventAggregatorWidget;
import gov.nist.toolkit.xdstools3.client.customWidgets.WaitPanel;
import gov.nist.toolkit.xdstools3.client.manager.Manager;
import gov.nist.toolkit.xdstools3.client.tabs.GenericCloseableTab;
import gov.nist.toolkit.xdstools3.client.manager.TabNamesManager;

import java.util.logging.Logger;

/**
 * Tab to handle a file conversion from mhd to xds
 */
public class MhdToXdsConverterTab2 extends GenericCloseableTab {
    private Logger logger=Logger.getLogger(MhdToXdsConverterTab2.class.getName());
    private EventAggregatorWidget eventMessageAggregatorWidget;

    // RPC services declaration
    private final static MHDTabsServicesAsync mhdToolkitService = GWT
            .create(MHDTabsServices.class);

    // tab title and header
    private static final String header="MHD to XDS Converter";

    // UI components
    private FormPanel uploadForm;
    private FileUpload fileUploadItem;
    private Button runBtn;

    public MhdToXdsConverterTab2() {
        super(header);
        bindUI();
    }

    /**
     * Implementation of the abstract method that build the UI.
     *
     * @return The UI as a Widget
     */
    @Override
    protected Widget createContents() {
        VStack vStack=new VStack();

        uploadForm = new FormPanel();
        uploadForm.setMethod(FormPanel.METHOD_POST);
        uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        uploadForm.setAction("fileUploadServlet");

        fileUploadItem = new FileUpload();
        fileUploadItem.setTitle("File to convert");
        fileUploadItem.setName("upload1FormElement");
        fileUploadItem.setWidth("600px");

        uploadForm.add(fileUploadItem);

        runBtn=new Button("Convert");
        runBtn.disable();

        vStack.addMember(createSubtitle1("Select MHD file to convert"));
        vStack.addMember(uploadForm);
        vStack.addMember(runBtn);

        vStack.addMember(waitPanel);

        // Event summary widget parameters
        String id = "f721daed-d17c-4109-b2ad-c1e4a8293281"; // "052c21b6-18c2-48cf-a3a7-f371d6dd6caf";
        String type = "validators";
        String[] displayColumns = new String[]{"ID","STATUS","MSG"};

        vStack.addMember(setupEventMessagesWidget(EventAggregatorWidget.ASSET_CLICK_EVENT.OUT_OF_CONTEXT, "Sim", id, type, displayColumns));

        return vStack;
    }

    /**
     * Method that bind the UI widgets together and call for distant services
     */
    private void bindUI(){
        // change handler on the file selector to enable the run button when a file is selected
        fileUploadItem.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                if (fileUploadItem.getFilename() != null && !fileUploadItem.getFilename().isEmpty()) {
                    if(fileUploadItem.getFilename().endsWith(".xml") || fileUploadItem.getFilename().endsWith(".json")) {
                        runBtn.enable();
                    }else{
                        runBtn.disable();
                        SC.warn("Invalid file format - must be xml or json");
                    }
                }
            }
        });
        // click handler on the run button which calls the server file upload servlet to save the file
        runBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                waitPanel.show();
                uploadForm.submit();
            }
        });
        // submit complete handler that assures the upload is done
        uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent submitCompleteEvent) {
                // call for the distant method that converts the mhd file to an xds file
                mhdToolkitService.convertMHDToXDS(new AsyncCallback<AssetNode>() {

                    @Override
                    public void onFailure(Throwable e) {
                        logger.warning("Error: " + e.getMessage());
                    }

                    @Override
                    public void onSuccess(AssetNode response) {
                        // Open the converted file saved on the server in a new Window
//                        Window.open(GWT.getHostPageBaseURL() + "files/" + response, response + "Converted File", "enabled");
                        eventMessageAggregatorWidget.setEventAssetNode(response);
                        waitPanel.hide();
                    }
                });
            }
        });
    }

    @Override
    protected String setTabName() {
        return TabNamesManager.getInstance().getMhdtoXdsConverterTabCode();
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
