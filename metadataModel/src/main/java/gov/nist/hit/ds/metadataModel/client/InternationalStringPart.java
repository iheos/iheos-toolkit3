package gov.nist.hit.ds.metadataModel.client;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InternationalStringPart  implements ModelElement, IsSerializable {
	String langCode;
	String value;
	
	@Override
	public AssertionGroup validate() {
		return null;
	}

}
