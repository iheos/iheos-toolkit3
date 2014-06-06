package edu.tn.xds.metadata.editor.client.widgets.uploader;

import javax.inject.Inject;

import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractMVP;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;

public class FileUploadMVP extends AbstractMVP<DocumentModel, FileUploadView, FileUploadPresenter> {
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
