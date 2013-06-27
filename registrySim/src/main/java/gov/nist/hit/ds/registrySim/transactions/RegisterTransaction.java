package gov.nist.hit.ds.registrySim.transactions;

import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.registryMetadata.IdParser;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.store.MetadataCollection;
import gov.nist.hit.ds.registrySim.store.ProcessMetadataForRegister;
import gov.nist.hit.ds.registrySim.store.ProcessMetadataInterface;
import gov.nist.hit.ds.registrySim.store.RegIndex;
import gov.nist.hit.ds.registrySim.store.RegistryFactory;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.simSupport.SimDb;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.valSupport.engine.MessageValidatorEngine;
import gov.nist.hit.ds.valSupport.engine.ValidationStep;
import gov.nist.hit.ds.valSupport.errrec.GwtErrorRecorderBuilder;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

public class RegisterTransaction  {
	public MetadataCollection mc;
	public MetadataCollection delta;
	protected MessageValidatorEngine mve;
	public Map<String, String> UUIDToSymbolic = null;
	Map<String, String> symbolicToUUID = null; 
	List<String> submittedUUIDs;
	ValidationContext vc;
	ErrorRecorder er;
	static Logger log = Logger.getLogger(RegisterTransaction.class);

	/*
	 * Must be initialized by Builder
	 */
	public Metadata m;
	public RegIndex regIndex;
	public boolean isExtraMetadataSupported;



	/* Not yet using builder */
	public RegisterTransaction(ValidationContext vc, ErrorRecorder er, RegIndex ri) {
		this.vc = vc;
		this.er = er;
		this.regIndex = ri;
	}


	/**
	 * 
	 * @param mve
	 * @return success?
	 */
	public boolean run(MessageValidatorEngine mve) {
		this.mve = mve;
		
		if (!runInitialValidations())
			return false;  // returns if SOAP Fault was generated
		
		if (mve.hasErrors()) {
			RegistryResponseSendingSim rrss = new RegistryResponseSendingSim(this);
			sendErrorsInRegistryResponse(er);
			return false;
		}

		
//		RegRSim rsim = new RegRSim(common, asc);
//		mve.addMessageValidator("RegistryActor", rsim, er);

		
		

		// These steps are common to Registry and Update.  They operate
		// on the entire metadata collection in both transactions.
		setup();

		// Check whether Extra Metadata is present, is allowed, and is legal
		extraMetadataCheck(m);

		processMetadata(m, new ProcessMetadataForRegister(er, mc, delta));

		// if errors then don't commit registry update
		if (hasErrors())
			return false;

		// save metadata objects XML
		saveMetadataXml(); 

		// delta will be flushed to disk, assuming no errors, by caller 
		
		
		
		registryResponseGenerator = new RegistryResponseGeneratorSim(common);
		mve.addMessageValidator("Attach Errors", registryResponseGenerator, er);

		mve.run();

		// wrap in soap wrapper and http wrapper
		mve.addMessageValidator("ResponseInSoapWrapper", 
				new SoapWrapperRegistryResponseSim(common, registryResponseGenerator), 
				er);

		// catch up on validators to be run so we can judge whether to commit or not
		mve.run();

		// commit updates (delta) to registry database
		if (!common.hasErrors())
			commit(mve, common, rsim.delta);
		
		return !common.hasErrors();


	}
	
	public boolean runInitialValidations() throws IOException {
		mve = vms.runValidation(vc, db, mve);
		mve.run();
		buildMVR();

		int stepsWithErrors = mve.getErroredStepCount();
		ValidationStep lastValidationStep = mve.getLastValidationStep();
		if (lastValidationStep != null) {
			lastValidationStep.getErrorRecorder().detail
			(stepsWithErrors + " steps with errors");
			logger.debug(stepsWithErrors + " steps with errors");
		} else {
			logger.debug("no steps with errors");
		}

		boolean sent = returnFaultIfNeeded();
		if (sent)
			faultReturned = true;
		return !sent;
	}


	public MessageValidatorEngine runValidation(ValidationContext vc, SimDb db, MessageValidatorEngine mvc) throws IOException {
		String httpMsgHdr = db.getRequestMessageHeader(); 
		byte[] httpMsgBody = db.getRequestMessageBody();
		GwtErrorRecorderBuilder gerb = new GwtErrorRecorderBuilder();
		
		if (mvc == null)
			mvc = new MessageValidatorEngine();
		HttpMessageValidator val = new HttpMessageValidator(vc, httpMsgHdr, httpMsgBody, gerb, mvc, rvi);
		mvc.addMessageValidator("Parse HTTP Message", val, gerb.buildNewErrorRecorder());
		mvc.run();
		
		return mvc;
	}

	
	
	// These steps are common to Registry and Update.  They operate
	// on the entire metadata collection in both transactions.
	protected void setup() {
		if (m == null) {
			er.err(Code.XDSRegistryError, "Internal Error: Metadata not available", this, null);
			return;
		}

		mc = regIndex.mc;

		// this will hold our updates - transaction style
		delta = mc.mkDelta();

		// allocate uuids for symbolic ids
		allocateUUIDs(m);

		m.re_index();

		logAssignedUUIDs();

		// Check for submission of id already present in registry
		checkSubmittedIdsNotInRegistry();

		// remove all instances of the home attribute
		rmHome();
	}

	// These steps are run on the entire metadata collection
	// for the Register transaction but only on an operation
	// for the Update transaction.  
	public void processMetadata(Metadata m, ProcessMetadataInterface pmi) {

		// Are all UUIDs, submitted and generated, valid?
		validateUUIDs();

		// MU will change
		pmi.checkUidUniqueness(m);

		// set logicalId to id 
		pmi.setLidToId(m);

		// install version attribute in SubmissionSet, DocumentEntry and Folder objects
		// install default version in Association, Classification, ExternalIdentifier
		pmi.setInitialVersion(m);

		// build update to metadata index with new objects
		// this will later be committed
		// This is done now because the operations below need this index
		buildMetadataIndex(m);

		// set folder lastUpdateTime on folders in the submission
		// must be done after metadata index built
		pmi.setNewFolderTimes(m);

		// set folder lastUpdateTime on folders already in the registry
		// that this submission adds documents to
		// must be done after metadata index built
		pmi.updateExistingFolderTimes(m);

		// verify that no associations are being added that:
		//     reference a non-existant object in submission or registry
		//     reference a Deprecated object in registry
		pmi.verifyAssocReferences(m);

		// check for RPLC and RPLC_XFRM and do the deprecation
		pmi.doRPLCDeprecations(m);

		// if a replaced doc is in a Folder, then new doc is placed in folder
		// and folder lastUpateTime is updated
		pmi.updateExistingFoldersWithReplacedDocs(m);
	}

	void rmHome() {
		for (OMElement ele : m.getAllObjects()) {
			OMAttribute homeAtt = ele.getAttribute(MetadataSupport.home_qname);
			if (homeAtt != null)
				ele.removeAttribute(homeAtt);
		}
	}

	void saveMetadataXml() {
		try {
			delta.storeMetadata(m);
		} catch (Exception e1) {
			er.err(XdsErrorCode.Code.XDSRegistryError, e1);
		}
	}

	void buildMetadataIndex(Metadata m) {
		try {
			RegistryFactory.buildMetadataIndex(m, delta);
		} catch (MetadataException e) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
		}
	}

	void logAssignedUUIDs() {
		er.detail("Assigned UUIDs");
		if (symbolicToUUID != null) {
			for (String symId : symbolicToUUID.keySet()) {
				String uuidId = symbolicToUUID.get(symId);
				er.detail(symId + " ==> " + uuidId);
			}
		}
	}

	// Check for submission of id already present in registry
	void checkSubmittedIdsNotInRegistry() {
		for (String id : submittedUUIDs) {
			if (mc.hasObject(id))
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "Submission includes pre-assigned id " + id + " which is already present in the Registry", this, null);
		}
	}

	// allocate uuids for symbolic ids
	protected void allocateUUIDs(Metadata m) {
		IdParser ra = new IdParser(m);
		try {
			symbolicToUUID = ra.compileSymbolicNamesIntoUuids();
		} catch (XdsInternalException e1) {
			er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e1);
		}
		submittedUUIDs = ra.getSubmittedUUIDs();

		UUIDToSymbolic = reverse(symbolicToUUID);
	}

	void validateUUIDs() {
		UuidValidator validator;

		validator = new UuidValidator(er, "Validating submitted UUID ");
		for (String uuid : submittedUUIDs) {
			validator.validateUUID(uuid);
		}

		validator = new UuidValidator(er, "Validating generated UUID ");
		for (String uuid : UUIDToSymbolic.keySet()) {
			validator.validateUUID(uuid);
		}

	}

	// check for Extra Metadata
	void extraMetadataCheck(Metadata m) {
		for (OMElement ele : m.getMajorObjects()) {
			String id = m.getId(ele);
			try {
				for (OMElement slotEle : m.getSlots(id)) {
					String slotName = m.getSlotName(slotEle);
					if (!slotName.startsWith("urn:"))
						continue;
					if (slotName.startsWith("urn:ihe:")) {
						// there are no slots defined by ihe with this prefix - reserved for future
						er.err(XdsErrorCode.Code.XDSRegistryError, "Illegal Slot name - " + slotName, "RegRSim.java", MetadataSupport.error_severity, "ITI-TF3:4.1.14");
						continue;
					}
					if (!isExtraMetadataSupported) {
						// register the warning to be returned
						er.err(XdsErrorCode.Code.XDSExtraMetadataNotSaved, "Extra Metadata Slot - " + slotName + " present. Extra metadata not supported by this registry", "RegRSim.java", MetadataSupport.warning_severity, "ITI-TF3:4.1.14");
						// remove the slot
						m.rmObject(slotEle);
					}
				}
			} catch (Exception e) {
				er.err(XdsErrorCode.Code.XDSRegistryError, e);
			}
		}
	}

	protected String getIdSubmittedValue(String id) {
		if (UUIDToSymbolic.get(id) == null)
			return id;
		return UUIDToSymbolic.get(id);
	}

	boolean isSubmittedIdValueUUID(String id) {
		String orig = getIdSubmittedValue(id);
		return orig.startsWith("urn:uuid:");
	}

	Map<String, String> reverse(Map<String, String> in)  {
		Map<String, String> out = new HashMap<String, String>();

		for (String key : in.keySet() ) {
			String val = in.get(key);
			out.put(val, key);
		}

		return out;
	}

	public boolean hasErrors() {
		return er.hasErrors() || mve.hasErrors();
	}

	public void save(Metadata m, boolean buildIndex) {
		try {
			if (m.getSubmissionSet() != null)
				log.debug("Save SubmissionSet(" + m.getSubmissionSetId() + ")");
			for (OMElement ele : m.getExtrinsicObjects()) 
				log.debug("Save DocEntry(" + m.getId(ele) + ")");
			for (OMElement ele : m.getFolders())
				log.debug("Save Folder(" + m.getId(ele) + ")");
			for (OMElement ele : m.getAssociations())
				log.debug("Save Assoc(" + m.getId(ele) + ")("+ m.getAssocSource(ele) + ", " + m.getAssocTarget(ele) + ", " + m.getSimpleAssocType(ele) + ")");
		} catch (Exception e) {}

		if (buildIndex) {
			// update index
			try {
				RegistryFactory.buildMetadataIndex(m, delta);
			} catch (MetadataException e) {
				er.err(XdsErrorCode.Code.XDSRegistryMetadataError, e);
			}
		}

		// save metadata objects XML
		try {
			delta.storeMetadata(m);
		} catch (Exception e1) {
			er.err(XdsErrorCode.Code.XDSRegistryError, e1);
		} 
	}
}
