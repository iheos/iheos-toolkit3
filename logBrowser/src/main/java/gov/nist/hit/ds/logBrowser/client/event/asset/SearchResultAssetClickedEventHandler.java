package gov.nist.hit.ds.logBrowser.client.event.asset;

import com.google.gwt.event.shared.EventHandler;

public interface SearchResultAssetClickedEventHandler extends EventHandler {

    void onAssetClick(SearchResultAssetClickedEvent event);

}
