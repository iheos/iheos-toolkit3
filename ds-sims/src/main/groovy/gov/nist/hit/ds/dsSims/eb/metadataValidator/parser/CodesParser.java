package gov.nist.hit.ds.dsSims.eb.metadataValidator.parser;

import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.UuidModel;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.codes.AllCodes;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.codes.Code;
import gov.nist.hit.ds.dsSims.eb.metadataValidator.model.codes.Codes;
import org.apache.axiom.om.OMElement;

import javax.xml.namespace.QName;
import java.util.Iterator;

public class CodesParser {
	AllCodes allCodes = new AllCodes();
	static final QName classSchemeQname = new QName("classScheme");
	static final QName codeQname = new QName("code");
	static final QName displayQname = new QName("display");
	static final QName codingSchemeQname = new QName("codingScheme");
	
	public AllCodes parse(OMElement rawCodes) {
		
		Iterator codeTypesIterator = rawCodes.getChildrenWithLocalName("CodeType");
		while (codeTypesIterator.hasNext()) {
			Object obj = codeTypesIterator.next();
			if (!(obj instanceof OMElement)) continue;
			OMElement codeType = (OMElement) obj;
			String classScheme = codeType.getAttributeValue(classSchemeQname);
			if (classScheme == null || classScheme.equals("")) continue;
			
			Codes codes = new Codes(new UuidModel(classScheme));
			allCodes.add(codes);
			parseCodes(codeType, codes);
		}
		return allCodes;
	}
	
	private void parseCodes(OMElement rawCodeType, Codes codes) {
		Iterator codeIterator = rawCodeType.getChildrenWithLocalName("Code");
		while (codeIterator.hasNext()) {
			Object obj = codeIterator.next();
			if (!(obj instanceof OMElement)) continue;
			OMElement rawCode = (OMElement) obj;
			String codeString = rawCode.getAttributeValue(codeQname);
			String displayString = rawCode.getAttributeValue(displayQname);
			String codingSchemeString = rawCode.getAttributeValue(codingSchemeQname);
			
			Code code = new Code(codeString, codingSchemeString, displayString);
			codes.add(code);		
		}
	}
	
	public AllCodes getAllCodes() { return allCodes; }
}
