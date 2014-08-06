package gov.nist.hit.ds.registryMetadataValidator.datatype;

import gov.nist.hit.ds.errorRecording.IAssertionGroup;
import gov.nist.hit.ds.registryMetadataValidator.field.ValidatorCommon;
import gov.nist.hit.ds.utilities.xml.XmlUtil;

import org.apache.axiom.om.OMElement;

public class SourcePatientInfoFormat extends FormatValidator{

	public SourcePatientInfoFormat(IAssertionGroup er, String context,
			String resource) {
		super(er, context, resource);
	}
	
	String xresource = "ITI TF-3: Table 4.1-5: sourcePatientInfo";

	public void validate(OMElement spi_slot) {
		OMElement value_list = XmlUtil.firstChildWithLocalName(spi_slot, "ValueList");
		int valueI = -1;
		for (OMElement value : XmlUtil.childrenWithLocalName(value_list, "Value")) {
			valueI++;
			String content = value.getText();
			if (content == null || content.equals("")) {
				err(context, "Slot sourcePatientInfo has empty Slot value (index " + valueI + 
						")", xresource);
				continue;
			}
			String[] parts = content.split("\\|");
			
			if (parts.length == 0) {
				err(context, twoPartsMsg(valueI), xresource);
				continue;
			}
			if (!parts[0].startsWith("PID-")) {
				err(context, twoPartsMsg(valueI), xresource);
				continue;
			}
			if (parts.length == 1 && parts[0].endsWith("|")) {
				// Example  PID-3|
				continue;
			}
			if (parts.length != 2) {
				err(context, twoPartsMsg(valueI), xresource);
				continue;
			}
			if (parts[0].startsWith("PID-3")) {
				String msg = ValidatorCommon.validate_CX_datatype(parts[1]);
				if (msg != null)
					err(context, "Slot sourcePatientInfo#PID-3 must be valid Patient ID: " + msg, xresource);
			}
		}
	}

	String twoPartsMsg(int valueI) { 
			return
					"Slot sourcePatientInfo Value " + 
					"(index " + valueI + 
					") must have two parts separated by | and the first part must be formatted as PID-x where x is a number";
	}


}
