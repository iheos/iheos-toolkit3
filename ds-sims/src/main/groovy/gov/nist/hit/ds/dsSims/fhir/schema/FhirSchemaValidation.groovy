package gov.nist.hit.ds.dsSims.fhir.schema

import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import groovy.util.logging.Log4j
import org.apache.xerces.parsers.DOMParser
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXException
/**
 * Created by bmajur on 11/18/14.
 */
@Log4j
public class FhirSchemaValidation {

    // empty string as result means no errors
    static public void run(String metadata, File localSchema, boolean includesFeed, ErrorHandler eHandler)  {
        DOMParser p;
        log.debug("Local Schema to be found at " + localSchema);

        // Decode schema location
        String schemaLocation = 'http://hl7.org/fhir ' + localSchema + '/fhir/fhir-all.xsd' +
                ' http://www.w3.org/2005/Atom ' + localSchema + '/fhir/fhir-atom.xsd'

        // build parse to do schema validation
        try {
            p=new DOMParser();
        } catch (Exception e) {
            throw new ToolkitRuntimeException("DOMParser failed: " + e.getMessage());
        }
        try {
            p.setFeature( "http://xml.org/sax/features/validation", true );
            p.setFeature("http://apache.org/xml/features/validation/schema", true);
            p.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", schemaLocation);
            p.setErrorHandler(eHandler);
        } catch (SAXException e) {
            throw new ToolkitRuntimeException("SchemaValidation: error in setting up parser property: SAXException thrown with message: "
                    + e.getMessage());
        }

        // testRun parser and collect parser and schema errors
        try {
            // translate urn:uuid: to urn_uuid_ since the colons really screw up schema stuff
//            String metadata2 = metadata.replaceAll("urn:uuid:", "urn_uuid_");
            InputSource is = new InputSource(new StringReader(metadata));
            p.parse(is);
        } catch (SAXException e) {
            if (e.exception != null && e.exception instanceof SoapFaultException)
                throw e.exception
            throw e
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
