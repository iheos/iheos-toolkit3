package gov.nist.hit.ds.registrySim.store.repository;

import gov.nist.hit.ds.registryMetadata.Metadata;
import gov.nist.hit.ds.registrysupport.MetadataSupport;
import gov.nist.hit.ds.repository.api.Asset;
import gov.nist.hit.ds.repository.api.Repository;
import gov.nist.hit.ds.repository.api.RepositoryException;
import gov.nist.hit.ds.repository.api.RepositoryFactory;
import gov.nist.hit.ds.repository.api.RepositorySource.Access;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.utilities.xml.OMFormatter;
import gov.nist.hit.ds.utilities.xml.XmlUtil;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import org.apache.axiom.om.OMElement;

public class MetadataRepository {
	SimId simId;

	public MetadataRepository(SimId simId) {
		this.simId = simId;
		if (simId == null)
			throw new ToolkitRuntimeException("MetadataRepository: SimId is null");
	}

	// TODO need to set assetId to metadata element id - sent email to Sunil about this.
	void saveObject(OMElement ele) {
		OMElement wrapper = XmlUtil.createElement("LeafRegistryObjectList", MetadataSupport.ebRIMns3);
		wrapper.addChild(ele);
		String id = new Metadata().getId(ele);
		byte[] data = new OMFormatter(wrapper).toString().getBytes();
		Repository repository = getRepository();
		Asset asset;
		try {
			asset = repository.getAsset(new SimpleId(id));
		} catch (RepositoryException e) {
			try {
				// need metadata element id here
				asset = repository.createAsset(id, "Simulator", new SimpleType("metadataObject"));
				asset.updateContent(data);
				asset.setMimeType("text/xml");
			} catch (Exception e1) {
				throw new ToolkitRuntimeException("MetadataRepository: cannot persist metadata object <" + id + ">", e1);
			}
		}
	}

	private Repository getRepository()  {
		RepositoryFactory fact = new RepositoryFactory(Configuration.getRepositorySrc(Access.RW_EXTERNAL));
		Repository repos = fact.createNamedRepository(
				"Metadata Store", 
				"Metadata Store", 
				new SimpleType("metadataRepos"),  // repository type
				simId.getId() + "-metadata"    // repository name
				);
		return repos;
	}


}
