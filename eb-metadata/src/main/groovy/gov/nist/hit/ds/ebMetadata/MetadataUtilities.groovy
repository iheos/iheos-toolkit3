package gov.nist.hit.ds.ebMetadata

import org.apache.axiom.om.OMElement

import javax.xml.namespace.QName

/**
 *
 * These utilities extend the model class Metadata with useful things
 *
 * Created by bmajur on 2/11/15.
 */
class MetadataUtilities {
    Metadata m

    MetadataUtilities(Metadata _m) { m = _m}

    String assocTag(OMElement obj) { "${simpleAssocType(m.getAssocType(obj))}(" + id(obj) + ")" }
    String assocTag(String id) { "${simpleAssocType(m.getAssocType(object(id)))}(" + id + ")" }

    String assocTypeTag(String type) { "Association(${type})"}
    String assocTypeTag(OMElement assoc) {
        if (assoc.localName == 'Association') assocTypeTag(m.getAssocType(assoc))
        else ''
    }

    String docEntryTag(OMElement obj) { docEntryTag(m.getId(obj)) }
    String docEntryTag(String id) { "DocumentEntry(" + id + ")" }

    String folderTag(OMElement obj) { folderTag(m.getId(obj)) }
    String folderTag(String id) { "Folder(" + id + ")" }

    String ssTag(OMElement obj) { ssTag(m.getId(obj)) }
    String ssTag(String id) { "SubmissionSet(${id})" }


    String attributeValue(OMElement ele, QName qName) { ele.getAttributeValue(qName)}
    String attributeValue(String id, QName qName) { object(id).getAttributeValue(qName)}

    List<OMElement> extrinsicObjects() { m.getExtrinsicObjects() }
    List<String> extrinsicObjectIds() { m.getExtrinsicObjectIds()}

    List<OMElement> docEntries() { extrinsicObjects() }
    List<String> docEntryIds() { extrinsicObjectIds() }

    List<OMElement> folders() { m.getFolders() }
    List<String> folderIds() { m.getFolderIds() }

    List<OMElement> submissionSets() { m.getSubmissionSets() }
    List<String> submissionSetIds() { m.getSubmissionSetIds() }

    List<OMElement> registryPackages() { m.registryPackages }
    List<String> registryPackageIds() { m.registryPackageIds }

    List<OMElement> associations() { m.associations }
    List<String> associationIds() { m.associationIds }

    OMElement object(String id) { m.getObjectById(id) }
    def id(OMElement ele) { ele.getAttributeValue(MetadataSupport.id_qname) }

    boolean isSubmissionSet(String id) {
        if (id == null)
            return false;
        try {
            if (m.getId(m.getSubmissionSet()).equals(id))
                return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    boolean isDocumentEntry(String id) {
        if (id == null)
            return false;
        return m.getExtrinsicObjectIds().contains(id);
    }

    boolean isAssoc(String id) {
        if (id == null)
            return false;
        return m.getAssociationIds().contains(id);
    }

    OMElement getObjectById(String id) {
        try {
            return m.getObjectById(id);
        } catch (Exception e) {
            return null;
        }
    }

    boolean submissionContains(String id) {
        return getObjectById(id) != null;
    }

    boolean isMemberOfSS(String id) {
        String ssid = m.getSubmissionSetId();
        return haveAssoc("HasMember", ssid, id);
    }

    void log_hasmember_usage() {

        infoFound("A HasMember association can be used to do the following:");
        infoFound("  Link the SubmissionSet to a DocumentEntry in the submission (if it has SubmissionSetStatus value of Original)");
        infoFound("  Link the SubmissionSet to a DocumentEntry already in the registry (if it has SubmissionSetStatus value of Reference)");
        infoFound("  Link the SubmissionSet to a Folder in the submission");
        infoFound("  Link the SubmissionSet to a HasMember association that links a Folder to a DocumentEntry.");
        infoFound("    The Folder and the DocumentEntry can be in the submisison or already in the registry");

    }

    boolean haveAssoc(String type, String source, String target) {
        String simpleType = simpleAssocType(type);
        for (OMElement assoc : m.getAssociations()) {
            if (!simpleType.equals(simpleAssocType(m.getAssocType(assoc))))
                continue;
            if (!source.equals(m.getAssocSource(assoc)))
                continue;
            if (!target.equals(m.getAssocTarget(assoc)))
                continue;
            return true;
        }
        return false;
    }

    boolean is_ss_to_de_hasmember(OMElement assoc) {
        String source = m.getAssocSource(assoc);
        String target = m.getAssocTarget(assoc);
        String type = getSimpleAssocType(assoc);

        if (source == null || target == null || type == null)
            return false;

        if (!type.equals("HasMember"))
            return false;

        if (!source.equals(m.getSubmissionSetId()))
            return false;

        if (!m.getExtrinsicObjectIds().contains(target))
            return false;

        if (!is_sss_original(assoc))
            return false;
        return true;
    }

    public boolean is_fol_to_de_hasmember(OMElement assoc) {
        String source = m.getAssocSource(assoc);
        String target = m.getAssocTarget(assoc);
        String type = getSimpleAssocType(assoc);

        if (source == null || target == null || type == null)
            return false;

        if (!type.equals("HasMember"))
            return false;

        if (!m.getFolderIds().contains(source)) {
            if (isUUID(source)) {
                if (rvi != null && !rvi.isFolder(source))
                    return false;
            } else {
                return false;
            }
        }

        if (!m.getExtrinsicObjectIds().contains(target)) {
            if (isUUID(target)) {
                if (rvi != null && !rvi.isDocumentEntry(target))
                    return false;
            } else {
                return false;
            }
        }

        return true;
    }

    boolean is_ss_to_existing_de_hasmember(OMElement assoc) {
        String source = m.getAssocSource(assoc);
        String target = m.getAssocTarget(assoc);
        String type = getSimpleAssocType(assoc);

        if (source == null || target == null || type == null)
            return false;

        if (!type.equals("HasMember"))
            return false;

        if (!source.equals(m.getSubmissionSetId()))
            return false;

        if (submissionContains(target) || !isUUID(target))
            return false;

        if (!is_sss_reference(assoc))
            return false;
        return true;
    }

    boolean is_ss_to_folder_hasmember(OMElement assoc) {
        String source = m.getAssocSource(assoc);
        String target = m.getAssocTarget(assoc);
        String type = getSimpleAssocType(assoc);

        if (source == null || target == null || type == null)
            return false;

        if (!type.equals("HasMember"))
            return false;

        if (!source.equals(m.getSubmissionSetId()))
            return false;

        if (!m.getFolderIds().contains(target))
            return false;

        return true;

    }

    boolean is_ss_to_folder_hasmember_hasmember(OMElement assoc) {
        String source = m.getAssocSource(assoc);
        String target = m.getAssocTarget(assoc);
        String type = getSimpleAssocType(assoc);

        if (source == null || target == null || type == null)
            return false;

        if (!type.equals("HasMember"))
            return false;

        if (!source.equals(m.getSubmissionSetId()))
            return false;

        if (!m.getAssociationIds().contains(target))
            return false;

        // target association - should link a folder and a documententry
        // folder can be in submission or registry
        // same for documententry
        OMElement tassoc = getObjectById(target);

        // both source and target of tassoc have to be uuids and not in submission
        // hopefully in registry

        String ttarget = m.getAssocTarget(tassoc);
        String tsource = m.getAssocSource(tassoc);


        // for both the target and source
        // if points to an object in submission, can be symbolic or uuid
        //     but object must be HasMember Association
        // if points to an object in registry, must be uuid

        if (submissionContains(tsource)) {
            // tsource must be folder
            if (!m.getFolderIds().contains(tsource))
                return false;
        } else {
            // in registry?
            if (isUUID(tsource)) {
                if (rvi != null && !rvi.isFolder(tsource))
                    return false;
            } else {
                return false;
            }
        }

        if (submissionContains(ttarget)) {
            // ttarget must be a DocumentEntry
            if (!m.getExtrinsicObjectIds().contains(ttarget))
                return false;
        } else {
            // in registry?
            if (isUUID(ttarget)) {
                if (rvi != null && !rvi.isDocumentEntry(ttarget))
                    return false;
            } else {
                return false;
            }
        }


        // registry contents validation needed here
        // to show that the tsource references a folder
        // and ttarget references a non-deprecated docentry

        return true;
    }

    boolean isUUID(String id) {
        return id != null && id.startsWith("urn:uuid:");
    }

    String objectType(String id) {
        if (id == null)
            return "null";
        if (m.getSubmissionSetIds().contains(id))
            return "SubmissionSet";
        if (m.getExtrinsicObjectIds().contains(id))
            return "DocumentEntry";
        if (m.getFolderIds().contains(id))
            return "Folder";
        if (m.getAssociationIds().contains(id))
            return "Association";
        return "Unknown";
    }

    String objectDescription(String id) {
        return objectType(id) + "(" + id + ")";
    }

    String objectDescription(OMElement ele) {
        return objectDescription(m.getId(ele));
    }

    String simpleAssocType(String qualifiedType) {
        if (qualifiedType == null)
            return "";
        int i = qualifiedType.lastIndexOf(':');
        if (i == -1)
            return qualifiedType;
        try {
            return qualifiedType.substring(i+1);
        } catch (Exception e) {
            return qualifiedType;
        }
    }

    boolean containsObject(String id) {
        try {
            if (m.containsObject(id))
                return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // does this id represent a folder in this metadata or in registry?
    // ammended to only check current submission since this code is in common
    public boolean isFolder(String id)  {
        if (id == null)
            return false;
        return m.getFolderIds().contains(id);
    }

    String getSimpleAssocType(OMElement a) {
        try {
            return m.getSimpleAssocType(a);
        } catch (Exception e) {
            return "";
        }
    }


    String assoc_type(String type) {
        if (m.isVersion2())
            return type;
        if (type.equals("HasMember"))
            return "urn:oasis:names:tc:ebxml-regrep:AssociationType:" + type;
        if (type.equals("RPLC") ||
                type.equals("XFRM") ||
                type.equals("APND") ||
                type.equals("XFRM_RPLC") ||
                type.equals("signs"))
            return "urn:ihe:iti:2007:AssociationType:" + type;
        return "";
    }

    List<String> getIdsOfReferencedObjects() {
        try {
            return m.getIdsOfReferencedObjects();
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    boolean isReferencedObject(String id) {
        try {
            return m.isReferencedObject(id);
        } catch (Exception e) {
            return false;
        }
    }

    boolean is_sss_original(OMElement assoc) {
        OMElement sss = get_sss_slot(assoc);
        if (sss == null)
            return false;
        String value = m.getSlotValue(assoc, "SubmissionSetStatus", 0);
        if (value == null)
            return false;
        if (value.equals("Original"))
            return true;
        return false;
    }

    boolean is_sss_reference(OMElement assoc) {
        OMElement sss = get_sss_slot(assoc);
        if (sss == null)
            return false;
        String value = m.getSlotValue(assoc, "SubmissionSetStatus", 0);
        if (value == null)
            return false;
        if (value.equals("Reference"))
            return true;
        return false;
    }

    List<String> slotValues(OMElement parent, String slotName) {
        m.getSlotValues(parent, slotName)
    }

    List<OMElement> slotsWithName(OMElement parent, String slotName) {
        m.getSlots(id(parent)).findAll { m.getSlotName(it) == slotName}
    }

    boolean has_sss_slot(OMElement assoc) {
        return get_sss_slot(assoc) != null;
    }

    OMElement get_sss_slot(OMElement assoc) {
        m.getSlot(assoc, "SubmissionSetStatus");
    }

    OMElement find_ss_hasmember_assoc(String target) {
        return find_assoc(m.getSubmissionSetId(), "HasMember", target);
    }

    boolean has_ss_hasmember_assoc(String target) {
        if (find_ss_hasmember_assoc(target) == null)
            return false;
        return true;
    }

    boolean has_assoc(String source, String type, String target) {
        if (find_assoc(source, type, target) == null)
            return false;
        return true;
    }

    List<OMElement> ss_DE_Assocs(OMElement de) { find_assocs(m.getSubmissionSetId(), assoc_type("HasMember"), de.getAttributeValue(MetadataSupport.id_qname))}

    OMElement find_assoc(String source, String type, String target) {
        find_assocs(source, type, target)?.find { true }
    }

    List<OMElement> find_assocs(String source, String type, String target) {
        if (!source || !type || !target) return []

        type = simpleAssocType(type);

        associations().findAll { assoc ->
            String a_target = m.getAssocTarget(assoc)
            String a_type = simpleAssocType(m.getAssocType(assoc))
            String a_source = m.getAssocSource(assoc)

            source == a_source && target == a_target && type == a_type
        }
    }

}
