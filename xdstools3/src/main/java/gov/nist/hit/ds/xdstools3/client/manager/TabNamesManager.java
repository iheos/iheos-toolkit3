package gov.nist.hit.ds.xdstools3.client.manager;

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
public class TabNamesManager {
    private static TabNamesManager instance = null;

    public static TabNamesManager getInstance(){
        if (instance == null) {
            instance = new TabNamesManager();
        }
        return instance;
    }

    private final String findDocumentsTabCode = "FIND_DOCUMENTS";
    private final String findDocumentsV2TabCode = "FIND_DOCUMENTS_V2";
    private final String v2HomeTabCode = "V2_HOME";
    private final String v2DynamicTabCode = "V2_DYNAMIC_TAB";
    private final String mpqFindDocumentsTabCode = "MPQ_FIND_DOCUMENTS";
    private final String messageValidatorTabCode = "MESSAGE_VALIDATOR";
    private final String adminTabCode = "ADMIN";
    private final String endpointsTabCode = "ENDPOINTS";
    private final String preConnectathonTestsTabCode = "PRE_CONNECTATHON";
    private final String documentMetadataEditorTabCode = "METADATA_EDITOR";
    private final String getDocumentsCode = "GET_DOCUMENTS";
    private final String v2TabCode = "v2_TAB";
    private final String findFoldersCode = "FIND_FOLDERS";
    private final String getFoldersCode = "GET_FOLDERS";
    private final String getFolderAndContentsCode="GET_FOLDER_AND_CONTENTS";
    private final String retrieveDocumentCode = "RETRIEVE_DOCUMENT";
    private final String getSubmissionSetAndContentsCode = "GET_SUB_SET_AND_CONTENTS";
    private final String getRelatedDocuments = "GET_RELATED_DOCUMENTS";
    private final String sourceStoresDocumentValidationCode = "SOURCE_STORES_DOC_VALIDATION";
    private final String registerAndQueryTabCode= "REGISTER_AND_QUERY";
    private final String lifecycleValidationTabCode = "LIFECYCLE_VALIDATION";
    private final String folderValidationTabCode = "FOLDER_VALIDATION";
    private final String submitRetrieveTabCode="SUBMIT_RETRIEVE";
    private final String homeTabCode="HOME";
    private final String helpTabCode="HELP";
    private final String mhdValidatorTabCode="MHD_VALIDATOR";
    private final String testDataSubmissionTabCode = "SUBMIT_TEST_DATA";
    private final String mhdToXdsConverterTabCode="MHD_CONVERTER";
    private final String logBrowserTabCode = "LOG_BROWSER";
    private final String contactTabCode = "CONTACT";
    private final String QRSCombinedTabCode = "QRS_COMBINED";



    public String getFindDocumentsTabCode() {
        return findDocumentsTabCode;
    }
    public String getFindDocumentsV2TabCode() {
        return findDocumentsV2TabCode;
    }
    public String getV2HomeTabCode() {return v2HomeTabCode;}
    public String getV2DynamicTabCode() {return v2DynamicTabCode;}
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
    public String getFolderValidationTabCode() {return folderValidationTabCode;}
    public String getSubmitRetrieveTabCode() { return submitRetrieveTabCode; }
    public String getv2TabCode() { return v2TabCode; }
    public String getMHDValidatorTabCode() {return mhdValidatorTabCode;}
    public String getHomeTabCode() {
        return homeTabCode;
    }
    public String getTestDataSubmissionTabCode() {return testDataSubmissionTabCode;}
    public String getMhdtoXdsConverterTabCode() {return mhdToXdsConverterTabCode;}
    public String getLogBrowserTabCode() {return logBrowserTabCode;}
    public String getHelpTabCode() {return helpTabCode;}
    public String getContactTabCode() {return contactTabCode;}
    public String getQRSCombinedTabCode() {return QRSCombinedTabCode;}

}
