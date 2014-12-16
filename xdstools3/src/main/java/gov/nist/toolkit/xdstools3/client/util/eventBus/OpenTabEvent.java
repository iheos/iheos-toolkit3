package gov.nist.toolkit.xdstools3.client.util.eventBus;

import com.google.gwt.event.shared.GwtEvent;

/**
 * To be used in GWT Eventbus. Opens a new tab. Takes as argument the name of the tab to open.
 * Example of use:  Util.EVENT_BUS.fireEvent(new OpenTabEvent("ENDPOINTS"));
 */
public class OpenTabEvent extends GwtEvent<OpenTabEventHandler> {
	public static Type<OpenTabEventHandler> TYPE = new Type<OpenTabEventHandler>();

	    // Data to be passed to the event object
	    private String tabName;

	    public OpenTabEvent(String _tabName){
	        tabName = _tabName;
	    }

	    public String getTabName(){
            return tabName;
	    }


	    @Override
	    protected void dispatch(OpenTabEventHandler handler) {
	        handler.onEvent(this);
	    }


	    @Override
	    public Type<OpenTabEventHandler> getAssociatedType() {
	        return TYPE;
	    }


}
