package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.registrySim.sq.generic.queries.GetRelatedDocuments;
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

public class GetRelatedDocumentsSim extends GetRelatedDocuments {
	RegIndex ri;

	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}


	public GetRelatedDocumentsSim(StoredQuerySupport sqs) {
		super(sqs);
	}

	protected Metadata runImplementation() throws MetadataException,
	XdsException, LoggerException {

		MetadataCollection mc = ri.getMetadataCollection();

		//		HashSet<String> uuids = new HashSet<String>();
		
		List<RegIndex.AssocType> assocTypes = new ArrayList<RegIndex.AssocType>();
		
		for (String atype : assoc_types) {
			assocTypes.add(RegIndex.getAssocType(atype));
		}

		HashSet<String> uuids = new HashSet<String>();

		HashSet<String> docEntryIds = new HashSet<String>(); 

		if (uuid != null) {
			DocEntry de = mc.docEntryCollection.getById(uuid);
			if (de != null)
				docEntryIds.add(de.getId());
		}

		if (uid != null) {
			List<DocEntry> des = mc.docEntryCollection.getByUid(uid);
			for (DocEntry de : des) {
				docEntryIds.add(de.getId());
			}
		}
		
		uuids.addAll(docEntryIds);
		
		for (String id : docEntryIds) {
			List<Assoc> as = mc.assocCollection.getBySourceOrDest(id, id);
			for (Assoc a : as) {
				if (!assocTypes.contains(a.getAssocType()))
					continue;
				
				String sourceId = a.getFrom();
				String targetId = a.getTo();
				DocEntry sourceDoc = mc.docEntryCollection.getById(sourceId);
				DocEntry targetDoc = mc.docEntryCollection.getById(targetId);
				
				// One side of Association points to our focus DocEntry
				// if the other side points to a DocEntry then include
				// the Assoc and DocEntry in the output
				if (!sourceId.equals(id) && sourceDoc != null) {
					uuids.add(sourceId);
					uuids.add(a.getId());
				}

				if (!targetId.equals(id) && targetDoc != null) {
					uuids.add(targetId);
					uuids.add(a.getId());
				}
					
					
			}
		}
		
		if (uuids.size() == 1)
			uuids.clear();


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
