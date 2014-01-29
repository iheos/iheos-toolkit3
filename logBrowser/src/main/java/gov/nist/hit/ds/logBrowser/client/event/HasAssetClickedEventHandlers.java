package gov.nist.hit.ds.logBrowser.client.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasAssetClickedEventHandlers extends HasHandlers {
	public HandlerRegistration addHasAssetClickedEventHandler(AssetClickedEventHandler handler);
}
