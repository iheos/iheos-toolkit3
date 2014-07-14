package edu.tn.xds.metadata.editor.client.editor.Submission;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractView;
import edu.tn.xds.metadata.editor.client.resources.AppImages;

/**
 * Created by onh2 on 7/11/2014.
 */
public class SubmissionPanelView extends AbstractView<SubmissionPanelPresenter> {
    final static TreeStore<SubmissionMenuData> treeStore = new TreeStore<SubmissionMenuData>(SubmissionMenuData.props.key());
    final static Tree<SubmissionMenuData, String> tree = new Tree<SubmissionMenuData, String>(treeStore, SubmissionMenuData.props.value());
    private final static SubmissionMenuData submissionSetTreeNode = new SubmissionMenuData("subSet", "Submission set");
    private final TextButton addDocEntryButton = new TextButton();
    private final TextButton removeDocEntryButton = new TextButton();
    ToolBar toolbar = new ToolBar();
    private TextButton clearDocEntriesButton = new TextButton();
    private TextButton helpButton = new TextButton();
    ;

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

    }

    public TreeStore<SubmissionMenuData> getTreeStore() {
        return treeStore;
    }

    public SubmissionMenuData getSubmissionSetTreeNode() {
        return submissionSetTreeNode;
    }
}
