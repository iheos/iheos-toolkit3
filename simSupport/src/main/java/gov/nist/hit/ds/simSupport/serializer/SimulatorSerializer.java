package gov.nist.hit.ds.simSupport.serializer;

import gov.nist.hit.ds.simSupport.client.SimulatorConfig;
import gov.nist.hit.ds.simSupport.sim.SimDb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SimulatorSerializer {
	
	public SimulatorConfig save(SimulatorConfig sc) throws IOException {
		SimDb simdb = SimDb.mkSim(sc.getId(), sc.getActorType());

		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		fos = new FileOutputStream(simdb.getSimulatorControlFile());
		out = new ObjectOutputStream(fos);
		out.writeObject(sc);
		out.close();
		
		return sc;
	}

	public SimulatorConfig load(File filename) throws IOException, ClassNotFoundException {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		SimulatorConfig config;
		fis = new FileInputStream(filename);
		in = new ObjectInputStream(fis);
		config = (SimulatorConfig)in.readObject();
		in.close();

		return config;
	}
}
