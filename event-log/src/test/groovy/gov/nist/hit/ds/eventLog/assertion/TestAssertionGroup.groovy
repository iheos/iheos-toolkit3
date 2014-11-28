package gov.nist.hit.ds.eventLog.assertion

import spock.lang.*

/**
 * Created by bill on 4/15/14.
 */
class TestAssertionGroup extends Specification {

    def "Assert in"() {

        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertIn(['one', 'two'] as String[], 'two', true)

        then:
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
    }

    def "Not Assert in"() {

        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertIn(['one', 'two'] as String[], 'too', true)

        then:
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
    }

    def "fail"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.fail('bye', true)

        then:
//        asser.expected == 'bye'
//        asser.found == ''
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
    }

    def "Assert Equals"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertEquals('bye', 'bye', true)

        then:
        asser.expected == 'bye'
        asser.found == 'bye'
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
    }

    def "Not Assert Equals"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertEquals('bye', 'by', true)

        then:
        asser.expected == 'bye'
        asser.found == 'by'
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
    }

    def "Assert True"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertTrue(true, true)

        then:
//        asser.expected == 'True'
//        asser.found == 'True'
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
    }

    def "Not Assert True"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertTrue(false, true)

        then:
        asser.expected == 'True'
        asser.found == 'False'
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
    }

    def "Assert NotNull"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertNotNull('one', true)

        then:
        asser.expected == 'Present'
        asser.found == 'Found'
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
    }

    def "Not Assert NotNull"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertNotNull(null, true)

        then:
        asser.expected == 'Present'
        asser.found == 'Missing'
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
    }

    def "InfoFound boolean"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.infoFound(true)

        then:
        asser.expected == ''
        asser.found == 'True'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
    }

    def "Not InfoFound boolean"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.infoFound(false)

        then:
        asser.expected == ''
        asser.found == 'False'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
    }

    def "InfoFound string"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.infoFound('hi')

        then:
        asser.expected == ''
        asser.found == 'hi'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
    }

    def "Not InfoFound string"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.infoFound(false)

        then:
        asser.expected == ''
        asser.found == 'False'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
    }


}
