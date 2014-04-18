package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.actorTransaction.ActorTypeFactory;
import gov.nist.hit.ds.actorTransaction.TransactionTypeFactory;
import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.event.EventBuilder;

import java.io.IOException;

import gov.nist.hit.ds.soapSupport.SoapFaultException;
import gov.nist.hit.ds.toolkit.installation.InitializationFailedException;
import gov.nist.hit.ds.toolkit.installation.Installation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ValidationRunTest  extends SimComponentBase {
	boolean ran = false;
	
	@Before
	public void init() throws RepositoryException, InitializationFailedException, IOException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
		event = new EventBuilder().buildEvent(new SimId("ST-reg-1"), ActorTypeFactory.find("registry").getShortName(), TransactionTypeFactory.find("register").getShortName());
		ag = new AssertionGroup();
//		event.addAssertionGroup(ag);
	}
	
	@Validation(id="VAL1", msg="One must equal one", ref="First Grade")
	public void validationTest() throws SoapFaultException {
		ran = true;
		assertEquals(1,1);
	}

	@Test
	public void runTest() {
		try {
			runValidationEngine();
			Assert.assertTrue(ran);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		Assertion a = ag.getFirstFailedAssertion();
		Assert.assertNull(a);
		
	}
	
	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException,
			RepositoryException {

	}

	@Override
	public boolean showOutputInLogs() {
		return false;
	}

}
