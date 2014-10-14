package gov.nist.hit.ds.simSupport.client.configElementTypes

import gov.nist.hit.ds.actorTransaction.EndpointType
import gov.nist.hit.ds.simSupport.endpoint.EndpointValue
import gov.nist.hit.ds.utilities.other.UuidAllocator

/**
 * Created by bill on 10/13/14.
 */
class RetrieveTransactionSimConfigElement extends TransactionSimConfigElement {
    String repositoryUniqueId

    RetrieveTransactionSimConfigElement(EndpointType _type, EndpointValue _endpoint) {
        super(_type, _endpoint)
        repositoryUniqueId = UuidAllocator.allocateOid()
    }
}
