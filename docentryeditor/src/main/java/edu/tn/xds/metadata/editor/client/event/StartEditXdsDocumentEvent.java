package edu.tn.xds.metadata.editor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import edu.tn.xds.metadata.editor.shared.model.DocumentModel;

/**
 * Created by onh2 on 7/18/2014.
 */
public class StartEditXdsDocumentEvent extends GwtEvent<StartEditXdsDocumentEvent.StartEditXdsDocumentHandler> {

    public static Type<StartEditXdsDocumentHandler> TYPE = new Type<StartEditXdsDocumentHandler>();
    private DocumentModel fileContent;

    public StartEditXdsDocumentEvent(DocumentModel fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public Type<StartEditXdsDocumentHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StartEditXdsDocumentHandler handler) {
        handler.onStartEditXdsDocument(this);
    }

    public DocumentModel getDocument() {
        return fileContent;
    }

    public interface StartEditXdsDocumentHandler extends EventHandler {
        public void onStartEditXdsDocument(StartEditXdsDocumentEvent event);
    }
}
