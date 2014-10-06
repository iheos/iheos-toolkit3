package gov.nist.hit.ds.simSupport.client.configElementTypes;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.actorTransaction.EndpointType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransactionSimConfigElement extends
        SimConfigElement implements IsSerializable, Serializable {

    private static final String SCHEMACHECK = "schemaCheck";
    private static final String MODELCHECK = "modelCheck";
    private static final String CODINGCHECK = "codingCheck";

    public EndpointValue endpointValue;
    public EndpointType endpointType;
    public List<SimConfigElement> elements  = new ArrayList<SimConfigElement>();

	private static final long serialVersionUID = 532031604752465534L;

    // The name attribute is used as the transaction name which is contained
    // in the EndpointLabel
	public TransactionSimConfigElement(EndpointType type, EndpointValue endpoint) {
		this.name = type.getTransType().getName();
        this.endpointType = type;
		this.endpointValue = endpoint;

        if (!hasBool(SCHEMACHECK)) addBool(SCHEMACHECK, true);
        if (!hasBool(MODELCHECK)) addBool(MODELCHECK, true);
        if (!hasBool(CODINGCHECK)) addBool(CODINGCHECK, true);
	}

    private BooleanSimConfigElement addBool(String name, boolean value) {
        BooleanSimConfigElement bool = new BooleanSimConfigElement(name, value);
        elements.add(bool);
        return bool;
    }

    private boolean hasBool(String name) { return getBool(name) != null; }

    public BooleanSimConfigElement getBool(String name) {
        for (SimConfigElement ele : elements) {
            if (!(ele instanceof BooleanSimConfigElement)) continue;
            BooleanSimConfigElement b = (BooleanSimConfigElement) ele;
            if (b.getName().equals(name)) return b;
        }
        return null;
    }

    public void setBool(String name, boolean value) {
        BooleanSimConfigElement bool = getBool(name);
        if (bool == null) { bool = addBool(name, value); }
        bool.setValue(value);
    }

    private boolean getCheck(String name) {
        for (SimConfigElement ele : elements) {
            if (!(ele instanceof BooleanSimConfigElement)) continue;
            BooleanSimConfigElement b = (BooleanSimConfigElement) ele;
            if (b.getName().equals(name)) return b.getValue();
        }
        return true;
    }

    public TransactionType getTransactionType() { return endpointType.getTransType(); }
    public String getTransactionName() { return endpointType.getTransType().getName(); }
    public EndpointType getEndpointType() { return endpointType; }
    public EndpointValue getEndpointValue() { return endpointValue; }

    public boolean isSchemaCheck() { return getCheck(SCHEMACHECK); }
    public boolean isModelCheck() { return getCheck(MODELCHECK); }
    public boolean isCodingCheck() { return getCheck(CODINGCHECK); }

    public String toString() {
        return endpointType.label() + " : " + endpointValue.getValue() + " "
                + SCHEMACHECK + "=" + isSchemaCheck() + " "
                + MODELCHECK + "=" + isModelCheck() + " "
                + CODINGCHECK + "=" + isCodingCheck()
                ;
    }

}

