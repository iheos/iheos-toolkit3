package gov.nist.hit.ds.soap;

import gov.nist.hit.ds.utilities.xml.XmlUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;

/**
 * Generate and send a SOAP Fault.
 * @author bill
 *
 */
public class SoapFault {
	String faultCode = null;
	String faultReason = null;
	List<String> details = new ArrayList<String>();
	SoapEnvironment soapEnv;
	
	public enum FaultCodes { VersionMismatch, MustUnderstand, DataEncodingUnknown, Sender, Receiver };

	/**
	 * Create SOAP fault.
	 * Most simple usage:
	 *    new SoapFault(SoapEnvironment, FaultCodes, String reason).send();
	 * @param soapEnv - SoapEnvironment describing return route.
	 * @param code = FaultCodes
	 * @param reason - reason string
	 */
	public SoapFault(SoapEnvironment soapEnv, FaultCodes code, String reason) {
		this.soapEnv = soapEnv;
		this.faultCode = getCodeString(code);
		this.faultReason = reason;
	}
	
//	public void addDetail(String adetail) {
//		details.add(adetail);
//	}

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

	OMElement getXML() {
		OMElement root = XmlUtil.om_factory.createOMElement(SoapUtil.fault_qnamens);

		OMElement code = XmlUtil.om_factory.createOMElement(SoapUtil.fault_code_qnamens);

		OMElement code_value = XmlUtil.om_factory.createOMElement(SoapUtil.fault_value_qnamens);
		code_value.setText(SoapUtil.fault_pre + ":" + faultCode);
		code.addChild(code_value);
		root.addChild(code);

		OMElement reason = XmlUtil.om_factory.createOMElement(SoapUtil.fault_reason_qnamens);
		OMElement text = XmlUtil.om_factory.createOMElement(SoapUtil.fault_text_qnamens);
		text.addAttribute("lang", "en", XmlUtil.xml_namespace);
		text.setText(faultReason + details);
		reason.addChild(text);
		root.addChild(reason);

//		if (details.size() > 0) {
//			OMElement detail = MetadataSupport.om_factory.createOMElement(MetadataSupport.fault_detail_qnamens);
//			
//			for (String d : details) {
//				OMElement r = MetadataSupport.om_factory.createOMElement("Nit", null);
//				r.setText(d);
//				detail.addChild(r);
//			}
//			
//			root.addChild(detail);
//		}
		
		return root;
	}
	
	public void send() throws Exception {
		SoapResponseGenerator gen = new SoapResponseGenerator(soapEnv, getXML());
		gen.send();
	}


}
