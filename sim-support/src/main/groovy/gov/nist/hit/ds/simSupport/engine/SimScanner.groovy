package gov.nist.hit.ds.simSupport.engine

import gov.nist.hit.ds.simSupport.exception.SimEngineExecutionException
import gov.nist.hit.ds.simSupport.simChain.SimChain

/**
 * Scan a SimChain for structural integrity, that each subscription
 * is matched by a prior publication. The SimChain is not executed,
 * use SimEngine for that.
 * Created by bmajur on 4/28/14.
 */
class SimScanner extends SimEngine {

    /**
     * Enable trace so we get diagnostic output.
     * @param simChain
     */
    SimScanner(SimChain simChain) {
        super(simChain)
        trace = true
    }

    /**
     * As a scanner, don't scan the component.
     * @param simStep
     */
    def runSimComponent(def simStep) {}

    /**
     * Since the component is not scan, there are no input/output
     * objects to be moved around via pub/sub.
     * @param match
     * @throws SimEngineExecutionException
     */
    void executePubSub(PubSubMatch match) throws SimEngineExecutionException {
        println match
    }
}
