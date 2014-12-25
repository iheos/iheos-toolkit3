package gov.nist.hit.ds.dsSims.eb.metadataValidator.factory;

import gov.nist.hit.ds.dsSims.eb.metadataValidator.parser.CodesParser;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.codes.AllCodes;
import gov.nist.toolkit.utilities.xml.Util;
import org.apache.axiom.om.OMElement;

import java.io.File;

public class CodesFactory {

	public AllCodes load(File codesFile)  {
		try {
			OMElement rawCodes = Util.parse_xml(codesFile);
			return new CodesParser().parse(rawCodes);
		} catch (Exception e) {
			throw new RuntimeException("Cannot load codes file <" + codesFile + ">.");
		}
	}

}
