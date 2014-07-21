package gov.nist.hit.ds.simSupport.endpoint

import spock.lang.Specification
/**
 * Created by bmajur on 6/6/14.
 */
class EndpointBuilderCleanTest extends Specification {

    def 'Clean aleady clean string'() {
        given:
        def builder = new EndpointBuilder()
        def x = 'abc'

        when:
        def y = builder.clean(x)

        then:
        x == y
    }

    def 'Clean prefix'() {
        given:
        def builder = new EndpointBuilder()
        def x = '/abc'

        when:
        def y = builder.clean(x)

        then:
        y == 'abc'
    }

    def 'Clean suffix'() {
        given:
        def builder = new EndpointBuilder()
        def x = 'abc/'

        when:
        def y = builder.clean(x)

        then:
        y == 'abc'
    }

    def 'Clean double prefix'() {
        given:
        def builder = new EndpointBuilder()
        def x = '//abc'

        when:
        def y = builder.clean(x)

        then:
        y == 'abc'
    }

    def 'Clean double suffix'() {
        given:
        def builder = new EndpointBuilder()
        def x = 'abc//'

        when:
        def y = builder.clean(x)

        then:
        y == 'abc'
    }

    def 'Clean both'() {
        given:
        def builder = new EndpointBuilder()
        def x = '//abc//'

        when:
        def y = builder.clean(x)

        then:
        y == 'abc'
    }
}
