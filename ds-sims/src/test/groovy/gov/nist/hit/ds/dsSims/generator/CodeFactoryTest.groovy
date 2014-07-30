package gov.nist.hit.ds.dsSims.generator

import spock.lang.Specification

/**
 * Created by bmajur on 7/14/14.
 */
class CodeFactoryTest extends Specification {

    def 'Random Code'() {
        when:
        def pgen = new CodeFactory()
        pgen.loadCodes(this.class.getClassLoader().getResourceAsStream('codes.xml').text)
        def code = pgen.randomCode('classCode')

        then:
        code
        code instanceof CodeFactory.Code
        code.code
        code.display
        code.scheme
    }
}
