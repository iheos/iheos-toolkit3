package gov.nist.hit.ds.metadataModel.client;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * TODO: AssertionGroup and its dependents do not implement IsSerializable for GWT
 * @author bmajur
 *
 */
public class DocumentEntry implements ModelElement, IsSerializable {

	@Override
	public AssertionGroup validate() {
		return null;
	}

}
