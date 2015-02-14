package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator
import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.ebMetadata.Metadata
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.ebMetadata.MetadataUtilities
import gov.nist.hit.ds.eventLog.errorRecording.ErrorRecorder
import gov.nist.hit.ds.eventLog.errorRecording.client.XdsErrorCode
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.validationEngine.ValComponentBase
import gov.nist.hit.ds.simSupport.validationEngine.annotation.ErrorCode
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Guard
import gov.nist.hit.ds.simSupport.validationEngine.annotation.Validation
import org.apache.axiom.om.OMElement

// TODO: Full Folder association validation not hooked up

public class SubmissionStructureValidator extends ValComponentBase {
    SimHandle simHandle
    Metadata m;
    ValidationContext vc
    RegistryValidationInterface rvi;
    @Delegate MetadataUtilities metadataUtilities


    SubmissionStructureValidator(SimHandle _simHandle, ValidationContext _vc, Metadata _m, RegistryValidationInterface _rvi)  {
        super(_simHandle.event)
        simHandle = _simHandle
        m = _m;
        vc = _vc
        rvi = _rvi;
        metadataUtilities = new MetadataUtilities(m)
    }

    @Validation(id='rosubstr005', msg='SubmissionSet, DocumentEntry, Folder and Association objects must have IDs', ref='ebRIM 2.4.1')
    def rosubstr005() {
        if (rvi.connected)
            infoMsg('Validating Registry content as needed')
        else
            infoMsg('Registry not available - not validating its content')
    }
        @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr010', msg='SubmissionSet, DocumentEntry, Folder and Association objects must have IDs', ref='ebRIM 2.4.1')
    def rosubstr010() {
        infoFound("${m.getExtrinsicObjects().size() + m.getSubmissionSets().size() + m.getFolders().size() + m.getAssociations().size()} objects to scan")
        submissionSetIds().each { id ->
            expected('valid id')
            assertHasValue("${dots}SubmissionSet.id", id)
        }
        extrinsicObjectIds().each { id ->
            expected('valid id')
            assertHasValue("${dots}ExtrinsicObject.id", id)
        }
        folderIds().each { id ->
            expected('valid id')
            assertHasValue("${dots}Folder.id", id)
        }
        associationIds().each { id ->
            expected('valid id')
            assertHasValue("${dots}Association.id", id)
        }
    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr020', msg='', ref='')
    def rosubstr020() {
        if (vc.isSubmit() && vc.isRequest)
            infoMsg("Validating as a submission");
        else
            infoMsg("Validating as a non-submission")
        submissionSetIds().each { infoMsg("${dots}contains SubmissionSet(${it})") }
        extrinsicObjectIds().each { infoMsg( "${dots}contains DocumentEntry(${it})") }
        folderIds().each { infoMsg("${dots}contains Folder(${it})")}
        associationIds().each { infoMsg("${dots}contains Association(${it})")}
    }
    boolean submission() { vc.isSubmit() && vc.isRequest }

    @Guard(methodNames=['submission', 'hasSubmissionSet'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr022', msg='Must contain objects beyond SubmissionSet', ref='ITI TF-3: 4.1.4')
    def rosubstr022() {
        def count = m.getExtrinsicObjects().size() + m.getSubmissionSets().size() + m.getFolders().size() + m.getAssociations().size()
        found("${count-1} other object(s)")
        expected('Objects besides SubmissionSet')
        if (count == 1) fail('Only SubmissionSet found')
    }

    @Guard(methodNames=['submission'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr025', msg='Has single SubmissionSet', ref='ITI TF-3: 4.1.4')
    def rosubstr025() {
        if (submissionSets().size() == 0) {
            fail("Submission does not contain a SubmissionSet")
        } else if (submissionSets().size() > 1) {
            fail("Submission contains multiple SubmissionSets")
        } else
            found("${submissionSets().size()} SubmissionSets")
    }

    boolean hasSubmissionSet() { submissionSets().size() }

    @Guard(methodNames=['submission', 'hasSubmissionSet'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr040', msg='All DocumentEntries linked to SubmissionSet', ref='ITI TF-3: 4.1.4.1')
    def rosubstr040() {
        infoMsg('Validating DocumentEntires are linked to SubmissionSet')
        docEntries().each { doc ->
            List<OMElement> assocs = ss_DE_Assocs(doc)
            new SStoDEHasMemberValidator(simHandle, m, doc, assocs).asSelf(this).run()
        }
    }

    @Guard(methodNames=['submission'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr050', msg='All Folders linked to SubmissionSet', ref='ITI TF-3: 4.1.4.2')
    def rosubstr050() {
        m.getFolders().each { fol ->
            infoMsg(folderTag(fol))
            if (has_assoc(m.getSubmissionSetId(), assoc_type("HasMember"), m.getId(fol))) {
            } else {
                fail("Folder " + m.getId(fol) + " is not linked to the SubmissionSet with a " + assoc_type("HasMember") + " Association")
            }
        }
    }

    @Guard(methodNames=['submission'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr060', msg='No symbolic references to object not in submission', ref='ITI TF-3: 4.1.12.3')
    def rosubstr060() {
        List<OMElement> assocs = m.getAssociations();

        boolean ok = true
        for (int i=0; i<assocs.size(); i++) {
            OMElement assoc = (OMElement) assocs.get(i);
            String target = assoc.getAttributeValue(MetadataSupport.target_object_qname);
            String type = assoc.getAttributeValue(MetadataSupport.association_type_qname);
            String source = assoc.getAttributeValue(MetadataSupport.source_object_qname);

            if (target == null || source == null || type == null)
                continue;

            if (!isUUID(source) && !submissionContains(source)) {
                fail(objectTag(assoc) + ": sourceObject has value " + source +
                        " which is not in the submission but cannot be in the receiving actor since it is not in UUID format")
                ok = false
            }

            if (!isUUID(target) && !submissionContains(target)) {
                fail(objectTag(assoc) + ": targetObject has value " + target +
                        " which is not in the submission but cannot be in the receiving actor since it is not in UUID format")
                ok = false
            }
        }
        if (ok) success()
    }

    @Guard(methodNames=['submission'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr070', msg='Verify Association types', ref=['ITI TF-3: 4.1.12.3', 'ITI TF-3: 4.1.6.1'])
    def rosubstr070() {
        infoFound('')
        boolean ok = true
        for (OMElement assoc : m.getAssociations()) {
            String type = m.getAssocType(assoc)
            if (type == null)
                continue
            type = simpleAssocType(type)
            if (type.equals("HasMember")) {
                ok = evalHasMember(assoc) && ok
            } else if (relationships.contains(type)) {
                ok = evalRelationship(assoc) && ok
            } else if (type.equals("signs")) {
                ok = evalSigns(assoc) && ok
            }
        }
        if (ok) assertTrue(true, "${dots}Association types ok")
    }

    @Guard(methodNames=['submission'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr080', msg='Verify Association types', ref=['ITI TF-3: 4.1.12.3', 'ITI TF-3: 4.1.6.1','ITI TF-3: 4.1.4.1'])
    def rosubstr080() {
        List<OMElement> assocs = m.getAssociations();
        String ss_id = m.getSubmissionSetId();

        for (int i=0; i<assocs.size(); i++) {
            OMElement assoc = (OMElement) assocs.get(i);
            String source = assoc.getAttributeValue(MetadataSupport.source_object_qname);
            if (source == null)
                continue;

            if ( !source.equals(ss_id))
                continue;

            String ss_status = m.getSlotValue(assoc, "SubmissionSetStatus", 1);
            if (ss_status != null)
                fail("SubmissionSetStatus Slot on SubmissionSet association has more than one value")
        }
    }

    @Guard(methodNames=['submission'])
    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr090', msg='Verify Patient ID consistency', ref='')
    def rosubstr090() {
        new PatientIdValidator(simHandle, m).asSelf(this).run();
    }

//    boolean errorsFound() { hasMemberError }
//
//    @Guard(methodNames=['errorsFound'])
//    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
//    @Validation(id='rosubstr100', msg='Verify Patient ID consistency', ref='')
//    def rosubstr100() {
//        log_hasmember_usage()
//    }

    @ErrorCode(code=XdsErrorCode.Code.XDSRegistryMetadataError)
    @Validation(id='rosubstr110', msg='Verify RegistryPackages are Folders or SubmissionSets', ref='ITI TF-3: 4.1.9.1')
    def rosubstr110() {
        for (String id : m.getRegistryPackageIds()) {
            if (!m.getSubmissionSetIds().contains(id) && !m.getFolderIds().contains(id))
                fail("RegistryPackage(" + id + ") : is not classified as SubmissionSet or Folder")
        }
    }


    String assocsRef = "ITI Tf-3: 4.1";

    boolean evalHasMember(OMElement assoc) {
        String source = m.getAssocSource(assoc);
        String target = m.getAssocTarget(assoc);
        String type = m.getAssocType(assoc);

        if (source == null || target == null || type == null)
            return true;

        if (is_ss_to_de_hasmember(assoc)) {
            infoMsg(dots + assocTag(assoc) + " is a SubmissionSet to DocmentEntry HasMember association")
        } else if (is_ss_to_existing_de_hasmember(assoc)) {
            infoMsg(dots + assocTag(assoc) + " is a SubmissionSet to existing DocmentEntry HasMember (ByReference) association");
        } else if (is_ss_to_folder_hasmember(assoc)) {
            infoMsg(dots + assocTag(assoc) + " is a SubmissionSet to Folder HasMember association");
        } else if (is_ss_to_folder_hasmember_hasmember(assoc)) {
            infoMsg(dots + assocTag(assoc) + " is a SubmissionSet to Folder-HasMember HasMember association (adds existing DocumentEntry to existing Folder)");
        } else if (is_fol_to_de_hasmember(assoc)) {
            infoMsg(dots + assocTag(assoc) + " is a Folder to DocumentEntry HasMember association");
        } else {
            fail(dots + assocTag(assoc) + " do not understand this HasMember association:")
            m.getSlots(id(assoc)).each { fail("${dots}${dots}has ${slotTag(it)}(${slotValues(it)})") }
            fail(dots + dots + "sourceObject is " + objectTag(source))
            fail(dots + dots + " and targetObject is " + objectTag(target))
            return false
        }
        return true
    }

    // ITI TF-3: 4.1.6.1
    boolean evalRelationship(OMElement assoc) {
        String source = m.getAssocSource(assoc);
        String target = m.getAssocTarget(assoc);
        String type = m.getAssocType(assoc);

        if (source == null || target == null || type == null)
            return true;

        boolean ok = true
        if (!isDocumentEntry(source)) {
            fail(dots + objectTag(assoc) + ": with type " + simpleAssocType(type) + " must reference a DocumentEntry in submission with its sourceObject attribute, it references " + objectTag(source))
            ok = false
        }

        if (containsObject(target)) {
            fail(dots + objectTag(assoc) + ": with type " + simpleAssocType(type) + " must reference a DocumentEntry in the registry with its targetObject attribute, it references " + objectTag(target) + " which is in the submission")
            ok = false
        }

        if (!isUUID(target)) {
            fail(dots + objectTag(assoc) + ": with type " + simpleAssocType(type) + " must reference a DocumentEntry in the registry with its targetObject attribute, it references " + objectTag(target) + " which is a symbolic ID that cannot reference an object in the registry")
            ok = false
        }
        return ok
    }

    boolean evalSigns(OMElement assoc) {
        return true
    }

    static List<String> relationships =
            Arrays.asList(
                    "HasMember",
                    "RPLC",
                    "XFRM",
                    "XFRM_RPLC",
                    "APND"
            );


    void cannotValidate(ErrorRecorder er, String context) {
        er.err(XdsErrorCode.Code.XDSRegistryMetadataError, context + ": cannot validate - error parsing", this, "ebRIM");
    }


    void sss_relates_to_ss(ErrorRecorder er, ValidationContext vc) {
        String ss_id = m.getSubmissionSetId();
        List<OMElement> assocs = m.getAssociations();

        for (int i=0; i<assocs.size(); i++) {
            OMElement assoc = (OMElement) assocs.get(i);
            String a_target = assoc.getAttributeValue(MetadataSupport.target_object_qname);
            String a_type = assoc.getAttributeValue(MetadataSupport.association_type_qname);
            String a_source = assoc.getAttributeValue(MetadataSupport.source_object_qname);
            if (a_target == null) {
                cannotValidate(er, "Association(" + assoc.getAttributeValue(MetadataSupport.id_qname) + ") - targetObject");
                return;
            }
            if (a_source == null) {
                cannotValidate(er, "Association(" + assoc.getAttributeValue(MetadataSupport.id_qname) + ") - sourceObject");
                return;
            }
            if (a_type == null) {
                cannotValidate(er, "Association(" + assoc.getAttributeValue(MetadataSupport.id_qname) + ") - associationType");
                return;
            }

            boolean target_is_included_is_doc = m.getExtrinsicObjectIds().contains(a_target);

            if (a_source.equals(ss_id)) {
                String hm = assoc_type("HasMember");
                if ( !a_type.equals(hm)) {
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "Association referencing SubmissionSet has type " + a_type + " but only type " + assoc_type("HasMember") + " is allowed", this, "ITI TF-3: 4.1.4");
                }
                if (target_is_included_is_doc) {
                    if ( ! m.hasSlot(assoc, "SubmissionSetStatus")) {
                        er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "Association(" +
                                assoc.getAttributeValue(MetadataSupport.id_qname) +
                                ") has sourceObject pointing to SubmissionSet and targetObject pointing to a DocumentEntry but contains no SubmissionSetStatus Slot", this, "ITI TF-3: 4.1.4.1"
                        );
                    }
                } else if (m.getFolderIds().contains(a_target)) {

                } else {

                }
            }
            else {
                if ( m.hasSlot(assoc, "SubmissionSetStatus") && !"Reference".equals(m.getSlotValue(assoc, "SubmissionSetStatus", 0)))
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "Association " +
                            assoc.getAttributeValue(MetadataSupport.id_qname) +
                            " does not have sourceObject pointing to SubmissionSet but contains SubmissionSetStatus Slot with value Original", this, "ITI TF-3: 4.1.4.1"
                    );
            }
        }
    }


    void by_value_assoc_in_submission(ErrorRecorder er, ValidationContext vc)  {
        List<OMElement> assocs = m.getAssociations();
        String ss_id = m.getSubmissionSetId();

        for (int i=0; i<assocs.size(); i++) {
            OMElement assoc = (OMElement) assocs.get(i);
            String source = assoc.getAttributeValue(MetadataSupport.source_object_qname);
            String target = assoc.getAttributeValue(MetadataSupport.target_object_qname);
            if (source == null)
                continue;
            if (target == null)
                continue;

            if ( !source.equals(ss_id))
                continue;

            boolean target_is_included_doc = m.getExtrinsicObjectIds().contains(target);

            if (m.getSlot(assoc, "SubmissionSetStatus") == null)
                return;

            String ss_status = m.getSlotValue(assoc, "SubmissionSetStatus", 0);

            if ( target_is_included_doc ) {

                if (ss_status == null || ss_status.equals("")) {
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "SubmissionSetStatus Slot on Submission Set association has no value", this, "ITI TF-3: 4.1.4.1");
                } else if (	ss_status.equals("Original")) {
                    if ( !containsObject(target))
                        er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "SubmissionSetStatus Slot on SubmissionSet association has value 'Original' but the targetObject " + target + " references an object not in the submission",
                                this, "ITI TF-3: 4.1.4.1");
                } else if (	ss_status.equals("Reference")) {
                    if (containsObject(target))
                        er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "SubmissionSetStatus Slot on SubmissionSet association has value 'Reference' but the targetObject " + target + " references an object in the submission",
                                this, "ITI TF-3: 4.1.4.1");
                } else {
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "SubmissionSetStatus Slot on Submission Set association has unrecognized value: " + ss_status, this, "ITI TF-3: 4.1.4.1");
                }
            } else {
                if (ss_status != null && !ss_status.equals("Reference"))
                    er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "A SubmissionSet Assocation has the SubmissionSetStatus Slot but the target ExtrinsicObject is not part of the Submission", this, "ITI TF-3: 4.1.4.1");

            }
        }
    }


//	boolean mustBeInRegistry(String id) {
//		return !containsObject(id) && isUUID(id);
//	}


//	void ss_implies_doc_or_fol_or_assoc(ErrorRecorder er, ValidationContext vc) {
//		if (	m.getSubmissionSet() != null &&
//				! (
//						m.getExtrinsicObjects().size() > 0 ||
//						m.getFolders().size() > 0 ||
//						m.getAssociations().size() > 0
//				))
//			er.err("Submission contains a SubmissionSet but no DocumentEntries or Folders or Associations", "ITI TF-3: 4.1.3.1");
//	}



    // Folder Assocs must be linked to SS by a secondary Assoc
    void folder_assocs(ErrorRecorder er, ValidationContext vc)  {
        String ssId = m.getSubmissionSetId();
        List<OMElement> non_ss_assocs = null;
        for (OMElement a : m.getAssociations()) {
            String sourceId = m.getAssocSource(a);
            if (m.getAssocTarget(a) == ssId)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "SubmissionSet may not the be target of an Association", this, "ITI TF-3: 4.1.4");
            // if sourceId points to a SubmissionSet in this metadata then no further work is needed
            // if sourceId points to a Folder (in or out of this metadata) then secondary Assoc required
            if (sourceId.equals(ssId))
                continue;
            if (isFolder(sourceId)) {
                if (non_ss_assocs == null)
                    non_ss_assocs = new ArrayList<OMElement>();
                non_ss_assocs.add(a);
            }
        }
        if (non_ss_assocs == null) return;

        // Show that the non-ss associations are linked to ss via a HasMember association
        // This only applies when the association's sourceObject is a Folder
        for (OMElement a : non_ss_assocs) {
            String aId = a.getAttributeValue(MetadataSupport.id_qname);
            boolean good = false;
            for (OMElement a2 : m.getAssociations()) {
                if (m.getAssocSource(a2).equals(ssId) &&
                        m.getAssocTarget(a2).equals(aId) &&
                        getSimpleAssocType(a2).equals("HasMember")) {
                    if (good) {
                        er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "Multiple HasMember Associations link SubmissionSet " + ssId +
                                " and Association\n" + a, this, "ITI TF-3: 4.1.4");
                    } else {
                        good = true;
                    }

                }
            }
            if (good == false)
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "A HasMember Association is required to link SubmissionSet " + ssId +
                        " and Folder/DocumentEntry Association\n" + a, this, "ITI TF-3: 4.1.4.2");
        }

    }




    String associationSimpleType(String assocType) {
        String[] parts = assocType.split(":");
        if (parts.length < 2)
            return assocType;
        return parts[parts.length-1];
    }

    //	void assocs_have_proper_namespace() {
    //		List<OMElement> assocs = m.getAssociations();
    //
    //		for (OMElement a_ele : assocs) {
    //			String a_type = a_ele.getAttributeValue(MetadataSupport.association_type_qname);
    //			if (m.isVersion2() && a_type.startsWith("urn"))
    //				err("XDS.a does not accept namespace prefix on association type:  found " + a_type);
    //			if ( ! m.isVersion2()) {
    //				String simpleType = associationSimpleType(a_type);
    //				if (Metadata.iheAssocTypes.contains(simpleType)) {
    //					if ( !a_type.startsWith(MetadataSupport.xdsB_ihe_assoc_namespace_uri))
    //						err("XDS.b requires namespace prefix urn:ihe:iti:2007:AssociationType on association type " + simpleType )	;
    //				} else {
    //					if ( !a_type.startsWith(MetadataSupport.xdsB_eb_assoc_namespace_uri))
    //						err("XDS.b requires namespace prefix urn:oasis:names:tc:ebxml-regrep:AssociationType on association type " + simpleType )	;
    //
    //				}
    //			}
    //		}
    //	}

    void rplced_doc_not_in_submission(ErrorRecorder er, ValidationContext vc)  {
        List<OMElement> assocs = m.getAssociations();

        for (int i=0; i<assocs.size(); i++) {
            OMElement assoc = (OMElement) assocs.get(i);
            String id = assoc.getAttributeValue(MetadataSupport.target_object_qname);
            String type = assoc.getAttributeValue(MetadataSupport.association_type_qname);
            if (MetadataSupport.relationship_associations.contains(type) && ! isReferencedObject(id))
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, "DocumentEntry referenced by a relationship style assocation " + MetadataSupport.relationship_associations +
                        " cannot be contained in submission\nThe following objects were found in the submission:"
                        + getIdsOfReferencedObjects().toString(), this, "ITI TF-3: 4.1.6.1");
        }
    }

    void validateFolderHasMemberAssoc(ErrorRecorder er, String assocId) {
        OMElement assoc = getObjectById(assocId);
        if (simpleAssocType(m.getAssocType(assoc)).equals("HasMember")) {
            // must relate folder to docentry
            String source = m.getAssocSource(assoc);
            String target = m.getAssocTarget(assoc);
            if (source == null || target == null)
                return;
            // try to verify that source is a Folder
            if (m.isFolder(source)) {
                // is folder
            } else if (source.startsWith("urn:uuid:")) {
                // may be folder
                er.externalChallenge(source + " must be shown to be a Folder already in the registry");
            } else {
                // is not folder
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, source + " is not a Folder in submission and cannot already be in registry", this, assocsRef);
            }
            // try to verify that target is a DocumentEntry
            if (m.isDocument(target)) {
                // is DocumentEntry
            } else if (target.startsWith("urn:uuid:")) {
                // may be DocumentEntry
                er.externalChallenge(source + " must be shown to be a DocumentEntry already in the registry");
            } else {
                // is not DocumentEntry
                er.err(XdsErrorCode.Code.XDSRegistryMetadataError, source + " is not a DocumentEntry in submission and cannot already be in registry", this, assocsRef);
            }
        } else {
            er.err(XdsErrorCode.Code.XDSRegistryMetadataError, assocTag(assocId) + ": only Folder to DocumentEntry associations can be members of SubmissionSet (linked to SubmissionSet object via HasMember association", this, assocsRef);
        }
    }


}
