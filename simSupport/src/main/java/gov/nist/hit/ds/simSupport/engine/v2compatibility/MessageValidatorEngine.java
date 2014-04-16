package gov.nist.hit.ds.simSupport.engine.v2compatibility;

import gov.nist.toolkit.valsupport.message.MessageValidator;

public interface MessageValidatorEngine {
	/**
	 * In the new architecture, new validators get added to the SimChain, here
	 * we add directly to the SimEngine for backwards compatibility with version 2.0
	 * @param stepName - descriptive name of the the validation
	 * @param v - MessageValidator from v2 - corresponds to ValSim in v3.
	 * @param er
	 */
	void appendMessageValidator(String stepName, MessageValidator v);

}
