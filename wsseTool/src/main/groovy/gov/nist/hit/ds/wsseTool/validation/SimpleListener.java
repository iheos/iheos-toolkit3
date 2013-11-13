package gov.nist.hit.ds.wsseTool.validation;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleListener extends RunListener {

	private static final Logger log = LoggerFactory.getLogger(SimpleListener.class);
	
	@Override
	public void testRunStarted(Description description) throws Exception {
		log.info("start saml tests.");
	}

	@Override
	public void testStarted(Description description) throws Exception {
		log.info("started " + description.getDisplayName() + " "
				+ description.getMethodName());

	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		log.info("done running saml tests. \n Number of tests run : " + result.getRunCount()
				+ " failed : " + result.getFailureCount());
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		log.error("failed " + failure.getDescription().getMethodName()
				+ ": " + failure.getMessage());
		log.debug(failure.getTrace());
	}
}
