package gov.nist.hit.ds.soapSupport.core

import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.xml.XmlUtil
import org.apache.axiom.om.OMElement
/**
 * Generate and client a SOAP Fault.
 * TODO: comply with reference: http://www.w3.org/TR/2005/CR-ws-addr-soap-20050817
 * TODO: comply with coding of FaultTo
 * @author bill
 *
 */
public class SoapFault {
	SoapEnvironment soapEnv;
	SoapFaultException e;
	
	/**
	 * Create SOAP fault.
	 * Most simple usage:
	 *    new SoapFault(SoapEnvironment, FaultCodes, String reason).client();
	 * @param soapEnv - SoapEnvironment describing return route.
	 * @param code = FaultCodes
	 * @param faultString - reason string
	 */
	public SoapFault(SoapEnvironment soapEnv, SoapFaultException e) {
		this.soapEnv = soapEnv;
		this.e = e;
        if (soapEnv != null)
		    soapEnv.setResponseAction("http://www.w3.org/2005/08/addressing/fault");
	}

//    String asXml() {
//        def writer = new StringWriter()
//        def xml = new MarkupBuilder(writer)
//
//
//
//        return writer.toString()
//    }
	
	OMElement getXML() {
		OMElement root = XmlUtil.om_factory.createOMElement(SoapUtil.fault_qnamens);

		OMElement code = XmlUtil.om_factory.createOMElement(SoapUtil.fault_code_qnamens);

		OMElement code_value = XmlUtil.om_factory.createOMElement(SoapUtil.fault_value_qnamens);
		code_value.setText(SoapUtil.fault_pre + ":" + e.getFaultCode());
		code.addChild(code_value);
		root.addChild(code);

		OMElement reason = XmlUtil.om_factory.createOMElement(SoapUtil.fault_reason_qnamens);
		OMElement text = XmlUtil.om_factory.createOMElement(SoapUtil.fault_text_qnamens);
		text.addAttribute("lang", "en", XmlUtil.xml_namespace);
		text.setText(e.getFaultCode() + ": " + e.getFaultString());
		reason.addChild(text);
		root.addChild(reason);
		
		return root;
	}
	
}
