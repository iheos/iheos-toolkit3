package gov.nist.hit.ds.simSupport.config;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.actorTransaction.EndpointType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue

public class TransactionSimConfigElement extends AbstractSimConfigElement
        implements IsSerializable, Serializable {

    // Booleans
    public static final String SCHEMACHECK = "schemaCheck";
    public static final String MODELCHECK = "modelCheck";
    public static final String CODINGCHECK = "codingCheck";
    public static final String SOAPCHECK = "soapCheck";

    // Text
    public static final String MSGCALLBACK = "msgCallback";

    public EndpointValue endpointValue;
    public EndpointType endpointType;
    public List<AbstractSimConfigElement> elements  = new ArrayList<AbstractSimConfigElement>();

	private static final long serialVersionUID = 532031604752465534L;

    // The displayName attribute is used as the transaction displayName which is contained
    // in the EndpointLabel
	public TransactionSimConfigElement(EndpointType type, EndpointValue endpoint) {
		this.name = type.getTransType().getName();
        this.endpointType = type;
		this.endpointValue = endpoint;

        if (!hasBool(SCHEMACHECK)) addBool(SCHEMACHECK, true);
        if (!hasBool(MODELCHECK)) addBool(MODELCHECK, true);
        if (!hasBool(CODINGCHECK)) addBool(CODINGCHECK, true);
        if (!hasBool(SOAPCHECK)) addBool(SOAPCHECK, true);

        if (!hasText(MSGCALLBACK)) addText(MSGCALLBACK, "");
	}

    private BooleanSimConfigElement addBool(String name, boolean value) {
        BooleanSimConfigElement bool = new BooleanSimConfigElement(name, value);
        elements.add(bool);
        return bool;
    }

    private boolean hasBool(String name) { return getBool(name) != null; }

    public BooleanSimConfigElement getBool(String name) {
        for (AbstractSimConfigElement ele : elements) {
            if (!(ele instanceof BooleanSimConfigElement)) continue;
            BooleanSimConfigElement b = (BooleanSimConfigElement) ele;
            if (b.getName().equals(name)) return b;
        }
        return null;
    }

    public void setBool(String name, boolean value) {
        BooleanSimConfigElement bool = getBool(name);
        if (bool == null) { addBool(name, value); return; }
        bool.setValue(value);
    }

    private boolean hasText(String name) { return getText(name) != null; }

    public TextSimConfigElement getText(String name) {
        for (AbstractSimConfigElement ele : elements) {
            if (!(ele instanceof TextSimConfigElement)) continue;
            TextSimConfigElement b = (TextSimConfigElement) ele;
            if (b.getName().equals(name)) return b;
        }
        return null;
    }

    public void setText(String name, String value) {
        TextSimConfigElement text = getText(name);
        if (text == null) { addText(name, value); return; }
        text.setValue(value);
    }

    private TextSimConfigElement addText(String name, String value) {
        TextSimConfigElement text = new TextSimConfigElement(name, value);
        elements.add(text);
        return text;
    }

    private boolean getCheck(String name) {
        for (AbstractSimConfigElement ele : elements) {
            if (!(ele instanceof BooleanSimConfigElement)) continue;
            BooleanSimConfigElement b = (BooleanSimConfigElement) ele;
            if (b.getName().equals(name)) return b.getValue();
        }
        return true;
    }

    public TransactionType getTransactionType() { return endpointType.getTransType(); }
    public String getTransactionName() { return endpointType.getTransType().code; }
    public EndpointType getEndpointType() { return endpointType; }
    public EndpointValue getEndpointValue() { return endpointValue; }

    public boolean isSchemaCheck() { return getCheck(SCHEMACHECK); }
    public boolean isModelCheck() { return getCheck(MODELCHECK); }
    public boolean isCodingCheck() { return getCheck(CODINGCHECK); }
    public boolean isSoapCheck() { return getCheck(SOAPCHECK); }

    public String toString() {
        return endpointType.label() + " : " + endpointValue.getValue() + " "
                + SCHEMACHECK + "=" + isSchemaCheck() + " "
                + MODELCHECK + "=" + isModelCheck() + " "
                + CODINGCHECK + "=" + isCodingCheck() + " "
                + SOAPCHECK + "=" + isSoapCheck()
                ;
    }

}

