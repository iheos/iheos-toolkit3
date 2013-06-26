package gov.nist.hit.ds.soap;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;


/**
 * Build a SOAP message.  Must be called in the order: buildSoapEnvelope, 
 * attachSoapHeader, attachSoapBody.
 * @author bill
 *
 */
public class SoapUtil {
	
	static public OMFactory om_factory = OMAbstractFactory.getOMFactory();
	static public String soap_env_uri = "http://www.w3.org/2003/05/soap-envelope";
	static public QName soap_env_qnamens = new QName(soap_env_uri, "Envelope", "S");
	static public QName soap_hdr_qnamens = new QName(soap_env_uri, "Header", "S");
	static public QName soap_body_qnamens = new QName(soap_env_uri, "Body", "S");
	
	static public OMNamespace soap_env_namespace = om_factory.createOMNamespace(soap_env_uri, "s");
	static public String fault_pre = "fault";
	static public QName fault_qnamens = new QName(soap_env_uri, "Fault", fault_pre);
	static public QName fault_code_qnamens = new QName(soap_env_uri, "Code", fault_pre);
	static public QName fault_value_qnamens = new QName(soap_env_uri, "Value", fault_pre);
	static public QName fault_reason_qnamens = new QName(soap_env_uri, "Reason", fault_pre);
	static public QName fault_detail_qnamens = new QName(soap_env_uri, "Detail", fault_pre);
	static public QName fault_text_qnamens = new QName(soap_env_uri, "Text", fault_pre);

	static public String ws_addressing_namespace_uri = "http://www.w3.org/2005/08/addressing";
	static public OMNamespace ws_addressing_namespace = om_factory.createOMNamespace(ws_addressing_namespace_uri, "wsa");

	
	static public OMElement buildSoapEnvelope() {
		return om_factory.createOMElement(soap_env_qnamens);
	}

	/**
	 * Add Soap Header to Soap Envelope
	 * @param to
	 * @param wsaction
	 * @param messageId
	 * @param relatesTo
	 * @param soapEnvelope
	 * @return soapEnvelope
	 */
	static public OMElement attachSoapHeader(String to, String wsaction, String messageId, String relatesTo, OMElement soapEnvelope) {
		OMElement header = om_factory.createOMElement(soap_hdr_qnamens);
		boolean muDone = false;

		if (wsaction != null) {
			OMElement mid = om_factory.createOMElement("Action", ws_addressing_namespace);
			mid.setText(wsaction);
			header.addChild(mid);

			if (!muDone) {
				OMAttribute a = om_factory.createOMAttribute("mustUnderstand", soap_env_namespace, "1");
				mid.addAttribute(a);
				muDone = true;
			}
		}

		if (messageId != null) {
			OMElement mid = om_factory.createOMElement("MessageID", ws_addressing_namespace);
			mid.setText(messageId);
			header.addChild(mid);

			if (!muDone) {
				mid.addAttribute("mustUnderstand", "1", soap_env_namespace);
				muDone = true;
			}
		}

		if (relatesTo != null) {
			OMElement mid = om_factory.createOMElement("RelatesTo", ws_addressing_namespace);
			mid.setText(relatesTo);
			header.addChild(mid);

			if (!muDone) {
				mid.addAttribute("mustUnderstand", "1", soap_env_namespace);
				muDone = true;
			}
		}

		if (to != null) {
			OMElement mid = om_factory.createOMElement("To", ws_addressing_namespace);
			mid.setText(to);
			header.addChild(mid);

			if (!muDone) {
				mid.addAttribute("mustUnderstand", "1", soap_env_namespace);
				muDone = true;
			}
		}

		if (soapEnvelope != null) {
			soapEnvelope.addChild(header);
		}

		return soapEnvelope;
	}

	/**
	 * Attach Soap Body to Soap Envelope given the contents.  The contents are
	 * first wrapped in a Soap Body before being attached.
	 * @param contents - contents that go in the Soap Body
	 * @param soapEnvelope
	 * @return Soap Envelope
	 */
	static public OMElement attachSoapBody(OMElement contents, OMElement soapEnvelope) {
		OMElement body = om_factory.createOMElement(soap_body_qnamens);

		if (contents != null) {
			body.addChild(contents);
		}

		if (soapEnvelope != null) {
			soapEnvelope.addChild(body);
		}

		return soapEnvelope;
	}

}
