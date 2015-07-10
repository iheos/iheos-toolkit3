package gov.nist.hit.ds.dsSims.eb.transactions
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.dsSims.eb.topLevel.ValidatorManager
import gov.nist.hit.ds.dsSims.eb.topLevel.ValidatorManagerV2
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.shared.ValidationLevel
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.soapSupport.core.Endpoint
import gov.nist.hit.ds.tkapis.validation.ValidateMessageResponse
import gov.nist.hit.ds.toolkit.Toolkit
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil
import gov.nist.toolkit.installation.Installation
import org.apache.log4j.BasicConfigurator
import spock.lang.Specification
/**
 * Created by bmajur on 9/24/14.
 */
class PnrRecipientV2Test extends Specification {
    static String config = '''
<ActorsTransactions>
    <transaction name="Provide and Register" id="prb" code="prb">
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.dsSims.eb.transactions.Pnr"/>
        <params multipart="true" soap="true"/>
    </transaction>
    <actor name="Document Recipient" id="docrec">
        <implClass value="gov.nist.hit.ds.simSupport.factories.DocumentRecipientActorFactory"/>
        <transaction id="prb"/>
    </actor>
</ActorsTransactions>
'''
    def simId
    def repoName = 'PnrRecipientTest'
    def endpoint = new Endpoint('http://localhost:8080/xdstools3/sim/1234/docrec/prb')
    File repoDataDir
    RepositorySource repoSource
    SimHandle simHandle
    def factory = new ActorTransactionTypeFactory()

    def setup() {
        BasicConfigurator.configure()
        // Initialize V3 toolkit
        SimSupport.initialize()
        // Initialize V2 toolkit
        Installation.installation().warHome(Toolkit.warRootFile)
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(config)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('PnrTest')
        SimUtils.recreate('docrec', simId, repoName)
    }

    ValidatorManagerV2 vMan
    ValidateMessageResponse response

    def run(String header, String body) {
        vMan = new ValidatorManagerV2()
        try {
            response = vMan.validateMessage(ValidatorManager.soapAction, header, body.getBytes(), ValidationLevel.INFO)
        } catch (Throwable t) {
            println ExceptionUtil.exception_details(t)
            SimUtils.close(simHandle)
        }
        simHandle = vMan.simHandle
    }

    def 'Test v2 initialization'() {
        when:
        File toolkitxFile = Installation.installation().toolkitxFile()

        then:
        !toolkitxFile.toString().startsWith('toolkitx')
    }

    def 'Test almost good message'() {
        setup:
        def header = getClass().classLoader.getResource('pnr/good/PnRSoapHeader.txt').text
        def body = getClass().classLoader.getResource('pnr/good/PnR1DocSoapBody.txt').text

        when:
        run(header,body)
        println simHandle.event.errorAssertionIds()

        then:
        response.validationStatus != ValidationStatus.OK
    }

    def 'Test wrong boundary'() {
        setup:
        def header = getClass().classLoader.getResource('pnr/wrongboundary/PnRSoapHeader.txt').text
        def body = getClass().classLoader.getResource('pnr/wrongboundary/PnR1DocSoapBody.txt').text

        when:
        run(header,body)

        then:
        response.validationStatus != ValidationStatus.OK
    }

    def 'Not parsing soap action'() {
        setup:
        def header = getClass().classLoader.getResource('pnr/nosoapaction/Request Header.txt').text
        def body = getClass().classLoader.getResource('pnr/nosoapaction/Request Body.bytes').text

        when:
        run(header,body)

        then:
        response.validationStatus != ValidationStatus.OK
    }

    def 'Unknown error'() {
        setup:
        def header = getClass().classLoader.getResource('pnr/unknownerror/Header.txt').text
        def body = getClass().classLoader.getResource('pnr/unknownerror/Message.bytes').text

        when:
        run(header,body)

        then:
        response.validationStatus != ValidationStatus.OK
    }

    def 'Response message'() {
        setup:
        def header = getClass().classLoader.getResource('pnr/responsemessage/Request Header.txt').text
        def body = getClass().classLoader.getResource('pnr/responsemessage/Request Body.bytes').text

        when:
        run(header,body)

        then:
        response.validationStatus != ValidationStatus.OK
    }
}
