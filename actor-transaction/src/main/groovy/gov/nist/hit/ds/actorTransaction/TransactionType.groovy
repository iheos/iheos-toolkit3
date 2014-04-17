package gov.nist.hit.ds.actorTransaction

import gov.nist.hit.ds.actorTransaction.exceptions.InvalidTransactionTypeDefinitionException;

/**
 * Created by bill on 4/16/14.
 */
class TransactionType {
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

    String toString() { return name }

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
