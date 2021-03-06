package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.sq.generic.queries.FindDocuments;
import gov.nist.hit.ds.registrySim.sq.generic.support.QueryReturnType;
import gov.nist.hit.ds.registrySim.sq.generic.support.SQCodeAnd;
import gov.nist.hit.ds.registrySim.sq.generic.support.SQCodeOr;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrySim.store.DocEntry;
import gov.nist.hit.ds.registrySim.store.MetadataCollection;
import gov.nist.hit.ds.registrySim.store.RegIndex;
import gov.nist.hit.ds.registrySim.store.StatusValue;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.MetadataValidationException;
import gov.nist.hit.ds.xdsException.XdsException;

import java.util.List;


public class FindDocumentsSim extends FindDocuments {
	RegIndex ri;
	
	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}


	public FindDocumentsSim(StoredQuerySupport sqs)
			throws MetadataValidationException {
		super(sqs);
	}
	
	protected Metadata runImplementation() throws MetadataException,
			XdsException {
		
		MetadataCollection mc = ri.getMetadataCollection();
		List<DocEntry> results;
		
		// match on patient id
		results = mc.docEntryCollection.findByPid(patient_id);

		// filter on availabilityStatus
		List<StatusValue> statuses = ri.translateStatusValues(this.status);
		results = mc.docEntryCollection.filterByStatus(statuses, results);
		
		// filter on authorPerson
		results = mc.docEntryCollection.filterByAuthorPerson(author_person, results);
		
		// filter on creation time
		results = mc.docEntryCollection.filterByCreationTime(creation_time_from, creation_time_to, results);
		
		// filter on serviceStartTime
		results = mc.docEntryCollection.filterByServiceStartTime(service_start_time_from, service_start_time_to, results);
		
		// filter on serviceStopTime
		results = mc.docEntryCollection.filterByServiceStopTime(service_stop_time_from, service_stop_time_to, results);
		
		// filter on formatCode
		if (format_codes != null && !format_codes.isEmpty()) {
			if (format_codes instanceof SQCodeOr) {
				results = mc.docEntryCollection.filterByFormatCode((SQCodeOr)format_codes, results);
			} else {
				throw new XdsException("FindDocumentsSim: cannot cast object of type " + format_codes.getClass().getName() + " (from formatCodes) into an instance of class SQCodeOr", null);
			}
		}
		
		// filter on classCode
		if (class_codes != null && !class_codes.isEmpty()) {
			if (class_codes instanceof SQCodeOr) {
				results = mc.docEntryCollection.filterByClassCode((SQCodeOr)class_codes, results);
			} else {
				throw new XdsException("FindDocumentsSim: cannot cast object of type " + class_codes.getClass().getName() + " (from classCodes) into an instance of class SQCodeOr", null);
			}
		}
		
		// filter on typeCode
		if (type_codes != null && !type_codes.isEmpty()) {
			if (type_codes instanceof SQCodeOr) {
				results = mc.docEntryCollection.filterByTypeCode((SQCodeOr)type_codes, results);
			} else {
				throw new XdsException("FindDocumentsSim: cannot cast object of type " + type_codes.getClass().getName() + " (from typeCodes) into an instance of class SQCodeOr", null);
			}
		}
		
		// filter on practiceSettingCode
		if (practice_setting_codes != null && !practice_setting_codes.isEmpty()) {
			if (practice_setting_codes instanceof SQCodeOr) {
				results = mc.docEntryCollection.filterByPracticeSettingCode((SQCodeOr)practice_setting_codes, results);
			} else {
				throw new XdsException("FindDocumentsSim: cannot cast object of type " + practice_setting_codes.getClass().getName() + " (from practiceSettingCode) into an instance of class SQCodeOr", null);
			}
		}
		
		// filter on hcftCode
		if (hcft_codes != null && !hcft_codes.isEmpty()) {
			if (hcft_codes instanceof SQCodeOr) {
				results = mc.docEntryCollection.filterByHcftCode((SQCodeOr)hcft_codes, results);
			} else {
				throw new XdsException("FindDocumentsSim: cannot cast object of type " + hcft_codes.getClass().getName() + " (from hcftCode) into an instance of class SQCodeOr", null);
			}
		}
		
		// filter on eventCode
		if (event_codes != null && !event_codes.isEmpty()) {
			if (event_codes instanceof SQCodeOr) {
				results = mc.docEntryCollection.filterByEventCode((SQCodeOr)event_codes, results);
			}
			else if (event_codes instanceof SQCodeAnd) {
					results = mc.docEntryCollection.filterByEventCode((SQCodeAnd)event_codes, results);
			} else {
				throw new XdsException("FindDocumentsSim: cannot cast object of type " + event_codes.getClass().getName() + " (from eventCode) into an instance of class SQCodeOr or SQCodeAnd", null);
			}
		}
		
		// filter on confCode
		if (conf_codes != null && !conf_codes.isEmpty()) {
			if (conf_codes instanceof SQCodeOr) {
				results = mc.docEntryCollection.filterByConfCode((SQCodeOr)conf_codes, results);
			} 
			else if (conf_codes instanceof SQCodeAnd) {
				results = mc.docEntryCollection.filterByConfCode((SQCodeAnd)conf_codes, results);
			} 
			else {
				throw new XdsException("FindDocumentsSim: cannot cast object of type " + conf_codes.getClass().getName() + " (from confCode) into an instance of class SQCodeOr or SQCodeAnd", null);
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
