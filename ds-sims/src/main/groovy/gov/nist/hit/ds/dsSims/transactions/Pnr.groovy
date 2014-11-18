package gov.nist.hit.ds.dsSims.transactions

import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus

/**
 * Created by bmajur on 9/24/14.
 */
class Pnr implements Transaction {
    SimHandle simHandle

    def Pnr(SimHandle _simHandle) { simHandle = _simHandle }

    @Override
    ValidationStatus validateRequest() {
        return null
    }

    @Override
    ValidationStatus validateResponse() {
        return null
    }

    ValidationStatus run() {
        println("Running PnR transaction")
    }
}
