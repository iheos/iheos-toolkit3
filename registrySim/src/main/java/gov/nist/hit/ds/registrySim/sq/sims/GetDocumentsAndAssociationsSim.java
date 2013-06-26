package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.registrySim.sq.generic.queries.GetDocumentsAndAssociations;
import gov.nist.hit.ds.registrySim.sq.generic.support.QueryReturnType;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrySim.store.Assoc;
import gov.nist.hit.ds.registrySim.store.DocEntry;
import gov.nist.hit.ds.registrySim.store.MetadataCollection;
import gov.nist.hit.ds.registrySim.store.RegIndex;
import gov.nist.toolkit.registryMetadata.Metadata;
import gov.nist.toolkit.registrysupport.logging.LoggerException;
import gov.nist.toolkit.xdsException.MetadataException;
import gov.nist.toolkit.xdsException.XdsException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GetDocumentsAndAssociationsSim extends GetDocumentsAndAssociations {
	RegIndex ri;
	
	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}


	public GetDocumentsAndAssociationsSim(StoredQuerySupport sqs) {
		super(sqs);
	}

	protected Metadata runImplementation() throws MetadataException,
			XdsException, LoggerException {

		MetadataCollection mc = ri.getMetadataCollection();
		
		HashSet<String> returnIds = new HashSet<String>();
		
		if (uuids != null) {
			for (String id : uuids) {
				DocEntry de = mc.docEntryCollection.getById(id);
				if (de != null)
					returnIds.add(de.getId());
			}
		}
		
		if (uids != null) {
			for (String uid : uids) {
				List<DocEntry> des = mc.docEntryCollection.getByUid(uid);
				for (DocEntry de : des) {
					returnIds.add(de.getId());
				}
			}
		}
		
		List<Assoc> assocs = new ArrayList<Assoc>();
		
		for (String id : returnIds) {
			List<Assoc> as = mc.assocCollection.getBySourceOrDest(id, id);
			assocs.addAll(as);
		}
		
		for (Assoc a : assocs)
			returnIds.add(a.getId());
		
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
