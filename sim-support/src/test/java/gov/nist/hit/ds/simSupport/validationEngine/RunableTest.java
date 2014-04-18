package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault;

import gov.nist.hit.ds.soapSupport.FaultCode;
import gov.nist.hit.ds.soapSupport.SoapFaultException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RunableTest extends SimComponentBase {
	
	@Before
	public void init() {
		getValidationEngine().reset();
	}
	
	@ValidationFault(id="VAL1", msg="One must equal one", faultCode= FaultCode.Sender, ref="First Grade")
	public void validationFaultTest() throws SoapFaultException {
		assertEquals(1,1);
	}

	@Test
	public void runTest() {
		try {
			getValidationEngine().scheduler();
			Assert.assertEquals(1, getValidationEngine().getRunableCount());
			Runable r = getValidationEngine().getNextRunable();
			Assert.assertNotNull(r);
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
