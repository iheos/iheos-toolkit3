package gov.nist.hit.ds.actorTransaction

import com.google.gwt.user.client.rpc.IsSerializable
import gov.nist.hit.ds.actorTransaction.exceptions.InvalidTransactionTypeDefinitionException
import groovy.transform.ToString;

/**
 * Created by bill on 4/16/14.
 */
@ToString(includeFields=true, includes="id, props")
class TransactionType implements IsSerializable, Serializable {
    public String id
    public String name
    public String shortName
    public String code
    public String asyncCode
    public String requestAction
    public String responseAction

    public String getCode() { return code }
    public String getName() { return name }
    public String getShortName() { return shortName }
    Map<String, String> props = new HashMap<String, String>()

    String getTransactionProperty(String key) { return props.get(key) }
    boolean hasTransactionProperty(String key) { return props.containsKey(key) }
    void putTransactionProperty(String key, String value) { props.put(key, value) }

    boolean equals(TransactionType tt) {
        tt.name.equalsIgnoreCase(name)
    }

    int hashCode() {
        name.toLowerCase().hashCode()
    }
    boolean identifiedBy(String s) {
        s.equalsIgnoreCase(name) ||
                s.equalsIgnoreCase(shortName) ||
                s.equalsIgnoreCase(code) ||
                s.equalsIgnoreCase(asyncCode)
    }

//    String toString() { return name }

    void check()  {
        String val;
        TransactionType tt = this;

        val = tt.id;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("id not defined");
        val = tt.name;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("name not defined");
        val = tt.shortName;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("shortName not defined");
        val = tt.code;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("code not defined");
        val = tt.asyncCode;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("asyncCode not defined");
        val = tt.requestAction;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("requestAction not defined");
        val = tt.responseAction;
        if (val == null || val.equals(""))
            throw new InvalidTransactionTypeDefinitionException("responseAction not defined");
    }


}
