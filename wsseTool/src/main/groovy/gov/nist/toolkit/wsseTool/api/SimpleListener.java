package gov.nist.toolkit.wsseTool.api;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class SimpleListener extends RunListener {

	@Override
	public void testRunStarted(Description description) throws Exception {
		System.out.println("started ");
	}

	@Override
	public void testStarted(Description description) throws Exception {
		System.out.println("started " + description.getDisplayName() + " "
				+ description.getMethodName());

	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		System.out.println("number of tests run : " + result.getRunCount()
				+ " failed : " + result.getFailureCount());
	}

	public void testFailure(Failure failure) throws Exception {
		System.out.println("failed " + failure.getDescription().getMethodName()
				+ ": " + failure.getMessage());
		System.out.println(failure.getTrace());
	}
}
