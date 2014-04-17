package gov.nist.hit.ds.actorTransaction;

import spock.lang.Specification;

/**
 * Created by bill on 4/17/14.
 */
public class ActorTypeTest extends Specification {

    def "Find transaction in Actor"() {
        setup:
        ActorTypeFactory.loadActorType('registry')

        when:
        def actorType = ActorTypeFactory.find('registry')
        def transactionType = actorType.find('register')

        then:
        transactionType != null

    }
}
