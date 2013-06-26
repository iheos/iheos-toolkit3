package gov.nist.hit.ds.registrySim.sq.sims;


import gov.nist.hit.ds.registrySim.sq.generic.queries.GetDocuments;
import gov.nist.hit.ds.registrySim.sq.generic.support.QueryReturnType;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrySim.store.DocEntry;
import gov.nist.hit.ds.registrySim.store.MetadataCollection;
import gov.nist.hit.ds.registrySim.store.RegIndex;
import gov.nist.toolkit.errorRecording.ErrorContext;
import gov.nist.toolkit.errorRecording.client.XdsErrorCode.Code;
import gov.nist.toolkit.registryMetadata.Metadata;
import gov.nist.toolkit.registrysupport.logging.LoggerException;
import gov.nist.toolkit.xdsException.MetadataException;
import gov.nist.toolkit.xdsException.XdsException;

import java.util.ArrayList;
import java.util.List;

public class GetDocumentsSim extends GetDocuments {
	RegIndex ri;

	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}


	public GetDocumentsSim(StoredQuerySupport sqs) {
		super(sqs);
	}

	protected Metadata runImplementation() throws MetadataException,
	XdsException, LoggerException {

		MetadataCollection mc = ri.getMetadataCollection();

		Metadata m = new Metadata();
		m.setVersion3();
		
		if (mc.vc.updateEnabled && !(metadataLevel == null || metadataLevel.equals("1") || metadataLevel.equals("2"))) {
			sqs.er.err(Code.XDSRegistryError, new ErrorContext("Do not understand $MetadataLevel = " + metadataLevel, "ITI TF-2b: 3.18.4.1.2.3.5.1"), this);
			return new Metadata();
		} 

		if (uuids != null) {
			if (sqs.returnType == QueryReturnType.LEAFCLASS || sqs.returnType == QueryReturnType.LEAFCLASSWITHDOCUMENT) {
				m = mc.loadRo(uuids);
			} else {
				m.mkObjectRefs(uuids);
			}
		} else if (uids != null) {
			List<DocEntry> des = new ArrayList<DocEntry>();
			for (String uid : uids) {
				des.addAll(mc.docEntryCollection.getByUid(uid));
			}
			
			List<String> uuidList = new ArrayList<String>();
			for (DocEntry de : des) {
				uuidList.add(de.getId());
			}
			if (sqs.returnType == QueryReturnType.LEAFCLASS || sqs.returnType == QueryReturnType.LEAFCLASSWITHDOCUMENT) {
				m = mc.loadRo(uuidList);
			} else {
				m.mkObjectRefs(uuidList);
			}
		} else if (lids != null) {
			if (!mc.vc.updateEnabled) {
				sqs.er.err(Code.XDSRegistryError, new ErrorContext("Do not understand parameter $XDSDocumentEntryLogicalID", "ITI TF-2b: 3.18.4.1.2.3.7.5"), this);
				return new Metadata();
			}
			if (metadataLevel == null || metadataLevel.equals("1")) {
				sqs.er.err(Code.XDSRegistryError, new ErrorContext("$XDSDocumentEntryLogicalID cannot be specified with $MetadataLevel = 1", "ITI TF-2b: 3.18.4.1.2.3.7.5"), this);
				return new Metadata();
			}
			
			List<DocEntry> des = new ArrayList<DocEntry> ();
			for (String lid : lids) {
				des.addAll(mc.docEntryCollection.getByLid(lid));
			}
			List<String> uuidList = new ArrayList<String>();
			for (DocEntry de : des) {
				uuidList.add(de.getId());
			}
			if (sqs.returnType == QueryReturnType.LEAFCLASS || sqs.returnType == QueryReturnType.LEAFCLASSWITHDOCUMENT) {
				m = mc.loadRo(uuidList);
			} else {
				m.mkObjectRefs(uuidList);
			}
			
		}

		return m;
	}

}
