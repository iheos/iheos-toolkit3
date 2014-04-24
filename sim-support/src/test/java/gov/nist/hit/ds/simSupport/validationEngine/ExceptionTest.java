package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.eventLog.EventFactory;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.eventLog.assertion.AssertionStatus;
import gov.nist.hit.ds.eventLog.assertion.annotations.Validation;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.FaultCode;
import gov.nist.hit.ds.soapSupport.SoapFaultException;
import gov.nist.hit.ds.toolkit.installation.InitializationFailedException;
import gov.nist.hit.ds.toolkit.installation.Installation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ExceptionTest extends SimComponentBase {

	@Before
	public void init() throws RepositoryException, InitializationFailedException, IOException {
		Installation.reset();
		Installation.installation().initialize();
		Configuration.configuration();
		event = new EventFactory().buildEvent(null);
		ag = new AssertionGroup();
//		event.addAssertionGroup(ag);
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException,
	RepositoryException {
	}

	@Override
	public boolean showOutputInLogs() {
		return false;
	}

	@Test
	public void runTest() throws SoapFaultException, RepositoryException {
		try {
			runValidationEngine();
		} catch (SoapFaultException e) {
			Assert.assertEquals(FaultCode.ActionNotSupported.toString(), e.getFaultCode().toString());
			Assert.assertEquals(AssertionStatus.INTERNALERROR.toString(), ag.getWorstStatus().toString());
//			Assert.assertTrue(ag.isSaveInLog());
			return;
		}
		Assert.fail();
	}
	
	@Validation(id="VAL1", msg="Throws an exception", ref="First Grade")
	public void validationTest() throws SoapFaultException {
		throw new SoapFaultException(
				ag,
				FaultCode.ActionNotSupported,
				"My Fault, Sorry.");
	}
}
