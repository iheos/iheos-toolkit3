package gov.nist.hit.ds.dsSims.eb.actors

import gov.nist.hit.ds.simSupport.actor.Actor
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus

/**
 * Created by bmajur on 1/21/15.
 */
class DocSrc implements Actor {
    @Override
    ValidationStatus validateRequest() {
        return null
    }

    @Override
    ValidationStatus validateResponse() {
        return null
    }

    @Override
    ValidationStatus acceptRequest() {
        return null
    }

    @Override
    ValidationStatus sendRequest() {
        return null
    }
}
