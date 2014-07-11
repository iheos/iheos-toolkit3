package gov.nist.hit.ds.simSupport.endpoint

import spock.lang.Specification

/**
 * Created by bmajur on 7/5/14.
 */
class EndpointParserTest extends Specification {

    def 'Verify parser'() {
        def endpoint = 'http://localhost:8080/base/sim/123/reg/sq'

        when:
        def EndpointBuilder builder = new EndpointBuilder()
        builder.parse(endpoint)

        then:
        builder.simId.id == '123'
        builder.actorCode == 'reg'
        builder.transCode == 'sq'
    }
}
