package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.initialization.installation.InitializationFailedException;
import gov.nist.hit.ds.initialization.installation.Installation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.event.EventBuilder;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FaultRunTest  extends SimComponentBase {
	boolean ran = false;
	
	@Before
	public void init() throws RepositoryException, InitializationFailedException, IOException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
		event = new EventBuilder().buildEvent(new SimId("ST-reg-1"), ActorType.REGISTRY.getShortName(), TransactionType.REGISTER.getShortName());
		ag = new AssertionGroup();
		event.addAssertionGroup(ag);
	}
	
	@ValidationFault(id="VAL1", msg="One must equal one", faultCode=FaultCode.Sender, ref="First Grade")
	public void validationFaultTest() throws SoapFaultException {
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
	}
	
	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException,
			RepositoryException {

	}

}
