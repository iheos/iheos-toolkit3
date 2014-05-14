package gov.nist.hit.ds.actorTransaction

import spock.lang.Specification

/**
 * Created by bmajur on 5/7/14.
 */
class LoadDefinitionsTest extends Specification {

    /**
     * Load definitions - tests are queries against this data
     * Test/resources contains actor and transaction definitions.  actorsAndTransactions.txt
     * contains a list of their names (not filenames). This tests the loading
     * of this configuration content.  The test runs against the content of the files
     * so if they change then the then: part of the test much change.
     *
     * actorsAndTransactions.txt contains the system-wide knowledge of what actors and
     * transactions are defined.  The copy used by this test is the testing version. The
     * production version has to live in a main/resources section in another package.
     * @return
     */
    def setup() { new ActorTransactionTypeFactory().load() }

    def 'Expect that actors and transactions have been loaded'() {
        given: 'The configurations are not empty'
        expect:
        TransactionTypeFactory.getTransactionTypeNames().size() > 0
        ActorTypeFactory.getActorTypeNames().size() > 0
    }

    def 'Verify that registry and repository actors can be found'() {
        given: 'The registry and repository actors are defined in the config'
        expect:
        ActorTypeFactory.find('registry') != null
        ActorTypeFactory.find('repository') != null
    }

    def 'Verify that reg and rep actors can be found by their short names'() {
        given: 'The registry and repository actors are defined in the config'
        expect:
        ActorTypeFactory.find('reg') != null
        ActorTypeFactory.find('rep') != null
    }

    def 'Verify that a transaction can be found'() {
        given: 'A transaction can only be identified within an actor'
        expect:
        ActorTypeFactory.find('registry', 'register') != null
    }

    def 'Verify that a non-existent transaction cannot be found'() {
        given: 'A transaction can only be identified within an actor'
        expect:
        ActorTypeFactory.find('registry', 'provideAndRegister') == null
    }
}
