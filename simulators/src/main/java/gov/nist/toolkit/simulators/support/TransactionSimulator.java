package gov.nist.toolkit.simulators.support;



public abstract class TransactionSimulator extends MessageValidator {
	protected SimCommon common;
	
	public TransactionSimulator(SimCommon common) {
		super(common.vc);
		this.common = common;
	}

	abstract public void run(ErrorRecorder er, MessageValidatorEngine mvc);

}
