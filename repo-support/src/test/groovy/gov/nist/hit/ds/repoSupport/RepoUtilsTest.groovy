package gov.nist.hit.ds.repoSupport
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.repository.simple.SimpleType
import gov.nist.hit.ds.toolkit.Toolkit
import spock.lang.Specification
/**
 * Created by bmajur on 7/5/14.
 */
class RepoUtilsTest extends Specification {
    Repository repos
    String assetName = 'one'

    def setup() {
        Toolkit.initialize()
        Configuration.configuration()
        RepoUtils.init()
        repos = RepoUtils.mkRepository('TestRepo', new SimpleType('simpleRepos'))
    }

    def cleanup() {
        RepoUtils.rmRepository(repos.getId())
    }

    def 'Created asset should be findable'() {
        when:
        def asset = RepoUtils.mkAsset(assetName, repos)

        then:
        RepoUtils.assetIfAvailable(asset.id, repos)

    }

    def 'Deleting asset should be detectable'() {
        given:
        def asset = RepoUtils.mkAsset(assetName, repos)

        when:
        RepoUtils.rmAsset(asset.id, repos)

        then:
        !RepoUtils.assetIfAvailable(asset.id, repos)
    }

    def 'Repository should be deletable' () {
        expect:
        RepoUtils.repositoryExists(repos.id)   // created in setup()

        when:
        RepoUtils.rmRepository(repos.id)

        then:
        !RepoUtils.repositoryExists(repos.id)
    }

}
