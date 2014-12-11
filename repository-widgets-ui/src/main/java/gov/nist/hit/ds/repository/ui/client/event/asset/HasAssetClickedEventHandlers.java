package gov.nist.hit.ds.repository.ui.client.event.asset;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasAssetClickedEventHandlers extends HasHandlers {
	public HandlerRegistration addHasAssetClickedEventHandler(AssetClickedEventHandler handler);
}
