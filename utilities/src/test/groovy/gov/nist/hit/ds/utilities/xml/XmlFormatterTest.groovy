package gov.nist.hit.ds.utilities.xml

import spock.lang.Specification

/**
 * Created by bmajur on 1/16/15.
 */
class XmlFormatterTest extends Specification {
    def data1 = '''
<hi><data if="a"/></hi>
'''

    def data2 = '''
<hi><data if='a'/></hi>
'''

    def 'Double quote test'() {
        when:
        def out = new XmlFormatter(data1).run()
        println out

        then:
        true
    }

    def 'Single quote test'() {
        when:
        def out = new XmlFormatter(data2).run()
        println out

        then:
        true
    }
}
