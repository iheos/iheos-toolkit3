package gov.nist.hit.ds.testEngine.results;

import gov.nist.hit.ds.testEngine.results.client.AssertionResult;
import gov.nist.hit.ds.testEngine.results.client.AssertionResults;
import gov.nist.hit.ds.testEngine.results.client.Result;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

import java.util.Calendar;

public class ResultBuilder {

	static public Result RESULT(String testName) {
		Calendar calendar = Calendar.getInstance();
		String timestamp = calendar.getTime().toString();
		Result r = new Result(timestamp);
		r.testName = testName;
		return r;
	}
	
	static public Result RESULT(String testName, AssertionResults assertions, AssertionResult assertion, Throwable t) {
		Calendar calendar = Calendar.getInstance();
		String timestamp = calendar.getTime().toString();
		Result r = new Result(timestamp);
		r.testName = testName;
		if (assertions != null) {
			if (r.assertions == null || r.assertions.size() == 0) {
				r.assertions = assertions;
			} else {
				r.assertions.add(assertions);
			}
		}
		if (assertion != null) {
			if (r.assertions == null)
				r.assertions = new AssertionResults();
			r.assertions.add(assertion);
		}
		if (t != null)
			r.addAssertion(ExceptionUtil.exception_details(t), false);
		r.pass = r.passed();
		return r;
	}
	

}
