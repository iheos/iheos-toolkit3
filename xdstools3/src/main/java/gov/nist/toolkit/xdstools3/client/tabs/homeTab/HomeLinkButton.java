package gov.nist.toolkit.xdstools3.client.tabs.homeTab;

import com.google.gwt.place.shared.PlaceController;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import gov.nist.toolkit.xdstools3.client.TabPlace;
import gov.nist.toolkit.xdstools3.client.Xdstools3EP;
import gov.nist.toolkit.xdstools3.client.Xdstools3GinInjector;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;

/**
 * Creates a button with the title passed in argument. Also creates a listener which fires an event through Eventbus,
 * opening the tab that matches the title parameter.
 */
public class HomeLinkButton extends IButton {

    //    @Inject
//    private PlaceController placeController = ((Xdstools3GinInjector) GWT.create(Xdstools3GinInjector.class)).getPlaceController();
    PlaceController placeController = Xdstools3GinInjector.injector.getPlaceController();

    public HomeLinkButton(String _title){
        final String title = _title;
        setStyleName("homelinkbutton");
        setTitle(title);
        setAlign(Alignment.LEFT);
        setBorder("0");
        setWidth(400);
        setHeight(40);

        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                // FIXME unsafe code if refactored
                if (title == "Find Documents")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getFindDocumentsTabCode()));
//                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getFindDocumentsTabCode()));
                else if (title == "MPQ Find Documents")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getMpqFindDocumentsTabCode()));
                else if (title == "Message Validator")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getMessageValidatorTabCode()));
                else if (title == "Document Metadata Editor")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getDocumentMetadataEditorTabCode()));// FIXME => why null?
//                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getDocumentMetadataEditorTabCode()));
                else if (title == "Pre-Connectathon Tests")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getPreConnectathonTestsTabCode()));
//                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getPreConnectathonTestsTabCode()));
                else if (title == "Get Documents")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getGetDocumentsTabCode()));
                else if (title == "Find Folders")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getFindFoldersCode()));
                else if (title == "Get Folders")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getGetFoldersTabCode()));
                else if (title == "Get Folder and Contents")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getGetFoldersAndContentsCode()));
                else if (title == "Retrieve Document")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getRetrieveDocumentTabCode()));
                else if (title == "Get Submission Set and Contents")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getGetSubmissionSetAndContentsTabCode()));
                else if (title == "Get Related Documents")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getGetRelatedDocumentsCode()));
                else if (title == "XDS.b Doc Source Stores Document")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getSourceStoresDocumentValidationCode()));
                else if (title == "XDS.b Registry Do This First")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getRegisterAndQueryTabCode()));
                else if (title == "XDS.b Lifecycle")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getLifecycleValidationTabCode()));
                else if (title == "XDS.b Registry Folder Handling")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getFolderValidationTabCode()));
                else if (title == "v2 Tab Example")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getv2TabCode()));
//                                        Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getv2TabCode()));

                else if (title == "XDS.b Repository Do This First")
                    placeController.goTo(new TabPlace(TabNamesUtil.getInstance().getSubmitRetrieveTabCode()));
                else SC.say("A link is missing. Please contact the support team.");
            }
        });

    }
}
