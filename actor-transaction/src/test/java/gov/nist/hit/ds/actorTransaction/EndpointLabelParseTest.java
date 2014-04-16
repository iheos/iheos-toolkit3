package gov.nist.hit.ds.actorTransaction;

import gov.nist.hit.ds.actorTransaction.client.TransactionType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EndpointLabelParseTest {
	TransactionType register;
	
	@Before
	public void before()  {
		ActorTypeFactory.find("registry");
		register = TransactionTypeFactory.find("register");
	}
	
	@Test
	public void parse1Test()  {
		EndpointLabel label;

		label = new EndpointLabel("Register_TLS_ASYNC");
		assertEquals("", register, label.getTransType());
		assertTrue(label.isTls());
		assertTrue(label.isAsync());
	}

	@Test
	public void parse2Test()  {
		EndpointLabel label;

		label = new EndpointLabel("Register_TLS");
		assertEquals("", register, label.getTransType());
		assertTrue(label.isTls());
		assertFalse(label.isAsync());
	}

	@Test
	public void parse3Test()  {
		EndpointLabel label;

		label = new EndpointLabel("Register_ASYNC");
		assertEquals("", register, label.getTransType());
		assertFalse(label.isTls());
		assertTrue(label.isAsync());
	}

	@Test
	public void parse4Test()  {
		EndpointLabel label;

		label = new EndpointLabel("Register");
		assertEquals("", register, label.getTransType());
		assertFalse(label.isTls());
		assertFalse(label.isAsync());
	}
}
