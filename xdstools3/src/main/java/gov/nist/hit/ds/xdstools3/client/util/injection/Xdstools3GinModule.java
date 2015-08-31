package gov.nist.hit.ds.xdstools3.client.util.injection;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import gov.nist.hit.ds.xdstools3.client.Xdstools3ActivityView;
import gov.nist.hit.ds.xdstools3.client.activitiesAndPlaces.Xdstools3ActivityMapper;
import gov.nist.hit.ds.xdstools3.client.manager.Manager;
import gov.nist.hit.ds.xdstools3.client.util.eventBus.OpenTabEvent;
import gov.nist.toolkit.xdstools2.client.Xdstools2;
import gov.nist.toolkit.xdstools2.client.event.tabContainer.V2TabOpenedEvent;
import gov.nist.toolkit.xdstools2.client.event.tabContainer.V2TabOpenedEventHandler;

import javax.inject.Inject;


/**
 * GIN counterpart of Guice's Module for GWT. It binds the various classes and providers
 * using a Guice module. The module class looks almost exactly like it would in regular
 * Guice. (GinModule and AbstractGinModule are used instead of Module and AbstractModule.)
 * If GIN can't findSimple a binding for a class, it falls back to calling GWT.create()
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
        // For the xdstools2 GUI
        bind(Xdstools2.class).in(Singleton.class);
    }

    /**
     * Specific provider for PlaceController.
     */
    public static class PlaceControllerProvider implements Provider<PlaceController> {
        @Inject
        EventBus eventBus; // inject the eventbus
        @Inject
        Xdstools2 xdstools2;
        @Inject
        Xdstools3ActivityView xdstools3ActivityView;

        private PlaceController controller;


        @SuppressWarnings("deprecation")
        @Override
        public PlaceController get() {
            if (controller == null) {
                controller = new PlaceController(eventBus); // instantiate placecontroller with the eventbus


                // Initialize Xdstools2 GUI
                xdstools2.loadTkProps();
                xdstools2.setEventBus(eventBus);

                eventBus.addHandler(V2TabOpenedEvent.TYPE, new V2TabOpenedEventHandler() {

                    @Override
                    public void onV2TabOpened(final V2TabOpenedEvent event) {
                        try {
                            // Window.alert("got event!" + event.getTitle() + "eventWidget is null?" + (event.getTopPanel() == null) + " event idx:" + event.getTabIndex());
                            // if (!"FindDocuments".equals(event.getTitle()))
//                            if (event.getTabIndex()>0)

//                            Window.alert("got event!" + event.getTitle() + "eventWidget is null?" + (event.getTopPanel() == null) + " event idx:" + event.getTabIndex());
//                            Window.alert(TabNamesManager.getInstance().getV2DynamicTabCode());
//                            xdstools3ActivityView.openTab(TabNamesManager.getInstance().getV2DynamicTabCode());
                            Manager.EVENT_BUS.fireEvent(new OpenTabEvent(event));

//                    Window.alert(xdstools3ActivityView.setTabId("test"));
                        } catch (Throwable t) {
                            //                    Window.alert("event handler error: " + t.toString());
                            Window.alert(getMessage(t));
//                                t.printStackTrace();
                        }

                    }
                });


            }
            return controller;
        }



    }

    private static String getMessage (Throwable throwable) {
        String ret="";
        while (throwable!=null) {
            if (throwable instanceof com.google.gwt.event.shared.UmbrellaException){
                for (Throwable thr2 :((com.google.gwt.event.shared.UmbrellaException)throwable).getCauses()){
                    if (ret != "")
                        ret += "\nCaused by: ";
                    ret += thr2.toString();
                    ret += "\n  at "+getMessage(thr2);
                }
            } else if (throwable instanceof com.google.web.bindery.event.shared.UmbrellaException){
                for (Throwable thr2 :((com.google.web.bindery.event.shared.UmbrellaException)throwable).getCauses()){
                    if (ret != "")
                        ret += "\nCaused by: ";
                    ret += thr2.toString();
                    ret += "\n  at "+getMessage(thr2);
                }
            } else {
                if (ret != "")
                    ret += "\nCaused by: ";
                ret += throwable.toString();
                for (StackTraceElement sTE : throwable.getStackTrace())
                    ret += "\n  at "+sTE;
            }
            throwable = throwable.getCause();
        }

        return ret;
    }


}
