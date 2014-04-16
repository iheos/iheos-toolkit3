package gov.nist.hit.ds.metadataModel.client;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InternationalString implements ModelElement, IsSerializable {
	List<InternationalStringPart> iStrings;
	
	@Override
	public AssertionGroup validate() {
		return null;
	}

}
