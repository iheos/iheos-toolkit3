package gov.nist.hit.ds.actorTransaction.client;


import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SOAP Request Actions
 */

/**
 * TODO: Need different shortName for async so endpoint is different.  Affects GenericSimulatorFactory.
 * @author bill
 *
 */

public enum TransactionType implements IsSerializable, Serializable {
    PROVIDE_AND_REGISTER("ITI-41", "Provide and Register", "prb", "pr.b", "pr.as", false),
    XDR_PROVIDE_AND_REGISTER("ITI-41", "XDR Provide and Register", "xdrpr", "xdrpr", "xdrpr.as", false),
    REGISTER("ITI-42", "Register", "rb", "r.b", "r.as", false),
    RETRIEVE("ITI-43", "Retrieve", "ret", "ret.b", "ret.as", true),
    IG_RETRIEVE("ITI-43", "Initiating Gateway Retrieve", "igr", "igr", "igr.as", false),
    ODDS_RETRIEVE("ITI-43", "On-Demand Document Source Retrieve", "odds", "odds", "odds.as", false),
    ISR_RETRIEVE("ITI-43", "Integrated Source/Repository Retrieve", "isr", "isr", "isr.as", false),
    STORED_QUERY("ITI-18", "Stored Query", "sq", "sq.b", "sq.as", false),
    IG_QUERY("ITI-18", "Initiating Gateway Query", "igq", "igq", "igq.as", false),
    UPDATE("ITI-57", "Update", "update", "update.b", "update.b.as", false),
    XC_QUERY("ITI-38", "Cross-Community Query", "xcq", "xcq", "xcq.as", false),
    XC_RETRIEVE("ITI-39", "Cross-Community Retrieve", "xcr", "xcr", "xcr.as", false),
    MPQ("ITI-51", "Multi-Patient Query", "mpq", "mpq", "mpq.as", false),
    XC_PATIENT_DISCOVERY("ITI-55", "Cross Community Patient Discovery", "xcpd", "xcpd", "xcpd.as", false),
    DIRECT("ONC-DIRECT", "ONC-DIRECT", "direct", "direct", "direct.as", false);

    private static final long serialVersionUID = 1L;
    String id = "";
    String name = "";
    String shortName = "";
    String code = "";   // like pr.b - used in actors table
    String asyncCode = "";
    boolean needsRepUid = false;

    TransactionType() {}  // For GWT

    TransactionType(String id, String name, String shortName, String code, String asyncCode, boolean needsRepUid) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.code = code;
        this.asyncCode = asyncCode;
        this.needsRepUid = needsRepUid;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getCode() {
        return code;
    }

    public String getAsyncCode() {
        return asyncCode;
    }

    // if lookup by id is needed, must also select off of receiving actor
    static public TransactionType find(String s) {
        if (s == null) return null;
        for (TransactionType t : values()) {
            if (s.equals(t.name)) return t;
            if (s.equals(t.shortName)) return t;
            if (s.equals(t.code)) return t;
            if (s.equals(t.asyncCode)) return t;
        }
        return null;
    }

    public boolean isIdentifiedBy(String s) {
        if (s == null) return false;
        return
                s.equals(id) ||
                        s.equals(name) ||
                        s.equals(shortName) ||
                        s.equals(code) ||
                        s.equals(asyncCode);
    }

    static public TransactionType find(ActorType a, String transString) {
        if (a == null) return null;

        for (TransactionType t : a.getTransactions()) {
            if (t.isIdentifiedBy(transString))
                return t;
        }

        return null;
    }

    static public TransactionType find(String receivingActorStr, String transString) {
        if (receivingActorStr == null || transString == null) return null;

        ActorType a = ActorType.findActor(receivingActorStr);
        return find(a, transString);
    }

    static public List<TransactionType> asList() {
        List<TransactionType> l = new ArrayList<TransactionType>();
        for (TransactionType t : values())
            l.add(t);
        return l;
    }

}
