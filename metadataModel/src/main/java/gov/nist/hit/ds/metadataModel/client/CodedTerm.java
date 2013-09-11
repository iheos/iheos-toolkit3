package gov.nist.hit.ds.metadataModel.client;

import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author bmajur
 *
 */
public abstract class CodedTerm implements IsSerializable, ModelElement {

	/**
	 * Validate code against codingScheme
	 * @param value
	 * @return
	 */
	abstract public AssertionGroup validateCode(String256 code);
	
	String256 code;
	CodingScheme codingScheme;
	String256 displayName;

	@Override
	public AssertionGroup validate() {
		// TODO Auto-generated method stub
		return null;
	}

}
