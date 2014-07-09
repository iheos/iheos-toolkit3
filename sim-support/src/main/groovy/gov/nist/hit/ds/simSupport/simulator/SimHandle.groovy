package gov.nist.hit.ds.simSupport.simulator

import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.repository.api.Asset
import gov.nist.hit.ds.simSupport.client.SimId

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
}
