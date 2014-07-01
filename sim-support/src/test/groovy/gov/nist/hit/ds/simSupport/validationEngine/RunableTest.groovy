package gov.nist.hit.ds.simSupport.validationEngine
import gov.nist.hit.ds.repository.api.RepositoryException
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ValidationFault
import gov.nist.hit.ds.soapSupport.FaultCode
import gov.nist.hit.ds.soapSupport.SoapFaultException
import org.junit.Assert
import org.junit.Test

public class RunableTest extends ValComponentBase {
	
	@ValidationFault(id="VAL1", msg="One must equal one", faultCode= FaultCode.Sender, ref="First Grade")
	public void validationFaultTest() throws SoapFaultException {
		assertEquals(1,1);
	}

	@Test
	public void runTest() {
		try {
			getValidationEngine().scanForValidationMethods();
			ValidationMethod r = getValidationEngine().getARunableValidationMethod();
			Assert.assertNotNull(r);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Override
	public void run() throws SoapFaultException,
			RepositoryException {

	}

	@Override
	public boolean showOutputInLogs() {
		return false;
	}
}
