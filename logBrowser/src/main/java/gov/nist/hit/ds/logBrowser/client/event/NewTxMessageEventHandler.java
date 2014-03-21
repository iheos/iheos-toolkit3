package gov.nist.hit.ds.logBrowser.client.event;

import com.google.gwt.event.shared.EventHandler;


public interface NewTxMessageEventHandler extends EventHandler {

	void onNewTxMessage(NewTxMessageEvent event);

}
