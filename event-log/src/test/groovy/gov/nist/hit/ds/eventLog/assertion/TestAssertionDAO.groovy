package gov.nist.hit.ds.eventLog.assertion

import spock.lang.*

/**
 * Created by bill on 4/15/14.
 */
class TestAssertionDAO extends Specification {
    def dao = new AssertionDAO()

    def "Translate Assertion through CSV"() {

        setup:
        def a = new Assertion()
        a.with {
            id = 'id1'
            status = AssertionStatus.SUCCESS
            found = 'dog'
            expected = 'cat'
            msg = 'hello'
            reference = [ 'one', 'two']
            code = 'secret'
            location = 'here'
        }

        when:
        def csv = dao.asCVSEntry(a)
        def a2 = dao.asAssertion(csv)

        then:
        a == a2
    }

}
