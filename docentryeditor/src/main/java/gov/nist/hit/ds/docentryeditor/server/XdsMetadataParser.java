package gov.nist.hit.ds.docentryeditor.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.hit.ds.docentryeditor.client.XdsParserServices;
import gov.nist.hit.ds.docentryeditor.shared.model.*;
import gov.nist.hit.ds.ebMetadata.Metadata;
import gov.nist.hit.ds.ebMetadata.MetadataParser;
import gov.nist.hit.ds.ebMetadata.MetadataSupport;
import gov.nist.hit.ds.utilities.xml.OMFormatter;
import gov.nist.hit.ds.xdsExceptions.MetadataException;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;
import org.apache.axiom.om.OMElement;

import javax.mail.Folder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by onh2 on 2/12/2015.
 */
public class XdsMetadataParser extends RemoteServiceServlet implements XdsParserServices{
    private List<XdsDocumentEntry> docEntries;
    private Metadata m;

    public XdsMetadataParser(){}

    public List<XdsDocumentEntry> getDocEntries(){
        return docEntries;
    }
    public XdsMetadata parseDocEntries(String fileContent){
        docEntries=new ArrayList<XdsDocumentEntry>();
        try {
            m=MetadataParser.parse(fileContent);

            for(OMElement eo : m.getExtrinsicObjects()){
                docEntries.add(parse(eo));
            }
        } catch (XdsInternalException e) {
            e.printStackTrace();
        } catch (MetadataException e) {
            e.printStackTrace();
        }
        XdsMetadata m=new XdsMetadata();
        m.setDocumentEntries(docEntries);
        return m;
    }


    public XdsMetadataParser(Metadata m, String label) {
        this.m = m;

        docEntries = new ArrayList<XdsDocumentEntry>();
        for (OMElement eo : m.getExtrinsicObjects()) {
            XdsDocumentEntry de = new XdsDocumentEntry();

            parse(eo);

            docEntries.add(de);
        }
    }

    String splitLast(String in, String separator) {
        String[] parts = in.split(separator);
        if (parts.length <= 1)
            return in;
        return parts[parts.length - 1];
    }

    private XdsDocumentEntry parse(OMElement ele) {
        Logger.getLogger(this.getClass().getName()).info("Start parsing doc entry");
        XdsDocumentEntry de=new XdsDocumentEntry();
        OMFormatter omf = new OMFormatter(ele);
        omf.noRecurse();
        String eoEleStr = omf.toHtml();

        de.setId(new String256(asString(m.getId(ele))));
        Logger.getLogger(this.getClass().getName()).info(de.getId().toString());
//        de.idX = eoEleStr;
        // not used in my model yet
//        de.lid = asString(m.getLid(ele));
//        de.lidX = eoEleStr;
        // not used in my model yet
//        de.version = asString(m.getVersion(ele));
//        de.versionX = new OMFormatter(MetadataSupport.firstChildWithLocalName(ele, "VersionInfo")).toHtml();

        // not used in my model yet
//        de.status = asString(m.getStatus(ele));
//        de.statusX = eoEleStr;

        // should be a list
//        de.title = asString(m.getNameValue(ele));
//        de.titleX = new OMFormatter(MetadataSupport.firstChildWithLocalName(ele, "Name")).toHtml();
        // should be a list
//        de.comments = asString(m.getDescriptionValue(ele));
//        de.commentsX = new OMFormatter(MetadataSupport.firstChildWithLocalName(ele, "Description")).toHtml();

        // not used in my model yet
//        de.home = asString(m.getHome(ele));
//        de.homeX = eoEleStr;

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
        serviceStartDate.getValues().add(new DTM(formatDate(serviceStartDateString)));
        de.setServiceStartTime(serviceStartDate);
//        de.serviceStartTimeX = new OMFormatter(m.getSlot(ele, "serviceStartTime")).toHtml();

        NameValueDTM serviceStopDate=new NameValueDTM();
        serviceStopDate.getValues().clear();
        String serviceStopDateString=asString(m.getSlotValue(ele, "serviceStopTime", 0));
        while(serviceStopDateString.length()<14){
            serviceStopDateString += "0";
        }
        serviceStopDate.getValues().add(new DTM(formatDate(serviceStopDateString)));
        de.setServiceStopTime(serviceStopDate);
//        de.serviceStopTimeX = new OMFormatter(m.getSlot(ele, "serviceStopTime")).toHtml();

        de.setRepoUId(new OID(new String256(asString(m.getSlotValue(ele, "repositoryUniqueId", 0)))));
//        de.repositoryUniqueIdX = new OMFormatter(m.getSlot(ele, "repositoryUniqueId")).toHtml();

//        Logger.getLogger(this.getClass().getName()).info(asString(m.getSlotValue(ele, "size", 0)));
//        de.getSize().getValues().add(Integer.parseInt(asString(m.getSlotValue(ele, "size", 0))));
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
        creationTime.getValues().add(new DTM(formatDate(creationTimeString)));
        de.setCreationTime(creationTime);
//        de.creationTimeX = new OMFormatter(m.getSlot(ele, "creationTime")).toHtml();


        List<String> schemes = new ArrayList<String>();
        schemes.add(MetadataSupport.XDSDocumentEntry_classCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_confCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_eventCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_formatCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_hcftCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_psCode_uuid);
        schemes.add(MetadataSupport.XDSDocumentEntry_typeCode_uuid);

        Map<String, List<String>> codes = null;
        /*
        try {
            codes = m.getCodesWithDisplayName(ele, schemes);

            de.classCode = codes.get(MetadataSupport.XDSDocumentEntry_classCode_uuid);
//            de.classCodeX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_classCode_uuid);

            de.confCodes = codes.get(MetadataSupport.XDSDocumentEntry_confCode_uuid);
//            de.confCodesX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_confCode_uuid);

            de.eventCodeList = codes.get(MetadataSupport.XDSDocumentEntry_eventCode_uuid);
//            de.eventCodeListX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_eventCode_uuid);

            de.formatCode = codes.get(MetadataSupport.XDSDocumentEntry_formatCode_uuid);
//            de.formatCodeX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_formatCode_uuid);

            de.hcftc = codes.get(MetadataSupport.XDSDocumentEntry_hcftCode_uuid);
//            de.hcftcX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_hcftCode_uuid);

            de.pracSetCode = codes.get(MetadataSupport.XDSDocumentEntry_psCode_uuid);
//            de.pracSetCodeX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_psCode_uuid);

            de.typeCode = codes.get(MetadataSupport.XDSDocumentEntry_typeCode_uuid);
//            de.typeCodeX = formatClassSrc(ele, MetadataSupport.XDSDocumentEntry_typeCode_uuid);

        } catch(Exception e) {}
*/
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
        Logger.getLogger(this.getClass().getName()).info(de.toXML());
        return de;
    }

    private Date formatDate(String sdate) {
        DateFormat lFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date=new Date();
        try {
            date = (Date)lFormatter.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


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

    String asString(String in) {
        if (in == null) return "";
        return in;
    }

    @Override
    public XdsMetadata parseXdsMetadata(String fileContent) {
        Logger.getLogger(this.getClass().getName()).info("Server parse xds");
        docEntries=new ArrayList<XdsDocumentEntry>();
        try {
        Logger.getLogger(this.getClass().getName()).info("before bill parser");
            m=MetadataParser.parse(fileContent);
        Logger.getLogger(this.getClass().getName()).info(m.toString());
        Logger.getLogger(this.getClass().getName()).info("after bill parser");

            for(OMElement eo : m.getExtrinsicObjects()){
        Logger.getLogger(this.getClass().getName()).info("doc entry found");

                docEntries.add(parse(eo));
                Logger.getLogger(this.getClass().getName()).info(eo.toString());
            }
        } catch (XdsInternalException e) {
            Logger.getLogger(this.getClass().getName()).info(e.getMessage());
            e.printStackTrace();
        } catch (MetadataException e) {
            Logger.getLogger(this.getClass().getName()).info(e.getMessage());
            e.printStackTrace();
        }
        XdsMetadata metadata=new XdsMetadata();
        metadata.setDocumentEntries(docEntries);
        return metadata;
    }
}
