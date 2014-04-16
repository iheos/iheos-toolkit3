package gov.nist.hit.ds.actorTransaction.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.actorTransaction.ActorTypeFactory;

import java.io.Serializable;


/**
 * SOAP Request Actions
 */

/**
 * TODO: Need different shortName for async so endpoint is different.  Affects GenericSimulatorFactory.
 * @author bill
 *
 */
public class TransactionType implements IsSerializable, Serializable {
	
	private static final long serialVersionUID = 1L;

    String id = "";
	String name = ""; 
	String shortName = "";
	String code = "";   // like pr.b - used in actors table
	String asyncCode = "";
	String requestAction;
	String responseAction;
	boolean needsRepUid = false;
	
	public TransactionType() {}

	TransactionType(String id, String name, String shortName, String code, String asyncCode, String requestAction,
                    String responseAction, boolean needsRepUid) {
		this.id = id;
		this.name = name;
		this.shortName = shortName;
		this.code = code;
		this.asyncCode = asyncCode;
		this.requestAction = requestAction;
		this.responseAction = responseAction;
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
	
	public String getRequestAction() {
		return requestAction;
	}
	
	public String getResponseAction() {
		return responseAction;
	}
	
	// moved to TransactionTypeFactory
	// if lookup by id is needed, must also select off of receiving actor
//	static public TransactionType find(String s) {
//		if (s == null) return null;
//		for (TransactionType t : values()) {
//			if (s.equalsIgnoreCase(t.name)) return t;
//			if (s.equalsIgnoreCase(t.shortName)) return t;
//			if (s.equalsIgnoreCase(t.code)) return t;
//			if (s.equalsIgnoreCase(t.asyncCode)) return t;
//		}
//		return null;
//	}
	
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
	
	static public TransactionType find(String receivingActorStr, String transString)   {
		if (receivingActorStr == null || transString == null) return null;
		
		ActorType a = ActorTypeFactory.find(receivingActorStr);
		return find(a, transString);
	}

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAsyncCode(String asyncCode) {
        this.asyncCode = asyncCode;
    }

    public void setRequestAction(String requestAction) {
        this.requestAction = requestAction;
    }

    public void setResponseAction(String responseAction) {
        this.responseAction = responseAction;
    }

    public void setNeedsRepUid(boolean needsRepUid) {
        this.needsRepUid = needsRepUid;
    }


}