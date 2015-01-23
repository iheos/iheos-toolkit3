package gov.nist.hit.ds.eventLog.assertion

import gov.nist.hit.ds.repository.shared.ValidationLevel
import spock.lang.Specification

/**
 * Created by bmajur on 1/14/15.
 */
class AssertionLogableTest extends Specification {

    def 'ValidationLevel equals'() {
        setup:
        def ag = new AssertionGroup()
        def a = new Assertion()

        when:
        a.status = AssertionStatus.ERROR
        ag.validationLevel = ValidationLevel.ERROR

        then:
        ag.isLogable(a)

        when:
        a.status = AssertionStatus.WARNING
        ag.validationLevel = ValidationLevel.ERROR

        then:
        !ag.isLogable(a)

        when:
        a.status = AssertionStatus.INFO
        ag.validationLevel = ValidationLevel.ERROR

        then:
        !ag.isLogable(a)

        when:
        a.status = AssertionStatus.ERROR
        ag.validationLevel = ValidationLevel.WARNING

        then:
        ag.isLogable(a)

        when:
        a.status = AssertionStatus.WARNING
        ag.validationLevel = ValidationLevel.WARNING

        then:
        ag.isLogable(a)

        when:
        a.status = AssertionStatus.INFO
        ag.validationLevel = ValidationLevel.WARNING

        then:
        !ag.isLogable(a)

        when:
        ag.worstStatus = AssertionStatus.SUCCESS
        ag.validationLevel = ValidationLevel.ERROR

        then:
        !ag.isLogable()
    }
}
