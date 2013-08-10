package gov.nist.hit.ds.simSupport.serializer;

import gov.nist.hit.ds.simSupport.client.ActorSimConfig;
import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SimulatorSerializer {
	
	public Simulator save(Simulator sim) throws IOException, XdsInternalException {
		SimDb simdb = null;  // storage reference for a single ActorSim
		
		// Build space for all ActorSims in this Simulator
		// The resultant simdb var will only be used as a reference
		// to the Simulator, not the individual ActorSim.
		for (ActorSimConfig config : sim.getConfigs()) 
			simdb = SimDb.createActorSim(sim.getId(), config.getActorType());
		if (simdb == null)
			throw new XdsInternalException("Cannot save empty Simulator (no ActorSims defined)");

		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		fos = new FileOutputStream(simdb.getSimulatorControlFile());
		out = new ObjectOutputStream(fos);
		out.writeObject(sim);
		out.close();
		
		return sim;
	}

	public Simulator load(File filename) throws IOException, ClassNotFoundException {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Simulator sim;
		fis = new FileInputStream(filename);
		in = new ObjectInputStream(fis);
		sim = (Simulator)in.readObject();
		in.close();

		return sim;
	}
}
