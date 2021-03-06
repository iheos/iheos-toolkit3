package gov.nist.hit.ds.registrySim.sq.generic.queries;

import gov.nist.hit.ds.docRef.EbRim;
import gov.nist.hit.ds.docRef.SqDocRef;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.sq.generic.support.SQCodedTerm;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuery;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.MetadataValidationException;
import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.util.ArrayList;
import java.util.List;

/**
Generic implementation of FindSubmissionSets Stored Query. This class knows how to parse a 
 * FindSubmissionSets Stored Query request producing a collection of instance variables describing
 * the request.  A sub-class must provide the runImplementation() method that uses the pre-parsed
 * information about the stored query and queries a metadata database.
 * @author bill
 *
 */
abstract public class FindSubmissionSets extends StoredQuery {

	/**
	 * Method required in subclasses (implementation specific class) to define specific
	 * linkage to local database
	 * @return matching metadata
	 * @throws MetadataException
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract protected Metadata runImplementation() throws MetadataException, XdsException, LoggerException;

	public FindSubmissionSets(StoredQuerySupport sqs) throws MetadataValidationException {
		super(sqs);
	}
	
	public void validateParameters() throws MetadataValidationException {
		
		//                         param name,                                 required?, multiple?, is string?,   is code?,     support AND/OR                          alternative
		sqs.validate_parm("$XDSSubmissionSetPatientId",                         true,      false,     true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetSourceId",                          false,     true,      true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetSubmissionTimeFrom",                false,     false,     true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetSubmissionTimeTo",                  false,     false,     true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetAuthorPerson",                      false,     false,     true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetContentType",                       false,     true,      true,         true,          false,                               (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetStatus",                            true,      true,      true,         false,         false,                                 (String[])null												);
		
		if (sqs.has_validation_errors) 
			throw new MetadataValidationException(QueryParmsErrorPresentErrMsg, SqDocRef.Individual_query_parms);
	}

	public Metadata runSpecific() throws XdsInternalException, XdsException, LoggerException {
				
		validateParameters();

		parseParameters();

		return runImplementation();
	}
	
	protected String               patient_id;
	protected List<String>    source_id;
	protected String               submission_time_from;
	protected String               submission_time_to;
	protected String               author_person;
	protected SQCodedTerm         content_type;
	protected List<String>    status;

	void parseParameters() throws XdsInternalException, XdsException, LoggerException {
		
		patient_id                               = sqs.params.getStringParm   ("$XDSSubmissionSetPatientId");
		source_id                                = sqs.params.getListParm("$XDSSubmissionSetSourceId");
		submission_time_from                     = sqs.params.getIntParm      ("$XDSSubmissionSetSubmissionTimeFrom");
		submission_time_to                       = sqs.params.getIntParm      ("$XDSSubmissionSetSubmissionTimeTo");
		author_person                            = sqs.params.getStringParm   ("$XDSSubmissionSetAuthorPerson");
		content_type                             = sqs.params.getCodedParm("$XDSSubmissionSetContentType");
		status                                   = sqs.params.getListParm("$XDSSubmissionSetStatus");

		String status_ns_prefix = MetadataSupport.status_type_namespace;
		
		ArrayList<String> new_status = new ArrayList<String>();
		for (int i=0; i<status.size(); i++) {
			String stat = (String) status.get(i);
			
			if ( ! stat.startsWith(status_ns_prefix)) 
				throw new MetadataValidationException("Status parameter must have namespace prefix " + status_ns_prefix + " found " + stat, EbRim.RegistryObject_attributes);
			new_status.add(stat.replaceFirst(status_ns_prefix, ""));
		}
		status = new_status;
	}

	
}
