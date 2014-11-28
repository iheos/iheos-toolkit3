package gov.nist.hit.ds.simSupport.config

import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import groovy.util.logging.Log4j

@Log4j
public class TransactionSimConfigElement {
    def name

    // Booleans
    static final String SCHEMACHECK = "schemaCheck";
    static final String MODELCHECK = "modelCheck";
    static final String CODINGCHECK = "codingCheck";
    static final String SOAPCHECK = "soapCheck";

    // Text
    static final String MSGCALLBACK = "msgCallback";

    EndpointValue endpointValue;
    EndpointType endpointType;
    List<AbstractConfigElement> elements  = new ArrayList<AbstractConfigElement>();

    // The displayName attribute is used as the transaction displayName which is contained
    // in the EndpointLabel
	TransactionSimConfigElement(EndpointType type, EndpointValue endpoint) {
		this.name = type.getTransType().getCode();
        this.endpointType = type;
		this.endpointValue = endpoint;

        if (!getBool(SCHEMACHECK)) addBool(SCHEMACHECK, true);
        if (!getBool(MODELCHECK)) addBool(MODELCHECK, true);
        if (!getBool(CODINGCHECK)) addBool(CODINGCHECK, true);
        if (!getBool(SOAPCHECK)) addBool(SOAPCHECK, true);

        if (!getText(MSGCALLBACK)) addText(MSGCALLBACK, "");
	}

    def clear() { elements.clear() }

    def contains(name) { elements.find { it.name == name }}

    def add(AbstractConfigElement ele) {
        assert !contains(ele.name)
        elements.add(ele)
    }

    def get(name) { elements.find { it.name == name}}

    def addBool(String name, boolean value) { add(new BooleanSimConfigElement(name, value)) }

    BooleanSimConfigElement getBool(String name) {
        def ele = get(name)
        if (ele == null) return null
        assert ele instanceof BooleanSimConfigElement
        return ele
    }

    TextSimConfigElement getText(String name) {
        def ele = get(name)
        if (ele == null) return null
        assert ele instanceof TextSimConfigElement
        return ele
    }
    def setBool(String name, boolean value) {
        BooleanSimConfigElement bool = getBool(name);
        if (bool == null) { addBool(name, value); return; }
        bool.setValue(value);
    }


    def setText(String name, String value) {
        TextSimConfigElement text = getText(name);
        if (text == null) { addText(name, value); return; }
        text.setValue(value);
    }

    TextSimConfigElement addText(String name, String value) {
        TextSimConfigElement text = new TextSimConfigElement(name, value);
        elements.add(text);
        return text;
    }

    boolean getValue(String name) { get(name)?.value }

    boolean isSchemaCheck() { getValue(SCHEMACHECK); }
    boolean isModelCheck() { getValue(MODELCHECK); }
    boolean isCodingCheck() { getValue(CODINGCHECK); }
    boolean isSoapCheck() { getValue(SOAPCHECK); }
    String getCallBack() { getValue(MSGCALLBACK)}


    TransactionType getTransactionType() { return endpointType.getTransType(); }
    String getTransactionName() { return endpointType.getTransType().code; }
    EndpointType getEndpointType() { return endpointType; }
    EndpointValue getEndpointValue() { return endpointValue; }


    public String toString() {
        return ("TransSim " + endpointType.label() + " : " + endpointValue.getValue() + " "
                + SCHEMACHECK + "=" + isSchemaCheck() + " "
                + MODELCHECK + "=" + isModelCheck() + " "
                + CODINGCHECK + "=" + isCodingCheck() + " "
                + SOAPCHECK + "=" + isSoapCheck())
                ;
    }

}

