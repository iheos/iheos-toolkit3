package gov.nist.hit.ds.logBrowser.client.event.asset;

import com.google.gwt.event.shared.EventHandler;

/**
 * Created by skb1 on 8/14/14.
 */
public interface GenericHandler<H> extends EventHandler {
    void onAssetClick(H event);
}

/*

public interface InContextAssetClickedEventHandler extends EventHandler {

    void onAssetClick(InContextAssetClickedEvent event);

}

*/