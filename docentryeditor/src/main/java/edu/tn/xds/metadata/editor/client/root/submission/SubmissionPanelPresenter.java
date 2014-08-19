package edu.tn.xds.metadata.editor.client.root.submission;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.TextResource;
import edu.tn.xds.metadata.editor.client.editor.EditorPlace;
import edu.tn.xds.metadata.editor.client.event.EditNewEvent;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent;
import edu.tn.xds.metadata.editor.client.event.StartEditXdsDocumentEvent;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractPresenter;
import edu.tn.xds.metadata.editor.client.parse.PreParse;
import edu.tn.xds.metadata.editor.client.parse.XdsParser;
import edu.tn.xds.metadata.editor.client.resources.AppResources;
import edu.tn.xds.metadata.editor.shared.model.String256;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

import javax.inject.Inject;

/**
 * Created by onh2 on 7/11/2014.
 */
public class SubmissionPanelPresenter extends AbstractPresenter<SubmissionPanelView> {
    @Inject
    PlaceController placeController;
    @Inject
    XdsParser xdsParser;

    private SubmissionMenuData currentlyEdited;
//    private final static TextResource prefillData = AppResources.INSTANCE.xdsPrefill();
    private int nextIndex = 1;

    @Override
    public void init() {
        bind();
    }

    private void bind() {
        ((MetadataEditorEventBus) getEventBus()).addFileLoadedHandler(new NewFileLoadedEvent.NewFileLoadedHandler() {
            @Override
            public void onNewFileLoaded(NewFileLoadedEvent event) {
                currentlyEdited = new SubmissionMenuData("DocEntry" + nextIndex, "Document Entry " + nextIndex, event.getDocument());
                nextIndex++;
                view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
                view.getTree().expandAll();
                view.getTree().getSelectionModel().select(currentlyEdited, false);
                getEventBus().fireEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
            }
        });
        ((MetadataEditorEventBus) getEventBus()).addEditNewEventHandler(new EditNewEvent.EditNewHandler() {
            @Override
            public void onEditNew(EditNewEvent event) {
                if (view.getTree().getStore().getChildCount(view.getSubmissionSetTreeNode()) < 1) {
                    createNewDocumentEntry();
                }
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
        if (!(placeController.getWhere() instanceof EditorPlace)) {
            placeController.goTo(new EditorPlace());
        }
        ((MetadataEditorEventBus) eventBus).fireStartEditXdsDocumentEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
    }

    /**
     * This method loads the editor interface with a selected document entry from the submission set tree.
     *
     * @param selectedItem tree selected node
     */
    public void loadDocumentEntry(SubmissionMenuData selectedItem) {
        if (!selectedItem.equals(view.getSubmissionSetTreeNode())) {
            currentlyEdited = selectedItem;
            ((MetadataEditorEventBus) eventBus).fireStartEditXdsDocumentEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
        }
    }

    public void createPrefilledDocumentEntry() {
        XdsDocumentEntry model = xdsParser.parse(PreParse.getInstance().doPreParse(AppResources.INSTANCE.xdsPrefill().getText()));
        model.setFileName(new String256("new-doc-entry"));
        //------------------------------------------- MIGHT CHANGE
        logger.info("Create new document entry");
        currentlyEdited = new SubmissionMenuData("DocEntry" + nextIndex, "Document Entry " + nextIndex, model);
        nextIndex++;
        view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
        view.getTree().expandAll();
        view.getTree().getSelectionModel().select(currentlyEdited, false);
        if (!(placeController.getWhere() instanceof EditorPlace)) {
            placeController.goTo(new EditorPlace());
        }
        ((MetadataEditorEventBus) eventBus).fireStartEditXdsDocumentEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
    }
}
