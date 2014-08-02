package edu.tn.xds.metadata.editor.client;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface MetadataEditorRequestFactory extends RequestFactory {
	SaveFileRequestContext saveFileRequestContext();
}