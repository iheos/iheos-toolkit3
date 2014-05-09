package gov.nist.toolkit.xdstools3.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * To be used in GWT Eventbus. Opens a new tab. Takes as argument the name of the tab to open.
 */
public class OpenTabEvent extends GwtEvent<EventHandler> {
	public static Type<EventHandler> TYPE = new Type<EventHandler>();

	    // Data to be passed to the event object
	    private String tabName;

	    public OpenTabEvent(String _tabName){
	        tabName = _tabName;
	    }

	    public String getTabName(){
            return tabName;
	    }


	    @Override
	    protected void dispatch(EventHandler handler) {
	        handler.onEvent(this);
	    }


	    @Override
	    public Type<EventHandler> getAssociatedType() {
	        return TYPE;
	    }


}
