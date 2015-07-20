package gov.nist.hit.ds.repoSupport
import gov.nist.hit.ds.repository.api.*
import gov.nist.hit.ds.repository.shared.PropertyKey
import gov.nist.hit.ds.repository.shared.data.AssetNode
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.repository.simple.SimpleId
import gov.nist.hit.ds.repository.simple.SimpleType
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

/**
 * Created by bmajur on 7/5/14.
 */

class RepoUtils {
    private static Logger log = Logger.getLogger(RepoUtils);
    static RepositorySource repoSource = null
    static RepositoryFactory repoFactory

    static Asset asset(ArtifactId id, Repository repository) {
        init()
        return repository.getAsset(id)
    }

    static Asset asset(ArtifactId id, String repositoryId) {
        init()
        return repository(new SimpleId(repositoryId)).getAsset(id)
    }

    static Asset asset(AssetNode assetNode) { asset(new SimpleId(assetNode.assetId), assetNode.repId) }

    static Asset assetIfAvailable(ArtifactId id, Repository repository) {
        try {
            return asset(id, repository)
        } catch (Exception e) { return null }
    }

    static Asset parent(Asset a) {
        String parentId = a.getProperty(PropertyKey.PARENT_ID)
        if (!parentId) return null
        String repoId = a.getProperty(PropertyKey.REPOSITORY_ID)
        return asset(new SimpleId(parentId), repository(new SimpleId(repoId)))
    }

    static Asset child(String name, Asset parent) {
        return parent.getChildByName(name)
    }

    static Asset getChildIfAvailable(String name, Asset parent) {
        try {
            return parent.getChildByName(name)
        } catch (RepositoryException e) { return null }
    }
    static Asset child(String name, Repository parent) {
        return parent.getChildByName(name)
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

    static rmAsset(String name, Repository repository) {
        init()
        try {
            Asset a = repository.getChildByName(name)
            if (a) repository.deleteAsset(a.getId())
        } catch (RepositoryException e) {}
    }

    static Asset mkChild(String name, Asset parent) {
        init()
        return mkChild(name, parent, new SimpleType('simpleType'))
    }

    static Asset mkChild(String name, Asset parent, Type type) {
        init()
        Asset a = mkAsset(name, type, parent.getRepository())
        return parent.addChild(a)
    }

//    // needs test
//    static Asset childWithId(Asset asset, ArtifactId aId) {
//        assert aId
//        for (AssetIterator it= asset.getAssets() ; it.hasNextAsset() ; ) {
//            def a = it.nextAsset()
//            assert a.id
//            assert a.id.idString
//            if (a.id.idString == aId.idString) { return a  }
//        }
//        return null
//    }

    static Repository repository(ArtifactId repositoryId) {
        init()
        return repoFactory.getRepository(repositoryId)
    }

    static ArtifactId repositoryId(Repository repository) {
        init()
        return repository.getId()
    }

    static boolean repositoryExists(ArtifactId repositoryId) {
        return Configuration.repositoryExists(repoSource, repositoryId)
    }

    static Repository mkRepository(String name, Type type) {
        init()
        def repo = repoFactory.createNamedRepository(name, '', type, name)
        repo.setSource(repoSource)
        return repo
    }

    static rmRepository(ArtifactId id) {
        init()
        repoFactory.deleteRepository(id)
    }

    /**
     * Get repository.  Create it if it doesn't already exist.
     * @param repositoryName
     * @param type - used only if it needs to be created - not verified
     * @return repository
     */
    static Repository getRepository(String repositoryName, Type type) {
        def source = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        RepositoryFactory factory = new RepositoryFactory(source)
        RepositoryIterator iter = factory.getRepositories()
        while(iter.hasNextRepository()) {
            Repository r = iter.nextRepository()
            r.setSource(source)
            if (r.id.idString == repositoryName) {
                log.debug("Found repository ${repositoryName}")
                return r
            }
        }
        RepoUtils.log.debug("Creating Repository ${repositoryName}")
        return mkRepository(repositoryName, type)
    }

    static Repository getRepository(String repositoryName) {
        return getRepository(repositoryName, new SimpleType('simpleRepos'))
    }

    static init() {
        if (repoSource) return
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoFactory = new RepositoryFactory(repoSource)
    }
}
