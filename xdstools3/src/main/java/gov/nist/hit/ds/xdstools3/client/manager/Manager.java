package gov.nist.hit.ds.xdstools3.client.manager;


import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import gov.nist.hit.ds.xdstools3.client.util.injection.Xdstools3GinInjector;
import gov.nist.toolkit.xdstools2.client.Xdstools2;

/** Stores static variables & variables common to the application. This is a singleton.
 *
 */
public class Manager {
    private static Manager instance = null;

    // ----- Common GIN injector and event bus -----
    private static final Xdstools3GinInjector injector=GWT.create(Xdstools3GinInjector.class);
    public static final /*Simple*/EventBus EVENT_BUS = injector.getEventBus();/*new SimpleEventBus();*/

    private static final Xdstools2 xdstools2 = new Xdstools2();


    // Singleton constructor
    private Manager(){
    }

    public static Manager getInstance(){
        if (instance == null) {
            instance = new Manager();
            xdstools2.loadTkProps();
        }
        return instance;
    }

    public static Xdstools2 getXdstools2() {
        return xdstools2;
    }

}
