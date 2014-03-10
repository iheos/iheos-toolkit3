package gov.nist.toolkit.simulators.sim.reg.sq;

import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrysupport.logging.LoggerException;
import gov.nist.toolkit.simulators.sim.reg.store.Assoc;
import gov.nist.toolkit.simulators.sim.reg.store.MetadataCollection;
import gov.nist.toolkit.simulators.sim.reg.store.RegIndex;
import gov.nist.toolkit.valregmsg.registry.storedquery.generic.GetAssociations;
import gov.nist.toolkit.valregmsg.registry.storedquery.generic.StoredQueryFactory.QueryReturnType;
import gov.nist.toolkit.valregmsg.registry.storedquery.support.StoredQuerySupport;

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
