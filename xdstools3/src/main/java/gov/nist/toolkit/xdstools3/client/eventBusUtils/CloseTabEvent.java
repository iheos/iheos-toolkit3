package gov.nist.toolkit.xdstools3.client.eventBusUtils;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * Event thrown to close a specific tab
 */
public class CloseTabEvent extends GwtEvent<CloseTabEvent.CloseTabEventHandler> {
    public static Type<CloseTabEventHandler> TYPE = new Type<CloseTabEventHandler>();
    private Tab tab;

    public CloseTabEvent(Tab tab){
        this.tab=tab;
    }

    public Tab getTab(){
        return tab;
    }

    @Override
    public Type<CloseTabEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CloseTabEventHandler handler) {
        handler.onCloseTabEvent(this);
    }

    public interface CloseTabEventHandler extends EventHandler {
        public void onCloseTabEvent(CloseTabEvent event);
    }
}
