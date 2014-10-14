package gov.nist.toolkit.xdstools3.client.util;


import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import gov.nist.toolkit.xdstools3.client.util.injection.Xdstools3GinInjector;

/** Stores static variables & variables common to the application. This is a singleton.
 *
 */
public class Util {
    private static Util instance = null;

    private static final Xdstools3GinInjector injector=GWT.create(Xdstools3GinInjector.class);

    // Static variables
    public static final /*Simple*/EventBus EVENT_BUS = injector.getEventBus();/*new SimpleEventBus();*/

    // Variables accessed by getter
    private boolean isLoggedAsAdmin =  false;

    // Singleton constructor
    private Util(){
    }

    public static Util getInstance(){
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

    // Login
    public boolean getLoggedAsAdminStatus(){ return isLoggedAsAdmin;  }
    public void setIsLoggedAsAdmin(){  isLoggedAsAdmin = true;  }

}
