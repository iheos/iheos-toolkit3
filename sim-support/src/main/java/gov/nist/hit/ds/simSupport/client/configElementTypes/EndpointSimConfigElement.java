package gov.nist.hit.ds.simSupport.client.configElementTypes;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.actorTransaction.EndpointType;
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue;

import java.io.Serializable;

public class EndpointSimConfigElement extends
        SimConfigElement implements IsSerializable, Serializable {

    EndpointValue endpointValue;
    EndpointType endpointType;

	private static final long serialVersionUID = 532031604752465534L;

    // The name attribute is used as the transaction name which is contained
    // in the EndpointLabel
	public EndpointSimConfigElement(EndpointType type, EndpointValue endpoint) {
		this.name = type.getTransType().getName();
        this.endpointType = type;
		this.endpointValue = endpoint;
	}

    public String getTransactionName() { return endpointType.getTransType().getName(); }
    public EndpointType getEndpointType() { return endpointType; }
    public EndpointValue getEndpointValue() { return endpointValue; }

//    public OMElement toXML() {
//        OMElement top = XmlUtil.om_factory.createOMElement(type.toString(), null);
//        OMAttribute typeAtt = XmlUtil.om_factory.createOMAttribute("name", null, name);
//        top.addAttribute(typeAtt);
//        OMAttribute isSecureAtt = XmlUtil.om_factory.createOMAttribute("isSecure", null, endpointType.isTls() ? "true" : "false");
//        top.addAttribute((isSecureAtt));
//        OMAttribute valueAtt = XmlUtil.om_factory.createOMAttribute("value", null, value);
//        top.addAttribute(valueAtt);
//        return top;
//    }


}

