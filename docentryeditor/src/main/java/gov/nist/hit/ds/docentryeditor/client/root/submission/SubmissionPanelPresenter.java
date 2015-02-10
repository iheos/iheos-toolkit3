package gov.nist.hit.ds.docentryeditor.client.root.submission;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import gov.nist.hit.ds.docentryeditor.client.MetadataEditorRequestFactory;
import gov.nist.hit.ds.docentryeditor.client.editor.EditorPlace;
import gov.nist.hit.ds.docentryeditor.client.event.*;
import gov.nist.hit.ds.docentryeditor.client.generics.abstracts.AbstractPresenter;
import gov.nist.hit.ds.docentryeditor.client.home.WelcomePlace;
import gov.nist.hit.ds.docentryeditor.client.parse.*;
import gov.nist.hit.ds.docentryeditor.client.resources.AppResources;
import gov.nist.hit.ds.docentryeditor.shared.model.*;

import javax.inject.Inject;

/**
 * Created by onh2 on 7/11/2014.
 */
public class SubmissionPanelPresenter extends AbstractPresenter<SubmissionPanelView> {
    @Inject
    PlaceController placeController;
    @Inject
    XdsParser xdsParser;
    @Inject
    MetadataEditorRequestFactory requestFactory;

    private SubmissionMenuData currentlyEdited;
    private int nextIndex = 1;
    private XdsDocumentEntry prefilledDocEntry;

    @Override
    public void init() {
        bind();
        requestFactory.initialize(eventBus);
    }

    /**
     * Method that handles the different event fired in the event bus.
     */
    private void bind() {
        ((MetadataEditorEventBus) getEventBus()).adddBackToHomePageEventHandler(new BackToHomePageEvent.BackToHomePageEventHandler() {
            @Override
            public void onBackToHomePage(BackToHomePageEvent event) {
                getView().getTree().getSelectionModel().deselectAll();
                placeController.goTo(new WelcomePlace());
            }
        });
        ((MetadataEditorEventBus) getEventBus()).addFileLoadedHandler(new NewFileLoadedEvent.NewFileLoadedHandler() {
            @Override
            public void onNewFileLoaded(NewFileLoadedEvent event) {
                currentlyEdited = new SubmissionMenuData("DocEntry" + nextIndex, "Document Entry " + nextIndex, event.getDocument());
                nextIndex++;
                view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
                view.getTree().expandAll();
                if (!(placeController.getWhere() instanceof EditorPlace)) {
                    logger.info(placeController.getWhere().toString());
                    placeController.goTo(new EditorPlace());
                }
                view.getTree().getSelectionModel().select(currentlyEdited, false);
            }
        });
        ((MetadataEditorEventBus) getEventBus()).addXdsEditorLoadedEventtHandler(new XdsEditorLoadedEvent.XdsEditorLoadedEventHandler() {
            @Override
            public void onXdsEditorLoaded(XdsEditorLoadedEvent event) {
                if (currentlyEdited != null) {
                    ((MetadataEditorEventBus) getEventBus()).fireStartEditXdsDocumentEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
                } else {
                    logger.info("No Document Entry in Submission Set");
                    createNewDocumentEntry();
                }
            }
        });
        ((MetadataEditorEventBus) getEventBus()).addLoadPreFilledDocEntryEventHandler(new LoadPrefilledDocEntryEvent.LoadPrefilledDocEntryEventHandler() {
            @Override
            public void onLoadPrefilledDocEntryHandler(LoadPrefilledDocEntryEvent event) {
                createPreFilledDocumentEntry();
            }
        });
    }

    /**
     * This method creates a new Document Entry and adds it to the submissionSet tree.
     */
    public void createNewDocumentEntry() {
        logger.info("Create new document entry");
        currentlyEdited = new SubmissionMenuData("DocEntry" + nextIndex, "Document Entry " + nextIndex, new XdsDocumentEntry());
        nextIndex++;
        view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
        view.getTree().expandAll();
        if (!(placeController.getWhere() instanceof EditorPlace)) {
            placeController.goTo(new EditorPlace());
        }
        view.getTree().getSelectionModel().select(currentlyEdited, false);
    }

    /**
     * This method loads the editor interface with a selected document entry from the submission set tree.
     *
     * @param selectedItem tree selected node
     */
    public void loadDocumentEntry(SubmissionMenuData selectedItem) {
        if (!selectedItem.equals(view.getSubmissionSetTreeNode())) {
            currentlyEdited = selectedItem;
            startEditing();
        }
    }

    /**
     * This method load the editor user interface with a pre-filled document entry which is added to the submission set tree.
     */
    public void createPreFilledDocumentEntry() {
        if (prefilledDocEntry==null) {
            prefilledDocEntry = xdsParser.parse(PreParse.getInstance().doPreParse(AppResources.INSTANCE.xdsPrefill().getText()));
            prefilledDocEntry.setFileName(new String256("new-doc-entry"));
        }
        //------------------------------------------- MIGHT CHANGE
        logger.info("Create new pre-filled document entry");
        currentlyEdited = new SubmissionMenuData("DocEntry" + nextIndex, "Document Entry " + nextIndex, prefilledDocEntry);
        nextIndex++;
//        if (!(placeController.getWhere() instanceof EditorPlace)) {
//            placeController.goTo(new EditorPlace());
//        }
        view.getTreeStore().add(view.getTreeStore().getRootItems().get(0), currentlyEdited);
        view.getTree().expandAll();
        view.getTree().getSelectionModel().select(currentlyEdited, false);
    }

    public void doSave() {
        ((MetadataEditorEventBus) eventBus).fireSaveFileEvent(new SaveFileEvent());
        ((MetadataEditorEventBus) eventBus).addSaveCurrentlyEditedDocumentHandler(new SaveCurrentlyEditedDocumentEvent.SaveCurrentlyEditedDocumentEventHandler() {
            @Override
            public void onSaveCurrentlyEditedDocumentEvent(SaveCurrentlyEditedDocumentEvent event) {
                currentlyEdited.setModel(event.getDocumentEntry());
//                save();
            }
        });
    }

    private void save() {
//        String xmlFileContent=new String();
//        for (SubmissionMenuData submissionMenuData:view.getTreeStore().getAll()){
//            if (submissionMenuData.getValue().startsWith("Document")){
//                xmlFileContent+=submissionMenuData.getModel().toXML()+"\n";
//                logger.info(xmlFileContent);
//            }
//        }
//        xmlFileContent.replaceAll("<\\?xml version=\"1\\.0\" encoding=\"UTF-8\"\\?>"," ");
//        logger.info(xmlFileContent);
//        if (!xmlFileContent.isEmpty()) {
        requestFactory.saveFileRequestContext().saveAsXMLFile(currentlyEdited.getModel().getFileName().toString(), currentlyEdited.getModel().toXML()).fire(new Receiver<String>() {

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
                        new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10, 5, 10, 5)));
                vp.add(htmlP, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10, 5, 10, 5)));
                d.add(vp);

                d.setPredefinedButtons(Dialog.PredefinedButton.OK);
                d.setButtonAlign(BoxLayoutContainer.BoxLayoutPack.CENTER);
                d.setHideOnButtonClick(true);
                d.setHeadingText("XML Metadata File Download");
                d.show();
            }
        });
    }

    /**
     * This method loads a document entry into the editor user interface, which is loaded if not already.
     */
    private void startEditing() {
        logger.info("Start editing selected document entry");
        if(!(placeController.getWhere() instanceof EditorPlace)){
            placeController.goTo(new EditorPlace());
        }
        ((MetadataEditorEventBus) getEventBus()).fireStartEditXdsDocumentEvent(new StartEditXdsDocumentEvent(currentlyEdited.getModel()));
    }

    public SubmissionMenuData getCurrentlyEdited(){
        return currentlyEdited;
    }
}
