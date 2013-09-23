package gov.nist.toolkit.wsseTool.api

import gov.nist.toolkit.wsseTool.api.config.KeystoreAccess
import gov.nist.toolkit.wsseTool.api.config.Context
import gov.nist.toolkit.wsseTool.api.config.ContextFactory
import gov.nist.toolkit.wsseTool.api.config.ValConfig;
import gov.nist.toolkit.wsseTool.api.exceptions.GenerationException
import gov.nist.toolkit.wsseTool.api.exceptions.ValidationException
import gov.nist.toolkit.wsseTool.validation.engine.annotations.Validation
import gov.nist.toolkit.wsseTool.parsing.Message;
import gov.nist.toolkit.wsseTool.parsing.MessageParser
import gov.nist.toolkit.wsseTool.util.MyXmlUtils
import gov.nist.toolkit.wsseTool.validation.AssertionSignatureVal
import gov.nist.toolkit.wsseTool.validation.AssertionVal
import gov.nist.toolkit.wsseTool.validation.AttributeStatementVal
import gov.nist.toolkit.wsseTool.validation.AuthnStatementVal
import gov.nist.toolkit.wsseTool.validation.AuthzDecisionStatementVal
import gov.nist.toolkit.wsseTool.validation.ParsingVal
import gov.nist.toolkit.wsseTool.validation.SignatureVerificationVal
import gov.nist.toolkit.wsseTool.validation.TimestampVal

import java.lang.reflect.Method
import java.security.KeyStoreException

import org.junit.runner.JUnitCore
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
public class WsseHeaderValidator {

	private static final Logger log = LoggerFactory.getLogger(WsseHeaderValidator.class)

	private int testCount = 0

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
	
	public void validateWithJUnitRunner(Element wsseHeader, Context context) throws ValidationException {
		ValConfig config = new ValConfig("2.0")
		Message _context = parseInput(wsseHeader, config, context)
		JUnitCore facade = new JUnitCore();
		facade.addListener(new SimpleListener());
		ParsingValJUnit.prepare(_context);
		facade.run(ParsingValJUnit.class);
		facade.run(ParsingValJUnit.class);
	}

	public void validate(Element wsseHeader, Context context) throws ValidationException {
		ValConfig config = new ValConfig("2.0")
		validate(wsseHeader, config, context)
	}

	public void validate(Element wsseHeader, ValConfig config, Context context) throws ValidationException {

		Message _context = parseInput(wsseHeader, config, context)

		//run validations by category

		try{
			ParsingVal pVal = new ParsingVal(_context)
			runValidation(pVal,config)

			SignatureVerificationVal svVal = new SignatureVerificationVal(_context)

			runValidation(svVal,config)

			AssertionVal aVal = new AssertionVal(_context)
			runValidation(aVal,config)

			TimestampVal tVal = new TimestampVal(_context)
			runValidation(tVal,config)

			AssertionSignatureVal asVal = new AssertionSignatureVal(_context)
			runValidation(asVal, config)

			AuthnStatementVal authnVal = new AuthnStatementVal(_context)
			runValidation(authnVal, config)

			AuthzDecisionStatementVal authzVal = new AuthzDecisionStatementVal(_context)
			runValidation(authzVal, config)

			AttributeStatementVal attrsVal = new AttributeStatementVal(_context)
			runValidation(attrsVal, config)

		}
		catch(Exception e){
			throw new ValidationException("an error occured during validation.", e)
		}
		finally{
			log.info("Total tests run : " + testCount)
		}
	}

	public Message parseInput(Element wsseHeader, ValConfig config, Context context){
		log.info("\n =============================" +
				"\n validation of the wsse header" +
				"\n =============================")
		String header = MyXmlUtils.DomToString(wsseHeader)
		log.debug("header to validate : {}", header)

		Message _context = validateContext(context)

		testCount = 0

		try{
			//add dom, gpath and opensaml representation to the context
			_context.setDomHeader(wsseHeader)
			_context.setGroovyHeader(MessageParser.parseToGPath(wsseHeader))
			_context.setOpensamlHeader(MessageParser.parseToOpenSaml(wsseHeader))
		}
		catch(Exception e){
			throw new ValidationException("an error occured during validation.", e)
		}

		return _context
	}

	private Message validateContext(Context context){

		Message _context = null

		try{
			if(context == null) throw new ValidationException("No context found.")
			_context = (Message) context
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
