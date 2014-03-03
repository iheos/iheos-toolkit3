package gov.nist.toolkit.testengine.transactions;

import gov.nist.toolkit.testengine.engine.StepContext;

import org.apache.axiom.om.OMElement;

public class MuTransaction extends RegisterTransaction {

	public MuTransaction() {
		super();
	}

	protected String getRequestAction() {
		return "urn:ihe:iti:2010:UpdateDocumentSet";
	}

	protected String getBasicTransactionName() {
		return "update";
	}


}
