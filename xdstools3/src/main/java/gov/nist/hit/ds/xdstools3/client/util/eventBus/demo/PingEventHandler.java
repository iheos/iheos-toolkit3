package gov.nist.hit.ds.xdstools3.client.util.eventBus.demo;

import com.google.gwt.event.shared.EventHandler;

public interface PingEventHandler extends EventHandler {

    void onEvent(PingEvent event);


}