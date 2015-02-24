package gov.nist.hit.ds.docentryeditor.client.root.submission;

import com.google.gwt.place.shared.PlaceController;
import gov.nist.hit.ds.docentryeditor.client.utils.MetadataEditorRequestFactory;
import gov.nist.hit.ds.docentryeditor.client.editor.EditorPlace;
import gov.nist.hit.ds.docentryeditor.client.event.*;
import gov.nist.hit.ds.docentryeditor.client.generics.abstracts.AbstractPresenter;
import gov.nist.hit.ds.docentryeditor.client.home.WelcomePlace;
import gov.nist.hit.ds.docentryeditor.client.parser.PreParse;
import gov.nist.hit.ds.docentryeditor.client.parser.XdsParser;
import gov.nist.hit.ds.docentryeditor.client.resources.AppResources;
import gov.nist.hit.ds.docentryeditor.shared.model.String256;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsDocumentEntry;

import javax.inject.Inject;

/**
 * This class presents the submission panel. It handles the mechanic of the submission set tree of the SubmissionPanelView.
 *
 * @see gov.nist.hit.ds.docentryeditor.client.root.submission.SubmissionPanelView
 * @see gov.nist.hit.ds.docentryeditor.client.root.submission.SubmissionMenuData
 * Created by onh2 on 7/11/2014.
 */
public class SubmissionPanelPresenter extends AbstractPresenter<SubmissionPanelView> {
    @Inject
    PlaceController placeController;
    @Inject
    XdsParser xdsParser;
    @Inject
    MetadataEditorRequestFactory requestFactory;

    private SubmissionMenuData currentlyEdited;
    private int nextIndex = 1;
    private XdsDocumentEntry prefilledDocEntry;

    @Override
    public void init() {
        bind();
        requestFactory.initialize(eventBus);
    }

    /**
     * Method that handles the different event fired in the event bus.
     */
    private void bind() {
        // this event catches handle the navigation back to the home page.
        ((MetadataEditorEventBus) getEventBus()).addBackToHomePageEventHandler(new BackToHomePageEvent.BackToHomePageEventHandler() {
            @Override
            public void onBackToHomePage(BackToHomePageEvent event) {
                getView().getTree().getSelectionModel().deselectAll();
                placeController.goTo(new WelcomePlace());
            }
        });
        // this event catches that a Document entry has been loaded from the user's file system.
        ((MetadataEditorEventBus) getEventBus()).addFileLoadedHandler(new NewFileLoadedEvent.NewFileLoadedHandler() {
            @Override
            public void onNewFileLoaded(NewFileLoadedEvent event) {
                currentlyEdited = new SubmissionMenuData("DocEntry" + nextIndex, "Document Entry " + nextIndex, event.getDocument());
                nextIndex++;
                view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
                view.getTree().expandAll();
                view.getTree().getSelectionModel().select(currentlyEdited, false);
            }
        });
        // this catches that the XDS Document entry editor view has loaded.
        ((MetadataEditorEventBus) getEventBus()).addXdsEditorLoadedEventtHandler(new XdsEditorLoadedEvent.XdsEditorLoadedEventHandler() {
            @Override
            public void onXdsEditorLoaded(XdsEditorLoadedEvent event) {
                logger.info("... receive Doc. Entry Editor loaded event.");
                if (currentlyEdited != null) {
                    // if a doc. entry is currently under edition, an event is fired to transfer it to the editor.
                    logger.info("A document is already selected. Loading it...");
                    ((MetadataEditorEventBus) getEventBus()).fireStartEditXdsDocumentEvent(currentlyEdited.getModel());
                } else {
                    // if no doc. entry is currently under edition, it means the app (editor view) has been loaded from
                    // by its URL from the browser navigation bar (external link).
                    logger.info("No Document Entry in Submission Set");
                    // a new doc. entry is create in the submission tree.
                    createNewDocumentEntry();
                }
            }
        });
        // this catches that a new pre-filled doc. entry creation has been required
        // from another place than the submission panel.
        ((MetadataEditorEventBus) getEventBus()).addLoadPreFilledDocEntryEventHandler(new LoadPrefilledDocEntryEvent.LoadPrefilledDocEntryEventHandler() {
            @Override
            public void onLoadPrefilledDocEntryHandler(LoadPrefilledDocEntryEvent event) {
                createPreFilledDocumentEntry();
            }
        });
    }

    /**
     * This method creates a new Document Entry and adds it to the submissionSet tree.
     */
    public void createNewDocumentEntry() {
        logger.info("Create new document entry");
        currentlyEdited = new SubmissionMenuData("DocEntry" + nextIndex, "Document Entry " + nextIndex, new XdsDocumentEntry());
        nextIndex++;
        view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
        view.getTree().expandAll();
        view.getTree().getSelectionModel().select(currentlyEdited, false);
    }

    /**
     * This method loads the editor interface with a selected document entry from the submission set tree.
     *
     * @param selectedItem tree selected node
     */
    public void loadDocumentEntry(SubmissionMenuData selectedItem) {
        if (!selectedItem.equals(view.getSubmissionSetTreeNode())) {
            currentlyEdited = selectedItem;
            startEditing();
        }
    }

    /**
     * This method load the editor user interface with a pre-filled document entry which is added to the submission set tree.
     */
    public void createPreFilledDocumentEntry() {
        if (prefilledDocEntry==null) {
            prefilledDocEntry = xdsParser.parse(PreParse.getInstance().doPreParse(AppResources.INSTANCE.xdsPrefill().getText()));
            prefilledDocEntry.setFileName(new String256("new-doc-entry"));
        }
        //------------------------------------------- MIGHT CHANGE
        logger.info("Create new pre-filled document entry");
        currentlyEdited = new SubmissionMenuData("DocEntry" + nextIndex, "Document Entry " + nextIndex, prefilledDocEntry);
        nextIndex++;
        view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
        view.getTree().expandAll();
        view.getTree().getSelectionModel().select(currentlyEdited, false);
    }

    public void doSave() {
        ((MetadataEditorEventBus) eventBus).fireSaveFileEvent();
        ((MetadataEditorEventBus) eventBus).addSaveCurrentlyEditedDocumentHandler(new SaveCurrentlyEditedDocumentEvent.SaveCurrentlyEditedDocumentEventHandler() {
            @Override
            public void onSaveCurrentlyEditedDocumentEvent(SaveCurrentlyEditedDocumentEvent event) {
                currentlyEdited.setModel(event.getDocumentEntry());
                //  save(); // not done yet
            }
        });
    }

    /**
     * This method loads a document entry into the editor user interface, which is loaded if not already.
     */
    private void startEditing() {
        if(!(placeController.getWhere() instanceof EditorPlace)){
            placeController.goTo(new EditorPlace());
        }
        logger.info("Fire Start Edit selected ("+currentlyEdited.getValue()+") document entry event...");
        ((MetadataEditorEventBus) getEventBus()).fireStartEditXdsDocumentEvent(currentlyEdited.getModel());
    }

    /**
     * Getter that return the entity currently under edition.
     * @return SubmissionMenuData.
     */
    public SubmissionMenuData getCurrentlyEdited(){
        return currentlyEdited;
    }
}
