package gov.nist.hit.ds.simSupport.validationEngine;

import java.io.IOException;

import gov.nist.hit.ds.actorTransaction.ActorTypeFactory;
import gov.nist.hit.ds.actorTransaction.TransactionTypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gov.nist.hit.ds.eventLog.assertion.Assertion;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.xdsException.soapSupport.SoapFaultException;

public class ValidationRunDependsOnTest   extends SimComponentBase {
	
	@Before
	public void init() throws RepositoryException, InitializationFailedException, IOException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
		event = new EventBuilder().buildEvent(new SimId("ST-reg-1"), ActorTypeFactory.find("registry").getShortName(), TransactionTypeFactory.find("register").getShortName());
		ag = new AssertionGroup();
		event.addAssertionGroup(ag);
	}
	
	@Validation(id="VAL1", msg="A Validation", ref="First Grade", dependsOn={"VAL2"})
	public void validation1Test() throws SoapFaultException {
		assertEquals(1,1);
	}

	@Validation(id="VAL2", msg="A Validation", ref="First Grade", dependsOn={"VAL3"})
	public void validation2Test() throws SoapFaultException {
		assertEquals(1,1);
	}

	@Validation(id="VAL3", msg="A Validation", ref="First Grade")
	public void validation3Test() throws SoapFaultException {
		assertEquals(1,1);
	}

	@Test
	public void runTest() {
		try {
			runValidationEngine();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		Assertion a = ag.getFirstFailedAssertion();
		Assert.assertNull(a);
		
		Assert.assertEquals(3, ag.size());
		Assert.assertEquals("VAL3", ag.getAssertion(0).getId());
		Assert.assertEquals("VAL2", ag.getAssertion(1).getId());
		Assert.assertEquals("VAL1", ag.getAssertion(2).getId());
		
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
