package gov.nist.hit.ds.actorTransaction

import spock.lang.*

/**
 * Created by bill on 4/16/14.
 */
class ActorTypeFactoryTest extends Specification {

    def "Explicit Load Actor Type"() {

        setup:
        ActorTypeFactory.clear()
        ActorTypeFactory.loadActorType('registry')

        when:
        def a = 1

        then:
        ActorTypeFactory.find('registry') != null
    }

    def "Implicit Load Actor Type"() {

        setup:
        ActorTypeFactory.clear()

        when:
        def a = 1

        then:
        ActorTypeFactory.find('registry') != null
    }

}
