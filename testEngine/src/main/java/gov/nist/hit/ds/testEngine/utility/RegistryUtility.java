package gov.nist.hit.ds.testEngine.utility;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registryMetadataValidator.field.MetadataValidator;
import gov.nist.hit.ds.registryMsgFormats.RegistryErrorListGenerator;
import gov.nist.hit.ds.valSupport.client.ValidationContext;
import gov.nist.hit.ds.valSupport.message.SchemaValidation;
import gov.nist.hit.ds.xdsException.SchemaValidationException;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.axiom.om.OMElement;


public class RegistryUtility {

	static public void schema_validate_local(OMElement ahqr, int metadata_type)
	throws XdsInternalException, SchemaValidationException {
		String schema_messages = null;
		try {
			schema_messages = SchemaValidation.validate_local(ahqr, metadata_type);
		} catch (Exception e) {
			throw new XdsInternalException("Schema Validation threw internal error: " + e.getMessage());
		}
		if (schema_messages != null && schema_messages.length() > 0)
			throw new SchemaValidationException("Input did not validate against schema:" + schema_messages);
	}
	
	static public RegistryErrorListGenerator metadata_validator(Metadata m, ValidationContext vc) throws XdsInternalException {
		RegistryErrorListGenerator rel = new RegistryErrorListGenerator();
		MetadataValidator mv = new MetadataValidator(m, vc, null);
		mv.run(rel);
		return rel;
	}

	public static String exception_details(Exception e) {
		if (e == null) 
			return "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
	}
	
	public static String exception_trace(Exception e) {
		if (e == null) 
			return "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		StackTraceElement ste[] = e.getStackTrace();
		ps.print("\n");
		for (int i=0; i<ste.length && i<15; i++)
			ps.print("\t" + ste[i].toString() + "\n");
		//e.printStackTrace(ps);

		return new String(baos.toByteArray());
	}

}
