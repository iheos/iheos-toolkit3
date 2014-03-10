package gov.nist.toolkit.valccda;

import gov.nist.hit.ds.xdsException.XdsInternalException;
import gov.nist.toolkit.utilities.xml.Util;
import org.apache.axiom.om.OMElement;

import javax.xml.parsers.FactoryConfigurationError;
import java.io.InputStream;

// does the supplied chunk of XML look like a CDA document?
public class CdaDetector {
	
	public CdaDetector() {}
	
	public boolean isCDA(OMElement ele) {
		if (ele == null)
			return false;
		if (ele.getLocalName().equals("ClinicalDocument"))
			return true;
		return false;
	}
	
	public boolean isCDA(String xml_text) throws XdsInternalException, FactoryConfigurationError {
		OMElement ele = Util.parse_xml(xml_text);
		return isCDA(ele);
	}
	
	public boolean isCDA(InputStream is) throws XdsInternalException, FactoryConfigurationError {
		OMElement ele = Util.parse_xml(is);
		return isCDA(ele);
	}

	public boolean isCDA(byte[] in) throws XdsInternalException, FactoryConfigurationError {
		OMElement ele = Util.parse_xml(new String(in));
		return isCDA(ele);
	}

}
