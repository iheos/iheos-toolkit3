package gov.nist.hit.ds.eventLog

import spock.lang.Specification
/**
 * Created by bill on 4/18/14.
 */
class EventTest extends Specification {

    def "Input Header"() {
        setup:
        def event = new EventFactory().buildEvent(null)

        when:
        def header = 'My Header'
        event.inOut.reqHdr = header

        then:
        header == event.inOut.reqHdr
    }

    def "Artifact"() {
        setup:
        def event = new EventFactory().buildEvent(null)

        when:
        def name = 'name'
        def value = 'value'
        event.artifacts.add(name, value)

        then:
        event.artifacts.artifactMap[name] == value
    }

}
