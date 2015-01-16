package gov.nist.hit.ds.simSupport.config
import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import groovy.util.logging.Log4j

//TODO: This is appropriate for a server.  Needs to be refactored into a
// base class appropriate to all and a server and client classes

@Log4j
public class ServerTransactionSimConfigElement extends AbstractTransactionSimConfigElement {
    EndpointValue endpointValue;
    EndpointType endpointType;

    // Booleans
    static final String SCHEMACHECK = "schemaCheck";
    static final String MODELCHECK  = "modelCheck";
    static final String CODINGCHECK = "codingCheck";
    static final String SOAPCHECK   = "soapCheck";

    // Text
    static final String MSGCALLBACK = "msgCallback";

    // The displayName attribute is used as the transaction displayName which is contained
    // in the EndpointLabel
	ServerTransactionSimConfigElement(EndpointType type, EndpointValue endpoint) {
		this.name = type.getTransType().getCode();
        this.endpointType = type;
		this.endpointValue = endpoint;

        if (!getBool(SCHEMACHECK)) addBool(SCHEMACHECK, true);
        if (!getBool(MODELCHECK))  addBool(MODELCHECK, true);
        if (!getBool(CODINGCHECK)) addBool(CODINGCHECK, true);
        if (!getBool(SOAPCHECK))   addBool(SOAPCHECK, true);

        if (!getText(MSGCALLBACK)) addText(MSGCALLBACK, "");
	}

    boolean isSchemaCheck() { getValue(SCHEMACHECK); }
    boolean isModelCheck() {  getValue(MODELCHECK); }
    boolean isCodingCheck() { getValue(CODINGCHECK); }
    boolean isSoapCheck() {   getValue(SOAPCHECK);  }
    String getCallBack() {    getValue(MSGCALLBACK)}

    EndpointType getEndpointType() { return endpointType; }
    EndpointValue getEndpointValue() { return endpointValue; }

    public String toString() {
        return ("TransSim "
                + "name = " + name + " "
                + endpointType.label() + " : " + endpointValue.getValue() + " "
                + SCHEMACHECK + "=" + isSchemaCheck() + " "
                + MODELCHECK + "=" + isModelCheck() + " "
                + CODINGCHECK + "=" + isCodingCheck() + " "
                + SOAPCHECK + "=" + isSoapCheck())
                ;
    }

}

