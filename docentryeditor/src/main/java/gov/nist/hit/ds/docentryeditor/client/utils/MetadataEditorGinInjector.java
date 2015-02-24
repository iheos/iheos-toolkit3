package gov.nist.hit.ds.docentryeditor.client.utils;

/*Imports*/

import com.google.gwt.core.shared.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import gov.nist.hit.ds.docentryeditor.client.editor.DocumentModelEditorMVP;
import gov.nist.hit.ds.docentryeditor.client.editor.DocumentModelEditorPresenter;
import gov.nist.hit.ds.docentryeditor.client.editor.DocumentModelEditorView;
import gov.nist.hit.ds.docentryeditor.client.editor.EditorActivity;
import gov.nist.hit.ds.docentryeditor.client.editor.widgets.AuthorWidgets.AuthorsListEditorWidget;
import gov.nist.hit.ds.docentryeditor.client.event.MetadataEditorEventBus;
import gov.nist.hit.ds.docentryeditor.client.home.WelcomeActivity;
import gov.nist.hit.ds.docentryeditor.client.parser.XdsParser;
import gov.nist.hit.ds.docentryeditor.client.root.CenterPanel;
import gov.nist.hit.ds.docentryeditor.client.root.MetadataEditorAppView;
import gov.nist.hit.ds.docentryeditor.client.root.NorthPanel;
import gov.nist.hit.ds.docentryeditor.client.root.submission.SubmissionPanelMVP;
import gov.nist.hit.ds.docentryeditor.client.root.submission.SubmissionPanelPresenter;
import gov.nist.hit.ds.docentryeditor.client.root.submission.SubmissionPanelView;
import gov.nist.hit.ds.docentryeditor.client.widgets.uploader.FileUploadMVP;

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
    DocumentModelEditorView getDocumentModelEditorView();
    DocumentModelEditorPresenter getDocumentModelEditorPresenter ();

    // Submission
    SubmissionPanelMVP getSubmissionPanelMVP();
    SubmissionPanelView getSubmissionPanelView();
    SubmissionPanelPresenter getSubmissionPanelPresenter();

    AuthorsListEditorWidget getAuthorsListEditorWidget();

    XdsParser getXdsParser();
}
