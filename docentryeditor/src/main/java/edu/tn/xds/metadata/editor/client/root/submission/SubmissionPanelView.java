package edu.tn.xds.metadata.editor.client.root.submission;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractView;
import edu.tn.xds.metadata.editor.client.resources.AppImages;

import javax.inject.Inject;

/**
 * Created by onh2 on 7/11/2014.
 */
public class SubmissionPanelView extends AbstractView<SubmissionPanelPresenter> {
    final static TreeStore<SubmissionMenuData> treeStore = new TreeStore<SubmissionMenuData>(SubmissionMenuData.props.key());
    final static Tree<SubmissionMenuData, String> tree = new Tree<SubmissionMenuData, String>(treeStore, SubmissionMenuData.props.value());
    private final static SubmissionMenuData submissionSetTreeNode = new SubmissionMenuData("subSet", "Submission set");
    private final ToolBar toolbar = new ToolBar();
    private final TextButton addDocEntryButton = new TextButton();
    private final TextButton removeDocEntryButton = new TextButton();
    private final TextButton clearDocEntriesButton = new TextButton();
    private final TextButton helpButton = new TextButton();

    @Inject
    MetadataEditorEventBus eventBus;

    @Override
    protected Widget buildUI() {
        ContentPanel cp = new ContentPanel();
        cp.setHeadingText("Submission");
        cp.setHeaderVisible(true);
        cp.setBorders(false);

        VerticalLayoutContainer vlc = new VerticalLayoutContainer();

        addDocEntryButton.setIcon(AppImages.INSTANCE.add());
        addDocEntryButton.setToolTip("Add a new Document Entry");
        removeDocEntryButton.setIcon(AppImages.INSTANCE.delete());
        removeDocEntryButton.setToolTip("Remove this Document Entry");
        clearDocEntriesButton.setIcon(AppImages.INSTANCE.clear());
        clearDocEntriesButton.setToolTip("Clear submission set from all document entries");
        helpButton.setIcon(AppImages.INSTANCE.help());
        helpButton.setToolTip("Help");
        toolbar.add(addDocEntryButton);
        toolbar.add(removeDocEntryButton);
        toolbar.add(clearDocEntriesButton);
        toolbar.add(helpButton);
        vlc.add(toolbar);
        buildTreeStore();
        tree.getStyle().setLeafIcon(AppImages.INSTANCE.file());
        tree.expandAll();
        tree.setAutoExpand(true);
        vlc.add(tree);

        cp.setWidget(vlc);
        return cp;
    }

    private void buildTreeStore() {
        if (treeStore.getAll().isEmpty()) {
            treeStore.add(submissionSetTreeNode);
        }
    }

    @Override
    protected void bindUI() {
        addDocEntryButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                presenter.createNewDocumentEntry();
            }
        });
        tree.getSelectionModel().addSelectionHandler(new SelectionHandler<SubmissionMenuData>() {
            @Override
            public void onSelection(SelectionEvent<SubmissionMenuData> event) {
                presenter.loadDocumentEntry(event.getSelectedItem());
            }
        });
    }

    public TreeStore<SubmissionMenuData> getTreeStore() {
        return treeStore;
    }

    public SubmissionMenuData getSubmissionSetTreeNode() {
        return submissionSetTreeNode;
    }
}
