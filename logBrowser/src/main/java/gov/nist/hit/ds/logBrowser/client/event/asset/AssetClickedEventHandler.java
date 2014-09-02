package gov.nist.hit.ds.logBrowser.client.event.asset;

import com.google.gwt.event.shared.EventHandler;

 

public interface AssetClickedEventHandler extends EventHandler {

	void onAssetClick(AssetClickedEvent event);

}
