package gov.nist.hit.ds.actorTransaction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EndpointLabelSetGetTest {

	TransactionType register;
    ActorType actorType;
	
	@Before
	public void before()  {
		actorType = ActorTypeFactory.find("registry");
		register = actorType.find("register");
	}
	
	@Test
	public void setGetTest()  {
		EndpointLabel label = new EndpointLabel(actorType, "REGISTER");

		assertEquals("", register, label.getTransType());
		
		label.setTransType(register);
		assertEquals("", register, label.getTransType());
		
		label.setAsync(true);
		assertEquals("", AsyncType.ASYNC, label.getAsyncType());
	
		label.setAsync(false);
		assertEquals("", AsyncType.SYNC, label.getAsyncType());
	
		label.setTls(true);
		assertEquals("", TlsType.TLS, label.getTlsType());

		label.setTls(false);
		assertEquals("", TlsType.NOTLS, label.getTlsType());
}
	
}
