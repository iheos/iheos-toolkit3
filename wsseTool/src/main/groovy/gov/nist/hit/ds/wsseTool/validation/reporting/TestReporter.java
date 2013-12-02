package gov.nist.hit.ds.wsseTool.validation.reporting;

import gov.nist.hit.ds.wsseTool.validation.WsseHeaderValidator;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * TODO move to reporting once inetgration issue with v2 will be resolved 
 * (by now need to be in validation package to be found!)
 */

public class TestReporter {

	private static final Logger log = LoggerFactory.getLogger(WsseHeaderValidator.class);
	
	public void report(Result result, AdditionalResultInfoBuilder moreResultInfo){
		long time = result.getRunTime();
		int runs = result.getRunCount();
		int fails = result.getFailureCount();
		List<Failure> failures = result.getFailures();
		List<Description> descriptions = moreResultInfo.testsDescriptions;
		List<Failure> optionalTestsNotRun = moreResultInfo.optionalTestsNotRun;

		log.info("\n Summary: \n"+ runs + " tests runs in " + time + " milliseconds , "
				+ fails + " have failed, " +
				moreResultInfo.optionalTestsNotRun.size() + " optional tests not triggered, " +
				result.getIgnoreCount() + " ignored \n");

		log.info("\n Tests run: \n");
		for (Description d : descriptions) {
			log.info(d.getDisplayName() +"\n");
		}

		if(optionalTestsNotRun.size() != 0 ){
			log.info("\n Optional tests not triggered: \n");
			for (Failure f : optionalTestsNotRun) {
				log.warn(f.getTestHeader() + " : \n" + f.getMessage() +"\n");
			}
		}

		if(fails != 0 ){
			log.info("\n Failures recorded: \n");
			for (Failure f : failures) {
				log.error(f.getTestHeader() + " : \n" + f.getMessage() + "\n");
			}
		}
	}
}
