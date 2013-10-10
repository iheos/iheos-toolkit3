package gov.nist.hit.ds.httpSoapValidator;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.httpSoapValidator.wsseToolkitAdapter.log4jToErrorRecorder.AppenderForErrorRecorder;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidator;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.loader.ValidationContext;
import gov.nist.toolkit.wsseTool.api.WsseHeaderValidator;
import gov.nist.toolkit.wsseTool.api.config.SecurityContext;
import gov.nist.toolkit.wsseTool.api.config.SecurityContextFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * Temporary adapter between toolkit legacy validation code and the wsse module
 * validation code.
 * 
 * TODO: check with Bill. In my own opinion, the design of the message validator
 * interface is flawed. As a first shot and since the goal is to enforce an
 * contract, an interface Validator with a run() method seems more appropriate.
 * ValidationContext could be push as a parameter of this method.
 * 
 * NOTE : CustomLogger is a quick way to log stuff from the wsse module without
 * having to define an object model of what is "logging"!
 * 
 * TODO clarify what vc , err, mvc are doing! How comes the element to validate
 * on in not part of the run() params?
 * 
 * TODO why should we pass the envelope in the constructor? Confusing.
 * 
 * TODO field er in MessageValidator is not initialized!
 * 
 * @author gerardin
 * 
 */

public class WsseHeaderValidatorAdapter extends MessageValidator {

	private static Logger log = LoggerFactory
			.getLogger(WsseHeaderValidatorAdapter.class);

	/**
	 * Validate our own generated message!
	 */
//	public static void main(String[] args) throws KeyStoreException,
//			GenerationException {
//		String store = "/Users/gerardin/IHE-Testing/xdstools2_environment/environment/AEGIS_env/keystore/keystore";
//		String sPass = "changeit";
//		String kPass = "changeit";
//		String alias = "hit-testing.nist.gov";
//		KeystoreAccess keystore = new KeystoreAccess(store, sPass, alias, kPass);
//		SecurityContext context = SecurityContextFactory.getInstance();
//		context.setKeystore(keystore);
//		Element wsseHeader = WsseHeaderGeneratorAdapter.buildHeader(context);
//
//		WsseHeaderValidatorAdapter validator = new WsseHeaderValidatorAdapter(
//				new ValidationContext(), wsseHeader);
//		ErrorRecorder er = new TextErrorRecorder();
//		MessageValidatorEngine mvc = new MessageValidatorEngine();
//		validator.run(er, mvc);
//	}

	private WsseHeaderValidator val;
	private Element header;

	public WsseHeaderValidatorAdapter(ValidationContext vc, Element wsseHeader) {
		super(vc);
		val = new WsseHeaderValidator();
		this.header = wsseHeader;
	}

	@Override
	public void run(IAssertionGroup er, MessageValidatorEngine mvc) {

		try {
			// We use a special appender to record message coming from the wsse
			// module in the error recorder framework
			AppenderForErrorRecorder wsseLogApp = new AppenderForErrorRecorder(
					vc, er, mvc);

			// those are the logs we are interested in
			org.apache.log4j.Logger logMainVal = org.apache.log4j.Logger
					.getLogger(WsseHeaderValidator.class);
			org.apache.log4j.Logger logVal = org.apache.log4j.Logger
					.getLogger("gov.nist.toolkit.wsseTool.validation");
			logVal.addAppender(wsseLogApp);
			logMainVal.addAppender(wsseLogApp);

			SecurityContext context = SecurityContextFactory.getInstance();
			// TODO need to check how to get information to put in the context!!
			// patientId, homeCommunityId, endpoint url..

			val.validate(header, context);
		} catch (Exception e) {
			//need to report back to the user that error appeared
			log.error(
					"errors occured during validation but we cannot do anything about it!",
					e);
		}

		log.info("\n" + "================================================"
				+ "Error summary generated by the error recorder : "
				+ "================================================");
		// er.showErrorInfo();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean showOutputInLogs() {
		// TODO Auto-generated method stub
		return false;
	}
}
