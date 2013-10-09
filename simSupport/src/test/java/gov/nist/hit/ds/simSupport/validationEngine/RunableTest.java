package gov.nist.hit.ds.simSupport.validationEngine;

import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.soapSupport.core.ValidationFault;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RunableTest extends SimComponentBase {
	
	@Before
	public void init() {
		getValidationEngine().reset();
	}
	
	@ValidationFault(id="VAL1", msg="One must equal one", faultCode=FaultCode.Sender, ref="First Grade")
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
}
