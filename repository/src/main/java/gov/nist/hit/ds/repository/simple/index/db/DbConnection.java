package gov.nist.hit.ds.repository.simple.index.db;

import com.zaxxer.hikari.HikariDataSource;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.index.IndexDataSource;
import gov.nist.hit.ds.toolkit.installation.Installation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * This class provides a JDBC compatible (Derby) database connection.
 * DbProvider is a singleton class that allows for automatic connection pooling through Hikari Pooling.
 * @author Sunil.Bhaskarla
 *
 */
public class DbConnection implements IndexDataSource {

	private static Logger logger = Logger.getLogger(DbConnection.class.getName());

//     private static DataSource bds = null;
    private static HikariDataSource bds = null;

    static DbConnection self = null;
	
	private DbConnection()  {		
		try {
			setupDataSource();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public DbConnection getInstance() {
		if (self==null) {
			synchronized (DbConnection.class) {
				if (self==null) {
					self = new DbConnection();
				}
			}
		}
		return self;
	}


    public void close()  {
        logger.info("Running close on DBConnection");


        try {
            if (bds!=null) {
                bds.close();
            }
        } catch (Throwable t) {
//            logger.severe(t.toString());
        }

        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (Throwable t) {
//            t.printStackTrace();
        }

    }

    @Override
	public void setupDataSource() throws RepositoryException {
		if (bds==null) {

			String ecDir = null; 
			try {
				// ecDir = Installation.installation().propertyServiceManager().getToolkitProperties().get("External_Cache");
				ecDir = Installation.installation().getExternalCache().toString();
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			
			if (ecDir==null) {
				System.out.println("E100: Could not get External_Cache from Installation setup. Database could not instantiated.");
				return;
			}

            try {
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();

            } catch (Throwable t) {
                t.printStackTrace();
            }



//            Properties props = new Properties();
//            props.setProperty("dataSourceClassName", "org.apache.derby.jdbc.ClientDataSource");
//            props.setProperty("dataSource.databaseName", ecDir +"/db");
//
//
//            HikariConfig config = new HikariConfig(props);
//            bds = new HikariDataSource(config);
//
            bds = new HikariDataSource();

            bds.setMaximumPoolSize(777);
            bds.setMinimumIdle(10);
//            bds.setDataSourceClassName("org.apache.derby.jdbc.ClientDataSource");


            if (DbAppCtxListener.pingNetworkServer()) {
                registerNetworkDriver(ecDir);
            } else {

                try {
                    DbAppCtxListener.startDerbyNetworkServer();
                    registerNetworkDriver(ecDir);

                    // TODO: provide a shutdown database task from the toolkit management console

                } catch (Throwable t) {
                    t.printStackTrace();

                    // Fail over to embedded driver in case network service could not be booted

                    try {
                        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
                        logger.info("Using Derby embedded server." );
                        bds.setJdbcUrl("jdbc:derby:" + ecDir +"/db;create=true");

                    } catch (Exception ex) {
                        logger.info(ex.toString());
                    }
                }


            }


//
            // Tomcat JDBC
//
//			  PoolProperties p = new PoolProperties();
//
//	          p.setUrl("jdbc:derby:"+ ecDir +"/db;create=true");
//
//	          //local "jdbc:derby:c:\\e\\myderby.db;create=true"
//
//	          p.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
////	          p.setUsername("root");
////	          p.setPassword("password");
////	          p.setJmxEnabled(true);
////	          p.setTestWhileIdle(false);
////	          p.setTestOnBorrow(true);
////	          p.setValidationQuery("SELECT 1");
////	          p.setTestOnReturn(false);
////	          p.setValidationInterval(30000);
//	          p.setTimeBetweenEvictionRunsMillis(15000);
//	          p.setMaxActive(777);
//	          p.setInitialSize(10);
//	          p.setMaxWait(35000);
//	          p.setRemoveAbandonedTimeout(1); // 20min
//	          p.setMinEvictableIdleTimeMillis(35000);
//	          p.setMinIdle(25);
//	          p.setLogAbandoned(false);
//	          p.setRemoveAbandoned(true);
////	          p.setJdbcInterceptors(
////	            "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
////	            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
//
//			  bds = new DataSource();
//	          bds.setPoolProperties(p);
//
			// DBCP 
			//bds = new BasicDataSource();	          			
//			bds.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
//			bds.setUrl("jdbc:derby:c:\\e\\myderby.db;create=true");			
		}
		
		
	}

    private void registerNetworkDriver(String ecDir) {
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        logger.info("Using Derby network server." );
        bds.setJdbcUrl("jdbc:derby://" + DbAppCtxListener.networkHostName  + ":" + DbAppCtxListener.networkPort + "/" + ecDir +"/db;create=true");
    }

    @Override
	public Connection getConnection()  {
		Connection cnx = null;
		try {
			if (bds!=null) {
				cnx = bds.getConnection();
				if (cnx.isClosed())
					System.out.println("Connection failed because it is already closed.");				
			} else {
				return null;
			}
			
		} catch (SQLException e) {
			System.out.println("Connect failed");
			e.printStackTrace();
		}

		return cnx;
	}

	
}
