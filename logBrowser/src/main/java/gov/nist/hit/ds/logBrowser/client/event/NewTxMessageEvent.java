package gov.nist.hit.ds.logBrowser.client.event;


import com.google.gwt.event.shared.GwtEvent;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;

import java.util.Map;


public class NewTxMessageEvent extends GwtEvent<NewTxMessageEventHandler>{
	public static final Type<NewTxMessageEventHandler> TYPE = new Type<NewTxMessageEventHandler>();


    private int messageCount;


    private Map<String,AssetNode> anMap;

	  public NewTxMessageEvent(int value, Map<String,AssetNode> anMap) {

          setMessageCount(value);
          setAnMap(anMap);
	  }

	  public int getValue() {
		return getMessageCount();
	}

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }


	@Override
	public Type<NewTxMessageEventHandler> getAssociatedType() {
	    return TYPE;
	}

	@Override
	protected void dispatch(NewTxMessageEventHandler handler) {
		
		handler.onNewTxMessage(this);
		
	}


    public Map<String, AssetNode> getAnMap() {
        return anMap;
    }

    public void setAnMap(Map<String, AssetNode> anMap) {
        this.anMap = anMap;
    }
}




