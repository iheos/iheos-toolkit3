package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.sq.generic.queries.GetFoldersForDocument;
import gov.nist.hit.ds.registrySim.sq.generic.support.QueryReturnType;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrySim.store.Assoc;
import gov.nist.hit.ds.registrySim.store.DocEntry;
import gov.nist.hit.ds.registrySim.store.Fol;
import gov.nist.hit.ds.registrySim.store.MetadataCollection;
import gov.nist.hit.ds.registrySim.store.RegIndex;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GetFoldersForDocumentSim extends GetFoldersForDocument {
	RegIndex ri;
	
	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}


	public GetFoldersForDocumentSim(StoredQuerySupport sqs) {
		super(sqs);
	}

	protected Metadata runImplementation() throws MetadataException,
			XdsException, LoggerException {
		
		MetadataCollection mc = ri.getMetadataCollection();
		
		List<String> docIds = new ArrayList<String>();
		
		if (uuid != null) {
			DocEntry de = mc.docEntryCollection.getById(uuid);
			if (de != null)
				docIds.add(de.getId());
		}
		
		if (uid != null) {
			List<DocEntry> des = mc.docEntryCollection.getByUid(uid);
			for (DocEntry de : des) {
				docIds.add(de.getId());
			}
		}
		
		HashSet<String> folIds = new HashSet<String>();
		
		for (String docid : docIds) {
			List<Assoc> as = mc.assocCollection.getBySourceDestAndType(null, docid, RegIndex.AssocType.HASMEMBER);
			for (Assoc a : as) {
				String sourceId = a.getFrom();
				Fol f = mc.folCollection.getById(sourceId);
				if (f != null)
					folIds.add(f.getId());
			}
		}

		
		Metadata m = new Metadata();
		m.setVersion3();
		if (sqs.returnType == QueryReturnType.LEAFCLASS || sqs.returnType == QueryReturnType.LEAFCLASSWITHDOCUMENT) {
			m = mc.loadRo(folIds);
		} else {
			m.mkObjectRefs(folIds);
		}
		
		return m;
	}

}
