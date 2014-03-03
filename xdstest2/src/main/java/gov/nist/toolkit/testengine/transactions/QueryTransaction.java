package gov.nist.toolkit.testengine.transactions;

import gov.nist.toolkit.testengine.engine.StepContext;

import org.apache.axiom.om.OMElement;

abstract public class QueryTransaction extends BasicTransaction {
	
	public QueryTransaction() {
		parse_metadata = false;
	}


}
