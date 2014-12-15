package gov.nist.toolkit.xdstools3.client.util.eventBus;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.smartgwt.client.widgets.tab.Tab;

/**
 * Event thrown to close a specific tab
 */
public class CloseOtherTabsEvent extends GwtEvent<CloseOtherTabsEvent.CloseOtherTabsEventHandler> {
    public static Type<CloseOtherTabsEventHandler> TYPE = new Type<CloseOtherTabsEventHandler>();
    private Tab tab;

    public CloseOtherTabsEvent(Tab tab){
        this.tab=tab;
    }

    public Tab getTab(){
        return tab;
    }

    @Override
    public Type<CloseOtherTabsEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CloseOtherTabsEventHandler handler) {
        handler.onCloseOtherTabsEvent(this);
    }

    public interface CloseOtherTabsEventHandler extends EventHandler {
        public void onCloseOtherTabsEvent(CloseOtherTabsEvent event);
    }
}
