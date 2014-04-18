package gov.nist.hit.ds.eventLog.assertion

import spock.lang.*

/**
 * Created by bill on 4/15/14.
 */
class AssertionGroupTest extends Specification {

    def "Assert in"() {

        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertIn(['one', 'two'] as String[], 'two')

        then:
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS

    }

    def "Not Assert in"() {

        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertIn(['one', 'two'] as String[], 'too')

        then:
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.ERROR
    }

    def "fail"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.fail('bye')

        then:
        asser.expected == 'bye'
        asser.found == ''
        asser.status == AssertionStatus.ERROR
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.ERROR
    }

    def "Assert Equals"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertEquals('bye', 'bye')

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

        when:
        def asser = ag.assertEquals('bye', 'by')

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

        when:
        def asser = ag.assertTrue(true)

        then:
        asser.expected == 'True'
        asser.found == 'True'
        asser.status == AssertionStatus.SUCCESS
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "Not Assert True"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.assertTrue(false)

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

        when:
        def asser = ag.assertNotNull('one')

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

        when:
        def asser = ag.assertNotNull(null)

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

        when:
        def asser = ag.infoFound(true)

        then:
        asser.expected == AssertionGroup.dashes
        asser.found == 'True'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "Not InfoFound boolean"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.infoFound(false)

        then:
        asser.expected == AssertionGroup.dashes
        asser.found == 'False'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "InfoFound string"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.infoFound('hi')

        then:
        asser.expected == AssertionGroup.dashes
        asser.found == 'hi'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }

    def "Not InfoFound string"() {
        setup:
        def ag = new AssertionGroup()

        when:
        def asser = ag.infoFound(false)

        then:
        asser.expected == AssertionGroup.dashes
        asser.found == 'False'
        asser.status == AssertionStatus.INFO
        ag.assertions.size() == 1
        ag.worstStatus == AssertionStatus.SUCCESS
    }


}
