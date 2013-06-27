package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.sq.generic.queries.GetSubmissionSets;
import gov.nist.hit.ds.registrySim.sq.generic.support.QueryReturnType;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrySim.store.Assoc;
import gov.nist.hit.ds.registrySim.store.MetadataCollection;
import gov.nist.hit.ds.registrySim.store.RegIndex;
import gov.nist.hit.ds.registrySim.store.SubSet;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsException;

import java.util.HashSet;
import java.util.List;

public class GetSubmissionSetsSim extends GetSubmissionSets {
	RegIndex ri;
	
	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}

	public GetSubmissionSetsSim(StoredQuerySupport sqs) {
		super(sqs);
	}

	protected Metadata runImplementation() throws MetadataException,
			XdsException, LoggerException {

		MetadataCollection mc = ri.getMetadataCollection();
		
		HashSet<String> returnIds = new HashSet<String>();
		
		for (String id : uuids) {
			List<Assoc> assocs = mc.assocCollection.getBySourceDestAndType(null, id, RegIndex.AssocType.HASMEMBER);
			for (Assoc assoc : assocs) {
				String sourceId = assoc.getFrom();
				SubSet s = mc.subSetCollection.getById(sourceId);
				if (s == null)
					continue;
				returnIds.add(sourceId);
				returnIds.add(assoc.getId());
			}
		}
		
		Metadata m = new Metadata();
		m.setVersion3();
		if (sqs.returnType == QueryReturnType.LEAFCLASS || sqs.returnType == QueryReturnType.LEAFCLASSWITHDOCUMENT) {
			m = mc.loadRo(returnIds);
		} else {
			m.mkObjectRefs(returnIds);
		}
		
		return m;
	}

}
