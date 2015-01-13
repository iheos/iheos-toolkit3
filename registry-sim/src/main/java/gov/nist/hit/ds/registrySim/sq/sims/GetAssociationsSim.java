package gov.nist.hit.ds.registrySim.sq.sims;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrySim.metadataModel.Assoc;
import gov.nist.hit.ds.registrySim.metadataModel.MetadataCollection;
import gov.nist.hit.ds.registrySim.metadataModel.RegIndex;
import gov.nist.hit.ds.registrySim.sq.generic.queries.GetAssociations;
import gov.nist.hit.ds.registrySim.sq.generic.support.QueryReturnType;
import gov.nist.hit.ds.registrySim.sq.generic.support.StoredQuerySupport;
import gov.nist.hit.ds.registrysupport.logging.LoggerException;
import gov.nist.hit.ds.xdsExceptions.MetadataException;
import gov.nist.hit.ds.xdsExceptions.XdsException;

import java.util.HashSet;
import java.util.List;

public class GetAssociationsSim extends GetAssociations {
	RegIndex ri;
	
	public void setRegIndex(RegIndex ri) {
		this.ri = ri;
	}


	public GetAssociationsSim(StoredQuerySupport sqs) {
		super(sqs);
	}

	protected Metadata runImplementation() throws MetadataException,
			XdsException, LoggerException {
		
		MetadataCollection mc = ri.getMetadataCollection();
		
		HashSet<String> returnIds = new HashSet<String>();

		for (String uuid : uuids) {
			List<Assoc> as = mc.assocCollection.getBySourceOrDest(uuid, uuid);
			for (Assoc a : as) {
				returnIds.add(a.getId());
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
