package gov.nist.toolkit.xdstools3.client;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.inject.Singleton;

import javax.inject.Inject;


/**
 * Created by onh2 on 9/22/2014.
 */
public class Xdstools3GinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(com.google.web.bindery.event.shared.EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        bind(PlaceController.class).toProvider(PlaceControllerProvider.class).in(Singleton.class);
        bind(ActivityMapper.class).to(Xdstools3ActivityMapper.class);
    }

    // Provider for PlaceController
    public static class PlaceControllerProvider implements Provider<PlaceController> {
        @Inject
        EventBus eventBus;
        private PlaceController controller;

        @SuppressWarnings("deprecation")
        @Override
        public PlaceController get() {
            if (controller == null) {
                controller = new PlaceController(eventBus);
            }
            return controller;
        }
    }

}
