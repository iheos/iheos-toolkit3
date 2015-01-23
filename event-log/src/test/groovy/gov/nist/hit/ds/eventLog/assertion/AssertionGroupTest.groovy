package gov.nist.hit.ds.eventLog.assertion

import gov.nist.hit.ds.repository.shared.ValidationLevel
import spock.lang.*

/**
 * Created by bill on 4/15/14.
 */
class AssertionGroupTest extends Specification {

    def "Assert in"() {

        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.assertIn(['one', 'two'] as String[], 'two', true)

        then:
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS

    }

    def "Not Assert in"() {

        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.assertIn(['one', 'two'] as String[], 'too', true)

        then:
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.ERROR
    }

    def "fail"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.fail('bye', true)

        then:
        asser.msg == 'bye'
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.ERROR
    }

    def "Assert Equals"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.assertEquals('bye', 'bye', true)

        then:
        asser.expected == 'bye'
        asser.found == 'bye'
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "Not Assert Equals"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.assertEquals('bye', 'by', true)

        then:
        asser.expected == 'bye'
        asser.found == 'by'
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.ERROR
    }

    def "Assert True"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.assertTrue(true, true)

        then:
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "Not Assert True"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.assertTrue(false, true)

        then:
        asser.expected == 'True'
        asser.found == 'False'
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.ERROR
    }

    def "Assert NotNull"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.assertNotNull('one', true)

        then:
        asser.expected == 'Present'
        asser.found == 'Found'
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "Not Assert NotNull"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.assertNotNull(null, true)

        then:
        asser.expected == 'Present'
        asser.found == 'Missing'
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.ERROR
    }

    def "InfoFound boolean"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.infoFound(true)

        then:
        asser.found == 'True'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "Not InfoFound boolean"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.infoFound(false)

        then:
        asser.found == 'False'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "InfoFound string"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.infoFound('hi')

        then:
        asser.found == 'hi'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "Not InfoFound string"() {
        setup:
        def ag = new AssertionGroup()
        ag.validationLevel = ValidationLevel.INFO

        when:
        def asser = ag.infoFound(false)

        then:
        asser.found == 'False'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }


}
