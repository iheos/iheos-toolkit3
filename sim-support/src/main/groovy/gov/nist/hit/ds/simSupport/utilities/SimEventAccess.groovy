package gov.nist.hit.ds.simSupport.utilities

import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.eventLog.testSupport.EventAccess
import gov.nist.hit.ds.simSupport.client.SimIdentifier

/**
 * Created by bmajur on 2/3/15.
 */
class SimEventAccess extends EventAccess {

    SimEventAccess(SimIdentifier simIdent, Event event) {
        super(simIdent.simId.id, simIdent.repoName, event)
    }
}
