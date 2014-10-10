package gov.nist.toolkit.xdstools3.client.util;

/**
 * Stores static variables & variables common to the application. This is a singleton.
 * The values set here are the Tabs codes used in TabSet to identify the different tabs
 * and to set the different TabPlaces tokens for the URLs.
 *
 * URLs will be http://app-main-url#TabPlace:XXXXXX
 *
 * where XXXXXX is a value defined underneath such as HOME, ADMIN, GET_DOCUMENTS
 * or GET_FOLDERS...
 *
 */
public class TabNamesUtil {
    private static TabNamesUtil instance = null;

    public static TabNamesUtil getInstance(){
        if (instance == null) {
            instance = new TabNamesUtil();
        }
        return instance;
    }

    private static final String findDocumentsTabCode = "FIND_DOCUMENTS";
    private static final String mpqFindDocumentsTabCode = "MPQ_FIND_DOCUMENTS";
    private static final String messageValidatorTabCode = "MESSAGE_VALIDATOR";
    private static final String adminTabCode = "ADMIN";
    private static final String endpointsTabCode = "ENDPOINTS";
    private static final String preConnectathonTestsTabCode = "PRE_CONNECTATHON";
    private static final String documentMetadataEditorTabCode = "METADATA_EDITOR";
    private static final String getDocumentsCode = "GET_DOCUMENTS";
    private static final String v2TabCode = "v2_TAB";
    private static final String findFoldersCode = "FIND_FOLDERS";
    private static final String getFoldersCode = "GET_FOLDERS";
    private static final String getFolderAndContentsCode="GET_FOLDER_AND_CONTENTS";
    private static final String retrieveDocumentCode = "RETRIEVE_DOCUMENT";
    private static final String getSubmissionSetAndContentsCode = "GET_SUB_SET_AND_CONTENTS";
    private static final String getRelatedDocuments = "GET_RELATED_DOCUMENTS";
    private static final String sourceStoresDocumentValidationCode = "SOURCE_STORES_DOC_VALIDATION";
    private static final String registerAndQueryTabCode= "REGISTER_AND_QUERY";
    private static final String lifecycleValidationTabCode = "LIFECYCLE_VALIDATION";
    private static final String folderValidationTabCode = "FOLDER_VALIDATION";
    private static final String submitRetrieveTabCode="SUBMIT_RETRIEVE";
    private static final String homeTabCode="HOME";


    public static String getFindDocumentsTabCode() {
        return findDocumentsTabCode;
    }

    public static String getAdminTabCode() {
        return adminTabCode;
    }

    public static String getEndpointsTabCode() {
        return endpointsTabCode;
    }

    public static String getMpqFindDocumentsTabCode() {
        return mpqFindDocumentsTabCode;
    }

    public static String getMessageValidatorTabCode() { return messageValidatorTabCode; }

    public static String getDocumentMetadataEditorTabCode() { return documentMetadataEditorTabCode; }
    public static String getPreConnectathonTestsTabCode() { return preConnectathonTestsTabCode; }
    public static String getGetDocumentsTabCode() { return getDocumentsCode; }
    public static String getFindFoldersCode() { return findFoldersCode; }
    public static String getGetFoldersTabCode() { return getFoldersCode; }
    public static String getGetFoldersAndContentsCode() {return getFolderAndContentsCode;}
    public static String getRetrieveDocumentTabCode() { return retrieveDocumentCode; }
    public static String getGetSubmissionSetAndContentsTabCode(){return getSubmissionSetAndContentsCode;}
    public static String getGetRelatedDocumentsCode(){return getRelatedDocuments;}
    public static String getSourceStoresDocumentValidationCode(){return sourceStoresDocumentValidationCode;}
    public static String getRegisterAndQueryTabCode() { return registerAndQueryTabCode;}
    public static String getLifecycleValidationTabCode() {return lifecycleValidationTabCode;}
    public static String getFolderValidationTabCode() {return folderValidationTabCode;}
    public static String getSubmitRetrieveTabCode() { return submitRetrieveTabCode; }
     public static String getv2TabCode() { return v2TabCode; }

    public static String getHomeTabCode() {
        return homeTabCode;
    }
}
