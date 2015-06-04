package gov.nist.hit.ds.simServlet.rest

import gov.nist.hit.ds.dsSims.eb.transactionSupport.EbSendRequestDAO
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.simServlet.api.SimApi
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.SimIdentifier
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.utilities.xml.OMFormatter
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
                throw new WebApplicationException(Response.Status.BAD_REQUEST)
            }
            println "xml parsed"

//            def simId = new SimId(simIdString)
            SimIdentifier simIdentifier = new SimIdentifier(username, simIdString)
            SimHandle simHandle = SimApi.send(simIdentifier, EbSendRequestDAO.toModel(message))

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
<transaction>${event.fault.faultTransaction}</transaction>
<code>${event.fault.faultCode}</code>
<msg>${event.fault.faultMsg}</msg>
<detail>${event.fault.faultDetail}</detail>
</fault>
"""
            }

            String responseStr = "<Log>${request}${response}${fault}</Log>"
            return new OMFormatter(responseStr).toString()
        } catch (Throwable t) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST)
        }
    }

}
