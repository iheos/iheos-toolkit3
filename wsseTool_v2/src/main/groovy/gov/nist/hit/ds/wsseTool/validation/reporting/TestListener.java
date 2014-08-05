package gov.nist.hit.ds.wsseTool.validation.reporting;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestListener extends RunListener {

	private static final Logger log = LoggerFactory.getLogger(TestListener.class);

	protected List<Description> testsDescriptions = new ArrayList<Description>();
	protected List<Failure> optionalTestsNotRun = new ArrayList<Failure>();

	@Override
	public void testRunStarted(Description description) throws Exception {
		log.debug("saml tests started.");
	}

	public void testFailure(Failure failure) throws Exception {
		log.debug("saml test failed : " + failure.getMessage());
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		log.debug("saml tests terminated.");
	}

	@Override
	public void testStarted(Description description) throws Exception {
//		log.info("test start :" + description.getDisplayName());
	}

	@Override
	public void testAssumptionFailure(Failure failure) {
		log.debug("optional test not triggered :" + failure.getDescription() +" : "+ failure.getMessage());
	}

}
