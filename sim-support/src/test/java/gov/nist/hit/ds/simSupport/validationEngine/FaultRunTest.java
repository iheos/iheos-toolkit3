package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.eventLog.EventFactory;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault;
import gov.nist.hit.ds.soapSupport.FaultCode;
import gov.nist.hit.ds.soapSupport.SoapFaultException;
import gov.nist.hit.ds.toolkit.installation.InitializationFailedException;
import gov.nist.hit.ds.toolkit.installation.Installation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class FaultRunTest  extends SimComponentBase {
	boolean ran = false;
	
	@Before
	public void init() throws RepositoryException, InitializationFailedException, IOException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
		event = new EventFactory().buildEvent(null);
		ag = new AssertionGroup();
//		event.addAssertionGroup(ag);
	}
	
	@ValidationFault(id="VAL1", msg="One must equal one", faultCode= FaultCode.Sender, ref="First Grade")
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

	@Override
	public boolean showOutputInLogs() {
		return false;
	}

}
