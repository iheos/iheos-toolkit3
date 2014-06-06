package edu.tn.xds.metadata.editor.client.editor;

import javax.inject.Inject;

import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractMVP;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;

public class DocumentModelEditorMVP extends AbstractMVP<DocumentModel, DocumentModelEditorView, DocumentModelEditorPresenter> {

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
