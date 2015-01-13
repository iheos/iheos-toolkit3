package gov.nist.hit.ds.registrySim.sq.generic.queries;

import gov.nist.hit.ds.docRef.SqDocRef;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.sq.generic.support.SQCodedTerm;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuery;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.xdsExceptions.MetadataException;
import gov.nist.hit.ds.xdsExceptions.MetadataValidationException;
import gov.nist.hit.ds.xdsExceptions.XDSRegistryOutOfResourcesException;
import gov.nist.hit.ds.xdsExceptions.XdsException;
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;

import java.util.List;

/**
Generic implementation of FindFolders Stored Query. This class knows how to parse a 
 * FindFolders Stored Query request producing a collection of instance variables describing
 * the request.  A sub-class must provide the runImplementation() method that uses the pre-parsed
 * information about the stored query and queries a metadata database.
 * @author bill
 *
 */
abstract public class FindFolders extends StoredQuery {

	/**
	 * Method required in subclasses (implementation specific class) to define specific
	 * linkage to local database
	 * @return matching metadata
	 * @throws MetadataException
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract protected Metadata runImplementation() throws MetadataException, XdsException, LoggerException;


	public FindFolders(StoredQuerySupport sqs) {
		super(sqs);
	}

	/**
	 * Implementation of Stored Query specific logic including parsing and validating parameters.
	 * @throws ToolkitRuntimeException
	 * @throws XdsException
	 * @throws LoggerException
	 * @throws XDSRegistryOutOfResourcesException
	 */
	public Metadata runSpecific() throws XdsException, LoggerException {
		validateParameters();

		parseParameters();

		return runImplementation();
	}

	public void validateParameters() throws MetadataValidationException {
		//                    param name,                      required?, multiple?, is string?,   is code?,       support AND/OR                 alternative
		sqs.validate_parm("$XDSFolderPatientId",             true,      false,     true,         false,            false,                      (String[])null);
		sqs.validate_parm("$XDSFolderLastUpdateTimeFrom",    false,     false,     false,        false,            false,                      (String[])null);
		sqs.validate_parm("$XDSFolderLastUpdateTimeTo",      false,     false,     false,        false,            false,                      (String[])null);
		sqs.validate_parm("$XDSFolderCodeList",              false,     true,      true,         true,             true,                     (String[])null);
		sqs.validate_parm("$XDSFolderStatus",                true,      true,      true,         false,            false,                      (String[])null);

		if (sqs.has_validation_errors) 
			throw new MetadataValidationException(QueryParmsErrorPresentErrMsg, SqDocRef.Individual_query_parms);
	}

	protected String patient_id;
	protected String update_time_from;
	protected String update_time_to;
	protected SQCodedTerm codes;
	protected List<String> status;

	void parseParameters() throws ToolkitRuntimeException, MetadataException, XdsException, LoggerException {
		patient_id              = sqs.params.getStringParm("$XDSFolderPatientId");
		update_time_from        = sqs.params.getIntParm("$XDSFolderLastUpdateTimeFrom");
		update_time_to          = sqs.params.getIntParm("$XDSFolderLastUpdateTimeTo");
		codes        			= sqs.params.getCodedParm("$XDSFolderCodeList");
		status       			= sqs.params.getListParm("$XDSFolderStatus");

	}
}
