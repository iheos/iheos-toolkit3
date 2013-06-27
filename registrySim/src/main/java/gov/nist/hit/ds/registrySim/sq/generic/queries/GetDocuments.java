package gov.nist.hit.ds.registrySim.sq.generic.queries;

import gov.nist.hit.ds.docRef.SqDocRef;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuery;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.MetadataValidationException;
import gov.nist.hit.ds.xdsException.XDSRegistryOutOfResourcesException;
import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.List;

/**
Generic implementation of GetDocuments Stored Query. This class knows how to parse a 
 * GetDocuments Stored Query request producing a collection of instance variables describing
 * the request.  A sub-class must provide the runImplementation() method that uses the pre-parsed
 * information about the stored query and queries a metadata database.
 * @author bill
 *
 */
abstract public class GetDocuments extends StoredQuery {

	/**
	 * Method required in subclasses (implementation specific class) to define specific
	 * linkage to local database
	 * @return matching metadata
	 * @throws MetadataException
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract protected Metadata runImplementation() throws MetadataException, XdsException, LoggerException;

	/**
	 * Basic constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public GetDocuments(StoredQuerySupport sqs) {
		super(sqs);
	}

	public void validateParameters() throws MetadataValidationException {

		//                         param name,                       required?, multiple?, is string?,   same size as,    alternative
		sqs.validate_parm("$XDSDocumentEntryUniqueId",                 true,      true,     true,         null,            new String[] {"$XDSDocumentEntryEntryUUID", "$XDSDocumentEntryLogicalID"});
		sqs.validate_parm("$XDSDocumentEntryEntryUUID",                true,      true,     true,         null,            new String[] {"$XDSDocumentEntryUniqueId", "$XDSDocumentEntryLogicalID"});
		sqs.validate_parm("$XDSDocumentEntryLogicalID",                true,      true,     true,         null,            new String[] {"$XDSDocumentEntryUniqueId", "$XDSDocumentEntryEntryUUID"});
		sqs.validate_parm("$MetadataLevel",                            false,     false,    true,         null,            (String[]) null);

		if (sqs.has_validation_errors) 
			throw new MetadataValidationException(QueryParmsErrorPresentErrMsg, SqDocRef.Individual_query_parms);
	}

	protected List<String> uids;
	protected List<String> uuids;
	protected List<String> lids;
	protected String metadataLevel;
	
	void parseParameters() throws XdsInternalException, XdsException, LoggerException {
		uids = sqs.params.getListParm("$XDSDocumentEntryUniqueId");
		uuids = sqs.params.getListParm("$XDSDocumentEntryEntryUUID");
		lids = sqs.params.getListParm("$XDSDocumentEntryLogicalID");
		metadataLevel = sqs.params.getIntParm("$MetadataLevel");
	}

	
	/**
	 * Implementation of Stored Query specific logic including parsing and validating parameters.
	 * @throws XdsInternalException
	 * @throws XdsException
	 * @throws LoggerException
	 * @throws XDSRegistryOutOfResourcesException
	 */
	public Metadata runSpecific() throws XdsException, LoggerException {

		validateParameters();
		parseParameters();
		
		if (uids == null && uuids == null && lids == null)
			throw new XdsInternalException("GetDocuments Stored Query: Internal Error : GetDocuments.java#runSpecific : uuid not found, uid not found, and lids not found");

		return runImplementation();
	}


}
