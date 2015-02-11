package gov.nist.hit.ds.simSupport.actor

import gov.nist.hit.ds.simSupport.transaction.ValidationStatus

/**
 * Created by bmajur on 1/21/15.
 */
interface Actor {
    ValidationStatus validateRequest();
    ValidationStatus validateResponse();
    ValidationStatus acceptRequest();
    ValidationStatus sendRequest();
}
