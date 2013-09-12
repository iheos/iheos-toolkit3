package gov.nist.hit.ds.repository.simple.index.db;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import gov.nist.hit.ds.repository.api.RepositoryException;

import java.sql.Connection;
import java.util.ArrayList;

import org.junit.Test;


public class DbConnectionTest {
	static final int NumberOfConnections = 100;
	
	@Test
	public void testDbProvider() {
		
		try {
			assertNotNull(DbConnection.getInstance());
			ArrayList<Connection>cList =  new ArrayList<Connection>();
			
			for (int i=0; i<NumberOfConnections; i++) {
				System.out.println("Getting connection number "+i);
				cList.add(DbConnection.getInstance().getConnection());
				DbConnection.getInstance().printConnectionSummary();
				
			}
			
			int cx=0;
			for (Connection c : cList ) {
				System.out.println("Closing..."+ cx++);
				DbConnection.getInstance().printConnectionSummary();
				c.close();
			}
			
			
		} catch (Exception e) {			
			e.printStackTrace();
			fail("connection test failed. It is possible that other instances of Derby are running outside this scope.");
		}
	}
	
	
	@Test
	public void testDbIndexContainer() {
		DbIndexContainer ic = new DbIndexContainer();
		try {
			System.out.println (ic.doesIndexContainerExist());
		} catch (RepositoryException e) {
			
			e.printStackTrace();
			fail("Index Container test Failed");
		}
	}
	
	

}

