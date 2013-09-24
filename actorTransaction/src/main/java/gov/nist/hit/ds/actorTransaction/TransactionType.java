package gov.nist.hit.ds.actorTransaction;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * SOAP Request Actions
 */

/**
 * TODO: Need different shortName for async so endpoint is different.  Affects GenericSimulatorFactory.
 * @author bill
 *
 */
public enum TransactionType  implements IsSerializable, Serializable {
	PROVIDE_AND_REGISTER      ("ITI-41", "Provide and Register",              "prb",    "prb",  "pr.as",    "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b", "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse", false),
	XDR_PROVIDE_AND_REGISTER  ("ITI-41", "XDR Provide and Register",          "xdrpr",  "xdrpr", "xdrpr.as", "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b", "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse", false),
	REGISTER                  ("ITI-42", "Register",                          "rb",     "rb",   "r.as",     "urn:ihe:iti:2007:RegisterDocumentSet-b", "urn:ihe:iti:2007:RegisterDocumentSet-bResponse", false),
	RETRIEVE                  ("ITI-43", "Retrieve",                          "ret",    "retb", "ret.as",   "urn:ihe:iti:2007:RetrieveDocumentSet", "urn:ihe:iti:2007:RetrieveDocumentSetResponse", true),
	IG_RETRIEVE               ("ITI-43", "Initiating Gateway Retrieve",       "igr",    "igr",   "igr.as",   "urn:ihe:iti:2007:RetrieveDocumentSet", "urn:ihe:iti:2007:RetrieveDocumentSetResponse", false),
	ODDS_RETRIEVE             ("ITI-43", "On-Demand Document Source Retrieve","odds",   "odds",  "odds.as",  "urn:ihe:iti:2007:RetrieveDocumentSet", "urn:ihe:iti:2007:RetrieveDocumentSetResponse", false),
	ISR_RETRIEVE              ("ITI-43", "Integrated Source/Repository Retrieve","isr",   "isr",  "isr.as",  "urn:ihe:iti:2007:RetrieveDocumentSet", "urn:ihe:iti:2007:RetrieveDocumentSetResponse", false),
	STORED_QUERY              ("ITI-18", "Stored Query",                      "sq",     "sqb",  "sq.as",    "urn:ihe:iti:2007:RegistryStoredQuery", "urn:ihe:iti:2007:RegistryStoredQueryResponse", false),
	IG_QUERY                  ("ITI-18", "Initiating Gateway Query",          "igq",    "igq",   "igq.as",   "urn:ihe:iti:2007:RegistryStoredQuery", "urn:ihe:iti:2007:RegistryStoredQueryResponse", false),
	UPDATE                    ("ITI-57", "Update",                            "update", "updateb", "update.b.as", "urn:ihe:iti:2010:UpdateDocumentSet", "urn:ihe:iti:2010:UpdateDocumentSetResponse", false),
	XC_QUERY                  ("ITI-38", "Cross-Community Query",             "xcq",    "xcq",   "xcq.as",   "urn:ihe:iti:2007:CrossGatewayQuery", "urn:ihe:iti:2007:CrossGatewayQueryResponse", false),
	XC_RETRIEVE               ("ITI-39", "Cross-Community Retrieve",          "xcr",    "xcr",   "xcr.as",   "urn:ihe:iti:2007:CrossGatewayRetrieve", "urn:ihe:iti:2007:CrossGatewayRetrieveResponse", false),
	MPQ                       ("ITI-51", "Multi-Patient Query",               "mpq",    "mpq",   "mpq.as",   "urn:ihe:iti:2009:MultiPatientStoredQuery", "urn:ihe:iti:2009:MultiPatientStoredQueryResponse", false),
	XC_PATIENT_DISCOVERY      ("ITI-55", "Cross Community Patient Discovery", "xcpd",   "xcpd",  "xcpd.as",  "urn:hl7-org:v3:PRPA_IN201305UV02:CrossGatewayPatientDiscovery", "urn:hl7-org:v3:PRPA_IN201306UV02:CrossGatewayPatientDiscovery", false);
	
	private static final long serialVersionUID = 1L;
	String id = "";
	String name = ""; 
	String shortName = "";
	String code = "";   // like pr.b - used in actors table
	String asyncCode = "";
	String requestAction;
	String responseAction;
	boolean needsRepUid = false;
	
	TransactionType() {}  // For GWT

	TransactionType(String id, String name, String shortName, String code, String asyncCode, String requestAction, String responseAction, boolean needsRepUid) {
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
	
	// if lookup by id is needed, must also select off of receiving actor
	static public TransactionType find(String s) {
		if (s == null) return null;
		for (TransactionType t : values()) {
			if (s.equalsIgnoreCase(t.name)) return t;
			if (s.equalsIgnoreCase(t.shortName)) return t;
			if (s.equalsIgnoreCase(t.code)) return t;
			if (s.equalsIgnoreCase(t.asyncCode)) return t;
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