package gov.nist.hit.ds.repository.simple.index.db;

import org.apache.derby.drda.NetworkServerControl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetAddress;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Created by skb1 on 12/17/14.
 */
public class DbAppCtxListener implements ServletContextListener  {

    private final Logger logger = Logger.getLogger(DbAppCtxListener.class
            .getName());
    public final static String networkHostName = "localhost";
    public final static int networkPort = NetworkServerControl.DEFAULT_PORTNUMBER;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // On Application Startup

        try {

            if (pingNetworkServer()) {
                logger.info("Derby network server was already started : " + networkHostName + ":"+  networkPort);
            } else {
                logger.info("Starting Derby network server: " + networkHostName + ":"+  networkPort);
                startDerbyNetworkServer();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void startDerbyNetworkServer() throws Exception {
        NetworkServerControl server = new NetworkServerControl
                (InetAddress.getByName(networkHostName), networkPort);
        server.start(null);
    }

    public static boolean pingNetworkServer() {
        try {
            NetworkServerControl server = new NetworkServerControl
                    (InetAddress.getByName(networkHostName), networkPort);
            server.ping();
        } catch (Throwable t) {
            return false;
        }
        return true;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        /*
        This method is only useful when the network service/embedded driver was started using the War application startup method. If the database was booted elsewhere (console management tool),
        then the network service thread must be shutdown using that tool. The DB folder on disk will be locked and cannot be deleted until the database driver was shutdown properly.
         */

        // ... First close any background tasks which may be using the DB ...
        // ... Then close any DB connection pools ...

        DbConnection.getInstance().close();

        // Now deregister JDBC drivers in this context's ClassLoader:
        // Get the webapp's ClassLoader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        // Loop through all drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                // This driver was registered by the webapp's ClassLoader, so deregister it:
                try {
                    logger.info("Deregistering JDBC driver "+ driver.toString());
                    DriverManager.deregisterDriver(driver);
                } catch (Throwable t) {
                    logger.severe("Error deregistering JDBC driver " + driver.toString() + " Exception: " + t.toString());
//                    t.printStackTrace();
                }
            } else {
                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
                // logger.info("Not deregistering JDBC driver "+ driver + " as it does not belong to this webapp's ClassLoader");
            }
        }
        logger.info("Shutting down Derby network server: " + networkHostName + ":"+  networkPort);
        shutdownDerbyNetworkServer();


    }

    public static void shutdownDerbyNetworkServer() {
        // Shutdown network port
        try {

            NetworkServerControl serverControl = new NetworkServerControl(InetAddress.getByName(networkHostName),networkPort);

            serverControl.shutdown();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
