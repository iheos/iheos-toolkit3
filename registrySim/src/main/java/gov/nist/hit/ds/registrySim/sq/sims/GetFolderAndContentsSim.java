package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.errorRecording.ErrorContext;
import gov.nist.hit.ds.errorRecording.client.XdsErrorCode.Code;
import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.sq.generic.queries.GetFolderAndContents;
import gov.nist.hit.ds.registrySim.sq.generic.support.QueryReturnType;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrySim.store.Assoc;
import gov.nist.hit.ds.registrySim.store.DocEntry;
import gov.nist.hit.ds.registrySim.store.Fol;
import gov.nist.hit.ds.registrySim.store.MetadataCollection;
import gov.nist.hit.ds.registrySim.store.RegIndex;
import gov.nist.hit.ds.registrySim.store.Ro;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsException;

import java.util.ArrayList;
import java.util.List;

public class GetFolderAndContentsSim extends GetFolderAndContents {
	RegIndex ri;
	
	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}


	public GetFolderAndContentsSim(StoredQuerySupport sqs) {
		super(sqs);
	}

	protected Metadata runImplementation() throws MetadataException,
			XdsException, LoggerException {
		
		MetadataCollection mc = ri.getMetadataCollection();
		List<Ro> results = new ArrayList<Ro>();
		
		Fol fol = null;
		if (fol_uuid != null) {
			fol = mc.folCollection.getById(fol_uuid);
		}
		else if (fol_uid != null) {
			fol = mc.folCollection.getByUid(fol_uid);
		} else {
			getStoredQuerySupport().er.err(Code.XDSRegistryError, new ErrorContext("Internal error: uid and uuid both null", null), this);
		}
		
		if (fol == null) {
			return new Metadata();
		} else {
			results.add(fol);
		}

		String folid = fol.getId();
		
		List<Assoc> folAssocs = mc.assocCollection.getBySourceDestAndType(folid, null, RegIndex.AssocType.HASMEMBER);
		List<DocEntry> docEntries = new ArrayList<DocEntry>();

//		results.addAll(folAssocs);
		
		for (Assoc a : folAssocs) {
			String toId = a.getTo();
			
			DocEntry de = mc.docEntryCollection.getById(toId);
			if (de != null) {
				docEntries.add(de);
				continue;
			}
			
		}
		
		// next remove docs that don't meet filter requirements based on formatCode and confidentialityCode
		try {
			if (format_code != null)
				docEntries = mc.docEntryCollection.filterByFormatCode(format_code, docEntries);
			if (conf_code != null)
				docEntries = mc.docEntryCollection.filterByConfCode(conf_code, docEntries);
		} catch (Exception e) {
			getStoredQuerySupport().er.err(Code.XDSRegistryError, new ErrorContext("Error filtering DocumentEntries by formatCode or confidentialityCode: " + e.getMessage(), null), this);
			return new Metadata();
		}

		List<Ro> ros = new ArrayList<Ro>();
		ros.add(fol);
		ros.addAll(docEntries);

		// add in assocs where source and target are in response
		// the source attributes all ref the folder
		for (Assoc a : folAssocs) {
			String target = a.getTo();
			
			if (mc.docEntryCollection.hasObject(target, docEntries))
				ros.add(a);
		}
		
		List<String> uuids = mc.getIdsForObjects(ros);

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
