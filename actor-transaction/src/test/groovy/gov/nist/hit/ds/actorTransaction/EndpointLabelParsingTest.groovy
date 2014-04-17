package gov.nist.hit.ds.actorTransaction

import spock.lang.Specification

/**
 * Created by bill on 4/17/14.
 */
class EndpointLabelParsingTest extends Specification {

    def "Name TLS Async"() {
        when:
        def actorType = ActorTypeFactory.find("registry");
        def register = actorType.find("register");
        def label = new EndpointLabel(actorType, "Register_TLS_ASYNC");

        then:
        register == label.getTransType()
        label.isAsync()
        label.isTls()
    }

    def "Name TLS Sync"() {
        when:
        def actorType = ActorTypeFactory.find("registry");
        def register = actorType.find("register");
        def label = new EndpointLabel(actorType, "Register_TLS_SYNC");

        then:
        register == label.getTransType()
        !label.isAsync()
        label.isTls()
    }

    def "Name Async"() {
        when:
        def actorType = ActorTypeFactory.find("registry");
        def register = actorType.find("register");
        def label = new EndpointLabel(actorType, "Register_ASYNC");

        then:
        register == label.getTransType()
        label.isAsync()
        !label.isTls()
    }

    def "Name"() {
        when:
        def actorType = ActorTypeFactory.find("registry");
        def register = actorType.find("register");
        def label = new EndpointLabel(actorType, "Register");

        then:
        register == label.getTransType()
        !label.isAsync()
        !label.isTls()
    }
}
