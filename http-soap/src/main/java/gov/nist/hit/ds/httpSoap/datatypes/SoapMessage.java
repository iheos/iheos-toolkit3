package gov.nist.hit.ds.httpSoap.datatypes;

import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;
import org.apache.axiom.om.OMElement;

public class SoapMessage {
	OMElement header;
	OMElement body;
	OMElement root;
	int partCount;

	public int getPartCount() {
		return partCount;
	}

	public SoapMessage setPartCount(int partCount) {
		this.partCount = partCount;
		return this;
	}

	public OMElement getRoot() {
		return root;
	}
	
	public SoapMessage setRoot(OMElement root) {
		this.root = root;
		return this;
	}
	
	public OMElement getHeader() {
		return header;
	}
	public SoapMessage setHeader(OMElement header) {
		this.header = header;
		return this;
	}
	public OMElement getBody() {
		return body;
	}
	public SoapMessage setBody(OMElement body) {
		this.body = body;
		return this;
	}

    public String getSoapAction() {
        if (header == null) throw new ToolkitRuntimeException("No SOAP Header");
        try {
            return ((OMElement) header.getChildrenWithLocalName("Action").next()).getText();
        } catch (NullPointerException e) {
            throw new ToolkitRuntimeException("No SOAP Action");
        }
    }
	
	
}
