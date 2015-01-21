package gov.nist.hit.ds.ebDocsrcSim.engine;

import gov.nist.hit.ds.xdsException.XdsInternalException;
import org.apache.axiom.om.OMElement;

public class BasicContext {
	OmLogger testLog = new TestLogFactory().getLogger();
	
	public BasicContext() {
	}
	
	String error(String msg) {
		System.out.println(msg);
		return msg;
	}

	void error(OMElement test_step_output, String msg) throws XdsInternalException {
        if (testLog != null)
		    testLog.add_name_value(test_step_output, "Error", msg);
		error(msg);
		throw new XdsInternalException("Error " + msg);
	}

	void fault(OMElement test_step_output, String code, String msg) throws XdsInternalException {
        if (testLog != null) {
            testLog.add_name_value(test_step_output, "SOAPFault", code + ": " + msg);
            testLog.add_name_value(test_step_output, "Error ", code + ": " + msg);
        }
		error(msg);
		throw new XdsInternalException(msg);
	}

}
