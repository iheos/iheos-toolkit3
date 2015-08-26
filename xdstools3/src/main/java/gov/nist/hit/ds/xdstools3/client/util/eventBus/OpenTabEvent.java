package gov.nist.hit.ds.xdstools3.client.util.eventBus;

import com.google.gwt.event.shared.GwtEvent;
import gov.nist.toolkit.xdstools2.client.event.tabContainer.V2TabOpenedEvent;

/**
 * To be used in GWT Eventbus. Opens a new tab. Takes as argument the name of the tab to open.
 * Example of use:  Util.EVENT_BUS.fireEvent(new OpenTabEvent("ENDPOINTS"));
 */
public class OpenTabEvent extends GwtEvent<OpenTabEventHandler> {
	public static Type<OpenTabEventHandler> TYPE = new Type<OpenTabEventHandler>();

	    // Data to be passed to the event object
	    private String tabName;

        // V2 Tab Open Event
        private V2TabOpenedEvent v2TabOpenedEvent;

	    public OpenTabEvent(String _tabName){
	        tabName = _tabName;
	    }

        public OpenTabEvent(V2TabOpenedEvent v2TabOpenedEvent){
            tabName = v2TabOpenedEvent.getTitle();
            this.v2TabOpenedEvent = v2TabOpenedEvent;
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


    public V2TabOpenedEvent getV2TabOpenedEvent() {
        return v2TabOpenedEvent;
    }

    public void setV2TabOpenedEvent(V2TabOpenedEvent v2TabOpenedEvent) {
        this.v2TabOpenedEvent = v2TabOpenedEvent;
    }
}
