package gov.nist.hit.ds.actorTransaction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EndpointLabelTest {

	@Test
	public void parseTest() {
		EndpointLabel label = new EndpointLabel(TransactionType.REGISTER, TlsType.TLS, AsyncType.ASYNC);
		assertTrue((TransactionType.REGISTER.name + "_TLS_ASYNC").equals(label.get()));

		label = new EndpointLabel("Register_TLS_ASYNC");
		assertTrue(TransactionType.REGISTER == label.getTransType());
		assertTrue(label.isTls());
		assertTrue(label.isAsync());
	
		label = new EndpointLabel("Register_TLS");
		assertTrue(TransactionType.REGISTER == label.getTransType());
		assertTrue(label.isTls());
		assertFalse(label.isAsync());

		label = new EndpointLabel("Register_ASYNC");
		assertTrue(TransactionType.REGISTER == label.getTransType());
		assertFalse(label.isTls());
		assertTrue(label.isAsync());
}

	@Test
	public void buildTest() {
		EndpointLabel label;
		label = new EndpointLabel(
				TransactionType.REGISTER,
				TlsType.TLS,
				AsyncType.ASYNC
				);
		assertTrue(label.get().equals("Register_TLS_ASYNC"));

		label = new EndpointLabel(
				TransactionType.REGISTER,
				TlsType.NOTLS,
				AsyncType.ASYNC
				);
		assertTrue(label.get().equals("Register_ASYNC"));

		label = new EndpointLabel(
				TransactionType.REGISTER,
				TlsType.TLS,
				AsyncType.SYNC
				);
		assertTrue(label.get().equals("Register_TLS"));

	}
}
