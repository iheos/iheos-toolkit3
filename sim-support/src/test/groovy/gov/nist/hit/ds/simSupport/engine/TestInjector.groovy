package gov.nist.hit.ds.simSupport.engine

import spock.lang.*

/**
 * Created by bill on 4/15/14.
 */
class TestInjector extends Specification {

    def "Single Setter"() {

        setup:
        def paramMap = [name : 'George']
        def object = new SingleSetter()
        def injector = new Injector(object, paramMap)

        when:
        injector.injectAll()

        then:
        object.name == 'George'
    }
}
