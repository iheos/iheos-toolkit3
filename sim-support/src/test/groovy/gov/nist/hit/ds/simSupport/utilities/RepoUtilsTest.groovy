package gov.nist.hit.ds.simSupport.utilities

import gov.nist.hit.ds.repository.api.ArtifactId
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.repository.simple.SimpleId
import gov.nist.hit.ds.repository.simple.SimpleType
import spock.lang.Specification
/**
 * Created by bmajur on 7/5/14.
 */
class RepoUtilsTest extends Specification {
    Repository repos
    String assetName = 'one'
    ArtifactId aId = new SimpleId(assetName)

    def setup() {
        repos = RepoUtils.mkRepository('TestRepo', new SimpleType('simpleRepos'))
    }

    def cleanup() {
        RepoUtils.rmAsset(aId, repos)
    }

    def 'Created asset should be findable'() {
        given:
        RepoUtils.rmAsset(aId, repos)

        when:
        RepoUtils.mkAsset(assetName, repos)

        then:
        RepoUtils.asset(new SimpleId(assetName), repos)

    }

    def 'Deleting asset should be detectable'() {
        given:
        RepoUtils.mkAsset(assetName, repos)

        when:
        RepoUtils.rmAsset(aId, repos)

        then:
        RepoUtils.assetIfAvailable(new SimpleId(assetName), repos) == null
    }

    def 'Repository should be deletable' () {
        expect:
        RepoUtils.repositoryExists(repos.id)

        when:
        RepoUtils.rmRepository(repos.id)

        then:
        !RepoUtils.repositoryExists(repos.id)
    }

    def 'Child should be findable by id'() {
        given:
        def parentAsset = RepoUtils.mkAsset('parent', repos)
        def childAsset = RepoUtils.mkChild('child', parentAsset)

        when:
        def childAsset2 = RepoUtils.childWithId(parentAsset, new SimpleId('child'))

        then:
        childAsset2 != null
        childAsset.id.idString == childAsset2.id.idString
    }
}
