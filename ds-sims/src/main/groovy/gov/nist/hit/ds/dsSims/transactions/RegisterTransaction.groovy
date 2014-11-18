package gov.nist.hit.ds.dsSims.transactions
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.dsSims.reg.DSMetadataProcessing
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.httpSoap.components.validators.SoapHeaderValidator
import gov.nist.hit.ds.httpSoap.validators.HttpHeaderValidator
import gov.nist.hit.ds.httpSoap.validators.SoapMessageValidator
import gov.nist.hit.ds.metadata.*
import gov.nist.hit.ds.metadata.client.MetadataCollection
import gov.nist.hit.ds.metadata.store.RegistryFactory
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.xdsException.MetadataException
import gov.nist.hit.ds.xdsException.XdsInternalException
import groovy.util.logging.Log4j
import org.apache.axiom.om.OMAttribute
import org.apache.axiom.om.OMElement
/**
 * Created by bmajur on 7/7/14.
 */
@Log4j
class RegisterTransaction implements Transaction {
    protected Metadata m = null;
    public MetadataCollection mc;
    public MetadataCollection delta;
    SimHandle simHandle

    public Map<String, String> UUIDToSymbolic = null;
    Map<String, String> symbolicToUUID = null;
    List<String> submittedUUIDs;

    RegisterTransaction(SimHandle _simHandle) { simHandle = _simHandle }

    ValidationStatus validateRequest() {
        log.debug('In RegisterTransaction#validateRequest')
        new HttpHeaderValidator(simHandle).asChild().run()

        def soapMessageParser = new SoapMessageValidator(simHandle, httpHdrVal.body)
        soapMessageParser.run()
        def header = soapMessageParser.header
        def body = soapMessageParser.body

        new SoapHeaderValidator(simHandle, header).asChild().run()

        new DSMetadataProcessing(simHandle, body).asChild().run()
    }

    @Override
    ValidationStatus validateResponse() {
        log.debug('In RegisterTransaction#validateResponse')
        return null
    }

    @Override
    ValidationStatus run() {
        log.debug('In RegisterTransaction#run')
        new HttpHeaderValidator(simHandle).asChild().run()

        def soapMessageParser = new SoapMessageValidator(simHandle, new String(simHandle.event.inOut.reqBody))
        soapMessageParser.asPeer().run()
        def header = soapMessageParser.header
        def body = soapMessageParser.body

        new SoapHeaderValidator(simHandle, header).asPeer().run()

        new DSMetadataProcessing(simHandle, body).asPeer().run()
        return null
    }

    ValidationStatus validateResponse(SimHandle _handle) { simHandle = _handle}

    ValidationStatus run(SimHandle _handle) {
        assert true // not actually used
        simHandle = _handle
        assert simHandle
        assert simHandle.event
        log.debug('Starting RegisterTransaction')


//        def metadataProcessing = new RegisterMetadataProcessing(open, soapBody)
//        metadataProcessing.run()


//        // These steps are common to Registry and Update.  They operate
//        // on the entire metadata collection in both transactions.
//        setup();
//
//        // Check whether Extra Metadata is present, is allowed, and is legal
//        extraMetadataCheck(m);
//
//        processMetadata(m, new ProcessMetadataForRegister(er, mc, delta));
//
//        // if errors then don't commit registry update
//        if (hasErrors())
//            return;
//
//        // save metadata objects XML
//        saveMetadataXml();
//
//        // delta will be flushed to disk, assuming no errors, by caller

    }

    // These steps are common to Registry and Update.  They operate
    // on the entire metadata collection in both transactions.
    protected void setup() {
        try {
            MetadataContainer metaCon = (MetadataContainer) common.getMessageValidator(MetadataContainer.class);
            m = metaCon.getMetadata();
            if (m == null) throw new Exception("");
        } catch (Exception e) {
            er.err(XdsErrorCode.Code.XDSRegistryError, "Internal Error: cannot access input metadata", this, null);
        }

        mc = common.regIndex.mc;

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

    public void buildMetadataIndex(Metadata m) {
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
//        SimulatorConfigElement extraMetadataASCE = asc.label(ActorFactory.extraMetadataSupported);
        boolean isExtraMetadataSupported = true  //extraMetadataASCE.asBoolean();

        for (OMElement ele : m.getMajorObjects()) {
            String id = m.getId(ele);
            try {
                for (OMElement slotEle : m.getSlots(id)) {
                    String slotName = m.getSlotName(slotEle);
                    if (!slotName.startsWith("urn:"))
                        continue;
                    if (slotName.equals("urn:ihe:iti:xds:2013:referenceIdList"))    // used by referenceIdList
                        continue;
                    if (slotName.startsWith("urn:ihe:")) {
                        // there are no slots defined by ihe with this prefix - reserved for future
                        er.err(XdsErrorCode.Code.XDSRegistryError, "Illegal Slot displayName - " + slotName, "RegRSim.java", MetadataSupport.error_severity, "ITI-TF3:4.1.14");
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

    Map<String, String> reverse(Map<String, String> inp)  {
        Map<String, String> out = new HashMap<String, String>();

        for (String key : inp.keySet() ) {
            String val = inp.get(key);
            out.put(val, key);
        }

        return out;
    }

    public boolean hasErrors() {
        return er.hasErrors() || mvc.hasErrors();
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

