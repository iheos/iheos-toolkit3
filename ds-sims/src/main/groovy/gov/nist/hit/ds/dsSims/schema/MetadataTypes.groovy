package gov.nist.hit.ds.dsSims.schema;

/**
 * Created by bmajur on 11/18/14.
 */
public class MetadataTypes {
    final public static int METADATA_TYPE_UNKNOWN = 0;
    final public static int METADATA_TYPE_SQ      = 1;
    final public static int METADATA_TYPE_Rb      = 2;
    final public static int METADATA_TYPE_RET     = 3;
    final public static int METADATA_TYPE_PRb     = 4;
    final public static int METADATA_TYPE_REGISTRY_RESPONSE3 = 5;

    static String[] names = new String[11];

    public static String getMetadataTypeName(int type) {
        if (names[0] == null) {
            names[0] = "METADATA_TYPE_UNKNOWN";
            names[1] = "METADATA_TYPE_SQ";
            names[2] = "METADATA_TYPE_Rb";
            names[3] = "METADATA_TYPE_RET";
            names[4] = "METADATA_TYPE_PRb";
            names[5] = "METADATA_TYPE_REGISTRY_RESPONSE3";
        }

        if (type < 7)
            return names[type];
        return "INVALID_TYPE";
    }
}
