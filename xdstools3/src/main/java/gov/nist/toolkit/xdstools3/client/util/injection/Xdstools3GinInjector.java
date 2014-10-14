package gov.nist.toolkit.xdstools3.client.util.injection;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import gov.nist.toolkit.xdstools3.client.Xdstools3ActivityView;

/**
 * Application Client's Factory. Ginjector type can be used to bootstrap injection.
 * It is similar to GWT bundles (image or text). You just create a method for each object
 * you want to instantiate and Gin will generate it for you.
 *
 * See how Gin and Guice works: https://code.google.com/p/google-gin/wiki/GinTutorial
 *                              https://github.com/google/guice/wiki/Motivation
 *
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
