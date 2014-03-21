package gov.nist.hit.ds.logBrowser.client.event;


import com.google.gwt.event.shared.GwtEvent;


public class NewTxMessageEvent extends GwtEvent<NewTxMessageEventHandler>{
	public static final Type<NewTxMessageEventHandler> TYPE = new Type<NewTxMessageEventHandler>();

	private int messageCount;

	  public NewTxMessageEvent(int value) {
	    this.messageCount=value;
	  }



	  public int getValue() {
		return this.messageCount;
	}


	@Override
	public Type<NewTxMessageEventHandler> getAssociatedType() {
	    return TYPE;
	}

	@Override
	protected void dispatch(NewTxMessageEventHandler handler) {
		
		handler.onNewTxMessage(this);
		
	}
}




