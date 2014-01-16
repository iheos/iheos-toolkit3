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
import gov.nist.hit.ds.wsseTool.validation.reporting.AdditionalResultInfoBuilder;
import gov.nist.hit.ds.wsseTool.validation.reporting.TestListener;
import gov.nist.hit.ds.wsseTool.validation.reporting.TestReporter;
import gov.nist.hit.ds.wsseTool.validation.tests.run.*
import gov.nist.hit.ds.wsseTool.validation.engine.annotations.*

import java.security.KeyStoreException
import java.util.List;

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
			Runner runner = new ValRunnerWithOrder(ParsingVal.class, message)
			Request request = Request.runner(runner)
			parseResult = run(request, config)
		}

		//For now, desactivate, we should use suite in the future
//		if(parseResult.getFailureCount() != 0){ //we do not want to go forward if parsing validation failed
//			log.error("parsing validation failed. Validation will stop.")
//			return
//		}

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
			TestListener listener1 = new TestListener()
			AdditionalResultInfoBuilder moreResults = new AdditionalResultInfoBuilder();
			facade.addListener(listener1)
			facade.addListener(moreResults)
			result = facade.run(filteredRequest)
			TestReporter reporter = new TestReporter();
			reporter.report(result, moreResults)
			return result
		}
		catch(Exception e){
			throw new ValidationException("an error occured during validation.", e)
		}
	}
	

	private Request applyFilters(Request request, ValConfig config){
		List<Filter> filters = createFilters(config);

		//TODO quick fix to give us some time to reconsider how we handle optional tests.
		//	filters.add(optionalFilter);
		//	log.info("optional tests will not be run.");

		for(Filter filter : filters){
			request = request.filterWith(filter);
		}

		return request;
	}


	//TODO to modify once we know how we will handle config
	private List<Filter> createFilters(ValConfig config){
		//	return Collections.singletonList(optionalFilter);
		return new LinkedList<Filter>();
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
