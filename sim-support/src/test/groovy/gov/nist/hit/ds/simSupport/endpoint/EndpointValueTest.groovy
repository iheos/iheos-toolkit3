package gov.nist.hit.ds.simSupport.endpoint

import spock.lang.Specification

/**
 * Created by bmajur on 2/3/15.
 */
class EndpointValueTest extends Specification {
    def 'Parse of full endpoint'() {
        when:
        def endpointString = 'http://host:port/context/sim/user/simid/actor/trans'
        def uri = '/context/sim/user/simid/actor/trans'
        def endpointValue = new EndpointValue(endpointString)

        then:
        endpointString == endpointValue.value
        uri == endpointValue.requestURI()
    }

    def 'Parse of requestURI'() {
        when:
        def uri = '/context/sim/user/simid/actor/trans'
        def endpointValue = new EndpointValue(uri)

        then:
        uri == endpointValue.requestURI()

    }
}
