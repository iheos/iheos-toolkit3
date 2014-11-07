package gov.nist.toolkit.xdstools3.client.util;

/**
 * Stores   variables & variables common to the application. This is a singleton.
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

    private   final String findDocumentsTabCode = "FIND_DOCUMENTS";
    private   final String mpqFindDocumentsTabCode = "MPQ_FIND_DOCUMENTS";
    private   final String messageValidatorTabCode = "MESSAGE_VALIDATOR";
    private   final String adminTabCode = "ADMIN";
    private   final String endpointsTabCode = "ENDPOINTS";
    private   final String preConnectathonTestsTabCode = "PRE_CONNECTATHON";
    private   final String documentMetadataEditorTabCode = "METADATA_EDITOR";
    private   final String getDocumentsCode = "GET_DOCUMENTS";
    private   final String v2TabCode = "v2_TAB";
    private   final String findFoldersCode = "FIND_FOLDERS";
    private   final String getFoldersCode = "GET_FOLDERS";
    private   final String getFolderAndContentsCode="GET_FOLDER_AND_CONTENTS";
    private   final String retrieveDocumentCode = "RETRIEVE_DOCUMENT";
    private   final String getSubmissionSetAndContentsCode = "GET_SUB_SET_AND_CONTENTS";
    private   final String getRelatedDocuments = "GET_RELATED_DOCUMENTS";
    private   final String sourceStoresDocumentValidationCode = "SOURCE_STORES_DOC_VALIDATION";
    private   final String registerAndQueryTabCode= "REGISTER_AND_QUERY";
    private   final String lifecycleValidationTabCode = "LIFECYCLE_VALIDATION";
    private   final String folderValidationTabCode = "FOLDER_VALIDATION";
    private   final String submitRetrieveTabCode="SUBMIT_RETRIEVE";
    private   final String homeTabCode="HOME";
    private   final String mhdValidatorTabCode="MHD_VALIDATOR";
    private   final String testDataSubmissionTabCode = "SUBMIT_TEST_DATA";


    public String getFindDocumentsTabCode() {
        return findDocumentsTabCode;
    }
    public String getAdminTabCode() {
        return adminTabCode;
    }
    public String getEndpointsTabCode() {
        return endpointsTabCode;
    }
    public String getMpqFindDocumentsTabCode() {
        return mpqFindDocumentsTabCode;
    }
    public String getMessageValidatorTabCode() { return messageValidatorTabCode; }
    public String getDocumentMetadataEditorTabCode() { return documentMetadataEditorTabCode; }
    public String getPreConnectathonTestsTabCode() { return preConnectathonTestsTabCode; }
    public String getGetDocumentsTabCode() { return getDocumentsCode; }
    public String getFindFoldersCode() { return findFoldersCode; }
    public String getGetFoldersTabCode() { return getFoldersCode; }
    public String getGetFoldersAndContentsCode() {return getFolderAndContentsCode;}
    public String getRetrieveDocumentTabCode() { return retrieveDocumentCode; }
    public String getGetSubmissionSetAndContentsTabCode(){return getSubmissionSetAndContentsCode;}
    public String getGetRelatedDocumentsCode(){return getRelatedDocuments;}
    public String getSourceStoresDocumentValidationCode(){return sourceStoresDocumentValidationCode;}
    public String getRegisterAndQueryTabCode() { return registerAndQueryTabCode;}
    public String getLifecycleValidationTabCode() {return lifecycleValidationTabCode;}
    public   String getFolderValidationTabCode() {return folderValidationTabCode;}
    public   String getSubmitRetrieveTabCode() { return submitRetrieveTabCode; }
    public   String getv2TabCode() { return v2TabCode; }
    public   String getMHDValidatorTabCode() {return mhdValidatorTabCode;}
    public   String getHomeTabCode() {
        return homeTabCode;
    }
    public   String getTestDataSubmissionTabCode() {return testDataSubmissionTabCode;}
}
