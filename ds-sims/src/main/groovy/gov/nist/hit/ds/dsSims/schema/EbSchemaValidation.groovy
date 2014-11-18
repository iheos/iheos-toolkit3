package gov.nist.hit.ds.dsSims.schema
import gov.nist.hit.ds.xdsException.XdsInternalException
import org.apache.xerces.parsers.DOMParser
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXException
/**
 * Created by bmajur on 11/18/14.
 */
public class EbSchemaValidation {

    // empty string as result means no errors
    static public void run(String metadata, int metadataType, File localSchema, ErrorHandler eHandler) throws XdsInternalException {
        DOMParser p;
        System.out.println("Local Schema to be found at " + localSchema);

        // Decode schema location
        String schemaLocation;
        switch (metadataType) {
            case MetadataTypes.METADATA_TYPE_Rb:
                schemaLocation = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0 " + localSchema + "/v3/lcm.xsd";
                break;
            case MetadataTypes.METADATA_TYPE_REGISTRY_RESPONSE3:
                schemaLocation = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0 " + localSchema + "/v3/rs.xsd";
                break;
            case MetadataTypes.METADATA_TYPE_SQ:
                schemaLocation = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 " + localSchema + "/v3/query.xsd " +
                        "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0 " + localSchema + "/v3/rs.xsd";
                break;
            case MetadataTypes.METADATA_TYPE_PRb:
            case MetadataTypes.METADATA_TYPE_RET:
                schemaLocation = "urn:ihe:iti:xds-b:2007 " + localSchema + "/v3/XDS.b_DocumentRepository.xsd " +
                        "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0 " + localSchema + "/v3/rs.xsd";
                break;
            default:
                throw new XdsInternalException("SchemaValidation: invalid metadata type = " + metadataType);
        }

        schemaLocation += " urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0 " + localSchema + "/v3/rim.xsd";

        schemaLocation += " http://schemas.xmlsoap.org/soap/envelope/ " + localSchema + "/v3/soap.xsd";

        schemaLocation += " http://docs.oasis-open.org/wsn/b-2 " + localSchema + "/wsn/b-2.xsd";

        schemaLocation += " http://docs.oasis-open.org/wsn/br-2 " + localSchema + "/wsn/br-2.xsd";

        schemaLocation += " http://docs.oasis-open.org/wsn/t-1 " + localSchema + "/wsn/t-1.xsd";

        // build parse to do schema validation
        try {
            p=new DOMParser();
        } catch (Exception e) {
            throw new XdsInternalException("DOMParser failed: " + e.getMessage());
        }
        try {
            p.setFeature( "http://xml.org/sax/features/validation", true );
            p.setFeature("http://apache.org/xml/features/validation/schema", true);
            p.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", schemaLocation);
            p.setErrorHandler(eHandler);
        } catch (SAXException e) {
            throw new XdsInternalException("SchemaValidation: error in setting up parser property: SAXException thrown with message: "
                    + e.getMessage());
        }

        // testRun parser and collect parser and schema errors
        try {
            // translate urn:uuid: to urn_uuid_ since the colons really screw up schema stuff
            String metadata2 = metadata.replaceAll("urn:uuid:", "urn_uuid_");
            InputSource is = new InputSource(new StringReader(metadata2));
            p.parse(is);
        } catch (Exception e) {
//            throw new XdsInternalException("SchemaValidation: XML parser/Schema validation error: " +
//                    exception_details(e));
        }
    }
    protected static String exception_details(Exception e) {
        if (e == null)
            return "No stack trace available";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);

        return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
    }
}
