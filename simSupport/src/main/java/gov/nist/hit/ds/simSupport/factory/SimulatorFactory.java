package gov.nist.hit.ds.simSupport.factory;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.AsyncType;
import gov.nist.hit.ds.actorTransaction.TlsType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.serializer.SimulatorSerializer;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.IOException;
import java.util.List;

public class SimulatorFactory {
	Simulator sim;
	SimId simId;

	public SimulatorFactory buildSimulator(SimId simId) {
		this.simId = simId;
		sim = new Simulator(simId);
		return this;
	}
	
	public GenericActorSimBuilder addActorSim(ActorType actorType, List<TransactionType> incomingTransactions, TlsType[] tlsTypes, AsyncType[] asyncTypes) {
		GenericActorSimBuilder builder = new GenericActorSimBuilder(sim).buildGenericConfiguration(actorType);
		for (int i=0; i<asyncTypes.length; i++) {
			AsyncType async = asyncTypes[i];
			for (int j = 0; j<tlsTypes.length; j++) {
				TlsType tls = tlsTypes[j];
				for (TransactionType transType : incomingTransactions) {
					builder.addEndpoint(actorType.getShortName(), 
							transType, 
							tls, 
							async);
				}
			}
		}
		
		return builder;
	}
		
	public SimulatorFactory save() throws XdsInternalException, IOException {
		new SimulatorSerializer().save(sim);
		return this;
	}
	
	public Simulator getSimulator() {
		return sim;
	}
}
