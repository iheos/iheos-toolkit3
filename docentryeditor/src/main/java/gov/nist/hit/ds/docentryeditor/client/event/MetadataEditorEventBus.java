package gov.nist.hit.ds.docentryeditor.client.event;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import gov.nist.hit.ds.docentryeditor.client.event.NewFileLoadedEvent.NewFileLoadedHandler;
import gov.nist.hit.ds.docentryeditor.client.event.SaveFileEvent.SaveFileEventHandler;

public class MetadataEditorEventBus extends SimpleEventBus {
    // TODO personalized handlers
    public HandlerRegistration addFileLoadedHandler(NewFileLoadedHandler handler) {
        return addHandler(NewFileLoadedEvent.TYPE, handler);
    }

    public void fireNewFileLoadedEvent(NewFileLoadedEvent event) {
        fireEvent(event);
    }

    public HandlerRegistration addSaveFileEventHandler(SaveFileEventHandler handler) {
        return addHandler(SaveFileEvent.TYPE, handler);
    }

    public void fireSaveFileEvent(SaveFileEvent event) {
        fireEvent(event);
    }

    public HandlerRegistration addStartEditXdsDocumentHandler(StartEditXdsDocumentEvent.StartEditXdsDocumentHandler handler) {
        return addHandler(StartEditXdsDocumentEvent.TYPE,handler);
    }
    public void fireStartEditXdsDocumentEvent(StartEditXdsDocumentEvent event) {
        fireEvent(event);
    }

    public HandlerRegistration addSaveCurrentlyEditedDocumentHandler(SaveCurrentlyEditedDocumentEvent.SaveCurrentlyEditedDocumentEventHandler handler) {
        return addHandler(SaveCurrentlyEditedDocumentEvent.TYPE, handler);
    }

    public void fireSaveCurrentlyEditedDocumentEvent(SaveCurrentlyEditedDocumentEvent event) {
        fireEvent(event);
    }

    public HandlerRegistration addXdsEditorLoadedEventtHandler(XdsEditorLoadedEvent.XdsEditorLoadedEventHandler handler) {
        return addHandler(XdsEditorLoadedEvent.TYPE, handler);
    }

    public void fireXdsEditorLoadedEvent(XdsEditorLoadedEvent event) {
        fireEvent(event);
    }

    public HandlerRegistration addLoadPreFilledDocEntryEventHandler(LoadPrefilledDocEntryEvent.LoadPrefilledDocEntryEventHandler handler) {
        return addHandler(LoadPrefilledDocEntryEvent.TYPE, handler);
    }
    public void fireLoadPreFilledDocEntryEvent() {
        fireEvent(new LoadPrefilledDocEntryEvent());
    }

    public void fireBackToHomePageEvent() {
        fireEvent(new BackToHomePageEvent());
    }
    public HandlerRegistration adddBackToHomePageEventHandler(BackToHomePageEvent.BackToHomePageEventHandler handler) {
        return addHandler(BackToHomePageEvent.TYPE, handler);
    }
}
