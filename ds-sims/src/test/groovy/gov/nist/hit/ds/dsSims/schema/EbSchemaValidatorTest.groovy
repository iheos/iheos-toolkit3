package gov.nist.hit.ds.dsSims.schema

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import spock.lang.Specification

/**
 * Created by bmajur on 11/18/14.
 */
class EbSchemaValidatorTest extends Specification {
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Register" code="rb" asyncCode="r.as">
       <implClass value="gov.nist.hit.ds.dsSims.transactions.RegisterTransaction"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
    </transaction>
    <actor displayName="Document Registry" id="ebxml"
      class="">
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
'''

    File repoDataDir
    RepositorySource repoSource
    SimId simId
    def repoName = 'EbSchemaValidatorTest'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimId('test')
        SimUtils.create('ebxml', simId, repoName)
    }

    def 'Good Example'() {
        String text = getClass().getResource('/Pnr1Doc.xml').text

        when:
        File localSchema = new File(getClass().getResource('/schema/v3/rim.xsd').toURI())
        localSchema = localSchema.getParentFile().getParentFile()
        Closure closure = { simHandle ->
            new EbSchemaValidator(simHandle, text, MetadataTypes.METADATA_TYPE_PRb, localSchema).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName, closure)
        transRunner.simHandle.event.addArtifact('Metadata', '')
        transRunner.runTest()

        then:
        !transRunner.simHandle.event.hasErrors()
    }

    def 'Bad Example'() {
        String text = getClass().getResource('/Pnr1DocBad.xml').text

        when:
        File localSchema = new File(getClass().getResource('/schema/v3/rim.xsd').toURI())
        localSchema = localSchema.getParentFile().getParentFile()
        Closure closure = { simHandle ->
            new EbSchemaValidator(simHandle, text, MetadataTypes.METADATA_TYPE_PRb, localSchema).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName, closure)
        transRunner.simHandle.event.addArtifact('Metadata', '')
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasErrors()
    }

    def 'Bad Example - will not xml parse'() {
        String text = getClass().getResource('/Pnr1DocBader.xml').text

        when:
        File localSchema = new File(getClass().getResource('/schema/v3/rim.xsd').toURI())
        localSchema = localSchema.getParentFile().getParentFile()
        Closure closure = { simHandle ->
            new EbSchemaValidator(simHandle, text, MetadataTypes.METADATA_TYPE_PRb, localSchema).asPeer().run()
        }
        def transRunner = new TransactionRunner('rb', simId, repoName, closure)
        transRunner.simHandle.event.addArtifact('Metadata', '')
        transRunner.runTest()

        then:
        transRunner.simHandle.event.hasErrors()
    }

}
