package gov.nist.toolkit.axis2SoapClientFacade.api;


import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTest {
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Rule
	public TestName name = new TestName();

	/*
	 * Test initialization
	 */
	@Before
	public final void start2() {
		log.info("------------ start : " + name.getMethodName()
				+ " -------------------");
	}

	@After
	public final void end2() {
		log.info("------------ done : " + name.getMethodName()
				+ " -------------------");
	}
	
	@BeforeClass
	public static final void checkTomcatIsOn() {
		try{
			ping(8080);
		}
		catch(Exception e){
			throw new RuntimeException("Tomcat is not started on port 8080");
		}
	}
	
	public static boolean ping(int port) throws UnknownHostException, IOException {
		Socket socket = null;
		boolean reachable = false;
		try {
		    socket = new Socket("localhost",port);
		    reachable = true;
		} finally {            
		    if (socket != null) try { socket.close(); } catch(IOException e) {}
		}
		
		return reachable;
	}
}
