package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.actorTransaction.exceptions.InvalidTransactionTypeDefinitionException
import groovy.transform.ToString
/**
 * Created by bill on 4/16/14.
 */
@ToString(includeFields=true, includes="name, code, acceptRequestClassName, props, isRetrieve")
class TransactionType /* implements IsSerializable, Serializable */ {
    public String name
    public String code
    public String asyncCode
    public String requestAction
    public String responseAction
    public String acceptRequestClassName
    public String sendRequestClassName
    public boolean multiPart
    public boolean soap
    public boolean isRetrieve
    Map<String, String> props = new HashMap<String, String>()
    public static final String retrieveTransactionTypeCode = 'ret.b'   // why?

    public String getCode() { return code }
    public String getName() { return name }
    public String setName(String name) { this.name = name; }


    TransactionType() {}

    String getTransactionProperty(String key) { return props.get(key) }
    boolean hasTransactionProperty(String key) { return props.containsKey(key) }
    void putTransactionProperty(String key, String value) { props.put(key, value) }

    boolean equals(TransactionType tt) {
        tt.code.equalsIgnoreCase(code)
    }

    int hashCode() {
        code.toLowerCase().hashCode()
    }
    boolean identifiedBy(String s) {
                s.equalsIgnoreCase(code) ||
                s.equalsIgnoreCase(asyncCode)
    }

//    String toString() { return displayName }

    void check()  {
        String val;
        TransactionType tt = this;

        val = tt.code;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("code not defined");
        val = tt.name;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("name not defined");
//        val = tt.asyncCode;
//        if (val == null || val.equals(""))
//            throw new InvalidTransactionTypeDefinitionException("asyncCode not defined");
        val = tt.requestAction;
        if (soap && (val == null || val.equals("")))
            throw new InvalidTransactionTypeDefinitionException("requestAction not defined");
        val = tt.responseAction;
        if (soap && (val == null || val.equals("")))
            throw new InvalidTransactionTypeDefinitionException("responseAction not defined");
        val = tt.acceptRequestClassName;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("implClass not defined");
    }


}
