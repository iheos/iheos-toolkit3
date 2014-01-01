package gov.nist.hit.ds.actorTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nist.hit.ds.actorTransaction.exceptions.InvalidActorTransactionTypeDefinition;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import org.junit.Before;
import org.junit.Test;

public class EndpointLabelBuildTest {
	
	@Before
	public void before() throws InvalidActorTransactionTypeDefinition {
		ActorTypeFactory.find("registry");
	}
	
	@Test
	public void buildTest()  {
		TransactionType register = TransactionTypeFactory.find("register");
		EndpointLabel label;
		label = new EndpointLabel(
				register,
				TlsType.TLS,
				AsyncType.ASYNC
				);
		assertEquals("", "Register_TLS_ASYNC", label.get());
		assertTrue(label.isTls());
		assertTrue(label.isAsync());
		assertEquals("", register, label.getTransType());

		label = new EndpointLabel(
				register,
				TlsType.NOTLS,
				AsyncType.ASYNC
				);
		assertEquals("", "Register_ASYNC", label.get());
		assertFalse(label.isTls());
		assertTrue(label.isAsync());
		assertEquals("", register, label.getTransType());

		label = new EndpointLabel(
				register,
				TlsType.TLS,
				AsyncType.SYNC
				);
		assertEquals("", "Register_TLS", label.get());
		assertTrue(label.isTls());
		assertFalse(label.isAsync());
		assertEquals("", register, label.getTransType());

		label = new EndpointLabel(
				register,
				TlsType.NOTLS,
				AsyncType.SYNC
				);
		assertEquals("", "Register", label.get());
		assertFalse(label.isTls());
		assertFalse(label.isAsync());
		assertEquals("", register, label.getTransType());
}

}
