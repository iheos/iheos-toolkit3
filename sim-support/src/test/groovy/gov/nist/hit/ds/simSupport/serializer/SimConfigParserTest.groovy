package gov.nist.hit.ds.simSupport.serializer

import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.simSupport.config.SimConfig
import gov.nist.hit.ds.simSupport.utilities.SimSupport
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import spock.lang.Specification

/**
 * Created by bill on 6/12/15.
 */
class SimConfigParserTest extends Specification {

    ActorTransactionTypeFactory factory;
    def actorTransactions = '''
<ActorsTransactions>
    <!--
        XDR/XDS
    -->
    <transaction name="Provide and Register" id="prb-server" code="prb">
        <params multipart="true" soap="true"/>
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.dsSims.eb.transactions.Pnr"/>
    </transaction>
    <transaction name="Provide and Register" id="prb-client" code="prb">
        <params multipart="true" soap="true"/>
        <request action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.ebDocsrcSim.transaction.Pnr"/>
    </transaction>
    <actor name="Document Recipient" id="docrec">
        <implClass value=""/>
        <transaction id="prb-server" type="server"/>
    </actor>
    <!--
        MHD
    -->
    <transaction name="Doc Reference Validation - XML" id="drv" code="drv">
        <implClass value="gov.nist.hit.ds.dsSims.fhir.mhd.validators.DocRefTransaction"/>
    </transaction>
    <transaction name="Doc Manifest Validation - XML" id="dmv" code="dmv">
        <implClass value="gov.nist.hit.ds.dsSims.fhir.mhd.validators.DocManTransaction"/>
    </transaction>
    <actor name="MHD Document Recipient" id="mhddocrec">
        <implClass value=""/>
        <!-- Pseudo-transactions - really only speciality validators -->
        <transaction id="drv"/>
        <transaction id="dmv"/>
    </actor>
    <!--
        XDS
    -->
    <transaction name="Register" id="rb" code="rb">
        <params multipart="false" soap="true"/>
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
        <implClass value="gov.nist.hit.ds.dsSims.eb.transactions.Reg"/>
    </transaction>
    <actor name="Document Registry" id="reg">
        <implClass value=""/>
        <transaction id="rb"/>
    </actor>
    <actor name="Document Source" id="docsrc">
        <implClass value=""/>
        <transaction id="prb-client" type="client"/>
    </actor>
</ActorsTransactions>'''
    def good = '''
<actor type='docsrc'>
    <environment name="default"/>
    <transaction name='prb'>
        <!-- example endpoint value -->
        <endpoint value='${noTlsEndpoint}' />
        <settings>
            <!-- Checks applied to response only -->
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <!-- on response -->
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb' />
    </transaction>
    <transaction name='prb'>
        <endpoint value='${tlsEndpoint}' />
        <settings>
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb_TLS' />
    </transaction>
</actor>
'''

    def badTrWSMatch = '''
<actor type='docsrc'>
    <environment name="default"/>
    <transaction name='prbx'>
        <!-- example endpoint value -->
        <endpoint value='http://foo:4000' />
        <settings>
            <!-- Checks applied to response only -->
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <!-- on response -->
            <text name='msgCallback' value='' />
        </settings>
        <webService value='prb' />
    </transaction>
</actor>
'''

    def badTrWS = '''
<actor type='docsrc'>
    <environment name="default"/>
    <transaction name='prbx'>
        <!-- example endpoint value -->
        <endpoint value='http://foo:4000' />
        <settings>
            <!-- Checks applied to response only -->
            <boolean name='schemaCheck' value='false' />
            <boolean name='modelCheck' value='false' />
            <boolean name='codingCheck' value='false' />
            <boolean name='soapCheck' value='false' />
            <!-- on response -->
            <text name='msgCallback' value='' />
        </settings>
        <webServicex value='prb' />
    </transaction>
</actor>
'''
    def setup() {
        SimSupport.initialize();
        factory = new ActorTransactionTypeFactory();
        factory.clear();
        factory.loadFromString(actorTransactions)
    }

    def 'Test Good'() {
        when:
        SimConfig config = SimulatorDAO.toModel(good)

        then:
        config.actorType.shortName == 'docsrc'
        config.transactions.size() == 2
    }


    def 'Bad transaction - webservice match'() {
        when:
        SimConfig config = SimulatorDAO.toModel(badTrWSMatch)

        then:
        ToolkitRuntimeException tre = thrown()
        tre.message.contains('does not match declared transaction')
    }

    def 'Bad webservice declaration'() {
        when:
        SimConfig config = SimulatorDAO.toModel(badTrWS)

        then:
        ToolkitRuntimeException tre = thrown()
        tre.message.contains('webService specification has no value')
    }
}
