package gov.nist.hit.ds.xdstools3.client.tabs.homeTab;

import com.google.gwt.place.shared.PlaceController;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import gov.nist.hit.ds.xdstools3.client.activitiesAndPlaces.TabPlace;
import gov.nist.hit.ds.xdstools3.client.util.injection.Xdstools3GinInjector;
import gov.nist.hit.ds.xdstools3.client.manager.TabNamesManager;

/**
 * Creates a button with the title passed in argument. Also creates a listener which fires an event through Eventbus,
 * opening the tab that matches the title parameter.
 */
public class HomeLinkButton extends IButton {

    PlaceController placeController = Xdstools3GinInjector.injector.getPlaceController();
    private final String title;

    public HomeLinkButton(String _title){
        title = _title;
        setTitle(title);
        setAlign(Alignment.LEFT);

        // for some reason, setting these properties in the CSS does not override SmartGWT defaults
        setBorder("0");
        setHeight(20);

        bindUI();
    }

    /**
     * Opens a given tab after a click on the corresponding link on the Home page
     * NB. syntax before TabPlaces was:
     * Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getFindDocumentsTabCode()));
     */
    private void bindUI() {
        // go to a new TabPlace on home's button click
        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                // FIXME unsafe code if refactored
                if (title == "Find Documents")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getFindDocumentsTabCode()));
                else if (title == "MPQ Find Documents")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getMpqFindDocumentsTabCode()));
                else if (title == "Message Validator")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getMessageValidatorTabCode()));
                else if (title == "Document Metadata Editor")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getDocumentMetadataEditorTabCode()));
                else if (title == "Pre-Connectathon Tests")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getPreConnectathonTestsTabCode()));
                else if (title == "Get Documents")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getGetDocumentsTabCode()));
                else if (title == "Find Folders")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getFindFoldersCode()));
                else if (title == "Get Folders")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getGetFoldersTabCode()));
                else if (title == "Get Folder and Contents")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getGetFoldersAndContentsCode()));
                else if (title == "Retrieve Document")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getRetrieveDocumentTabCode()));
                else if (title == "Get Submission Set and Contents")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getGetSubmissionSetAndContentsTabCode()));
                else if (title == "Get Related Documents")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getGetRelatedDocumentsCode()));
                else if (title == "XDS.b Doc Source Stores Document")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getSourceStoresDocumentValidationCode()));
                else if (title == "XDS.b Registry Do This First")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getRegisterAndQueryTabCode()));
                else if (title == "XDS.b Lifecycle")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getLifecycleValidationTabCode()));
                else if (title == "XDS.b Registry Folder Handling")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getFolderValidationTabCode()));
                else if (title == "v2 Tab Example")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getv2TabCode()));
                else if (title == "XDS.b Repository Do This First")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getSubmitRetrieveTabCode()));
                else if (title == "MHD Validator")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getMHDValidatorTabCode()));
                else if (title == "Submit Test Data")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getTestDataSubmissionTabCode()));
                else if (title == "MHD to XDS Converter")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getMhdtoXdsConverterTabCode()));
                else if (title == "Query - Retrieve - Submit")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getQRSCombinedTabCode()));
                else if (title == "Tests Overview")
                    placeController.goTo(new TabPlace(TabNamesManager.getInstance().getTestsOverviewTabCode()));
                else SC.say("A link is missing. Please contact the support team.");
            }
        });
    }
}
