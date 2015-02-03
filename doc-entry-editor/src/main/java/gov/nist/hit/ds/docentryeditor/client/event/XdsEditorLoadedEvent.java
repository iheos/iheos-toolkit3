package gov.nist.hit.ds.docentryeditor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class XdsEditorLoadedEvent extends GwtEvent<XdsEditorLoadedEvent.XdsEditorLoadedEventHandler> {

    public static Type<XdsEditorLoadedEventHandler> TYPE = new Type<XdsEditorLoadedEventHandler>();

    public XdsEditorLoadedEvent() {
    }

    @Override
    public Type<XdsEditorLoadedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(XdsEditorLoadedEventHandler handler) {
        handler.onXdsEditorLoaded(this);
    }

    public interface XdsEditorLoadedEventHandler extends EventHandler {
        public void onXdsEditorLoaded(XdsEditorLoadedEvent event);
    }

}
