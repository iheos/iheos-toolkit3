package gov.nist.hit.ds.wsseTool.validation

import gov.nist.hit.ds.wsseTool.api.Validator
import gov.nist.hit.ds.wsseTool.api.WsseHeaderGenerator
import gov.nist.hit.ds.wsseTool.api.config.Context
import gov.nist.hit.ds.wsseTool.api.config.ContextFactory
import gov.nist.hit.ds.wsseTool.api.config.KeystoreAccess
import gov.nist.hit.ds.wsseTool.api.config.ValConfig
import gov.nist.hit.ds.wsseTool.api.exceptions.GenerationException
import gov.nist.hit.ds.wsseTool.api.exceptions.ValidationException
import gov.nist.hit.ds.wsseTool.parsing.Message
import gov.nist.hit.ds.wsseTool.parsing.MessageFactory
import gov.nist.hit.ds.wsseTool.parsing.ParseException
import gov.nist.hit.ds.wsseTool.parsing.WSSEHeaderParser
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerBuilder
import gov.nist.hit.ds.wsseTool.validation.engine.ValRunnerWithOrder
import gov.nist.hit.ds.wsseTool.validation.engine.ValSuite
import gov.nist.hit.ds.wsseTool.validation.tests.run.*
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.*

import java.security.KeyStoreException

import org.junit.*
import org.junit.runner.Description
import org.junit.runner.JUnitCore
import org.junit.runner.Request
import org.junit.runner.Result
import org.junit.runner.Runner
import org.junit.runner.manipulation.Filter
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunListener
import org.junit.runners.model.RunnerBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import org.w3c.dom.Element


/**
 * API of the validation module.
 *
 * @author gerardin
 *
 */
public class WsseHeaderValidator implements Validator {

	private static final Logger log = LoggerFactory.getLogger(WsseHeaderValidator.class)

	public static void main(String[] args) throws GenerationException, KeyStoreException {
		String store = "src/test/resources/keystore/keystore"
		String sPass = "changeit"
		String kPass = "changeit"
		String alias = "hit-testing.nist.gov"
		Context context = ContextFactory.getInstance()
		context.setKeystore(new KeystoreAccess(store,sPass,alias,kPass))
		context.getParams().put("patientId", "D123401^^^&1.1&ISO")
		Document doc = new WsseHeaderGenerator().generateWsseHeader(context)
		new WsseHeaderValidator().validate(doc.getDocumentElement(),context)
	}

	public WsseHeaderValidator(){
	}

	public void validate(Element wsseHeader, Context context) throws ValidationException {
		ValConfig config = new ValConfig("2.0")
		validate(wsseHeader, config, context)
	}

	//TODO Rather than fixing behavior, having a flag like fail on parse, or fail on first test.
	public void validate(Element wsseHeader, ValConfig config, Context context) throws ValidationException {

		Message message = null;
		Result parseResult = null
		
		try{
		//create the message representation.
			message = MessageFactory.getMessage(wsseHeader,context)
		}
		catch(ParseException e){
			log.error(e);
			log.warn("problem during parsing but validation will continue so we can report errors properly.");
			
		}
		finally{
			//in any case we run the parsing validation for proper reporting
			Runner runner = new ValRunnerWithOrder(ParsingVal.class,message)
			Request request = Request.runner(runner)
			parseResult = run(request, config)
		}
		
		if(parseResult.getFailureCount() != 0){ //we do not want to go forward if parsing validation failed
			log.error("parsing validation failed. Validation will stop.")
			return
		}

		// parsing succeeded, we run all the other validations.
		RunnerBuilder builder = new ValRunnerBuilder(message)
		Runner runner = new ValSuite(CompleteTestSuite.class, builder)
		Request request = Request.runner(runner)
		run(request, config)
	}

	private Result run(Request request, ValConfig config) throws ValidationException {

		Request filteredRequest = applyFilters(request, config);
		
		Result result = null

		try{
			JUnitCore facade = new JUnitCore()
			TestsListener listener1 = new TestsListener()
			facade.addListener(listener1)
			result = facade.run(filteredRequest)
			report(result, listener1)
			return result
		}
		catch(Exception e){
			throw new ValidationException("an error occured during validation.", e)
		}
	}
	
	
	private Request applyFilters(Request request, ValConfig config){
		List<Filter> filters = createFilters(config);
		for(Filter filter : filters){
			request = request.filterWith(filter);
		}
		
		return request;
	}
	
	
	//TODO to modify once we know how we will handle config
	private List<Filter> createFilters(ValConfig config){
	//	return Collections.singletonList(optionalFilter);
		return Collections.emptyList();
	}

	private void report(Result result, TestsListener listener1){
		long time = result.getRunTime()
		int runs = result.getRunCount()
		int fails = result.getFailureCount()
		List<Failure> failures = result.getFailures()

		System.out.println("\n Summary: \n"+ runs + " tests runs in " + time + " milliseconds , "
				+ fails + " have failed, " + result.getIgnoreCount()
				+ " ignored")

		System.out.println("\n Names of tests run:")
		for (String s : listener1.testsDescriptions) {
			System.out.println(s)
		}

		System.out.println("\n Failures recorded:")
		for (Failure f : failures) {
			System.out.println(f.getTestHeader())
			System.out.println(f.getMessage())
		}
	}

	private class TestsListener extends RunListener {

		private static final Logger log = LoggerFactory.getLogger(TestsListener.class)

		public List<String> testsDescriptions = new ArrayList<String>()

		@Override
		public void testRunStarted(Description description) throws Exception {
			log.info("listener says : tests started")
		}

		public void testFailure(Failure failure) throws Exception {
			log.info("listener says : tests failed : " + failure.getMessage())
		}

		@Override
		public void testRunFinished(Result result) throws Exception {
			log.info("listener says : tests stopped")
		}

		@Override
		public void testStarted(Description description) throws Exception {
			testsDescriptions.add(description.getDisplayName())
		}

	}


	private String getStatus(Validation annos){
		String status = ""
		if(annos.status() == ValConfig.Status.not_implementable){
			status = "NOT IMPLEMENTABLE - "
		}
		else if(annos.status() == ValConfig.Status.review){
			status = "NEED REVIEW - "
		}

		return status
	}
	
	Filter optionalFilter = new Filter() {
		
				@Override
				public boolean shouldRun(Description description) {
					boolean shouldRun = description.getAnnotation(Optional.class) == null;
					return shouldRun;
				}
		
				@Override
				public String describe() {
					return "optional filter : filtered out all tests marked with @Optional annotation";
				}
		
			};
}
