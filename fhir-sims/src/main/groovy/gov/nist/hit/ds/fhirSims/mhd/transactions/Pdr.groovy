package gov.nist.hit.ds.fhirSims.mhd.transactions

import Transaction
import ValidationStatus
import SimHandle

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
