package gov.nist.hit.ds.docentryeditor.client.event;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import gov.nist.hit.ds.docentryeditor.client.event.NewFileLoadedEvent.NewFileLoadedHandler;
import gov.nist.hit.ds.docentryeditor.client.event.SaveFileEvent.SaveFileEventHandler;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsDocumentEntry;

/**
 * This is the application event bus. It is used through the entire application.
 * It has its own personalized handlers.
 */
public class MetadataEditorEventBus extends SimpleEventBus {
    // TODO personalized handlers

    /**
     * Method that handle actions that must be triggered when a new file is loaded.
     * @param handler
     * @return
     */
    public HandlerRegistration addFileLoadedHandler(NewFileLoadedHandler handler) {
        return addHandler(NewFileLoadedEvent.TYPE, handler);
    }

    /**
     * Method that signals to the application that a new file has been loaded into the system.
     * @param documentEntry
     */
    public void fireNewFileLoadedEvent(XdsDocumentEntry documentEntry) {
        fireEvent(new NewFileLoadedEvent(documentEntry));
    }

    /**
     * Method that adds an handler for when a request to save a file is made.
     * @param handler
     * @return
     */
    public HandlerRegistration addSaveFileEventHandler(SaveFileEventHandler handler) {
        return addHandler(SaveFileEvent.TYPE, handler);
    }

    /**
     * This method signals to the entire application that request to save a file has been made.
     */
    public void fireSaveFileEvent() {
        fireEvent(new SaveFileEvent());
    }

    /**
     * This method adds an handler that will enable to trigger actions when a Document Entry starts to be edited.
     * @param handler
     * @return
     */
    public HandlerRegistration addStartEditXdsDocumentHandler(StartEditXdsDocumentEvent.StartEditXdsDocumentHandler handler) {
        return addHandler(StartEditXdsDocumentEvent.TYPE,handler);
    }

    /**
     * This methods signals a request to start editing a specific document entry.
     * @param documentEntry
     */
    public void fireStartEditXdsDocumentEvent(XdsDocumentEntry documentEntry) {
        fireEvent(new StartEditXdsDocumentEvent(documentEntry));
    }

    public HandlerRegistration addSaveCurrentlyEditedDocumentHandler(SaveCurrentlyEditedDocumentEvent.SaveCurrentlyEditedDocumentEventHandler handler) {
        return addHandler(SaveCurrentlyEditedDocumentEvent.TYPE, handler);
    }

    public void fireSaveCurrentlyEditedDocumentEvent(XdsDocumentEntry documentEntry) {
        fireEvent(new SaveCurrentlyEditedDocumentEvent(documentEntry));
    }

    public HandlerRegistration addXdsEditorLoadedEventtHandler(XdsEditorLoadedEvent.XdsEditorLoadedEventHandler handler) {
        return addHandler(XdsEditorLoadedEvent.TYPE, handler);
    }

    public void fireXdsEditorLoadedEvent() {
        fireEvent(new XdsEditorLoadedEvent());
    }

    public HandlerRegistration addLoadPreFilledDocEntryEventHandler(LoadPrefilledDocEntryEvent.LoadPrefilledDocEntryEventHandler handler) {
        return addHandler(LoadPrefilledDocEntryEvent.TYPE, handler);
    }
    public void fireLoadPreFilledDocEntryEvent() {
        fireEvent(new LoadPrefilledDocEntryEvent());
    }

    /**
     * This method adds an event handler that will do the navigation to the home page.
     * @param handler
     * @return
     */
    public HandlerRegistration addBackToHomePageEventHandler(BackToHomePageEvent.BackToHomePageEventHandler handler) {
        return addHandler(BackToHomePageEvent.TYPE, handler);
    }

    /**
     * This method signals a request to navigate back to the home page of the application.
     */
    public void fireBackToHomePageEvent() {
        fireEvent(new BackToHomePageEvent());
    }
}
