package gov.nist.hit.ds.simSupport.transaction;

import gov.nist.hit.ds.simSupport.simulator.SimHandle;

/**
 * Created by bmajur on 8/28/14.
 */
public interface Transaction {
    ValidationStatus validateRequest(SimHandle simHandle);
    ValidationStatus validateResponse(SimHandle simHandle);
    ValidationStatus acceptRequest(SimHandle simHandle);
    ValidationStatus sendRequest(SimHandle simHandle);
}
