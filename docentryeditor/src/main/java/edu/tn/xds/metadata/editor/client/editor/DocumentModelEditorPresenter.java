package edu.tn.xds.metadata.editor.client.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import edu.tn.xds.metadata.editor.client.MetadataEditorRequestFactory;
import edu.tn.xds.metadata.editor.client.event.MetadataEditorEventBus;
import edu.tn.xds.metadata.editor.client.event.SaveFileEvent;
import edu.tn.xds.metadata.editor.client.event.SaveFileEvent.SaveFileEventHandler;
import edu.tn.xds.metadata.editor.client.event.StartEditXdsDocumentEvent;
import edu.tn.xds.metadata.editor.client.generics.abstracts.AbstractPresenter;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;

import javax.inject.Inject;
import java.util.logging.Logger;

public class DocumentModelEditorPresenter extends AbstractPresenter<DocumentModelEditorView> {

    protected static Logger logger = Logger.getLogger(DocumentModelEditorPresenter.class.getName());
    protected DocumentModel model;
    EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @Inject
    MetadataEditorRequestFactory requestFactory;

    @Override
    public void init() {
        model = new DocumentModel();
        initDriver(model);
        requestFactory.initialize(eventBus);
        bind();
    }

    private void initDriver(DocumentModel model) {
        editorDriver.initialize(view);
        getView().author.initEditorDriver();
//		getView().title.initEditorDriver();
//		getView().comment.initEditorDriver();
//		getView().confidentialityCode.initEditorDriver();
        editorDriver.edit(model);
    }

    private void bind() {
        getEventBus().addHandler(StartEditXdsDocumentEvent.TYPE, new StartEditXdsDocumentEvent.StartEditXdsDocumentHandler() {

            @Override
            public void onStartEditXdsDocument(StartEditXdsDocumentEvent event) {
                model = event.getDocument();
                initDriver(model);
            }
        });
        ((MetadataEditorEventBus) getEventBus()).addSaveFileEventHandler(new SaveFileEventHandler() {

            @Override
            public void onFileSave(SaveFileEvent event) {
                doSave();
            }
        });
    }

    /**
     * Method to handle edited metadata file download with editor's validation check.
     */
    public void doSave() {
        if (editorDriver.isDirty()) {
            model = editorDriver.flush();
            logger.info(model.toXML());

            if (editorDriver.hasErrors()) {
                final ConfirmMessageBox cmb = new ConfirmMessageBox("Error", "There are errors in your editor. Are you sure you want to download a copy of these data? They may not be usable.");
                cmb.show();
                cmb.addHideHandler(new HideEvent.HideHandler() {
                    public void onHide(HideEvent event) {
                        if (cmb.getHideButton() == cmb.getButtonById(PredefinedButton.YES.name())) {
                            // perform YES action
                            save();
                            cmb.hide();
                        } else if (cmb.getHideButton() == cmb.getButtonById(PredefinedButton.NO.name())) {
                            // perform NO action
                        }
                    }
                });
//				StringBuilder errorBuilder = new StringBuilder();
//				for (EditorError error : editorDriver.getErrors()) {
//					errorBuilder.append(error.getMessage() + "\n");
//				}
//				Window.alert(errorBuilder.toString());
            } else {
                save();
            }
        } else {
            final ConfirmMessageBox cmb = new ConfirmMessageBox("", "Data have not changed. Are you sure you want to download a copy of these data?");
            cmb.show();
            cmb.addHideHandler(new HideEvent.HideHandler() {
                public void onHide(HideEvent event) {
                    if (cmb.getHideButton() == cmb.getButtonById(PredefinedButton.YES.name())) {
                        // perform YES action
                        save();
                        cmb.hide();
                    } else if (cmb.getHideButton() == cmb.getButtonById(PredefinedButton.NO.name())) {
                        // perform NO action
                    }
                }
            });
        }
    }

    /**
     * Method which actually handle saving (on server) and download for the edited metadata file.
     */
    private void save() {
        String filename = model.getFileName().toString();
        requestFactory.saveFileRequestContext().saveAsXMLFile(filename, model.toXML()).fire(new Receiver<String>() {

            @Override
            public void onSuccess(String response) {
                logger.info("saveAsXMLFile call succeed");
                logger.info("Generated filename sent to the client \n" + response);
                logger.info("File's URL: " + GWT.getHostPageBaseURL() + "files/" + response);
                Window.open(GWT.getHostPageBaseURL() + "files/" + response, response + " Metadata File", "enabled");
                Dialog d = new Dialog();
                HTMLPanel htmlP = new HTMLPanel("<a href='" + GWT.getHostPageBaseURL() + "files/" + response + "'>"
                        + GWT.getHostPageBaseURL() + "files/" + response + "</a>");
                VerticalLayoutContainer vp = new VerticalLayoutContainer();
                vp.add(new Label("Your download is in progress, please allow this application to open popups with your browser..."),
                        new VerticalLayoutData(1, 1, new Margins(10, 5, 10, 5)));
                vp.add(htmlP, new VerticalLayoutData(1, 1, new Margins(10, 5, 10, 5)));
                d.add(vp);

                d.setPredefinedButtons(PredefinedButton.OK);
                d.setButtonAlign(BoxLayoutPack.CENTER);
                d.setHideOnButtonClick(true);
                d.setHeadingText("XML Metadata File Download");
                d.show();
            }
        });


    }

    public DocumentModel getModel() {
        return model;
    }

    interface EditorDriver extends SimpleBeanEditorDriver<DocumentModel, DocumentModelEditorView> {

    }

}
