package gov.nist.hit.ds.dsSims.fhir.mhd.transactions

import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.simSupport.simulator.SimHandle

/**
 * Created by bmajur on 12/4/14.
 */
class Pdr implements Transaction {
    SimHandle simHandle

    def Pdr(SimHandle _simHamdle) { simHandle = _simHamdle}

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
