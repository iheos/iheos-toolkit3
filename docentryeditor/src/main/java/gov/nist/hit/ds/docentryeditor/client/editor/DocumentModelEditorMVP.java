package gov.nist.hit.ds.docentryeditor.client.editor;

import gov.nist.hit.ds.docentryeditor.client.generics.abstracts.AbstractMVP;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsDocumentEntry;

import javax.inject.Inject;

/**
 * Class that represents and build the Model View Presenter objects for the Document entry editor view.
 */
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
