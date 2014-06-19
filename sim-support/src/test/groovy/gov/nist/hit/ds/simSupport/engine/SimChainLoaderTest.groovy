package gov.nist.hit.ds.simSupport.engine
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
import spock.lang.Specification
/**
 * Created by bmajur on 5/1/14.
 */
class SimChainLoaderTest extends Specification {

    def 'SimChain load from missing properties file'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)

        when:
        simChainFactory.loadFromPropertyBasedResource('simChainTwoStepsxx.properties')
        SimChain simChain = simChainFactory.simChain

        then:
        ToolkitRuntimeException ex = thrown()
        ex.message.indexOf('SimChainFactory failed') != -1

    }

    def 'Load simple SimChain from properties file - bad parameter'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)

        when:
        simChainFactory.loadFromPropertyBasedResource('simChainTwoStepsBadParm.properties')

        then:
        SoapFaultException ex = thrown()
        ex.message != null
        ex.message.indexOf('method does not exist') != -1
    }

    def 'Load simple SimChain from properties file - bad class'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)

        when:
        simChainFactory.loadFromPropertyBasedResource('simChainTwoStepsBadClass.properties')

        then:
        SoapFaultException ex = thrown()
        ex.message != null
        ex.message.indexOf('class not found') != -1
    }

    def 'Load simple SimChain from properties file'() {
        setup:
        Event event = new EventFactory().buildEvent(null)
        def simChainFactory = new SimChainFactory(event)

        when:
        simChainFactory.loadFromPropertyBasedResource('simChainTwoSteps.properties')
        SimChain simChain = simChainFactory.simChain

        then:
        simChain.steps.size() == 2
    }
}
