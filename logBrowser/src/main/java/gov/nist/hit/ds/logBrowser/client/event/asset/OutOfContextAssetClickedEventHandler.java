package gov.nist.hit.ds.logBrowser.client.event.asset;

import com.google.gwt.event.shared.EventHandler;

public interface OutOfContextAssetClickedEventHandler extends EventHandler {

    void onAssetClick(OutOfContextAssetClickedEvent event);

}
