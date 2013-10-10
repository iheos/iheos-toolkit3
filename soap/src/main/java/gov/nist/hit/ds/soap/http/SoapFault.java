package gov.nist.hit.ds.soap.http;

import gov.nist.hit.ds.utilities.xml.XmlUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;

public class SoapFault {
	String faultCode = null;
	String faultReason = null;
	List<String> details = new ArrayList<String>();

	public enum FaultCodes { VersionMismatch, MustUnderstand, DataEncodingUnknown, Sender, Receiver };

	public SoapFault(FaultCodes code, String reason) {
		faultCode = getCodeString(code);
		faultReason = reason;
	}
	
	public void addDetail(String adetail) {
		details.add(adetail);
	}

	String getCodeString(FaultCodes code) {
		switch (code) {
		case VersionMismatch:
			return "VersionMismatch";
		case MustUnderstand:
			return "MustUnderstand";
		case DataEncodingUnknown:
			return "DataEncodingUnknown";
		case Sender:
			return "Sender";
		case Receiver:
			return "Receiver";
		}

		return "Unknown";
	}

	public OMElement getXML() {
		OMElement root = XmlUtil.om_factory.createOMElement(XmlUtil.fault_qnamens);

		OMElement code = XmlUtil.om_factory.createOMElement(XmlUtil.fault_code_qnamens);

		OMElement code_value = XmlUtil.om_factory.createOMElement(XmlUtil.fault_value_qnamens);
		code_value.setText(XmlUtil.fault_pre + ":" + faultCode);
		code.addChild(code_value);
		root.addChild(code);

		OMElement reason = XmlUtil.om_factory.createOMElement(XmlUtil.fault_reason_qnamens);
		OMElement text = XmlUtil.om_factory.createOMElement(XmlUtil.fault_text_qnamens);
		text.addAttribute("lang", "en", XmlUtil.xml_namespace);
		text.setText(faultReason + details);
		reason.addChild(text);
		root.addChild(reason);

//		if (details.size() > 0) {
//			OMElement detail = XmlUtil.om_factory.createOMElement(MetadataSupport.fault_detail_qnamens);
//			
//			for (String d : details) {
//				OMElement r = XmlUtil.om_factory.createOMElement("Nit", null);
//				r.setText(d);
//				detail.addChild(r);
//			}
//			
//			root.addChild(detail);
//		}
		
		return root;
	}
}
