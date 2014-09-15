package gov.nist.toolkit.xdstools3.client.tabs.homeTab;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import gov.nist.toolkit.xdstools3.client.eventBusUtils.OpenTabEvent;
import gov.nist.toolkit.xdstools3.client.util.TabNamesUtil;
import gov.nist.toolkit.xdstools3.client.util.Util;

/**
 * Creates a button with the title passed in argument. Also creates a listener which fires an event through Eventbus,
 * opening the tab that matches the title parameter.
 */
public class HomeLinkButton extends IButton {

    public HomeLinkButton(String _title){
        final String title = _title;
        setStyleName("homelinkbutton"); // TODO doesn't seem to work with Xdstools3.css, why?
        setTitle(title);
        setAlign(Alignment.LEFT);
        setBorder("0");
        setWidth(400);
        setHeight(40);

        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                // FIXME unsafe code if refactored
                if (title == "Find Documents")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getFindDocumentsTabCode()));
                else if (title == "MPQ Find Documents")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getMpqFindDocumentsTabCode()));
                else if (title == "Message Validator")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getMessageValidatorTabCode()));
                else if (title == "Document Metadata Editor")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getDocumentMetadataEditorTabCode()));
                else if (title == "Pre-Connectathon Tests")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getPreConnectathonTestsTabCode()));
                else if (title == "Get Documents")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getGetDocumentsCode()));
               else if (title == "Find Folders")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getFindFoldersCode()));
               else if (title == "Get Folders")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getGetFoldersCode()));
               else if (title == "Get Folder and Contents")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getGetFoldersAndContentsCode()));
               else if (title == "Retrieve Document")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getRetrieveDocumentCode()));
               else if (title == "Get Submission Set and Contents")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getGetSubmissionSetAndContentsCode()));
               else if (title == "Get Related Documents")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getGetRelatedDocumentsCode()));
               else if (title == "XDS.b Doc Source Stores Document")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getSourceStoresDocumentValidationCode()));
               else if (title == "XDS.b Registry Do This First")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getRegisterAndQueryTabCode()));
               else if (title == "XDS.b Lifecycle")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getLifecycleValidationTabCode()));
               else if (title == "XDS.b Registry Folder Handling")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getFolderValidationTabCode()));
               else if (title == "XDS.b Repository Do This First")
                    Util.EVENT_BUS.fireEvent(new OpenTabEvent(TabNamesUtil.getInstance().getSubmitRetrieveTabCode()));
                else SC.say("A link is missing. Please contact the support team.");
            }
        });

    }
}
