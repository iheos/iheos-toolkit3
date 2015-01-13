package gov.nist.toolkit.valregmsg.registry.storedquery.generic;

import gov.nist.toolkit.docref.SqDocRef;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrysupport.logging.LoggerException;
import gov.nist.toolkit.valregmsg.registry.storedquery.support.StoredQuerySupport;
import gov.nist.hit.ds.xdsExceptions.MetadataException;
import gov.nist.hit.ds.xdsExceptions.MetadataValidationException;
import gov.nist.hit.ds.xdsExceptions.XDSRegistryOutOfResourcesException;
import gov.nist.hit.ds.xdsExceptions.XdsException;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;

import java.util.List;

/**
Generic implementation of GetFolders Stored Query. This class knows how to parse a 
 * GetFolders Stored Query request producing a collection of instance variables describing
 * the request.  A sub-class must provide the runImplementation() method that uses the pre-parsed
 * information about the stored query and queries a metadata database.
 * @author bill
 *
 */
abstract public class GetFolders extends StoredQuery {

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
	public GetFolders(StoredQuerySupport sqs) {
		super(sqs);
	}

	public void validateParameters() throws MetadataValidationException {
		//                         param name,                             required?, multiple?, is string?,   same size as,    alternative
		sqs.validate_parm("$XDSFolderEntryUUID",                         true,      true,     true,         null,            "$XDSFolderUniqueId");
		sqs.validate_parm("$XDSFolderUniqueId",                          true,      true,     true,         null,            "$XDSFolderEntryUUID");

		if (sqs.has_validation_errors) 
			throw new MetadataValidationException(QueryParmsErrorPresentErrMsg, SqDocRef.Individual_query_parms);
	}

	protected List<String> fol_uuid;
	protected List<String> fol_uid;

	void parseParameters() throws XdsInternalException, XdsException, LoggerException {
		fol_uuid = sqs.params.getListParm("$XDSFolderEntryUUID");
		fol_uid = sqs.params.getListParm("$XDSFolderUniqueId");
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

		if (fol_uuid == null && fol_uid == null) 
			throw new XdsInternalException("GetFolders Stored Query");
		return runImplementation();
	}


}
