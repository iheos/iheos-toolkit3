package gov.nist.toolkit.xdstools3.client.eventBusUtils;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event to throw to close all the open tabs.
 */
public class CloseAllTabsEvent extends GwtEvent<CloseAllTabsEvent.CloseAllTabsEventHandler> {
    public static Type<CloseAllTabsEventHandler> TYPE = new Type<CloseAllTabsEventHandler>();

    @Override
    public Type<CloseAllTabsEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CloseAllTabsEventHandler handler) {
        handler.onCloseAllTabsEvent(this);
    }

    public interface CloseAllTabsEventHandler extends EventHandler {
        public void onCloseAllTabsEvent(CloseAllTabsEvent event);
    }
}

