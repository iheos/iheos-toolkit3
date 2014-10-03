package edu.tn.xds.metadata.editor.client;

/*Imports*/

import com.google.gwt.core.shared.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import edu.tn.xds.metadata.editor.client.editor.DocumentModelEditorMVP;
import edu.tn.xds.metadata.editor.client.editor.EditorActivity;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.home.WelcomeActivity;
import edu.tn.xds.metadata.editor.client.parse.XdsParser;
import edu.tn.xds.metadata.editor.client.root.CenterPanel;
import edu.tn.xds.metadata.editor.client.root.MetadataEditorAppView;
import edu.tn.xds.metadata.editor.client.root.NorthPanel;
import edu.tn.xds.metadata.editor.client.root.submission.SubmissionPanelMVP;
import edu.tn.xds.metadata.editor.client.root.submission.SubmissionPanelPresenter;
import edu.tn.xds.metadata.editor.client.root.submission.SubmissionPanelView;
import edu.tn.xds.metadata.editor.client.widgets.uploader.FileUploadMVP;

@GinModules(MetadataEditorGinModule.class)
public interface MetadataEditorGinInjector extends Ginjector {

    public static MetadataEditorGinInjector instance = GWT.create(MetadataEditorGinInjector.class);

    MetadataEditorEventBus getEventBus();

    PlaceController getPlaceController();

    MetadataEditorRequestFactory getRequestFactory();

    // ------------------------------------------
    // ~ Root
    MetadataEditorAppView getMetadataEditorAppView();

    CenterPanel getCenterPanel();

    NorthPanel getNorthPanel();

    // ------------------------------------------
    // ~ Various widgets
    FileUploadMVP getFileUploadMVP();

    // ------------------------------------------
    // ~ ACTIVITIES
    WelcomeActivity getWelcomeActivity();

    EditorActivity getEditorActivity();

    DocumentModelEditorMVP getDocumentModelEditorMVP();

    // Submission
    SubmissionPanelMVP getSubmissionPanelMVP();

    SubmissionPanelView getSubmissionPanelView();

    SubmissionPanelPresenter getSubmissionPanelPresenter();

    XdsParser getXdsParser();
}
