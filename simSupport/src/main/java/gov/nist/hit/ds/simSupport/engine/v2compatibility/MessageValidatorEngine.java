package gov.nist.hit.ds.simSupport.engine.v2compatibility;


public interface MessageValidatorEngine {
	/**
	 * In the new architecture, new validators get added to the SimChain, here
	 * we add directly to the SimEngine for backwards compatibility with version 2.0
	 * @param stepName - descriptive name of the the validation
	 * @param v - MessageValidator from v2 - corresponds to ValSim in v3.
	 * @param er
     *
     * MessageValidator removed.
	 */
	void appendMessageValidator(String stepName);

}
