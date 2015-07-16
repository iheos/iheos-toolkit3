package gov.nist.hit.ds.dsSims.direct

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.repository.api.RepositorySource
import gov.nist.hit.ds.repository.simple.Configuration
import gov.nist.hit.ds.simSupport.simulator.SimIdentifier
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.utilities.xml.Util
import org.apache.axiom.om.OMElement
import spock.lang.Specification

/**
 * Created by bmajur on 2/24/15.
 */
class DirectAddrBlockValidatorTest extends Specification {
    // The actors/transactions defined are irrelevant to the test
    // just need something to hang the validator off of
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction name="Register" id="rb" code="rb" asyncCode="r.as">
       <implClass value="RegisterTransaction"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <params multiPart="false" soap="true"/>
        <params direct="true"/>
    </transaction>
    <actor name="Document Registry" id="reg">
      <implClass value=""/>
        <transaction id="rb"/>
    </actor>
</ActorsTransactions>
'''

    File repoDataDir
    RepositorySource repoSource
    SimIdentifier simId
    def repoName = 'DirectAddrBlockValidatorTest'

    def setup() {
        SimSupport.initialize()
        new ActorTransactionTypeFactory().clear()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
        repoSource = Configuration.getRepositorySrc(RepositorySource.Access.RW_EXTERNAL)
        repoDataDir = Configuration.getRepositoriesDataDir(repoSource)
        simId = new SimIdentifier(repoName, 'test')
        SimUtils.create('reg', simId)
    }

    def 'Test example from spec'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:from>mailto:entity1@direct.example.org</direct:from>
      <direct:to>mailto:entity2@direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        !transRunner.simHandle.event.hasErrors()
    }

    def 'Bad namespace on direct block'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressingxxx"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:from>mailto:entity1@direct.example.org</direct:from>
      <direct:to>mailto:entity2@direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab020'
    }

    def 'Bad namespace on role'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelopexx">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:from>mailto:entity1@direct.example.org</direct:from>
      <direct:to>mailto:entity2@direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab030'
    }

    def 'Bad namespace on relay'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope" xmlns:env1="http://www.w3.org/2003/05/soap-envelopexx">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env1:relay="true">
      <direct:from>mailto:entity1@direct.example.org</direct:from>
      <direct:to>mailto:entity2@direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab040'
    }

    def 'From missing'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:to>mailto:entity2@direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab050'
    }

    def 'From has wrong namespace'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <env:from>mailto:entity2@direct.example.org</env:from>
      <direct:to>mailto:entity2@direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab060'
    }

    def 'From not an email address'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:from>entity2@direct.example.org</direct:from>
      <direct:to>mailto:entity2@direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab070'
    }

    def 'From is a bad email address'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:from>mailto:entity2_direct.example.org</direct:from>
      <direct:to>mailto:entity2@direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab075'
    }

    def 'To missing'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:from>mailto:entity2@direct.example.org</direct:from>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab080'
    }

    def 'To has wrong namespace'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:from>mailto:entity2@direct.example.org</direct:from>
      <env:to>mailto:entity2@direct.example.org</env:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab090'
    }

    def 'To not an email address'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:from>mailto:entity2@direct.example.org</direct:from>
      <direct:to>entity2@direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab100'
    }

    def 'To is a bad email address'() {
        setup:
        def headerText = '''
<env:Header xmlns:env="http://www.w3.org/2003/05/soap-envelope">
  <direct:addressBlock
    xmlns:direct="urn:direct:addressing"
    env:role="urn:direct:addressing:destination"
    env:relay="true">
      <direct:from>mailto:entity2@direct.example.org</direct:from>
      <direct:to>mailto:entity2_direct.example.org</direct:to>
  </direct:addressBlock>
</env:Header>
'''
        OMElement header = Util.parse_xml(headerText)

        when:
        Closure closure = { simHandle ->
            new DirectAddrBlockValidator(simHandle, header).run()
        }
        def transRunner = new TransactionRunner('rb', simId, closure)
        transRunner.runTest()
        println transRunner.simHandle.event.errorAssertionIds()

        then:
        SoapFaultException ex = thrown()
        ex.faultDetail == 'dab105'
    }

//    def 'Groovy scan attributes'() {
//        when:
//        def x = '''
//<a>
//  <b x="1" y="2"/>
//</a>
//'''
//        def xml = new XmlSlurper().parseText(x)
//        def b = xml.b
//        assert b
//
//        b[0].attributes().each { key, value -> println key }
//
//        then: true
//    }

}
