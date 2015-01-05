package gov.nist.toolkit.xdstools3.client.util.eventBus.demo;

import com.google.gwt.event.shared.GwtEvent;



public class PingEvent extends GwtEvent<PingEventHandler> {
	public static Type<PingEventHandler> TYPE = new Type<PingEventHandler>();

	    // This is the only data that we intend to pass right now in the event object
	    private String message;

	    public PingEvent(String msg){
	        this.message = msg;
	        System.out.println(msg);
	    }

	    public String getMessage(){
	        return this.message;
	    }
	     

	    @Override
	    protected void dispatch(PingEventHandler handler) {
	        handler.onEvent(this);
	    }


	    @Override
	    public com.google.gwt.event.shared.GwtEvent.Type<PingEventHandler> getAssociatedType() {
	        return TYPE;
	    }


}
