package edu.tn.xds.metadata.editor.client.editor;

import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractMVP;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

import javax.inject.Inject;

public class DocumentModelEditorMVP extends AbstractMVP<XdsDocumentEntry, DocumentModelEditorView, DocumentModelEditorPresenter> {

    @Inject
    DocumentModelEditorView editorView;

    @Inject
    DocumentModelEditorPresenter editorPresenter;

    @Override
    public DocumentModelEditorView buildView() {
        return editorView;
    }

    @Override
    public DocumentModelEditorPresenter buildPresenter() {
        // editorPresenter.initDriver();
        return editorPresenter;
    }

}
