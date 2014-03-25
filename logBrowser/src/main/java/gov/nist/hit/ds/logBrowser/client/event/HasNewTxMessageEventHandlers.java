package gov.nist.hit.ds.logBrowser.client.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasNewTxMessageEventHandlers extends HasHandlers {
	public HandlerRegistration addHasNewTxMessageEventHandler(NewTxMessageEventHandler handler);
}
