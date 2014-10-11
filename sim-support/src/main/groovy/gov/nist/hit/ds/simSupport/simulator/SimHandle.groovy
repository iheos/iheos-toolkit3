package gov.nist.hit.ds.simSupport.simulator
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.EventFactory
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.repository.api.Repository
import gov.nist.hit.ds.simSupport.client.ActorSimConfig
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.endpoint.EndpointBuilder
import gov.nist.hit.ds.soapSupport.core.SoapEnvironment

/**
 * Created by bmajur on 7/5/14.
 */
class SimHandle {
    SimId simId
    Asset simAsset
    Asset siteAsset
    Asset configAsset
    Asset eventLogAsset
    Event event
    TransactionType transactionType
    Repository repository
    ActorSimConfig actorSimConfig
    EndpointBuilder endpointBuilder
    SoapEnvironment soapEnvironment
    boolean open = true
    def options = []
    boolean complete = false

    // Event creation is lazy so create doesn't build unused event
    Event getEvent() {
        if (!event) {
            def event1 = new EventFactory().buildEvent(repository, eventLogAsset)
            event = event1
            event.init()
        }
        return event
    }

    boolean hasOption(String option) { options.contains(option)}

    SimHandle fault(Fault fault) {
        event.fault = fault
        complete = true
        return this
    }
}
