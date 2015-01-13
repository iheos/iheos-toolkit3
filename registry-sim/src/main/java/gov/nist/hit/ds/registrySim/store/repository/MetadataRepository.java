package gov.nist.hit.ds.registrySim.store.repository;

import gov.nist.hit.ds.registrySim.exception.MetadataCannotBeOverwrittenException;
import gov.nist.hit.ds.repository.api.*;
import gov.nist.hit.ds.repository.simple.SimpleId;
import gov.nist.hit.ds.repository.simple.SimpleType;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;

/**
 * Generate/reference the repository where metadata is stored.  This is used by
 * actor simulators that manage metadata.
 */
public class MetadataRepository {
    private Simulator simulator;
    private Asset metadataDirectory;

    public MetadataRepository(Simulator simulator) {
        this.simulator = simulator;
        if (simulator == null)
            throw new ToolkitRuntimeException("MetadataRepository: SimId is null");
        initialize();
    }

    private void initialize() {
        try {
            // by default this asset is not auto-flushed
            metadataDirectory = simulator.repository().getAsset(new SimpleId("metadata"));
        } catch (RepositoryException e) {
            try {
                metadataDirectory = simulator.repository().createNamedAsset("metadata", "Metadata Repository", new SimpleType("metadataAsset"), "metadata");
            } catch (Exception e1) {
                throw new ToolkitRuntimeException("MetadataRepository: cannot load/create metadata directory", e1);
            }
        }
    }

    public Asset saveObject(byte[] data, String id) throws MetadataCannotBeOverwrittenException {
        ArtifactId assetId = new SimpleId(id);
        // metadata cannot be overwritten
        if (assetExists(assetId)) throw new MetadataCannotBeOverwrittenException("MetadataRepository: attempt to overwrite <" + id + ">");

        try {
            Asset registryObjectAsset;
            registryObjectAsset = simulator.repository().createNamedAsset(id, "Simulator", new SimpleType("metadataObject"), id);
            registryObjectAsset.updateContent(data);
            registryObjectAsset.setMimeType("text/xml");
            metadataDirectory.addAsset(registryObjectAsset);
            return registryObjectAsset;
        } catch (Exception e1) {
            throw new ToolkitRuntimeException("MetadataRepository: cannot persist metadata object <" + id + ">", e1);
        }
    }

    // does asset exist as child of metadataDirectory?
    private boolean assetExists(ArtifactId assetId) {

// TODO replace this with real repository code - not sure this is the best approach
        try {
            Asset registryObjectAsset;
            registryObjectAsset = simulator.repository().getAsset(assetId);
            return registryObjectAsset != null;
        } catch (RepositoryException e) {
            return false;
        }
    }
}
