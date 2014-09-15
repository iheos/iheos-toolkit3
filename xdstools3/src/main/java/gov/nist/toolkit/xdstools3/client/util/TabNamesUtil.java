package gov.nist.toolkit.xdstools3.client.util;

/** Stores static variables & variables common to the application. This is a singleton.
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

    public static final String findDocumentsTabCode = "FIND_DOCUMENTS";
    public static final String mpqFindDocumentsTabCode = "MPQ_FIND_DOCUMENTS";
    public static final String messageValidatorTabCode = "MESSAGE_VALIDATOR";
    public static final String adminTabCode = "ADMIN";
    public static final String endpointsTabCode = "ENDPOINTS";
    public static final String preConnectathonTestsTabCode = "PRE_CONNECTATHON";
    public static final String documentMetadataEditorTabCode = "METADATA_EDITOR";
    public static final String getDocumentsCode = "GET_DOCUMENTS";
    public static final String v2TabCode = "v2_TAB";
    public static final String findFoldersCode = "FIND_FOLDERS";
    public static final String getFoldersCode = "GET_FOLDERS";
    private static final String getFolderAndContentsCode="GET_FOLDER_AND_CONTENTS";
    public static final String retrieveDocumentCode = "RETRIEVE_DOCUMENT";
    public static final String getSubmissionSetAndContentsCode = "GET_SUB_SET_AND_CONTENTS";
    public static final String getRelatedDocuments = "GET_RELATED_DOCUMENTS";
    public static final String sourceStoresDocumentValidationCode = "SOURCE_STORES_DOC_VALIDATION";
    public static final String registerAndQueryTabCode= "REGISTER_AND_QUERY";
    public static final String lifecycleValidationTabCode = "LIFECYCLE_VALIDATION";
    public static final String folderValidationTabCode = "FOLDER_VALIDATION";
    public static final String submitRetrieveTabCode="SUBMIT_RETRIEVE";


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
    public static String getGetDocumentsCode() { return getDocumentsCode; }
    public static String getFindFoldersCode() { return findFoldersCode; }
    public static String getGetFoldersCode() { return getFoldersCode; }
    public static String getGetFoldersAndContentsCode() {return getFolderAndContentsCode;}
    public static String getRetrieveDocumentCode() { return retrieveDocumentCode; }
    public static String getGetSubmissionSetAndContentsCode(){return getSubmissionSetAndContentsCode;}
    public static String getGetRelatedDocumentsCode(){return getRelatedDocuments;}
    public static String getSourceStoresDocumentValidationCode(){return sourceStoresDocumentValidationCode;}
    public static String getRegisterAndQueryTabCode() { return registerAndQueryTabCode;}
    public static String getLifecycleValidationTabCode() {return lifecycleValidationTabCode;}
    public static String getFolderValidationTabCode() {return folderValidationTabCode;}
    public static String getSubmitRetrieveTabCode() { return submitRetrieveTabCode; }
     public static String getv2TabCode() { return v2TabCode; }
}
