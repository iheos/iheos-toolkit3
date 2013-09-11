package gov.nist.hit.ds.actorTransaction;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EndpointLabelSetGetTest {

	@Test
	public void setGetTest() {
		EndpointLabel label = new EndpointLabel("REGISTER");

		assertEquals("", TransactionType.REGISTER, label.getTransType());
		
		label.setTransType(TransactionType.REGISTER);
		assertEquals("", TransactionType.REGISTER, label.getTransType());
		
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
