package gov.nist.hit.ds.simSupport.utilities
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import spock.lang.Specification
/**
 * Created by bmajur on 7/6/14.
 */
class RunTransactionTest extends Specification {
    // The transaction implementation class is this class
    def actorsTransactions = '''
<ActorsTransactions>
    <transaction displayName="Golf" id="gf" code="gf" asyncCode="gf.as"
       class="gov.nist.hit.ds.simSupport.utilities.RunTransactionTest">
        <request action="urn:ihe:iti:2007:RegisterDocumentSet-b"/>
        <response action="urn:ihe:iti:2007:RegisterDocumentSet-bResponse"/>
    </transaction>
    <actor displayName="Golf Outing" id="golf"
      class="">
        <transaction id="gf"/>
    </actor>
</ActorsTransactions>
'''

    def setup() {
        println 'Test Initializing'
        SimSupport.initialize()
        new ActorTransactionTypeFactory().loadFromString(actorsTransactions)
    }

    // Used as the run method of the transaction implementation
    static hasRun
    def run(SimHandle handle) {
        hasRun = true  // so its running can be detected
        println "SimId is ${handle.simId}"
    }


    def 'Run method should get called'() {
        def simId = new SimId('1234')
        def simAsset = SimUtils.mkSim('golf', simId)
        def simHandle = SimUtils.handle(simId)
        def endpoint = 'http://localhost:8080/xdstools3/sim/1234/golf/gf'

        when:
        hasRun = false
        SimUtils.runTransaction(endpoint, 'My Header', 'My Body'.bytes)

        then:
        hasRun
    }
}
