package gov.nist.hit.ds.registrySim.sq.generic.support;

import gov.nist.hit.ds.docRef.EbRS;
import gov.nist.hit.ds.docRef.SqDocRef;
import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.ErrorRecorder;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registryMsgFormats.AdhocQueryResponse;
import gov.nist.hit.ds.registryMsgFormats.RegistryErrorListGenerator;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.MetadataValidationException;
import gov.nist.hit.ds.xdsException.XDSRegistryOutOfResourcesException;
import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import org.apache.axiom.om.OMElement;

/**
 * Generic Stored Query Factory class that is sub-classed to define a specific stored query implementation.
 * The generic/specific nature relates to the underlying implementation.  The key method, 
 * buildStoredQueryHandler(), which is to be defined in the sub-class, decides which stored queries
 * are implemented and what the implementation classes are. This class provides the generic stored
 * query parsing and support.
 * @author bill
 *
 */
abstract public class StoredQueryFactory {

	/**
	 * Returns an object of generic type StoredQuery which implements a single stored query 
	 * type implemented against a specific registry implementation. The sub-class that implements 
	 * this method is specific to an implementation.
	 * @param sqs
	 * @throws MetadataValidationException
	 * @throws LoggerException 
	 */
	abstract public StoredQueryFactory buildStoredQueryHandler(StoredQuerySupport sqs) throws MetadataValidationException, LoggerException;

	OMElement ahqr;	
	
	QueryReturnType returnType = QueryReturnType.OBJECTREF;
	SqParams params;
	protected String query_id;
	protected StoredQuery storedQueryImpl;
	String service_name;
	boolean is_secure = false;
	protected AdhocQueryResponse response = null;
	protected ErrorRecorder er = null;
	String homeCommunityId = null;

	public void setIsSecure(boolean is) { is_secure = is; }
	public void setServiceName(String serviceName) { serviceName = service_name; }
	public void setQueryId(String qid) { query_id = qid; }
	public String getHome() { return homeCommunityId; }
	public boolean hasHome() { return homeCommunityId != null; }

	public boolean isLeafClassReturnType() {
		OMElement response_option = XmlUtil.firstChildWithLocalName(ahqr, "ResponseOption");
		if (response_option == null) return true;
		String return_type = response_option.getAttributeValue(MetadataSupport.return_type_qname);
		if (return_type == null || return_type.equals("") || !return_type.equals("LeafClass")) return false;
		return true;
	}

	public StoredQueryFactory(OMElement ahqr) throws XdsException {
		this(null, ahqr, new RegistryErrorListGenerator());
	}

	public StoredQueryFactory(OMElement ahqr, ErrorRecorder er) throws XdsException {
		this(null, ahqr, er);
	}

	public StoredQueryFactory(OMElement ahqr, RegistryErrorListGenerator rel) throws XdsException {
		this(null, ahqr, rel);
	}

	public StoredQueryFactory(SqParams params)  throws XdsInternalException, MetadataException, XdsException {
		this(params, null, new RegistryErrorListGenerator());
	}
	
	public StoredQueryFactory(SqParams params, OMElement ahqr, ErrorRecorder er) throws XdsException {
		this.ahqr = ahqr;
		this.params = params;
		this.er = er;
		
		build();
	}


	void build() throws XdsException  {
		
		if (er == null)
			er = new RegistryErrorListGenerator();

		OMElement response_option = XmlUtil.firstChildWithLocalName(ahqr, "ResponseOption") ;
		if (response_option == null) 
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("Cannot find /AdhocQueryRequest/ResponseOption element", "ebRS 3.0 Section 6.1.1.1"), this);

		String return_type = response_option.getAttributeValue(MetadataSupport.return_type_qname);

		if (return_type == null) throw new XdsException("Attribute returnType not found on query request", SqDocRef.Return_type);
		if (return_type.equals("LeafClass"))
			returnType = QueryReturnType.LEAFCLASS;
		else if (return_type.equals("ObjectRef"))
			returnType = QueryReturnType.OBJECTREF;
		else if (return_type.equals("LeafClassWithRepositoryItem"))
			returnType = QueryReturnType.LEAFCLASSWITHDOCUMENT;
		
		else
			er.err(XdsErrorCode.Code.XDSRegistryError, new ErrorContext("/AdhocQueryRequest/ResponseOption/@returnType must be LeafClass or ObjectRef or for some special queries LeafClassWithRepositoryItem. Found value "
					+ return_type, EbRS.ReturnTypes), this);

		OMElement adhoc_query = XmlUtil.firstChildWithLocalName(ahqr, "AdhocQuery") ;
		if (adhoc_query == null) {
			throw new XdsInternalException("Cannot find /AdhocQueryRequest/AdhocQuery element");
		}
		
		homeCommunityId = adhoc_query.getAttributeValue(MetadataSupport.home_qname);

		ParamParser parser = new ParamParser();
		params = parser.parse(ahqr);

		if (response == null) {
			response = new AdhocQueryResponse();
		}

		query_id = adhoc_query.getAttributeValue(MetadataSupport.id_qname);

		StoredQuerySupport sqs = new StoredQuerySupport(params, returnType, er, is_secure);

		buildStoredQueryHandler(sqs); // this goes to a sub-class that knows about a specific implementation
	}

	public StoredQuery getImpl() {
		return storedQueryImpl;
	}

	public Metadata run() throws XDSRegistryOutOfResourcesException, XdsException, LoggerException {
		if (storedQueryImpl == null)
			throw new XdsInternalException("storedQueryImpl is null");
		return storedQueryImpl.run();
	}

	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 * @throws XDSRegistryOutOfResourcesException 
	 */
	abstract public Metadata FindDocuments(StoredQuerySupport sqs) throws XdsException, LoggerException, XDSRegistryOutOfResourcesException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata FindFolders(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata FindSubmissionSets(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetAssociations(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetDocuments(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetDocumentsAndAssociations(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetFolderAndContents(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetFolders(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetFoldersForDocument(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetRelatedDocuments(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetSubmissionSetAndContents(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetSubmissionSets(StoredQuerySupport sqs) throws XdsException, LoggerException;
}
