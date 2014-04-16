package gov.nist.toolkit.testengine.transactions;

import gov.nist.toolkit.testengine.engine.StepContext;

import org.apache.axiom.om.OMElement;

public class IGQTransaction extends StoredQueryTransaction {

	public IGQTransaction() {
		super();
	}

	protected String getBasicTransactionName() {
		return "igq";
	}


	public void configure() {
		useAddressing = true;
		soap_1_2 = true;
	}

}
