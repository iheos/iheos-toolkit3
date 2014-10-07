package edu.tn.xds.metadata.editor.client.widgets.uploader;

import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractMVP;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

import javax.inject.Inject;

public class FileUploadMVP extends AbstractMVP<XdsDocumentEntry, FileUploadView, FileUploadPresenter> {
    @Inject
    FileUploadView fileUploadView;
    @Inject
    FileUploadPresenter fileUploadPresenter;

    public FileUploadMVP() {

    }

    @Override
    public FileUploadView buildView() {
        return fileUploadView;
    }

    @Override
    public FileUploadPresenter buildPresenter() {
        return fileUploadPresenter;
    }

}
