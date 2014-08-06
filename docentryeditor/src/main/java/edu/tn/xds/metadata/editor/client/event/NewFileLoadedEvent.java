package edu.tn.xds.metadata.editor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import edu.tn.xds.metadata.editor.client.event.NewFileLoadedEvent.NewFileLoadedHandler;
import edu.tn.xds.metadata.editor.shared.model.XdsDocumentEntry;

public class NewFileLoadedEvent extends GwtEvent<NewFileLoadedHandler> {

    public static Type<NewFileLoadedHandler> TYPE = new Type<NewFileLoadedHandler>();
    private final XdsDocumentEntry fileContent;

    public NewFileLoadedEvent(XdsDocumentEntry fileContent) {
        this.fileContent = fileContent;
    }

    public XdsDocumentEntry getDocument() {
        return fileContent;
    }

    @Override
    public Type<NewFileLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NewFileLoadedHandler handler) {
        handler.onNewFileLoaded(this);
    }

    public interface NewFileLoadedHandler extends EventHandler {
        public void onNewFileLoaded(NewFileLoadedEvent event);
    }

}
