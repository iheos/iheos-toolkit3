package gov.nist.toolkit.xdstools3.client.util;


import com.google.web.bindery.event.shared.SimpleEventBus;

/** Stores static variables & variables common to the application. This is a singleton.
 *
 */
public class Util {
    private static Util instance = null;

    // Static variables
    public static final SimpleEventBus EVENT_BUS = new SimpleEventBus();

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
