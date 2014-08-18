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
}
