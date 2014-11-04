package gov.nist.hit.ds.soapSupport.core

import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.utilities.xml.XmlUtil
import org.apache.axiom.om.OMElement

/**
 * Created by bmajur on 10/1/14.
 */
class SoapFaultGenerator {
    Fault fault
    SoapEnvironment soapEnvironment

    public SoapFaultGenerator(SoapEnvironment soapEnvironment, Fault fault) {
        this.soapEnvironment = soapEnvironment
        this.fault = fault
        soapEnvironment.responseAction = "http://www.w3.org/2005/08/addressing/fault"
    }

    OMElement getXML() {
        OMElement root = XmlUtil.om_factory.createOMElement(SoapUtil.fault_qnamens);

        OMElement code = XmlUtil.om_factory.createOMElement(SoapUtil.fault_code_qnamens);

        OMElement code_value = XmlUtil.om_factory.createOMElement(SoapUtil.fault_value_qnamens);
        code_value.setText(SoapUtil.fault_pre + ":" + fault.faultCode);
        code.addChild(code_value);
        root.addChild(code);

        OMElement reason = XmlUtil.om_factory.createOMElement(SoapUtil.fault_reason_qnamens);
        OMElement text = XmlUtil.om_factory.createOMElement(SoapUtil.fault_text_qnamens);
        text.addAttribute("lang", "en", XmlUtil.xml_namespace);
        text.setText(fault.faultCode + ": " + fault.faultMsg);
        reason.addChild(text);
        root.addChild(reason);

        return root;
    }



}
