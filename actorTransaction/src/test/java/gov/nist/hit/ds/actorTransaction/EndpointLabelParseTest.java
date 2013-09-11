package gov.nist.hit.ds.actorTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EndpointLabelParseTest {
	@Test
	public void parse1Test() {
		EndpointLabel label;

		label = new EndpointLabel("Register_TLS_ASYNC");
		assertEquals("", TransactionType.REGISTER, label.getTransType());
		assertTrue(label.isTls());
		assertTrue(label.isAsync());
	}

	@Test
	public void parse2Test() {
		EndpointLabel label;

		label = new EndpointLabel("Register_TLS");
		assertEquals("", TransactionType.REGISTER, label.getTransType());
		assertTrue(label.isTls());
		assertFalse(label.isAsync());
	}

	@Test
	public void parse3Test() {
		EndpointLabel label;

		label = new EndpointLabel("Register_ASYNC");
		assertEquals("", TransactionType.REGISTER, label.getTransType());
		assertFalse(label.isTls());
		assertTrue(label.isAsync());
	}

	@Test
	public void parse4Test() {
		EndpointLabel label;

		label = new EndpointLabel("Register");
		assertEquals("", TransactionType.REGISTER, label.getTransType());
		assertFalse(label.isTls());
		assertFalse(label.isAsync());
	}
}
