package gov.nist.hit.ds.simSupport.simEngine

import spock.lang.*

import java.lang.reflect.Method

/**
 * Created by bill on 4/15/14.
 */
class InjectorTest extends Specification {

    def 'Find Method'() {
        setup:
        def paramMap = [name : 'George']
        def object = new SingleSetter()
        def injector = new Injector(object, paramMap)

        when:
        Method method = injector.getSetterMethod('name')

        then:
        method != null
    }

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
