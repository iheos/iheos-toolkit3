package gov.nist.hit.ds.simServlet

import groovy.xml.MarkupBuilder
import spock.lang.Specification

/**
 * Created by bmajur on 11/26/14.
 */
class BuilderTest extends Specification {

    def 'Insert XML into builder'() {
        when:
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.top(type: 'my') {
            tex('<foo/>')
        }
        def output = writer.toString()

        then:
        true
    }
}
