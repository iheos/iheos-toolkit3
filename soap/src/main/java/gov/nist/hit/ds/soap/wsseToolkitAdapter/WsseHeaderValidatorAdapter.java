package gov.nist.hit.ds.soap.wsseToolkitAdapter;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.errorRecording.TextErrorRecorder;
import gov.nist.hit.ds.eventLog.assertion.AssertionGroup;
import gov.nist.hit.ds.soap.wsseToolkitAdapter.log4jToErrorRecorder.AppenderForErrorRecorder;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.valSupport.engine.MessageValidator;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;
import gov.nist.hit.ds.wsseTool.api.config.ContextFactory;
import gov.nist.hit.ds.wsseTool.api.config.GenContext;
import gov.nist.hit.ds.wsseTool.api.config.KeystoreAccess;
import gov.nist.hit.ds.wsseTool.api.exceptions.GenerationException;
import gov.nist.hit.ds.wsseTool.validation.WsseHeaderValidator;

import java.security.KeyStoreException;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;
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

	private static Logger log = Logger
			.getLogger(WsseHeaderValidatorAdapter.class);

	/**
	 * Validate our own generated message!
	 */
	public static void main(String[] args) throws KeyStoreException,
			GenerationException {
		String store = "/Users/gerardin/IHE-Testing/xdstools2_environment/environment/AEGIS_env/keystore/keystore";
		String sPass = "changeit";
		String kPass = "changeit";
		String alias = "hit-testing.nist.gov";
		KeystoreAccess keystore = new KeystoreAccess(store, sPass, alias, kPass);
		GenContext context = ContextFactory.getInstance();
		context.setKeystore(keystore);
		context.setParam("To", "http://endpoint1.hostname1.nist.gov" );
		Element wsseHeader = WsseHeaderGeneratorAdapter.buildHeader(context);

		WsseHeaderValidatorAdapter validator = new WsseHeaderValidatorAdapter(
				new ValidationContext(), wsseHeader);
		AssertionGroup er = new AssertionGroup();
		MessageValidatorEngine mvc = new MessageValidatorEngine();
		validator.run(er, mvc);
	}

	private WsseHeaderValidator val;
	private Element header;
	private GenContext context;

	public WsseHeaderValidatorAdapter(ValidationContext vc, Element wsseHeader) {
		super(vc);
		val = new WsseHeaderValidator();
		this.header = wsseHeader;
		this.context = ContextFactory.getInstance();
		// TODO need to check how to get information to put in the context!!
		// patientId, homeCommunityId, endpoint url..
	}
	
	//One quick to passing soap info to the library
	public WsseHeaderValidatorAdapter(ValidationContext vc, Element wsseHeader,
			OMElement soapHeader) {
		this(vc, wsseHeader);
		
		List<OMElement> to = XmlUtil.childrenWithLocalName(soapHeader, "To");
		
		context.setParam("To", to.get(0));
	}

	@Override
	public void run(IAssertionGroup er, MessageValidatorEngine mvc) {

		try {
			// We use a special appender to record message coming from the wsse
			// module in the error recorder framework
			AppenderForErrorRecorder wsseLogApp = new AppenderForErrorRecorder(vc, er.buildNewErrorRecorder(), mvc);

			// those are the logs we are interested in
			org.apache.log4j.Logger logMainVal = org.apache.log4j.Logger
					.getLogger(WsseHeaderValidator.class);
			org.apache.log4j.Logger logVal = org.apache.log4j.Logger
					.getLogger("gov.nist.toolkit.wsseTool.validation");
			logVal.addAppender(wsseLogApp);
			logMainVal.addAppender(wsseLogApp);

			

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
}