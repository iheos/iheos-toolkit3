package edu.tn.xds.metadata.editor.client.event;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent.NewFileLoadedHandler;
import edu.tn.xds.metadata.editor.client.event.SaveFileEvent.SaveFileEventHandler;

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


//    public void fireEditNewEvent(EditNewEvent event) {
//        fireEvent(event);
//    }
//
//    public HandlerRegistration addStartEditXdsDocumentHandler(StartEditXdsDocumentEvent.StartEditXdsDocumentHandler handler) {
//        return addHandler(StartEditXdsDocumentEvent.TYPE, handler);
//    }

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
}
