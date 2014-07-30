package edu.tn.xds.metadata.editor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

/**
 * Created by onh2 on 7/18/2014.
 */
public class StartEditXdsDocumentEvent extends GwtEvent<StartEditXdsDocumentEvent.StartEditXdsDocumentHandler> {

    public static Type<StartEditXdsDocumentHandler> TYPE = new Type<StartEditXdsDocumentHandler>();
    private XdsDocumentEntry fileContent;

    public StartEditXdsDocumentEvent(XdsDocumentEntry fileContent) {
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

    public XdsDocumentEntry getDocument() {
        return fileContent;
    }

    public interface StartEditXdsDocumentHandler extends EventHandler {
        public void onStartEditXdsDocument(StartEditXdsDocumentEvent event);
    }
}
