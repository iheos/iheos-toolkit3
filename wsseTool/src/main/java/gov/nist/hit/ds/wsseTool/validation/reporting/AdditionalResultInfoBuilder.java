package gov.nist.hit.ds.wsseTool.validation.reporting;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdditionalResultInfoBuilder extends RunListener {

	private static final Logger log = LoggerFactory.getLogger(AdditionalResultInfoBuilder.class);

	public List<Description> testsDescriptions = new ArrayList<Description>();
	public List<Failure> optionalTestsNotRun = new ArrayList<Failure>();

	@Override
	public void testStarted(Description description) throws Exception {
		testsDescriptions.add(description);
	}

	@Override
	public void testAssumptionFailure(Failure failure) {
		optionalTestsNotRun.add(failure);
	}

}
