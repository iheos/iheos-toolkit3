package gov.nist.hit.ds.simSupport.service
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.ActorType
import gov.nist.hit.ds.simSupport.client.SimId
import gov.nist.hit.ds.simSupport.client.Simulator
import gov.nist.hit.ds.simSupport.factory.SimulatorFactory
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException

/**
 * Created by bmajur on 9/22/14.
 */
class SimService {

    def create(String simIdStr, String actorTypeName) {
        def atfactory = new ActorTransactionTypeFactory()
        SimId simId = new SimId(simIdStr)
        if (SimulatorFactory.exists(simId)) throw new ToolkitRuntimeException("Simulator ${simIdStr} already exists.")
        ActorType actorType = atfactory.getActorType(actorTypeName)
        SimulatorFactory factory = new SimulatorFactory()
        factory.initializeSimulator(simId)
        factory.addActorSim(actorType)
        factory.save(factory.getSimulator())
    }

    Simulator load(String simIdStr) {
        SimId simId = new SimId(simIdStr)
        return SimulatorFactory.load(simId)
    }

    def delete(String simIdStr) {
        SimId simId = new SimId(simIdStr)
        SimulatorFactory.delete(simId)
    }

    def exists(String simIdStr) { return SimulatorFactory.exists(new SimId(simIdStr))}
}
