package gov.nist.hit.ds.testClient.eb

import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.utilities.xml.OMFormatter
import gov.nist.hit.ds.utilities.xml.Util
import org.apache.axiom.om.OMElement
import spock.lang.Specification

/**
 * Created by bmajur on 1/13/15.
 */
class PnrSendTest extends Specification {
    def endpoint = 'http://ihexds.nist.gov:12090/tf6/services/xdsrepositoryb'
    OMElement metadata_element
    Map<String, DocumentHandler> documents = [ : ]

    def 'Test against PR'() {
        setup:
        metadata_element = MetadataSupport.om_factory.createOMElement("Message", null)
        String submission = getClass().classLoader.getResource('PnR1Doc.xml').text
        OMElement submissionEle = Util.parse_xml(submission)
        documents['Document01'] = new DocumentHandler(new File(getClass().classLoader.getResource('hello.txt').toURI()))

        when:
        PnrSend pnr = new PnrSend(submissionEle, documents, endpoint)
        def logs = pnr.run()
        println '**************      LOGS      ******************'
        logs.each { println new OMFormatter(it).toString() }

        def log = new XmlSlurper().parseText(new OMFormatter(logs[0]).toString())
        def fault = log.name() == 'Fault'

        then: !fault
    }
}
