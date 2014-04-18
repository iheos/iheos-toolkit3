package gov.nist.hit.ds.eventLog

import gov.nist.hit.ds.eventLog.assertion.Assertion
import gov.nist.hit.ds.eventLog.errorRecording.RequiredOptional
import spock.lang.Specification

/**
 * Created by bill on 4/18/14.
 */
class EventTest extends Specification {

    def "Input Header"() {
        setup:
        def event = new EventBuilder().buildEvent(null)

        when:
        def header = 'My Header'
        event.inOut.reqHdr = header

        then:
        header == event.inOut.reqHdr
    }

    def "Artifact"() {
        setup:
        def event = new EventBuilder().buildEvent(null)

        when:
        def name = 'name'
        def value = 'value'
        event.artifacts.add(name, value)

        then:
        event.artifacts.artifactMap[name] == value
    }

    def 'Assertion'() {
        setup:
        def event = new EventBuilder().buildEvent(null)

        when:
        def assertion = new Assertion()
        assertion.with {
            found = 'dog'
            expected = 'cat'
            msg = 'here kitty'
            code = 'decode ring'
            location = 'home'
            requiredOptional = RequiredOptional.R
        }
        event.assertionGroup.addAssertion(assertion)

        then:
        event.assertionGroup.assertions[0] == assertion
        event.assertionGroup.assertions.size() == 1
    }
}
