package gov.nist.hit.ds.actorTransaction;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TransactionTypeFactoryTest {

	// Loading an actor loads the necessary transactions as well.
	@Before
	public void before()  {
        TransactionTypeFactory.loadTransactionType("register");
//        ActorTypeFactory.find("registry");
	}

	// This is testing against the registerTransaction.properties file
	// which is part of toolkit configuration
	@Test
	public void getTypeNamesTest() {
		assertTrue(TransactionTypeFactory.getTransactionTypeNames().contains("register"));
	}

	@Test
	public void getTransactionTypeByNameTest()  {
		assertNotNull(TransactionTypeFactory.find("Register"));
	}

	@Test
	public void getTransactionTypeByShortNameTest()  {
		assertNotNull(TransactionTypeFactory.find("rb"));
	}

	@Test
	public void getTransactionTypeByAsyncCodeTest()  {
		assertNotNull(TransactionTypeFactory.find("r.as"));
	}

	@Test
	public void verifyFieldsTest()   {
		TransactionType tt = TransactionTypeFactory.getTransactionType("Register");
		assertTrue(tt.id.equals("ITI-42"));
		assertTrue(tt.name.equals("Register"));
		assertTrue(tt.shortName.equals("rb"));
		assertTrue(tt.code.equals("rb"));
		assertTrue(tt.asyncCode.equals("r.as"));
		assertTrue(tt.requestAction.equals("urn:ihe:iti:2007:RegisterDocumentSet-b"));
		assertTrue(tt.responseAction.equals("urn:ihe:iti:2007:RegisterDocumentSet-bResponse"));
	}
}
