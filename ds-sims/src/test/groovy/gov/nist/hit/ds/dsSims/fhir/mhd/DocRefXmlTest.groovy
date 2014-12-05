package gov.nist.hit.ds.dsSims.fhir.mhd
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.RepositorySource
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
class DocRefXmlTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Doc Reference Validation" id="drv" code="drv">
        <implClass value="gov.nist.hit.ds.dsSims.fhir.mhd.validators.DocRefXml"/>
    </transaction>
    <transaction name="Doc Manifest Validation" id="dmv" code="dmv">
        <implClass value="gov.nist.hit.ds.dsSims.fhir.mhd.validators.DocManXml"/>
    </transaction>
    <transaction name="Provide Doc Reference Validation" id="pdr" code="pdr">
        <implClass value="gov.nist.hit.ds.dsSims.fhir.mhd.validators.DocRefXml"/>
    </transaction>
    <transaction name="Doc Ref Validation - XML" id="drvx" code="drvx">
        <implClass value="gov.nist.hit.ds.dsSims.fhir.mhd.validators.PdrXml"/>
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
//        simId = new SimId('DocRefXmlTest')
//        simHandle = SimUtils.create('mhddocrec', simId, repoName)
    }

    def 'XML Test'() {
        when:
        String xml
        def url = getClass().classLoader.getResource('mhd/full_docref.xml')
        url.withInputStream {
            xml = Io.getStringFromInputStream(it)
        }
        Asset a = new ValidationApi().validateRequest('drv', xml)

        then:
        a.properties.getProperty('status') != 'ERROR'
    }

    def 'JSON Test'() {
        when:
        String xml
        def url = getClass().classLoader.getResource('mhd/minimal_docref.json')
        url.withInputStream {
            xml = Io.getStringFromInputStream(it)
        }
        Asset a = new ValidationApi().validateRequest('drv', xml)

        then:
        a.properties.getProperty('status') != 'ERROR'
    }
}
