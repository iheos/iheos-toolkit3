package gov.nist.hit.ds.logBrowser.client.event;


import gov.nist.hit.ds.repository.simple.search.client.AssetNode;

import com.google.gwt.event.shared.GwtEvent;


public class AssetClickedEvent extends GwtEvent<AssetClickedEventHandler>{
	public static final Type<AssetClickedEventHandler> TYPE = new Type<AssetClickedEventHandler>();

	private AssetNode an;

	  public AssetClickedEvent(AssetNode value) {
	    this.an=value;
	  }



	  public AssetNode getValue() {
		return this.an;
	}



	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AssetClickedEventHandler> getAssociatedType() {
	    return TYPE;
	}

	@Override
	protected void dispatch(AssetClickedEventHandler handler) {
		
		handler.onAssetClick(this);
		
	}
}




