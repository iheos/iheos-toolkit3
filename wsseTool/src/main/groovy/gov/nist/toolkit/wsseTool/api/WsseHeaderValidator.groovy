package gov.nist.toolkit.wsseTool.api

import gov.nist.toolkit.wsseTool.api.WsseHeaderGenerator
import gov.nist.toolkit.wsseTool.api.config.KeystoreAccess
import gov.nist.toolkit.wsseTool.api.config.SecurityContext
import gov.nist.toolkit.wsseTool.api.config.SecurityContextFactory
import gov.nist.toolkit.wsseTool.api.exceptions.GenerationException
import gov.nist.toolkit.wsseTool.api.exceptions.ValidationException
import gov.nist.toolkit.wsseTool.context.SecurityContextImpl
import gov.nist.toolkit.wsseTool.parsing.Parser
import gov.nist.toolkit.wsseTool.util.MyXmlUtils
import gov.nist.toolkit.wsseTool.validation.AssertionSignatureVal
import gov.nist.toolkit.wsseTool.validation.AssertionVal
import gov.nist.toolkit.wsseTool.validation.AttributeStatementVal
import gov.nist.toolkit.wsseTool.validation.AuthnStatementVal
import gov.nist.toolkit.wsseTool.validation.AuthzDecisionStatementVal
import gov.nist.toolkit.wsseTool.validation.ParsingVal
import gov.nist.toolkit.wsseTool.validation.SignatureVerificationVal
import gov.nist.toolkit.wsseTool.validation.TimestampVal
import gov.nist.toolkit.wsseTool.validation.Validation

import java.lang.reflect.Method
import java.security.KeyStoreException

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

	private int testCount = 0;

	public static void main(String[] args) throws GenerationException, KeyStoreException {
		String store = "src/test/resources/keystore/keystore"
		String sPass = "changeit"
		String kPass = "changeit"
		String alias = "hit-testing.nist.gov"
		SecurityContext context = SecurityContextFactory.getInstance()
		context.setKeystore(new KeystoreAccess(store,sPass,alias,kPass))
		context.getParams().put("patientId", "D123401^^^&1.1&ISO")
		Document doc = new WsseHeaderGenerator().generateWsseHeader(context)
		new WsseHeaderValidator().validate(doc.getDocumentElement(),context)
	}

	public WsseHeaderValidator(){
	}

	public void validate(Element wsseHeader, SecurityContext context) throws ValidationException {
		ValConfig config = new ValConfig("2.0")
		validate(wsseHeader, config, context)
	}

	public void validate(Element wsseHeader, ValConfig config, SecurityContext context) throws ValidationException {
		log.info("\n =============================" +
				"\n validation of the wsse header" +
				"\n =============================")
		String header = MyXmlUtils.DomToString(wsseHeader)
		log.debug("header to validate : {}", header)

		SecurityContextImpl _context = validateContext(context);

		testCount = 0

		try{
			//add dom, gpath and opensaml representation to the context
			_context.setDomHeader(wsseHeader)
			_context.setGroovyHeader(Parser.parseToGPath(wsseHeader))
			_context.setOpensamlHeader(Parser.parseToOpenSaml(wsseHeader))
			
			//run validations by category

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
	
	private SecurityContextImpl validateContext(SecurityContext context){
		
		SecurityContextImpl _context = null;
		
		try{
			if(context == null) throw new ValidationException("No context found.");
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
					String status = getStatus(annos);
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
		String status = "";
		if(annos.status() == ValConfig.Status.not_implementable){
			status = "NOT IMPLEMENTABLE - ";
		}
		else if(annos.status() == ValConfig.Status.review){
			status = "NEED REVIEW - ";
		}
		
		return status;
	}
}
