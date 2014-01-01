package gov.nist.hit.ds.simSupport.engine.v2compatibility;


public interface MessageValidatorEngine {
	/**
	 * In the new architecture, new validators get added to the SimChain, here
	 * we add directly to the SimEngine for backwards compatibility with version 2.Ò
	 * @param stepName - descriptive name of the the validation
	 * @param v - MessageValidator from v2 - corresponds to ValSim in v3.
	 * @param er
	 */
	void appendMessageValidator(String stepName, MessageValidator v);

}
