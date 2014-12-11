package gov.nist.hit.ds.repository.ui.client.event;


import com.google.gwt.event.shared.GwtEvent;


public class ListenerStatusEvent extends GwtEvent<ListenerStatusEventHandler>{
	public static final Type<ListenerStatusEventHandler> TYPE = new Type<ListenerStatusEventHandler>();



    private Boolean listening;

	  public ListenerStatusEvent(Boolean on) {

          setListening(on);
	  }


	@Override
	public Type<ListenerStatusEventHandler> getAssociatedType() {
	    return TYPE;
	}

	@Override
	protected void dispatch(ListenerStatusEventHandler handler) {

        handler.onListenerStatus(this);

		
	}

    public Boolean getListening() {
        return listening;
    }

    public void setListening(Boolean listening) {
        this.listening = listening;
    }


}




