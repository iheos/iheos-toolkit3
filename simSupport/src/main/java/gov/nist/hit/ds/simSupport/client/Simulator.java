package gov.nist.hit.ds.simSupport.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.repository.api.*;
import gov.nist.hit.ds.repository.simple.Configuration;
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Holder of a simulator definition.  Simulators are identified by their
 * SimId.  But, since sometimes it is necessary to create combimed simulators, a
 * combined Repository/Registry for example, this class does not have an assigned
 * SimId.  Instead, the individual simulator configurations, represented by instances
 * of the class SimulatorConfig, each have SimIds.
 *
 * @author bill
 */
public class Simulator implements Serializable, IsSerializable {
    private static final long serialVersionUID = 8914156242225793229L;
    private List<ActorSimConfig> configs = new ArrayList<ActorSimConfig>();
    private SimId simId;
    private Asset simAsset = null;
    private ArtifactId repositoryId = null;

    public ArtifactId getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(ArtifactId repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Repository repository() {
        if (repositoryId == null) throw new ToolkitRuntimeException("Simulator: repositoryId not initialized");
        RepositoryFactory repositoryFactory = new RepositoryFactory(Configuration.getRepositorySrc(RepositorySource
                .Access.RW_EXTERNAL));
        return repositoryFactory.getRepository(repositoryId);
    }

    /**
     * Should only be called by the factory class.
     */
    public Simulator() {
        this.simId = new SimId();
    }

    /**
     * Should only be called by the factory class.
     */
    public Simulator(SimId simId) { this.simId = (simId == null) ? new SimId() : simId; }

    public Simulator setSimAsset(Asset simAsset) {
        this.simAsset = simAsset;
        return this;
    }

    public Asset getSimAsset() {
        return simAsset;
    }

    public Simulator add(ActorSimConfig config) {
        configs.add(config);
        return this;
    }

    public Simulator addAll(List<ActorSimConfig> configs) {
        this.configs.addAll(configs);
        return this;
    }

    public List<ActorSimConfig> getConfigs() {
        return configs;
    }

    public SimId getSimId() {
        return simId;
    }

    public ActorSimConfig getActorSimConfig(ActorType actorType) {
        for (ActorSimConfig config : configs) {
            if (config.isActorType(actorType))
                return config;
        }
        throw new ToolkitRuntimeException("ActorType <" + actorType + "> does not exist in Simulator <" + simId + ">");
    }

    public int size() { return configs.size(); }

    public ActorSimConfig getConfig(int i) { return configs.get(i); }

    public String getEndpoint(TransactionType transType, TlsType tlsType, AsyncType asyncType) {
        for (ActorSimConfig config : configs) {
            String endpoint = config.getEndpoint(transType, tlsType, asyncType);
            if (endpoint != null)
                return endpoint;
        }
        return null;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();

        for (ActorSimConfig config : configs) {
            buf.append(config.toString());
        }

        return buf.toString();
    }
}
