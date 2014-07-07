package gov.nist.hit.ds.simSupport.utilities
import gov.nist.hit.ds.repository.api.*
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.repository.simple.SimpleType
/**
 * Created by bmajur on 7/5/14.
 */
class RepoUtils {

    static Asset asset(ArtifactId id, Repository repository) {
        init()
        return repository.getAsset(id)
    }

    static Asset assetIfAvailable(ArtifactId id, Repository repository) {
        try {
            return asset(id, repository)
        } catch (Exception e) { return null }
    }

    static Asset mkAsset(String name, Type type, ArtifactId repositoryId) {
        init()
        return mkAsset(name, type, repository(repositoryId))
    }

    static Asset mkAsset(String name, Type type, Repository repository) {
        init()
        return repository.createNamedAsset(name, name, type, name)
    }

    static Asset mkAsset(String name, Repository repository) {
        init()
        return mkAsset(name, new SimpleType('simpleType'), repository)
    }

    static rmAsset(ArtifactId aId, Repository repository) {
        init()
        try {
            repository.deleteAsset(aId)
        } catch (RepositoryException e) {}
    }

    static Asset mkChild(String name, Asset parent) {
        init()
        return mkChild(name, parent, new SimpleType('simpleType'))
    }

    static Asset mkChild(String name, Asset parent, Type type) {
        init()
        Asset a = mkAsset(name, type, parent.getRepository())
        parent.addAsset(a)
        return a
    }

    // needs test
    static Asset childWithId(Asset asset, ArtifactId aId) {
        for (AssetIterator it= asset.getAssets() ; it.hasNextAsset() ; ) {
            def a = it.nextAsset()
            println a.id
            if (a.id.idString == aId.idString) { return a  }
        }
        return null
    }

    static Repository repository(ArtifactId repositoryId) {
        init()
        return SimSupport.repoFactory.getRepository(repositoryId)
    }

    static boolean repositoryExists(ArtifactId repositoryId) {
        return Configuration.repositoryExists(SimSupport.repoSource, repositoryId)
    }

    static Repository mkRepository(String name, Type type) {
        init()
        return SimSupport.repoFactory.createNamedRepository(name, '', type, name)
    }

    static rmRepository(ArtifactId id) {
        init()
        SimSupport.repoFactory.deleteRepository(id)
    }

    static init() { SimSupport.initialize() }
}
