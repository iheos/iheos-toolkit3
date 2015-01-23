package gov.nist.hit.ds.testClient.logging;

/**
 * Interface for classes that are factories that build ErrorRecorders
 * @author bill
 *
 */
public interface ErrorRecorderBuilder {

	public ErrorRecorder buildNewErrorRecorder();
}
