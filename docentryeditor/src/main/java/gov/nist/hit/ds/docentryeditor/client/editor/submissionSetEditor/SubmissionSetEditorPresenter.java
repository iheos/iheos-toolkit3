package gov.nist.hit.ds.docentryeditor.client.editor.submissionSetEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import gov.nist.hit.ds.docentryeditor.client.editor.documentEntryEditor.DocumentEntryEditorView;
import gov.nist.hit.ds.docentryeditor.client.generics.abstracts.AbstractPresenter;
import gov.nist.hit.ds.docentryeditor.client.parser.XdsParserServices;
import gov.nist.hit.ds.docentryeditor.client.parser.XdsParserServicesAsync;
import gov.nist.hit.ds.docentryeditor.client.utils.MetadataEditorRequestFactory;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsDocumentEntry;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsSubmissionSet;

import javax.inject.Inject;

/**
 * Created by onh2 on 3/2/2015.
 */
public class SubmissionSetEditorPresenter extends AbstractPresenter<SubmissionSetEditorView> {
    private final static XdsParserServicesAsync xdsParserServicesAsync=GWT.create(XdsParserServices.class);
    protected XdsSubmissionSet model=new XdsSubmissionSet();
    private SubmissionSetEditorDriver editorDriver = GWT.create(SubmissionSetEditorDriver.class);

//    @Inject
//    MetadataEditorRequestFactory requestFactory;

    /**
     * Method that initializes the editor and the request factory on submission set activity start.
     */
    @Override
    public void init() {
        bind();
        initDriver(model);
//        requestFactory.initialize(eventBus);
    }

    /**
     * Method that initializes the editor with a document entry object.
     * @param model
     */
    private void initDriver(XdsSubmissionSet model) {
        this.model=model;
        editorDriver.initialize(view);
//        getView().authors.getAuthorWidget().initEditorDriver();
        logger.info("Init driver with: "+model.toString());
        editorDriver.edit(model);
//        getView().refreshGridButtonsDisplay();
    }

    /**
     * Method that ties actions and view together. It mostly handles gwt event form the event bus.
     */
    private void bind() {

    }

    /**
     * Interface of a XDS Submission Set Editor Driver
     */
    interface SubmissionSetEditorDriver extends SimpleBeanEditorDriver<XdsSubmissionSet, SubmissionSetEditorView> {

    }
}
