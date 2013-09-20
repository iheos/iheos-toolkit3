package gov.nist.toolkit.wsseTool.api

import gov.nist.toolkit.wsseTool.api.config.KeystoreAccess
import gov.nist.toolkit.wsseTool.api.config.SecurityContext
import gov.nist.toolkit.wsseTool.api.config.SecurityContextFactory
import gov.nist.toolkit.wsseTool.api.exceptions.GenerationException
import gov.nist.toolkit.wsseTool.api.exceptions.ValidationException
import gov.nist.toolkit.wsseTool.context.SecurityContextImpl
import gov.nist.toolkit.wsseTool.engine.MyRunnerBuilder
import gov.nist.toolkit.wsseTool.engine.MySuite
import gov.nist.toolkit.wsseTool.engine.TestData
import gov.nist.toolkit.wsseTool.engine.annotations.Validation
import gov.nist.toolkit.wsseTool.parsing.Parser
import gov.nist.toolkit.wsseTool.util.MyXmlUtils
import gov.nist.toolkit.wsseTool.validation.AssertionVal
import gov.nist.toolkit.wsseTool.validation.Parsing
import gov.nist.toolkit.wsseTool.validation.ParsingVal

import java.lang.reflect.Method
import java.security.KeyStoreException

import org.junit.*
import org.junit.runner.Description
import org.junit.runner.JUnitCore
import org.junit.runner.Request
import org.junit.runner.Result
import org.junit.runner.Runner
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
public class UnitWsseHeaderValidator {

	private static final Logger log = LoggerFactory.getLogger(UnitWsseHeaderValidator.class)

	private int testCount = 0

	public static void main(String[] args) throws GenerationException, KeyStoreException {
		String store = "src/test/resources/keystore/keystore"
		String sPass = "changeit"
		String kPass = "changeit"
		String alias = "hit-testing.nist.gov"
		SecurityContext context = SecurityContextFactory.getInstance()
		context.setKeystore(new KeystoreAccess(store,sPass,alias,kPass))
		context.getParams().put("patientId", "D123401^^^&1.1&ISO")
		Document doc = new WsseHeaderGenerator().generateWsseHeader(context)
		new UnitWsseHeaderValidator().validate(doc.getDocumentElement(),context)
	}

	public WsseHeaderValidator(){
	}

	public void validate(Element wsseHeader, SecurityContext context) throws ValidationException {
		ValConfig config = new ValConfig("2.0")
		validate(wsseHeader, config, context)
	}



	public void validate(Element wsseHeader, ValConfig config, SecurityContext context) throws ValidationException {

		SecurityContextImpl _context = parseInput(wsseHeader, config, context)

		//run validations by category

		try{
			Parsing pVal = new Parsing(_context).parse();
			
			RunnerBuilder builder = new MyRunnerBuilder(_context);
			Runner runner = new MySuite(ATestSuite.class, builder);
			Request request = Request.runner(runner)
			JUnitCore facade = new JUnitCore()
			TestsListener listener1 = new TestsListener()

			facade.addListener(listener1)
			Result result = facade.run(request)
			long time = result.getRunTime()
			int runs = result.getRunCount()
			int fails = result.getFailureCount()
			List<Failure> failures = result.getFailures()

			System.out.println(runs + " tests runs in " + time + " milliseconds , "
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
				System.out
						.println("from spec: "
						+ f.getDescription()
						.getAnnotation(Validation.class).spec())
			}


		}
		catch(Exception e){
			throw new ValidationException("an error occured during validation.", e)
		}
		finally{
			log.info("Total tests run : " + testCount)
		}
	}
	
	private class TestsListener extends RunListener {
		
				public List<String> testsDescriptions = new ArrayList<String>();
		
				@Override
				public void testRunStarted(Description description) throws Exception {
					System.out.println("listener says : tests started");
				}
		
				@Override
				public void testRunFinished(Result result) throws Exception {
					System.out.println("listener says : tests stopped");
				}
		
				@Override
				public void testStarted(Description description) throws Exception {
					testsDescriptions.add(description.getDisplayName());
				}
		
			}

	public TestData parseInput(Element wsseHeader, ValConfig config, SecurityContext context){
		log.info("\n =============================" +
				"\n validation of the wsse header" +
				"\n =============================")
		String header = MyXmlUtils.DomToString(wsseHeader)
		log.debug("header to validate : {}", header)

		SecurityContextImpl _context = validateContext(context)


		testCount = 0

		try{
			//add dom, gpath and opensaml representation to the context
			_context.setDomHeader(wsseHeader)
			_context.setGroovyHeader(Parser.parseToGPath(wsseHeader))
			_context.setOpensamlHeader(Parser.parseToOpenSaml(wsseHeader))
		}
		catch(Exception e){
			throw new ValidationException("an error occured during validation.", e)
		}

		return _context
	}

	private SecurityContextImpl validateContext(SecurityContext context){

		SecurityContextImpl _context = null

		try{
			if(context == null) throw new ValidationException("No context found.")
			_context = (SecurityContextImpl) context
			if(context.getParams().get("homeCommunityId") == null){ log.warn("no homeCommunityId found in context. Some validations will not be performed.")}
			if(context.getParams().get("To") == null){ log.warn("no ws-addressing \"To\" info found in context. Some validations will not be performed.")}
		}
		catch(Exception e) {
			throw new ValidationException("problem occured while trying to understand context. Validation must terminate.", e)
		}

		return _context
	}

	private runValidation(Object valInstance, ValConfig config){
		Method[] methods = valInstance.getClass().getMethods()
		for (Method method : methods) {
			Validation annos = method.getAnnotation(Validation.class)
			if (annos != null) {
				try {
					String status = getStatus(annos)
					log.info(" {} - {} run validation with -id : {}{} -name : {} \n requirements checked are: {}", getCategory(annos), status, annos.prefixId(), annos.id() , method.getName(), annos.rtm())
					method.invoke(valInstance)
					testCount ++
				} catch (Exception e) {
					log.debug(e.printStackTrace())
				}
			}
		}
	}

	private String getCategory(Validation annos){
		return annos.category().toUpperCase()
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
}
