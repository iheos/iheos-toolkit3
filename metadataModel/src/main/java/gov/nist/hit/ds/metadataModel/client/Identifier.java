package gov.nist.hit.ds.metadataModel.client;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class Identifier implements IsSerializable, ModelElement {
	String256 value;
	IdType type;
	
	/**
	 * Validate value against IdType
	 * @param value
	 * @return
	 */
	abstract public AssertionGroup validateValue(String256 value);
	
	@Override
	public AssertionGroup validate() {
		return null;
	}

}
