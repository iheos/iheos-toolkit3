package gov.nist.hit.ds.simSupport.simEngine

import spock.lang.Specification

/**
 * Created by bmajur on 4/22/14.
 */
class SimComponentLoaderTest extends Specification {

    def 'Simple Load from call parameters'() {
        when:
        def parms = ['lang':'goblin']
        def loader = new SimComponentFactory('gov.nist.hit.ds.simSupport.components.ParmUser', parms)
        loader.load()
        def component = loader.getComponent()

        then:
        component.getLang() == 'goblin'
    }

}
