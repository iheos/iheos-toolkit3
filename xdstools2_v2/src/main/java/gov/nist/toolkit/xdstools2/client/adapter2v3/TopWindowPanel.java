package gov.nist.toolkit.xdstools2.client.adapter2v3;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.widgets.layout.events.HasPaneChangedHandlers;
import com.smartgwt.client.widgets.layout.events.PaneChangedEvent;
import com.smartgwt.client.widgets.layout.events.PaneChangedHandler;


public class TopWindowPanel extends VerticalPanel implements HasPaneChangedHandlers {

    public TopWindowPanel() {
        super();
    }


    @Override
    public HandlerRegistration addPaneChangedHandler(PaneChangedHandler paneChangedHandler) {
        return addHandler(paneChangedHandler, PaneChangedEvent.getType());

    }

}
