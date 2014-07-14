package edu.tn.xds.metadata.editor.client.editor.Submission;

import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractPresenter;

/**
 * Created by onh2 on 7/11/2014.
 */
public class SubmissionPanelPresenter extends AbstractPresenter<SubmissionPanelView> {
    SubmissionMenuData currentlyEdited;

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
            }
        });
    }
}
