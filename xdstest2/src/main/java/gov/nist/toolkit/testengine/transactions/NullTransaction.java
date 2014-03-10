package gov.nist.toolkit.testengine.transactions;

import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import org.apache.axiom.om.OMElement;

public class NullTransaction extends BasicTransaction {

	public NullTransaction() {
        super();
		defaultEndpointProcessing = false;
		noMetadataProcessing = true;
	}


	protected String getBasicTransactionName() {
		return "Null";
	}

	protected String getRequestAction() {
		return null;
	}

	protected void parseInstruction(OMElement part) throws XdsInternalException {
		parseBasicInstruction(part);
	}

	protected void run(OMElement request) throws XdsException {
	}

}
