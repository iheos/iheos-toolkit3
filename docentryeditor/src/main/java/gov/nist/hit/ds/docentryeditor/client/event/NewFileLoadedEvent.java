package gov.nist.hit.ds.docentryeditor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import gov.nist.hit.ds.docentryeditor.client.event.NewFileLoadedEvent.NewFileLoadedHandler;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsDocumentEntry;
import gov.nist.hit.ds.docentryeditor.shared.model.XdsMetadata;

public class NewFileLoadedEvent extends GwtEvent<NewFileLoadedHandler> {

    public static Type<NewFileLoadedHandler> TYPE = new Type<NewFileLoadedHandler>();
    private final XdsMetadata metadata;

    public NewFileLoadedEvent(XdsMetadata metadata) {
        this.metadata = metadata;
    }

    public XdsMetadata getMetadata() {
        return metadata;
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
