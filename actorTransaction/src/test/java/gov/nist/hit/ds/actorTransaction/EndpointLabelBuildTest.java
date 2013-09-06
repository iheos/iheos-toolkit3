package gov.nist.hit.ds.actorTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EndpointLabelBuildTest {
	@Test
	public void buildTest() {
		EndpointLabel label;
		label = new EndpointLabel(
				TransactionType.REGISTER,
				TlsType.TLS,
				AsyncType.ASYNC
				);
		assertEquals("", "Register_TLS_ASYNC", label.get());
		assertTrue(label.isTls());
		assertTrue(label.isAsync());
		assertEquals("", TransactionType.REGISTER, label.getTransType());

		label = new EndpointLabel(
				TransactionType.REGISTER,
				TlsType.NOTLS,
				AsyncType.ASYNC
				);
		assertEquals("", "Register_ASYNC", label.get());
		assertFalse(label.isTls());
		assertTrue(label.isAsync());
		assertEquals("", TransactionType.REGISTER, label.getTransType());

		label = new EndpointLabel(
				TransactionType.REGISTER,
				TlsType.TLS,
				AsyncType.SYNC
				);
		assertEquals("", "Register_TLS", label.get());
		assertTrue(label.isTls());
		assertFalse(label.isAsync());
		assertEquals("", TransactionType.REGISTER, label.getTransType());

		label = new EndpointLabel(
				TransactionType.REGISTER,
				TlsType.NOTLS,
				AsyncType.SYNC
				);
		assertEquals("", "Register", label.get());
		assertFalse(label.isTls());
		assertFalse(label.isAsync());
		assertEquals("", TransactionType.REGISTER, label.getTransType());
}

}
