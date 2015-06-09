package gov.nist.hit.ds.simServlet.rest

import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequest
import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequestDAO
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.utilities.xml.HtmlizeXml
import gov.nist.hit.ds.utilities.xml.OMFormatter
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil
import groovy.util.logging.Log4j
import groovy.xml.StreamingMarkupBuilder
import org.xml.sax.SAXParseException

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bmajur on 2/3/15.
 */
@Path('/sim/client/{username}/{simid}')
@Log4j
class   Send {
    @POST
    @Consumes(MediaType.APPLICATION_XML)
//    @Produces(MediaType.APPLICATION_XML)
    String create(@PathParam('username') String username, @PathParam('simid') String simIdString, String message) {
        try {
            def xml
            assert username
            assert simIdString
            println "Accepting REST msg: Message is:\n${message}"
            try {
                xml = new XmlSlurper().parseText(message)
            } catch (SAXParseException spe) {
                log.debug ExceptionUtil.exception_details(spe)
                throw new WebApplicationException(Response.Status.BAD_REQUEST)
            }
            println "xml parsed"

//            def simId = new SimId(simIdString)
            SimIdentifier simIdentifier = new SimIdentifier(username, simIdString)
            if (!simIdentifier.isValid()) {
                def msg = "Sim username (${username}) or simId (${simIdString}) is invalid - no such simulator"
                log.error msg
                throw new WebApplicationException(Response.Status.BAD_REQUEST)
            }
            EbSendRequest erequest = EbSendRequestDAO.toModel(message)
            println "request is ${erequest}"
            SimHandle simHandle = SimApi.send(simIdentifier, erequest)

//            def actorTypeName = xml.@type.text()
//            println "actor type is ${actorTypeName}"
//            if (!actorTypeName)
//                throw new WebApplicationException(Response.Status.BAD_REQUEST)

            Event event = simHandle.event
            def reqHdr = event.inOut.reqHdr
            String reqBody = new String(event.inOut.reqBody)

            def resHdr = event.inOut.respHdr
            String resBody = "<soapenv:Body xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">${new String(event.inOut.respBody)}</soapenv:Body>"

            def request = "<Request><SoapHeader>${reqHdr}</SoapHeader><SoapBody>${reqBody}</SoapBody></Request>"
            def response = "<Response><SoapHeader>${resHdr}</SoapHeader><SoapBody>${resBody}</SoapBody></Response>"

            def fault = ''
            if (event.fault) {
                fault = """
<fault>
<transaction>${HtmlizeXml.run(event.fault.faultTransaction)}</transaction>
<code>${HtmlizeXml.run(event.fault.faultCode)}</code>
<msg>${HtmlizeXml.run(event.fault.faultMsg)}</msg>
<detail>${HtmlizeXml.run(event.fault.faultDetail)}</detail>
</fault>
"""
            }

            String responseStr = "<Log>${request}${response}${fault}</Log>"
            return new OMFormatter(responseStr).toString()
        } catch (Throwable t) {
            log.debug ExceptionUtil.exception_details(t)
            throw new WebApplicationException(Response.Status.BAD_REQUEST)
        }
    }

}
