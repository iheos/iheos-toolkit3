package gov.nist.hit.ds.ebIT
import gov.nist.hit.ds.dsSims.eb.transactionSupport.DocumentHandler
import gov.nist.hit.ds.ebDocsrcSim.engine.PnrSend
import gov.nist.hit.ds.ebMetadata.MetadataSupport
import gov.nist.hit.ds.toolkit.environment.EnvironmentAccess
import gov.nist.hit.ds.utilities.xml.OMFormatter
import gov.nist.hit.ds.utilities.xml.Util
import org.apache.axiom.om.OMElement
import spock.lang.Specification
/**
 * Created by bmajur on 1/13/15.
 */
// TODO: Move to IT module
class PnrSendITxx extends Specification {
    OMElement metadata_element
    Map<String, DocumentHandler> documents = [ : ]

    def 'Test against PR'() {
        setup:
        def endpoint = 'http://ihexds.nist.gov:12090/tf6/services/xdsrepositoryb'
        metadata_element = MetadataSupport.om_factory.createOMElement("Message", null)
        String submission = getClass().classLoader.getResource('PnR1Doc.xml').text
        OMElement submissionEle = Util.parse_xml(submission)
        documents['Document01'] = new DocumentHandler(new File(getClass().classLoader.getResource('hello.txt').toURI()))

        when:
        PnrSend pnr = new PnrSend(submissionEle, documents, endpoint, null)
        def logs = pnr.run()
        println '**************      LOGS      ******************'
        logs.each { println new OMFormatter(it).toString() }

        def log = new XmlSlurper().parseText(new OMFormatter(logs[0]).toString())
        def fault = log.name() == 'Fault'

        then: !fault
    }

    def 'Test against PR with TLS'() {
        setup:
        def endpoint = 'https://ihexds.nist.gov:12091/tf6/services/xdsrepositoryb'
        metadata_element = MetadataSupport.om_factory.createOMElement("Message", null)
        String submission = getClass().classLoader.getResource('PnR1Doc.xml').text
        OMElement submissionEle = Util.parse_xml(submission)
        documents['Document01'] = new DocumentHandler(new File(getClass().classLoader.getResource('hello.txt').toURI()))
        def environmentAccess = new EnvironmentAccess(new File(getClass().classLoader.getResource('NA2015').toURI()))

        when:
        PnrSend pnr = new PnrSend(submissionEle, documents, endpoint, environmentAccess)
        def logs = pnr.run()
        println '**************      LOGS      ******************'
        logs.each { println new OMFormatter(it).toString() }

        def log = new XmlSlurper().parseText(new OMFormatter(logs[0]).toString())
        def fault = log.name() == 'Fault'

        then: !fault
    }

    def 'Environment check'() {
        when:
        def file = new File(getClass().classLoader.getResource('NA2015').toURI())

        then:
        file.exists()
        file.isDirectory()

        when:
        def keystoreDir = new File(file, 'keystore')

        then:
        keystoreDir.exists()
        keystoreDir.isDirectory()

        when:
        def keystoreFile = new File(keystoreDir, 'keystore')
        def propertiesFile = new File(keystoreDir, 'keystore.properties')

        then:
        keystoreFile.exists()
        propertiesFile.exists()
    }
}

