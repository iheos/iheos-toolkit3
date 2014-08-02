package edu.tn.xds.metadata.editor.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class EditNewEvent extends GwtEvent<EditNewEvent.EditNewHandler> {

    public interface EditNewHandler extends EventHandler {
        public void onEditNew(EditNewEvent event);
    }

    public static Type<EditNewHandler> TYPE = new Type<EditNewHandler>();


    public EditNewEvent() {
    }

    @Override
    public Type<EditNewHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditNewHandler handler) {
        handler.onEditNew(this);
    }

}
