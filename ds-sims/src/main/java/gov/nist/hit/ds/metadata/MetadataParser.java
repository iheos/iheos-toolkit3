package gov.nist.hit.ds.metadata;

import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.MetadataValidationException;
import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.utilities.xml.Util;
import org.apache.axiom.om.OMElement;

import javax.xml.parsers.FactoryConfigurationError;
import java.io.File;

public class MetadataParser {

	public MetadataParser() {
	}

	static public Metadata parseNonSubmission(OMElement e) throws MetadataException, MetadataValidationException {
		return parseNonSubmission(e, false);
	}

	static public Metadata parseNonSubmission(String s) throws MetadataException, MetadataValidationException, XdsInternalException, FactoryConfigurationError {
		return parseNonSubmission(Util.parse_xml(s), false);
	}

	static public Metadata parseNonSubmission(OMElement e, boolean rm_duplicates) throws MetadataException, MetadataValidationException {
		Metadata m = new Metadata();

		m.setGrokMetadata(false);

		if (e != null) {
			m.setMetadata(e);

			m.runParser(rm_duplicates);
		}

		return m;
	}
	
	static public Metadata parseObject(OMElement e) throws MetadataException {
		Metadata m = new Metadata();
		
		m.setGrokMetadata(false);
				
		m.parseObject(e);
		
		return m;
	}


	static public Metadata parseNonSubmission(File metadata_file) throws MetadataException, MetadataValidationException, XdsInternalException {

		return parseNonSubmission(Util.parse_xml(metadata_file));

	}
	
	static public Metadata noParse(OMElement e) {
		Metadata m = new Metadata();

		m.setGrokMetadata(false);

		if (e != null) {
			m.setMetadata(e);

		}
		return m;
	}

	static public Metadata noParse(File metadata_file) throws MetadataException,XdsInternalException  {
		return noParse(Util.parse_xml(metadata_file));
	}
	
	static public Metadata parse(OMElement e)  throws MetadataException,XdsInternalException, MetadataValidationException {
		return new Metadata(e);
	}
}
