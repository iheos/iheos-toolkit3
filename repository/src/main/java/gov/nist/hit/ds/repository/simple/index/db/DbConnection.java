package gov.nist.hit.ds.repository.simple.index.db;

import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.index.IndexDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import gov.nist.hit.ds.toolkit.installation.Installation;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * This class provides a JDBC compatible (Derby) database connection.
 * DbProvider is a singleton class that allows for automatic connection pooling through Tomcat JDBC Pooling.
 * @author Sunil.Bhaskarla
 *
 */
public class DbConnection implements IndexDataSource {

	private static Logger logger = Logger.getLogger(DbConnection.class.getName());
	
	// See this web page on why Tomcat's own JDBC pooling is preferred to DBCP
	// http://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.htm
	
	// This is for the DBCP method	
	//private static BasicDataSource bds = null;
	
     private static DataSource bds = null;
     
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
			
			  PoolProperties p = new PoolProperties();
			 
	          p.setUrl("jdbc:derby:"+ ecDir +"/db;create=true");
	          
	          //local "jdbc:derby:c:\\e\\myderby.db;create=true"
	          
	          p.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
//	          p.setUsername("root");
//	          p.setPassword("password");
//	          p.setJmxEnabled(true);
//	          p.setTestWhileIdle(false);
//	          p.setTestOnBorrow(true);
//	          p.setValidationQuery("SELECT 1");
//	          p.setTestOnReturn(false);
//	          p.setValidationInterval(30000);
	          p.setTimeBetweenEvictionRunsMillis(15000);
	          p.setMaxActive(777);
	          p.setInitialSize(10);
	          p.setMaxWait(35000);
	          p.setRemoveAbandonedTimeout(1200); // 20min
	          p.setMinEvictableIdleTimeMillis(35000);
	          p.setMinIdle(25);
	          p.setLogAbandoned(false);
	          p.setRemoveAbandoned(true);
//	          p.setJdbcInterceptors(
//	            "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
//	            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

			  bds = new DataSource();
	          bds.setPoolProperties(p);
	          
			// DBCP 
			//bds = new BasicDataSource();	          			
//			bds.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
//			bds.setUrl("jdbc:derby:c:\\e\\myderby.db;create=true");			
		}
		
		
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
		
		printConnectionSummary();
		
		return cnx;
	}
	
	/**
	 * 
	 */
	public void printConnectionSummary() {
		logger.fine("----- Active " + bds.getActive()
		+ " Max active "+ bds.getMaxActive()
		+ " Max idle "+ bds.getMaxIdle()
		+ " Max wait "+ bds.getMaxWait()
		+ "-----");
	}
	
}