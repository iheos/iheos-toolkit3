package gov.nist.hit.ds.metadataModel.client;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class NameValue implements IsSerializable, ModelElement {
	String256 name;
	List<String256> values;
	
	abstract public AssertionGroup validateValueType(String256 value);
	
	@Override
	public AssertionGroup validate() {
		return null;
	}

}
