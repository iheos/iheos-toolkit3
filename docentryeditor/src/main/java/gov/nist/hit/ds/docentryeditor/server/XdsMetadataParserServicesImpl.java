package gov.nist.hit.ds.docentryeditor.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.hit.ds.docentryeditor.client.parser.XdsParserServices;
import gov.nist.hit.ds.docentryeditor.shared.model.*;
import gov.nist.hit.ds.ebMetadata.Metadata;
import gov.nist.hit.ds.ebMetadata.MetadataParser;
import gov.nist.hit.ds.ebMetadata.MetadataSupport;
import gov.nist.hit.ds.utilities.xml.OMFormatter;
import gov.nist.hit.ds.xdsExceptions.MetadataException;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;
import org.apache.axiom.om.OMElement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class is a parser meant to generate a XdsMetadata object from an ebRim Metadata
 * XML file.
 *
 * @See XdsMetadata
 */
public class XdsMetadataParserServicesImpl extends RemoteServiceServlet implements XdsParserServices{
    private Metadata m;
    private XdsMetadata xdsMetadata;

    private List<String> schemes = new ArrayList<String>();
    private Map<String, List<String>> codes = null;

    public XdsMetadataParserServicesImpl(){
        schemes.add(MetadataSupport.XDSDocumentEntry_classCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_confCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_eventCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_formatCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_hcftCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_psCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_typeCode_uuid);
        schemes.add(MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid);
    }

    /**
     * Method that returns a complete XdsMetadata object containing
     * all the metadata information extracted from the given XML
     * ebRim Metadata document.
     * @param fileContent ebRim document XML.
     * @return XdsMetadataObject
     */
    @Override
    public XdsMetadata parseXdsMetadata(String fileContent) {
        xdsMetadata=new XdsMetadata();
        //docEntries=new ArrayList<XdsDocumentEntry>();
        try {
            m=MetadataParser.parse(fileContent);
            for(OMElement oe : m.getRegistryPackages()){
                xdsMetadata.setSubmissionSet(parseSubmissionSet(oe));
            }
            for(OMElement eo : m.getExtrinsicObjects()){
                xdsMetadata.getDocumentEntries().add(parseDocumentEntry(eo));
            }
        } catch (XdsInternalException e) {
            Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
            e.printStackTrace();
        } catch (MetadataException e) {
            Logger.getLogger(this.getClass().getName()).warning(e.getMessage());
            e.printStackTrace();
        }
        return xdsMetadata;
    }

    private XdsSubmissionSet parseSubmissionSet(OMElement oe) {
        XdsSubmissionSet subSet=new XdsSubmissionSet();
        OMFormatter omf=new OMFormatter(oe);
        omf.noRecurse();

        subSet.setEntryUUID(new String256(m.getSubmissionSetId()));
        String status = m.getStatus(oe);
        if(status!= null) {
            subSet.setAvailabilityStatus(new String256(status));
        }
        String home=m.getHome(oe);
        if (home!=null){
            subSet.setHomeCommunityId(new String256(home));
        }
        NameValueDTM submissionTime=new NameValueDTM();
        submissionTime.getValues().clear();
        String submissionTimeString=asString(m.getSlotValue(oe, "submissionTime", 0));
        while(submissionTimeString.length()<14){
            submissionTimeString += "0";
        }
        submissionTime.getValues().add(new DTM(parserDate(submissionTimeString)));
        subSet.setSubmissionTime(submissionTime);
        for(String s:m.getSlotValues(oe, "intendedRecipient")){
            subSet.getIntendedRecipient().getValues().add(new String256(s));
        }
        try {
            String patientId=m.getSubmissionSetPatientId();
            if (patientId!=null) {
                subSet.setPatientId(new IdentifierString256(new String256(patientId), new String256(MetadataSupport.XDSSubmissionSet_patientid_uuid)));
            }
            subSet.setUniqueId(new IdentifierOID(new OID(new String256(m.getSubmissionSetUniqueId())),new String256(MetadataSupport.XDSSubmissionSet_uniqueid_uuid)));
            subSet.setSourceId(new IdentifierOID(new OID(new String256(m.getSubmissionSetSourceId(oe))),new String256(MetadataSupport.XDSSubmissionSet_sourceid_uuid)));
            codes = m.getCodesWithDisplayName(oe, schemes);
            String[] contentTypeCode = codes.get(MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid).get(0).split("\\^");
            subSet.setContentTypeCode(new CodedTerm(contentTypeCode[0],contentTypeCode[1],contentTypeCode[2]));
            List<OMElement> authorClassifications = m.getClassifications(oe, MetadataSupport.XDSSubmissionSet_author_uuid);
            subSet.setAuthors(parseAuthors(authorClassifications));
        } catch (MetadataException e) {
            e.printStackTrace();
        } catch (Exception e) {e.printStackTrace();}
        Logger.getLogger(this.getClass().getName()).info(subSet.toString());
        return subSet;
    }

    /**
     * Method that parses an ebRim ExtrinsicObject from an ebRim Metadata document
     * to create a XdsDocumentEntry object.
     * @param ele ebRim ExtrinsicObject from a Metadata document.
     * @return XdsDocumentEntry
     */
    private XdsDocumentEntry parseDocumentEntry(OMElement ele) {
        XdsDocumentEntry de=new XdsDocumentEntry();
        OMFormatter omf = new OMFormatter(ele);
        omf.noRecurse();

        de.setId(new String256(asString(m.getId(ele))));
//        de.idX = eoEleStr;
        de.setLogicalId(new String256(asString(m.getLid(ele))));
//        de.lidX = eoEleStr;

        de.setVersion(new String256(asString(m.getVersion(ele))));
//        de.versionX = new OMFormatter(MetadataSupport.firstChildWithLocalName(ele, "VersionInfo")).toHtml();

        de.setAvailabilityStatus(new String256(asString(m.getStatus(ele))));
//        de.statusX = eoEleStr;

        de.setHomeCommunityId(new String256(asString(m.getHome(ele))));
//        de.homeX = eoEleStr;

        // FIXME should be a list
//        de.title = asString(m.getNameValue(ele));
//        de.titleX = new OMFormatter(MetadataSupport.firstChildWithLocalName(ele, "Name")).toHtml();
        //FIXME should be a list
//        de.comments = asString(m.getDescriptionValue(ele));
//        de.commentsX = new OMFormatter(MetadataSupport.firstChildWithLocalName(ele, "Description")).toHtml();

        de.setMimeType(new String256(asString(m.getMimeType(ele))));
//        de.mimeTypeX = eoEleStr;

        de.setHash(new String256(asString(m.getSlotValue(ele, "hash", 0))));
//        de.hashX = new OMFormatter(m.getSlot(ele, "hash")).toHtml();

        de.setLanguageCode(LanguageCode.getValueOf(asString(m.getSlotValue(ele, "languageCode", 0))));
//        de.langX = new OMFormatter(m.getSlot(ele, "languageCode")).toHtml();

        de.getLegalAuthenticator().getValues().add(new String256(asString(m.getSlotValue(ele, "legalAuthenticator", 0))));
//        de.legalAuthX = new OMFormatter(m.getSlot(ele, "legalAuthenticator")).toHtml();

        NameValueDTM serviceStartDate=new NameValueDTM();
        serviceStartDate.getValues().clear();
        String serviceStartDateString=asString(m.getSlotValue(ele, "serviceStartTime", 0));
        while(serviceStartDateString.length()<14){
            serviceStartDateString += "0";
        }
        serviceStartDate.getValues().add(new DTM(parserDate(serviceStartDateString)));
        de.setServiceStartTime(serviceStartDate);
//        de.serviceStartTimeX = new OMFormatter(m.getSlot(ele, "serviceStartTime")).toHtml();

        NameValueDTM serviceStopDate=new NameValueDTM();
        serviceStopDate.getValues().clear();
        String serviceStopDateString=asString(m.getSlotValue(ele, "serviceStopTime", 0));
        while(serviceStopDateString.length()<14){
            serviceStopDateString += "0";
        }
        serviceStopDate.getValues().add(new DTM(parserDate(serviceStopDateString)));
        de.setServiceStopTime(serviceStopDate);
//        de.serviceStopTimeX = new OMFormatter(m.getSlot(ele, "serviceStopTime")).toHtml();

        de.setRepoUId(new OID(new String256(asString(m.getSlotValue(ele, "repositoryUniqueId", 0)))));
        de.setUri(new String256(asString(m.getSlotValue(ele, "URI", 0))));
//        de.repositoryUniqueIdX = new OMFormatter(m.getSlot(ele, "repositoryUniqueId")).toHtml();

        String sizeString=asString(m.getSlotValue(ele, "size", 0));
        if (sizeString!=null&&!sizeString.equals("")) {
            de.getSize().getValues().clear();
            de.getSize().getValues().add(Integer.parseInt(sizeString));
        }
//        de.sizeX = new OMFormatter(m.getSlot(ele, "size")).toHtml();

//        parseExtra(de, ele);

        try {
            de.setPatientID(new IdentifierString256(new String256(asString(m.getPatientId(ele))),new String256("urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446")));
//            de.patientIdX = new OMFormatter(m.getExternalIdentifierElement(de.id, MetadataSupport.XDSXdsDocumentEntry_patientid_uuid)).toHtml();
            de.setUniqueId(new IdentifierOID(new OID(new String256(asString(m.getUniqueIdValue(ele)))),new String256("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab")));
//            de.uniqueIdX = new OMFormatter(m.getExternalIdentifierElement(de.id, MetadataSupport.XDSXdsDocumentEntry_uniqueid_uuid)).toHtml();
        } catch (MetadataException e) {
            e.printStackTrace();
        }
        de.getSourcePatientId().getValues().add(new String256(asString(m.getSlotValue(ele, "sourcePatientId", 0))));
//        de.sourcePatientIdX = new OMFormatter(m.getSlot(ele, "sourcePatientId")).toHtml();

        NameValueDTM creationTime=new NameValueDTM();
        creationTime.getValues().clear();
        String creationTimeString =asString(m.getSlotValue(ele, "creationTime", 0));
        while(creationTimeString.length()<14){
            creationTimeString+="0";
        }
        creationTime.getValues().add(new DTM(parserDate(creationTimeString)));
        de.setCreationTime(creationTime);
//        de.creationTimeX = new OMFormatter(m.getSlot(ele, "creationTime")).toHtml();

        try {
            codes = m.getCodesWithDisplayName(ele, schemes);

            String[] classCodeStrings = codes.get(MetadataSupport.XDSDocumentEntry_classCode_uuid).get(0).split("\\^");
            de.setClassCode(new CodedTerm(classCodeStrings[0],classCodeStrings[1],classCodeStrings[2]));
//            de.classCode = codes.get(MetadataSupport.XDSDocumentEntry_classCode_uuid);
//            de.classCodeX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_classCode_uuid);

            String[] formatCodeStrings = codes.get(MetadataSupport.XDSDocumentEntry_formatCode_uuid).get(0).split("\\^");
            de.setFormatCode(new CodedTerm(formatCodeStrings[0], formatCodeStrings[1], formatCodeStrings[2]));
//            de.formatCode = codes.get(MetadataSupport.XDSDocumentEntry_formatCode_uuid);
//            de.formatCodeX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_formatCode_uuid);

            String[] hcftCodeStrings = codes.get(MetadataSupport.XDSDocumentEntry_hcftCode_uuid).get(0).split("\\^");
            de.setHealthcareFacilityType(new CodedTerm(hcftCodeStrings[0], hcftCodeStrings[1], hcftCodeStrings[2]));
//            de.hcftc = codes.get(MetadataSupport.XDSDocumentEntry_hcftCode_uuid);
//            de.hcftcX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_hcftCode_uuid);

            String[] practiceSettingCodeStrings = codes.get(MetadataSupport.XDSDocumentEntry_psCode_uuid).get(0).split("\\^");
            de.setPracticeSettingCode(new CodedTerm(practiceSettingCodeStrings[0], practiceSettingCodeStrings[1], practiceSettingCodeStrings[2]));
//            de.pracSetCode = codes.get(MetadataSupport.XDSDocumentEntry_psCode_uuid);
//            de.pracSetCodeX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_psCode_uuid);

            String[] typeCodeStrings = codes.get(MetadataSupport.XDSDocumentEntry_typeCode_uuid).get(0).split("\\^");
            de.setTypeCode(new CodedTerm(typeCodeStrings[0], typeCodeStrings[1], typeCodeStrings[2]));
//            de.typeCode = codes.get(MetadataSupport.XDSDocumentEntry_typeCode_uuid);
//            de.typeCodeX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_typeCode_uuid);

            List<CodedTerm> confidentialityCodes=new ArrayList<CodedTerm>();
            for (String confCode:codes.get(MetadataSupport.XDSDocumentEntry_confCode_uuid)){
                String[] confCodeStrings=confCode.split("\\^");
                confidentialityCodes.add(new CodedTerm(confCodeStrings[0],confCodeStrings[1],confCodeStrings[2]));
            }
            de.setConfidentialityCodes(confidentialityCodes);

            List<CodedTerm> eventCodes=new ArrayList<CodedTerm>();
            for(String eventCodeString:codes.get(MetadataSupport.XDSDocumentEntry_eventCode_uuid)){
                String[] eventCodeStrings=eventCodeString.split("\\^");
                eventCodes.add(new CodedTerm(eventCodeStrings[0],eventCodeStrings[1],eventCodeStrings[2]));
            }
            de.setEventCode(eventCodes);
//            de.eventCodeList = codes.get(MetadataSupport.XDSDocumentEntry_eventCode_uuid);
//            de.eventCodeListX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_eventCode_uuid);

        } catch(Exception e) {}

        try {
            List<OMElement> authorClassifications = m.getClassifications(ele, MetadataSupport.XDSDocumentEntry_author_uuid);
            de.setAuthors(parseAuthors(authorClassifications));
//            de.authorsX = new ArrayList<String>();
            for (OMElement auEle : authorClassifications) {
//                de.authorsX.add(new OMFormatter(auEle).toHtml());
            }
        } catch (Exception e) {}

        for(String s:m.getSlotValues(ele, "sourcePatientInfo")){
            de.getSourcePatientInfo().getValues().add(new String256(s));
        }
//        de.sourcePatientInfoX = new OMFormatter(m.getSlot(ele, "sourcePatientInfo")).toHtml();
        return de;
    }

    /**
     * Method that parses a list of ebRim author classification
     * to return a list of Author java objects.
     * @param authorClassifications list of ebRim author classification.
     * @return a list of Author java objects.
     */
    List<Author> parseAuthors(List<OMElement> authorClassifications) {
        List<Author> authors = new ArrayList<Author>();

        for (OMElement authorClas : authorClassifications) {
            String name = m.getSlotValue(authorClas, "authorPerson", 0);
            List<String> institutions = m.getSlotValues(authorClas, "authorInstitution");
            List<String> roles = m.getSlotValues(authorClas, "authorRole");
            List<String> specialties = m.getSlotValues(authorClas, "authorSpecialty");
            Author a = new Author();
            a.setAuthorPerson(new String256(name));
            for (String s:institutions)
                a.getAuthorInstitutions().add(new String256(s));
            for (String s:roles)
                a.getAuthorRoles().add(new String256(s));
            for (String s:specialties)
                a.getAuthorSpecialties().add(new String256(s));
            // FIXME telecommunication is missing
            authors.add(a);
        }

        return authors;
    }

    public String toEbRim(XdsMetadata metadata){
        Metadata m=new Metadata();
        // Submission Set
        // FIXME authors, comments, titles are missing
        XdsSubmissionSet subSet=metadata.getSubmissionSet();
        OMElement regPackage=m.mkSubmissionSet(subSet.getEntryUUID().toString());
        m.addSubmissionSetPatientId(regPackage,subSet.getPatientId().toString());
        m.addSubmissionSetUniqueId(regPackage,subSet.getUniqueId().toString());
        m.addSlot(regPackage,"submissionTime",formatDate(subSet.getSubmissionTime().getValues().get(0).getDtm()));
        m.addExtClassification(regPackage,MetadataSupport.XDSSubmissionSet_contentTypeCode_uuid,
                subSet.getContentTypeCode().getCodingScheme().toString(),
                subSet.getContentTypeCode().getDisplayName().toString(),
                subSet.getContentTypeCode().getCode().toString());
        m.addExternalId(regPackage,MetadataSupport.XDSSubmissionSet_sourceid_uuid,subSet.getSourceId().toString(),"sourceId");
        if (subSet.getHomeCommunityId()!=null && !subSet.getHomeCommunityId().toString().equals("")){
            m.setHome(regPackage,subSet.getHomeCommunityId().toString());
        }
        if (subSet.getAvailabilityStatus()!=null && !subSet.getAvailabilityStatus().toString().equals("")){
            m.setStatus(regPackage,subSet.getAvailabilityStatus().toString());
        }
        if (!subSet.getIntendedRecipient().getValues().isEmpty()) {
            OMElement intendedRecipient = m.addSlot(regPackage, "intendedRecipient");
            for (String256 r : subSet.getIntendedRecipient().getValues()) {
                m.addSlotValue(intendedRecipient, r.toString());
            }
        }
        // DocEntry
        // FIXME authors, titles and comments are missing
        XdsDocumentEntry documentEntry=metadata.getDocumentEntries().get(0);
        OMElement extObj=m.mkExtrinsicObject(documentEntry.getId().toString(),documentEntry.getMimeType().toString());
        m.addDocumentEntryPatientId(extObj, documentEntry.getPatientID().getValue().toString());
        m.addDocumentEntryUniqueId(extObj, documentEntry.getUniqueId().getValue().toString());
        m.addSlot(extObj,"languageCode",documentEntry.getLanguageCode().toString());
        m.addExtClassification(extObj, MetadataSupport.XDSDocumentEntry_classCode_uuid, documentEntry.getClassCode().getCodingScheme().toString(), documentEntry.getClassCode().getDisplayName().toString(), documentEntry.getClassCode().getCode().toString());
        m.addExtClassification(extObj, MetadataSupport.XDSDocumentEntry_formatCode_uuid, documentEntry.getFormatCode().getCodingScheme().toString(), documentEntry.getFormatCode().getDisplayName().toString(), documentEntry.getFormatCode().getCode().toString());
        m.addExtClassification(extObj, MetadataSupport.XDSDocumentEntry_hcftCode_uuid, documentEntry.getHealthcareFacilityType().getCodingScheme().toString(), documentEntry.getHealthcareFacilityType().getDisplayName().toString(), documentEntry.getHealthcareFacilityType().getCode().toString());
        m.addExtClassification(extObj, MetadataSupport.XDSDocumentEntry_typeCode_uuid, documentEntry.getTypeCode().getCodingScheme().toString(), documentEntry.getTypeCode().getDisplayName().toString(), documentEntry.getTypeCode().getCode().toString());
        m.addExtClassification(extObj, MetadataSupport.XDSDocumentEntry_psCode_uuid, documentEntry.getPracticeSettingCode().getCodingScheme().toString(), documentEntry.getPracticeSettingCode().getDisplayName().toString(), documentEntry.getPracticeSettingCode().getCode().toString());
        if (documentEntry.getCreationTime().getValues().get(0) != null) {
            m.addSlot(extObj,"creationTime",formatDate(documentEntry.getCreationTime().getValues().get(0).getDtm()));
        }
        if (documentEntry.getHash()!=null && !documentEntry.getHash().toString().equals("")) {
            m.addSlot(extObj, "hash", documentEntry.getHash().toString());
        }
        if(documentEntry.getSize().getValues().get(0)!=null) {
            m.addSlot(extObj, "size", documentEntry.getSize().getValues().get(0).toString());
        }
        if (documentEntry.getRepoUId()!=null && !documentEntry.getRepoUId().toString().equals("")) {
            m.addSlot(extObj, "repositoryUniqueId", documentEntry.getRepoUId().toString());
        }
        if (documentEntry.getUri()!=null && !documentEntry.getUri().toString().equals("")) {
            m.addSlot(extObj, "URI", documentEntry.getUri().toString());
        }
        if (documentEntry.getHomeCommunityId()!=null && !documentEntry.getHomeCommunityId().toString().equals("")){
            m.setHome(extObj,documentEntry.getHomeCommunityId().toString());
        }
        if (documentEntry.getAvailabilityStatus()!=null && !documentEntry.getAvailabilityStatus().toString().equals("")){
            m.setStatus(extObj,documentEntry.getAvailabilityStatus().toString());
        }
        if(documentEntry.getLegalAuthenticator().getValues().get(0)!=null) {
            m.addSlot(extObj, "legalAuthenticator", documentEntry.getLegalAuthenticator().getValues().get(0).toString());
        }
        if (documentEntry.getSourcePatientId().getValues().get(0)!=null) {
            m.addSlot(extObj, "sourcePatientId", documentEntry.getSourcePatientId().getValues().get(0).getString());
        }
        if (!documentEntry.getSourcePatientInfo().getValues().isEmpty()) {
            OMElement sourcePatientInfo = m.addSlot(extObj, "sourcePatientInfo");
            for (String256 value : documentEntry.getSourcePatientInfo().getValues()) {
                m.addSlotValue(sourcePatientInfo, value.toString());
            }
        }
        for(CodedTerm ct:documentEntry.getEventCode()){
            m.addExtClassification(extObj, MetadataSupport.XDSDocumentEntry_eventCode_uuid, ct.getCodingScheme().toString(), ct.getDisplayName().toString(), ct.getCode().toString());
        }
        for(CodedTerm ct:documentEntry.getConfidentialityCodes()){
            m.addExtClassification(extObj, MetadataSupport.XDSDocumentEntry_confCode_uuid, ct.getCodingScheme().toString(), ct.getDisplayName().toString(), ct.getCode().toString());
        }
        if (documentEntry.getServiceStartTime().getValues().get(0)!=null) {
            m.addSlot(extObj, "serviceStartTime", formatDate(documentEntry.getServiceStartTime().getValues().get(0).getDtm()));
        }
        if (documentEntry.getServiceStopTime().getValues().get(0)!=null) {
            m.addSlot(extObj, "serviceStopTime", formatDate(documentEntry.getServiceStopTime().getValues().get(0).getDtm()));
        }
        // FIXME this is not working
        for(Author author:documentEntry.getAuthors()) {
            OMElement authorClassification=m.addIntClassification(extObj, MetadataSupport.XDSDocumentEntry_author_uuid);
            m.addSlot(authorClassification,"authorPerson",author.getAuthorPerson().toString());
            OMElement authorInstitutionClassification=m.addSlot(authorClassification, "authorInstitution");
            for(String256 institution:author.getAuthorInstitutions()) {
                m.addSlotValue(authorInstitutionClassification,institution.toString());
            }
            OMElement authorRoleClassification=m.addSlot(authorClassification,"authorRole");
            for (String256 role:author.getAuthorRoles()) {
                m.addSlotValue(authorRoleClassification,role.toString());
            }
            OMElement authorSpecialtyClassification=m.addSlot(authorClassification,"authorSpecialty");
            for (String256 specialty:author.getAuthorSpecialties()) {
                m.addSlotValue(authorSpecialtyClassification,specialty.toString());
            }
            OMElement authorTelecommunicationClassification=m.addSlot(authorClassification,"authorTelecommunication");
            for (String256 telecommunication:author.getAuthorTelecommunications()) {
                m.addSlotValue(authorTelecommunicationClassification, telecommunication.toString());
            }
        }
        Logger.getLogger(this.getClass().getName()).info(m.format());
//        return m.getExtrinsicObject(0).toString();
        return m.format();
    }

    private String formatDate(Date date){
        DateFormat dFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return dFormatter.format(date);
    }

    private Date parserDate(String sdate) {
        DateFormat lFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date=new Date();
        try {
            date = (Date)lFormatter.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String splitLast(String in, String separator) {
        String[] parts = in.split(separator);
        if (parts.length <= 1)
            return in;
        return parts[parts.length - 1];
    }

    private String asString(String in) {
        if (in == null) return "";
        return in;
    }
}
