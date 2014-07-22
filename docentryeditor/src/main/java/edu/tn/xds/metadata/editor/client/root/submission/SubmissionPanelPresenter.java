package edu.tn.xds.metadata.editor.client.root.submission;

import com.google.gwt.place.shared.PlaceController;
import edu.tn.xds.metadata.editor.client.editor.EditorPlace;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent;
import edu.tn.xds.metadata.editor.client.event.StartEditXdsDocumentEvent;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractPresenter;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;

import javax.inject.Inject;

/**
 * Created by onh2 on 7/11/2014.
 */
public class SubmissionPanelPresenter extends AbstractPresenter<SubmissionPanelView> {
    @Inject
    PlaceController placeController;
    SubmissionMenuData currentlyEdited;

    private int nextIndex = 1;

    @Override
    public void init() {
        bind();
    }

    private void bind() {
        ((MetadataEditorEventBus) getEventBus()).addFileLoadedHandler(new NewFileLoadedEvent.NewFileLoadedHandler() {
            @Override
            public void onNewFileLoaded(NewFileLoadedEvent event) {
                int treeSize = view.getTreeStore().getAllItemsCount();
                currentlyEdited = new SubmissionMenuData("DocEntry" + treeSize, "Document Entry " + treeSize, event.getDocument());
                view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
                view.tree.expandAll();
                view.tree.getSelectionModel().select(currentlyEdited, false);
//                view.refresh();
                getEventBus().fireEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
//                ((MetadataEditorEventBus) eventBus).fireStartEditXdsDocumentEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
            }
        });
    }

    public void createNewDocumentEntry() {
        logger.info("Create new document entry");
//        int treeSize = view.getTreeStore().getAllItemsCount();
        currentlyEdited = new SubmissionMenuData("DocEntry" + nextIndex, "Document Entry " + nextIndex, new DocumentModel());
        nextIndex++;
        view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
        view.tree.expandAll();
        view.tree.getSelectionModel().select(currentlyEdited, false);
        if (!(placeController.getWhere() instanceof EditorPlace)) {
            placeController.goTo(new EditorPlace());
        }
        ((MetadataEditorEventBus) eventBus).fireStartEditXdsDocumentEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
    }

    public void loadDocumentEntry(SubmissionMenuData selectedItem) {
        if (!selectedItem.equals(view.getSubmissionSetTreeNode())) {
            currentlyEdited = selectedItem;
            ((MetadataEditorEventBus) eventBus).fireStartEditXdsDocumentEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
        }
    }

}
