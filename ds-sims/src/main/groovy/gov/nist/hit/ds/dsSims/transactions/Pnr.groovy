package gov.nist.hit.ds.dsSims.transactions

import gov.nist.hit.ds.simSupport.simulator.SimHandle

/**
 * Created by bmajur on 9/24/14.
 */
class Pnr {
    SimHandle simHandle

    def Pnr(SimHandle _simHandle) { simHandle = _simHandle }

    def run() {  println("Running PnR transaction")}
}
