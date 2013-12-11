package gov.nist.hit.ds.registrySim.transactions;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.eventLog.assertion.ValidationRef;
import gov.nist.hit.ds.httpSoapValidator.datatypes.SoapMessage;
import gov.nist.hit.ds.registryMetadata.IdParser;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registryMetadata.MetadataParser;
import gov.nist.hit.ds.registrySim.store.MetadataCollection;
import gov.nist.hit.ds.registrySim.store.ProcessMetadataForRegister;
import gov.nist.hit.ds.registrySim.store.ProcessMetadataInterface;
import gov.nist.hit.ds.registrySim.store.RegIndex;
import gov.nist.hit.ds.registrySim.store.RegistryFactory;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.simSupport.datatypes.SimEndpoint;
import gov.nist.hit.ds.simSupport.engine.SimComponentBase;
import gov.nist.hit.ds.simSupport.engine.annotations.Inject;
import gov.nist.hit.ds.simSupport.engine.v2compatibility.MessageValidatorEngine;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.soapSupport.exceptions.SoapFaultException;
import gov.nist.hit.ds.soapSupport.soapFault.FaultCode;
import gov.nist.hit.ds.utilities.other.StringHashMapUtil;
import gov.nist.hit.ds.valSupport.ValidationException;
import gov.nist.hit.ds.valSupport.fields.UuidValidator;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

/**
 * Simulator for the Register Transaction. The containing Actor simulator is expected to be
 * found earlier on the SimChain.  This is enforced by the the @Inject of type SimDb. If this
 * data type is not available from an earlier SimComponent then the SimEngine will throw
 * the appropriate error.
 * @author bmajur
 *
 */
public class RegisterTransactionSim extends SimComponentBase {
	SimEndpoint simEndPoint;
//	SimDb db;
	OMElement body;
	Metadata m;
	RegIndex regIndex;
	MetadataCollection delta;
	MetadataCollection mc;
	static Logger logger = Logger.getLogger(RegisterTransactionSim.class);
	
	// keep track of ids during processing
	public Map<String, String> UUIDToSymbolic = null;
	Map<String, String> symbolicToUUID = null; 
	List<String> submittedUUIDs;


	@Inject
	public void setSimEndPoint(SimEndpoint simEndPoint) {
		this.simEndPoint = simEndPoint;
	}
	
	@Inject
	public void setSoapMessage(SoapMessage soapMessage) {
		body = soapMessage.getBody();
	}
	
//	@Inject
//	public void setSimDb(SimDb db) {
//		this.db = db;
//	}
	
	@Inject
	public void setRegIndex(RegIndex regIndex) {
		this.regIndex = regIndex;
	}
	
	private void validateEndpoint() throws SoapFaultException {
		if (!"rb".equals(simEndPoint.getTransaction()))
				throw new SoapFaultException(
						ag,
						FaultCode.Receiver,
						new ErrorContext("Configuration error, Register Transaction (rb) was expect: found (" + simEndPoint.getTransaction() + ") instead" )
						);
	}

	@Override
	public void run(MessageValidatorEngine mve) throws SoapFaultException {
		logger.trace("Run RegisterTransactionSim");
		validateEndpoint();
		
		// validate root elements
		assertEquals("SubmitObjectsRequest", body.getLocalName(), 
				new ValidationRef("RegTrans01", 
						Code.XDSRegistryError, 
						"SubmitObjectsRequest expected as XML child of soap body, found <" + body.getLocalName() + "> instead", 
						new String[] {"ebRS 3.0 Section 5.1"}));
		
		OMElement registryObjectList = body.getFirstElement();
		assertEquals("RegistryObjectList", registryObjectList.getLocalName(),
				new ValidationRef("RegTrans02", 
						Code.XDSRegistryError, 
						"RegistryObjectList expected as XML child of SubmitObjectsRequest, found <" + body.getLocalName() + "> instead", 
						new String[] {"ebRS 3.0 Section 5.1"}));
		
		// validate metadata
		try {
			m = MetadataParser.parse(registryObjectList);
		} catch (Exception e) {
			fail(new ValidationRef("RegTrans03", 
							Code.XDSRegistryError, 
							e.getMessage(), 
							new String[] {"ebRIM 3.0", "ITI TF-3"}));
		}
		// unlikely this could happen...but
		assertNotNull(m,
				new ValidationRef("RegTrans04", 
						Code.XDSRegistryError, 
						"Cannot find metadata in message", 
						new String[] {"ebRS 3.0 Section 5.1"}));
		if (hasErrors())
			return;
		
		setup();
		
		// Check whether Extra Metadata is present, is allowed, and is legal
		extraMetadataCheck(m);

		processMetadata(m, new ProcessMetadataForRegister(this, mc, delta));

		// if errors then don't commit registry update
		if (hasErrors())
			return;

		// save metadata objects XML
		saveMetadataXml(); 

		// delta will be flushed to disk, assuming no errors, by caller 

	}
	
	// These steps are common to Registry and Update.  They operate
	// on the entire metadata collection in both transactions.
	private void setup() {

		mc = regIndex.mc;

		// this will hold our updates - transaction style
		delta = mc.mkDelta();

		// allocate uuids for symbolic ids
		allocateUUIDs(m);
		if (hasErrors())
			return;

		// new ids require a re-indexing of the in-memory metadata db
		m.re_index();

		logAssignedUUIDs();

		// Check for submission of id already present in registry
		checkSubmittedIdsNotInRegistry(mc);

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


	// allocate uuids for symbolic ids
	private void allocateUUIDs(Metadata m) {
		IdParser ra = new IdParser(m);
		try {
			symbolicToUUID = ra.compileSymbolicNamesIntoUuids();
		} catch (XdsInternalException e1) {
			fail(new ValidationRef("RegTrans05", 
							Code.XDSRegistryError, 
							e1.getMessage(), 
							new String[] {""}));

		}
		submittedUUIDs = ra.getSubmittedUUIDs();

		UUIDToSymbolic = StringHashMapUtil.reverse(symbolicToUUID);
	}
	
	private void logAssignedUUIDs() {
		StringBuffer buf = new StringBuffer();
		if (symbolicToUUID != null) {
			for (String symId : symbolicToUUID.keySet()) {
				String uuidId = symbolicToUUID.get(symId);
				buf.append(symId).append(" ==> ").append(uuidId).append("\n");
			}
		}
		try {
			event.addArtifact("Assigned UUIDs", buf.toString());
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}

	// Check for submission of id already present in registry
	private void checkSubmittedIdsNotInRegistry(MetadataCollection mc) {
		List<String> idsAlreadyPresent = new ArrayList<String>();
		for (String id : submittedUUIDs) {
			if (mc.hasObject(id))
				idsAlreadyPresent.add(id);
		}
		assertTrue(idsAlreadyPresent.size() == 0, 
				new ValidationRef("RegTrans06", 
						Code.XDSRegistryMetadataError, 
						"Submission includes pre-assigned ids " + idsAlreadyPresent + " which are already present in the Registry", 
						new String[] {""}));
	}

	// Remove home attribute before saving
	private void rmHome() {
		for (OMElement ele : m.getAllObjects()) {
			OMAttribute homeAtt = ele.getAttribute(MetadataSupport.home_qname);
			if (homeAtt != null)
				ele.removeAttribute(homeAtt);
		}
	}

	// check for Extra Metadata
	// TODO: Re-insert check for extraMetadata support
	private void extraMetadataCheck(Metadata m) {
//		SimulatorConfigElement extraMetadataASCE = asc.get(ActorFactory.extraMetadataSupported);
//		boolean isExtraMetadataSupported = extraMetadataASCE.asBoolean();
		boolean isExtraMetadataSupported = false;
		
		for (OMElement ele : m.getMajorObjects()) {
			String id = m.getId(ele);
			try {
				for (OMElement slotEle : m.getSlots(id)) {
					String slotName = m.getSlotName(slotEle);
					if (!slotName.startsWith("urn:"))
						continue;
					if (slotName.startsWith("urn:ihe:")) {
						// there are no slots defined by ihe with this prefix - reserved for future
						fail(new ValidationRef("", Code.XDSRegistryError, "Illegal Slot name - " + slotName, new String[] {"ITI-TF3:4.1.14"}).setLocation(getClass()));
						continue;
					}
					if (!isExtraMetadataSupported) {
						// register the warning to be returned
						fail(new ValidationRef("", Code.XDSExtraMetadataNotSaved, "Extra Metadata Slot - " + slotName + " present. Extra metadata not supported by this registry", new String[] {"ITI-TF3:4.1.14"}).setLocation(getClass()));
						// remove the slot
						m.rmObject(slotEle);
					}
				}
			} catch (Exception e) {
				fail(Code.XDSRegistryError, e);
			}
		}
	}

	private void saveMetadataXml() {
		try {
			delta.storeMetadata(m);
		} catch (Exception e1) {
			fail(Code.XDSRegistryError, e1);
		}
	}

	private void validateUUIDs() {
		UuidValidator validator;
		
		validator = new UuidValidator("Validating submitted UUID ");
		for (String uuid : submittedUUIDs) {
			try {
				validator.validateUUID(uuid);
			} catch (ValidationException e) {
				fail(Code.XDSRegistryMetadataError, e.getMessage());
			}
		}

		validator = new UuidValidator("Validating generated UUID ");
		for (String uuid : UUIDToSymbolic.keySet()) {
			try {
				validator.validateUUID(uuid);
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail(Code.XDSRegistryMetadataError, e.getMessage());
			}
		}

	}

	public void buildMetadataIndex(Metadata m) {
		try {
			RegistryFactory.buildMetadataIndex(m, delta);
		} catch (MetadataException e) {
			fail(Code.XDSRegistryMetadataError, e);
		}
	}

	private void fail(Code code, Exception e) {
		fail(new ValidationRef("", code, e.getMessage(), new String[] {""}).setLocation(this.getClass()));
	}

	public boolean fail(String msg) {
		fail(new ValidationRef("", Code.XDSRegistryMetadataError, msg, new String[] {""}).setLocation(this.getClass()));
		return false;
	}

	private void fail(Code code, String msg) {
		fail(new ValidationRef("", code, msg, new String[] {""}).setLocation(this.getClass()));
	}


	@Override
	public boolean showOutputInLogs() {
		return true;
	}

}
