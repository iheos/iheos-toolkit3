package gov.nist.hit.ds.dsSims.transactions

import gov.nist.hit.ds.dsSims.Transaction
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.tkapis.validation.ValidationStatus

/**
 * Created by bmajur on 10/21/14.
 */
class Reg implements Transaction {
    SimHandle simHandle

    def Reg(SimHandle _simHandle) { simHandle = _simHandle }
    @Override
    ValidationStatus validateRequest() {
        return null
    }

    @Override
    ValidationStatus validateResponse() {
        return null
    }

    @Override
    ValidationStatus run() {
        return null
    }
}
