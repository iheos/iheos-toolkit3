package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.metadataModel.MetadataCollection;
import gov.nist.hit.ds.registrySim.metadataModel.RegIndex;
import gov.nist.hit.ds.registrySim.metadataModel.StatusValue;
import gov.nist.hit.ds.registrySim.metadataModel.SubSet;
import gov.nist.hit.ds.registrySim.sq.generic.queries.FindSubmissionSets;
import gov.nist.hit.ds.registrySim.sq.generic.support.QueryReturnType;
import gov.nist.hit.ds.registrySim.sq.generic.support.SQCodeOr;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.xdsExceptions.MetadataException;
import gov.nist.hit.ds.xdsExceptions.MetadataValidationException;
import gov.nist.hit.ds.xdsExceptions.XdsException;

import java.util.List;

public class FindSubmissionSetsSim extends FindSubmissionSets {
	RegIndex ri;
	
	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}

	public FindSubmissionSetsSim(StoredQuerySupport sqs)
			throws MetadataValidationException {
		super(sqs);
	}

	protected Metadata runImplementation() throws MetadataException,
			XdsException, LoggerException {

		MetadataCollection mc = ri.getMetadataCollection();
		
		List<SubSet> results;
		
		// match on patient id
		results = mc.subSetCollection.findByPid(patient_id);
		
		// filter on availabilityStatus
		List<StatusValue> statuses = ri.translateStatusValues(this.status);
		results = mc.subSetCollection.filterByStatus(statuses, results);
		
		// filter on sourceId
		if (source_id != null)
			results = mc.subSetCollection.filterBySourceId(source_id, results);
		
		// filter on submissionTime
		results = mc.subSetCollection.filterBySubmissionTime(submission_time_from, submission_time_to,  results);
		
		// filter on authorPerson
		results = mc.subSetCollection.filterByAuthorPerson(author_person, results);
		
		// filter on contentType
		if (content_type != null && !content_type.isEmpty()) {
			if (content_type instanceof SQCodeOr) {
				results = mc.subSetCollection.filterByContentTypeCode((SQCodeOr)content_type, results);
			} else {
				throw new XdsException("FindSubmissionSetsSim: cannot cast object of type " + content_type.getClass().getName() + " (from contentTypeCodes) into an instance of class SQCodeOr", null);
			}
		}


		List<String> uuids = mc.getIdsForObjects(results);

		Metadata m = new Metadata();
		m.setVersion3();
		if (sqs.returnType == QueryReturnType.LEAFCLASS || sqs.returnType == QueryReturnType.LEAFCLASSWITHDOCUMENT) {
			m = mc.loadRo(uuids);
		} else {
			m.mkObjectRefs(uuids);
		}
		
		return m;
	}

}
