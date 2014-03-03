package gov.nist.toolkit.testengine.transactions;

import gov.nist.toolkit.testengine.engine.StepContext;

import org.apache.axiom.om.OMElement;

public class XDRProvideAndRegisterTransaction extends
		ProvideAndRegisterTransaction {

	public XDRProvideAndRegisterTransaction() {
		super();
	}
	
	protected  String getBasicTransactionName() {
		return "xdrpr";
	}


}
