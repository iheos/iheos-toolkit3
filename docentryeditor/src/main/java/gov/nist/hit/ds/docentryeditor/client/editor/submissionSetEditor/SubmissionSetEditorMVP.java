package gov.nist.hit.ds.docentryeditor.client.editor.submissionSetEditor;

import gov.nist.hit.ds.docentryeditor.client.generics.abstracts.AbstractMVP;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsSubmissionSet;

import javax.inject.Inject;

/**
 * Created by onh2 on 3/2/2015.
 */
public class SubmissionSetEditorMVP extends AbstractMVP<XdsSubmissionSet,SubmissionSetEditorView,SubmissionSetEditorPresenter> {

    @Inject
    SubmissionSetEditorView editorView;
    @Inject
    SubmissionSetEditorPresenter editorPresenter;

    @Override
    public SubmissionSetEditorView buildView() {
        return editorView;
    }

    @Override
    public SubmissionSetEditorPresenter buildPresenter() {
        return editorPresenter;
    }
}
