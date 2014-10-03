package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Created by onh2 on 9/22/2014.
 */
@GinModules(Xdstools3GinModule.class)
public interface Xdstools3GinInjector extends Ginjector{

    public final static Xdstools3GinInjector injector = GWT.create(Xdstools3GinInjector.class);

    Xdstools3ActivityView getXdstools3();
    EventBus getEventBus();
    PlaceController getPlaceController();

    ActivityMapper getActivityMapper();
}
