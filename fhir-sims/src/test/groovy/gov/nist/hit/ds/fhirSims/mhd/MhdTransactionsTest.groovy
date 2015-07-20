package gov.nist.hit.ds.fhirSims.mhd
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repoSupport.RepoUtils
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.shared.PropertyKey
import gov.nist.hit.ds.repository.shared.data.AssetNode
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.api.ValidationApi
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.utilities.io.Io
import spock.lang.Specification
/**
 * Created by bmajur on 12/4/14.
 */
class MhdTransactionsTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Doc Reference Validation" id="drv" code="drv">
        <implClass value="DocRefTransaction"/>
    </transaction>
    <transaction name="Doc Manifest Validation" id="dmv" code="dmv">
        <implClass value="DocManTransaction"/>
    </transaction>
    <transaction name="Provide Doc Reference Validation" id="pdr" code="pdr">
        <implClass value="FeedTransaction"/>
    </transaction>
    <actor name="MHD Document Recipient" id="mhddocrec">
        <implClass value=""/>
        <!-- Pseudo-transactions - really only speciality validators -->
        <transaction id="drv"/>
        <transaction id="dmv"/>
        <!-- A real transaction -->
        <transaction id="pdr"/>
    </actor>
</ActorsTransactions>
'''
    File repoDataDir
    RepositorySource repoSource
    SimId simId
    def repoName = 'Sim'
    SimHandle simHandle

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
    }

    def 'Doc Ref XML Test'() {
        when:
        String xml
        def url = getClass().classLoader.getResource('mhd/full_docref.xml')
        url.withInputStream {
            xml = Io.getStringFromInputStream(it)
        }
        AssetNode an = new ValidationApi().validateRequest('drv', xml, 'Content-Type: application/atom+xml')
        Asset a = RepoUtils.asset(an)

        then:
        a.getProperty(PropertyKey.STATUS) == 'SUCCESS'
    }

    def 'Doc Ref JSON Test'() {
        when:
        String xml
        def url = getClass().classLoader.getResource('mhd/minimal_docref.json')
        url.withInputStream {
            xml = Io.getStringFromInputStream(it)
        }
        AssetNode an = new ValidationApi().validateRequest('drv', xml, 'Content-Type: application/atom+xml')
        Asset a = RepoUtils.asset(an)
        println "Asset is ${a.id.idString}"
        println "Final status is ${a.getProperty(PropertyKey.STATUS)}"
        println "type ${a.getProperty(PropertyKey.ASSET_TYPE)}"
        println "keys ${a.getProperties().keySet().toString()}"

        then:
        a.getProperty(PropertyKey.STATUS) == 'SUCCESS'
    }

    def 'Doc Man XML Test'() {
        when:
        String xml
        def url = getClass().classLoader.getResource('mhd/minimal_docman.xml')
        url.withInputStream {
            xml = Io.getStringFromInputStream(it)
        }
        AssetNode an = new ValidationApi().validateRequest('dmv', xml, 'Content-Type: application/atom+xml')
        Asset a = RepoUtils.asset(an)

        then:
        a.getProperty(PropertyKey.STATUS) == 'SUCCESS'
    }

    def 'Feed XML Test'() {
        when:
        String xml
        def url = getClass().classLoader.getResource('mhd/feed.xml')
        url.withInputStream {
            xml = Io.getStringFromInputStream(it)
        }
        AssetNode an = new ValidationApi().validateRequest('pdr', xml, 'Content-Type: application/atom+xml')
        Asset a = RepoUtils.asset(an)

        then:
        a.getProperty(PropertyKey.STATUS) == 'SUCCESS'
    }

    def 'Feed JSON Test'() {
        when:
        String xml
        def url = getClass().classLoader.getResource('mhd/feed.json')
        url.withInputStream {
            xml = Io.getStringFromInputStream(it)
        }
        AssetNode an = new ValidationApi().validateRequest('pdr', xml, 'Content-Type: application/json+fhir')
        Asset a = RepoUtils.asset(an)

        then:
        a.getProperty(PropertyKey.STATUS) == 'SUCCESS'
    }
}
