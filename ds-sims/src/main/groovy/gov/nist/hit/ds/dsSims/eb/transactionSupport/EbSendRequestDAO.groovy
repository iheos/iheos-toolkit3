package gov.nist.hit.ds.dsSims.eb.transactionSupport

import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import groovy.xml.StreamingMarkupBuilder

/**
 * Created by bmajur on 2/3/15.
 */
class EbSendRequestDAO {

    static EbSendRequest toModel(String xmlString) {
        EbSendRequest request = new EbSendRequest()
        def xml = new XmlSlurper().parseText(xmlString)

        def simRef = xml.simReference.text()
        if (!simRef) throw new ToolkitRuntimeException("EbSendRequestDAO.toModel: simReference not present")
        def (username, simid) = simRef.split('/')
        request.simIdentifier = new SimIdentifier(username, simid)

        request.transactionName = xml.transactionName.text()
        if (!request.transactionName) throw new ToolkitRuntimeException("EbSendRequestDAO.toModel: transactionName not present")

        request.tls = xml.tls.@value.text().compareToIgnoreCase('true') == 0

        request.messageId = xml.messageId.text()

        def outputBuilder = new StreamingMarkupBuilder()
        String metadataString = outputBuilder.bind{ mkp.yield xml.metadata.children()[0] }
        request.metadata = metadataString
        if (!request.metadata) throw new ToolkitRuntimeException("EbSendRequestDAO.toModel: metadata not present")

        String extraHeadersString = outputBuilder.bind{ mkp.yield xml.extraHeaders }
        request.extraHeaders = extraHeadersString

        xml.document.each {
            def id = it.@id.text()
            def mimeType = it.@mimeType.text()
            def value = it.text()
            if (!id) throw new ToolkitRuntimeException("EbSendRequestDAO.toModel: found document without id")
            if (!mimeType) throw new ToolkitRuntimeException("EbSendRequestDAO.toModel: found document without mimeType (id ${id}")
            if (!value) throw new ToolkitRuntimeException("EbSendRequestDAO.toModel: document ${id} has no content")
            DocumentHandler handle = new DocumentHandler(value, mimeType)
            request.documents[id] = handle
        }

        return request
    }

}
