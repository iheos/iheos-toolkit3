package gov.nist.hit.ds.logBrowser.client.event;


import com.google.gwt.event.shared.GwtEvent;
import gov.nist.hit.ds.repository.simple.search.client.AssetNode;


public class NewTxMessageEvent extends GwtEvent<NewTxMessageEventHandler>{
	public static final Type<NewTxMessageEventHandler> TYPE = new Type<NewTxMessageEventHandler>();



    private int messageCount;


    private AssetNode assetNode;

	  public NewTxMessageEvent(int value, AssetNode an) {

          setMessageCount(value);

          setAssetNode(an);
	  }

	  public int getValue() {
		return this.messageCount;
	}

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public AssetNode getAssetNode() {
        return assetNode;
    }

    public void setAssetNode(AssetNode assetNode) {
        this.assetNode = assetNode;
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




