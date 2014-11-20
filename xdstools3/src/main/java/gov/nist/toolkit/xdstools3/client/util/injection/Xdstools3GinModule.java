package gov.nist.toolkit.xdstools3.client.util.injection;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.inject.Singleton;
import gov.nist.toolkit.xdstools3.client.activitiesAndPlaces.Xdstools3ActivityMapper;

import javax.inject.Inject;


/**
 * GIN counterpart of Guice's Module for GWT. It binds the various classes and providers
 * using a Guice module. The module class looks almost exactly like it would in regular
 * Guice. (GinModule and AbstractGinModule are used instead of Module and AbstractModule.)
 * If GIN can't find a binding for a class, it falls back to calling GWT.create()
 * on that class. What this means that image bundles and translated messages
 * will just magically work.
 *
 * Created by onh2 on 9/22/2014.
 */
public class Xdstools3GinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        // binding the abstract class EventBus to its implementation SimpleEventBus.
        bind(com.google.web.bindery.event.shared.EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        // binding the place controller to its provider who knows how to instantiate the class.
        bind(PlaceController.class).toProvider(PlaceControllerProvider.class).in(Singleton.class);
        // binding the ActivityMapper interface to its implementation in Xdstools3AcitivtyMapper
        bind(ActivityMapper.class).to(Xdstools3ActivityMapper.class).in(Singleton.class);
    }

    /**
     * Specific provider for PlaceController.
     */
    public static class PlaceControllerProvider implements Provider<PlaceController> {
        @Inject
        EventBus eventBus; // inject the eventbus
        private PlaceController controller;

        @SuppressWarnings("deprecation")
        @Override
        public PlaceController get() {
            if (controller == null) {
                controller = new PlaceController(eventBus); // instanciate placecontroller with the eventbus
            }
            return controller;
        }
    }

}
