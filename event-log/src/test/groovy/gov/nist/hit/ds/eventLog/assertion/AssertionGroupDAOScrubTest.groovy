package gov.nist.hit.ds.eventLog.assertion

import spock.lang.Specification

/**
 * Created by bmajur on 1/15/15.
 */
class AssertionGroupDAOScrubTest extends Specification {

    def 'Scrub assertions 1'() {
        setup:
        def a1 = new Assertion()
        def a2 = new Assertion()
        a1.with { (status, msg) = [AssertionStatus.INFO, 'Hello'] }
        a2.with { (status, msg) = [AssertionStatus.ERROR, '.Hello'] }
        def assertions = [ a1, a2 ]

        when:
        def scrubbed = AssertionGroupDAO.scrubAssertions(assertions)

        then:
        scrubbed.size() == 2
    }

    def 'Scrub assertions 2'() {
        setup:
        def a0 = new Assertion()
        def a1 = new Assertion()
        def a2 = new Assertion()
        def a3 = new Assertion()
        a0.with { (status, msg) = [AssertionStatus.SUCCESS, 'Hi'] }
        a1.with { (status, msg) = [AssertionStatus.INFO, 'Hello'] }
        a2.with { (status, msg) = [AssertionStatus.INFO, '.Hello'] }
        a3.with { (status, msg) = [AssertionStatus.ERROR, '.Hello'] }
        def assertions = [ a0, a1, a2, a3 ]

        when:
        def scrubbed = AssertionGroupDAO.scrubAssertions(assertions)

        then:
        scrubbed.size() == 3
    }


    def 'Scrub assertions 3'() {
        setup:
        def a0 = new Assertion()
        def a1 = new Assertion()
        def a2 = new Assertion()
        def a3 = new Assertion()
        a0.with { (status, msg) = [AssertionStatus.SUCCESS, 'Hi'] }
        a1.with { (status, msg) = [AssertionStatus.INFO, 'Hello'] }
        a2.with { (status, msg) = [AssertionStatus.ERROR, '.Hello'] }
        a3.with { (status, msg) = [AssertionStatus.INFO, 'Hello'] }
        def assertions = [ a0, a1, a2, a3 ]

        when:
        def scrubbed = AssertionGroupDAO.scrubAssertions(assertions)

        then:
        scrubbed.size() == 2
    }

    def 'Scrub assertions 4'() {
        setup:
        def a0 = new Assertion()
        def a1 = new Assertion()
        def a2 = new Assertion()
        def a3 = new Assertion()
        a0.with { (status, msg) = [AssertionStatus.SUCCESS, 'Hi'] }
        a1.with { (status, msg) = [AssertionStatus.INFO, 'Hello'] }
        a2.with { (status, msg) = [AssertionStatus.ERROR, '.Hello'] }
        a3.with { (status, msg) = [AssertionStatus.INFO, '.Hello'] }
        def assertions = [ a0, a1, a2, a3 ]

        when:
        def scrubbed = AssertionGroupDAO.scrubAssertions(assertions)

        then:
        scrubbed.size() == 2
    }

    def 'Scrub assertions 5'() {
        setup:
        def a0 = new Assertion()
        def a1 = new Assertion()
        def a2 = new Assertion()
        def a3 = new Assertion()
        a0.with { (status, msg) = [AssertionStatus.SUCCESS, 'Hi'] }
        a1.with { (status, msg) = [AssertionStatus.INFO, 'Hello'] }
        a2.with { (status, msg) = [AssertionStatus.ERROR, '.Hello'] }
        a3.with { (status, msg) = [AssertionStatus.ERROR, '.Hello'] }
        def assertions = [ a0, a1, a2, a3 ]

        when:
        def scrubbed = AssertionGroupDAO.scrubAssertions(assertions)

        then:
        scrubbed.size() == 3
    }
}
