package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.metadataModel.Fol;
import gov.nist.hit.ds.registrySim.metadataModel.MetadataCollection;
import gov.nist.hit.ds.registrySim.metadataModel.RegIndex;
import gov.nist.hit.ds.registrySim.metadataModel.StatusValue;
import gov.nist.hit.ds.registrySim.sq.generic.queries.FindFolders;
import gov.nist.hit.ds.registrySim.sq.generic.support.QueryReturnType;
import gov.nist.hit.ds.registrySim.sq.generic.support.SQCodeAnd;
import gov.nist.hit.ds.registrySim.sq.generic.support.SQCodeOr;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsException;

import java.util.List;

public class FindFoldersSim extends FindFolders {
	RegIndex ri;
	
	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}


	public FindFoldersSim(StoredQuerySupport sqs) {
		super(sqs);
	}

	protected Metadata runImplementation() throws MetadataException,
			XdsException, LoggerException {

		MetadataCollection mc = ri.getMetadataCollection();
		List<Fol> results;
		
		// match on patient id
		results = mc.folCollection.findByPid(patient_id);

		// filter on availabilityStatus
		List<StatusValue> statuses = ri.translateStatusValues(this.status);
		results = mc.folCollection.filterByStatus(statuses, results);
		
		// filter on lastUpdateTime
		results = mc.folCollection.filterBylastUpdateTime(update_time_from, update_time_to, results);
		
		// filter on folderCodeList
		if (codes != null && !codes.isEmpty()) {
			if (codes instanceof SQCodeOr) {
				results = mc.folCollection.filterByFolderCodeList((SQCodeOr)codes, results);
			}
			else if (codes instanceof SQCodeAnd) {
					results = mc.folCollection.filterByFolderCodeList((SQCodeAnd)codes, results);
			} else {
				throw new XdsException("FindDocumentsSim: cannot cast object of type " + codes.getClass().getName() + " (from eventCode) into an instance of class SQCodeOr or SQCodeAnd", null);
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
