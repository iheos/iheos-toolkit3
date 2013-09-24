package gov.nist.hit.ds.simSupport.serializer;

import gov.nist.hit.ds.simSupport.client.Simulator;
import gov.nist.hit.ds.simSupport.sim.SimDb;
import gov.nist.hit.ds.xdsException.XdsInternalException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * TODO: All ActorSimConfigs in the Simulator should be saved to the same 
 * actor.xml file.
 * @author bmajur
 *
 */
public class SimulatorSerializer {
	static final boolean useJson = true;
	static Logger logger = Logger.getLogger(SimulatorSerializer.class);

	public SimDb save(Simulator sim) throws IOException, XdsInternalException {
		
		SimDb simdb = null;  // storage reference for a single ActorSim

		// Build space for all ActorSims in this Simulator
		// The resultant simdb var will only be used as a reference
		// to the Simulator, not the individual ActorSim.
		simdb = SimDb.createActorSim(sim.getId());

		if (useJson)
			jsonSave(simdb, sim);
		else
			javaSave(simdb, sim);
		return simdb;
	}

	Simulator javaSave(SimDb simdb, Simulator sim) throws IOException {

		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		fos = new FileOutputStream(simdb.getSimulatorControlFile());
		out = new ObjectOutputStream(fos);
		out.writeObject(sim);
		out.close();

		return sim;
	}

	Simulator jsonSave(SimDb simdb, Simulator sim) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);

		FileOutputStream fos = null;
		File outFile = simdb.getSimulatorJsonControlFile();
		logger.info("Write sim file to <" + outFile + ">");
		try {
			fos = new FileOutputStream(outFile);

			// convert user object to json string, and save to a file
			mapper.writeValue(fos, sim);
		} 
		finally {
			if (fos != null)
				fos.close();
		}
		return sim;
	}

	public Simulator load(File filename) throws IOException, ClassNotFoundException {
		if (useJson)
			return jsonLoad(filename);
		else
			return javaLoad(filename);
	}

	Simulator javaLoad(File filename) throws IOException, ClassNotFoundException {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		Simulator sim;
		fis = new FileInputStream(filename);
		in = new ObjectInputStream(fis);
		sim = (Simulator)in.readObject();
		in.close();

		return sim;
	}

	Simulator jsonLoad(File filename) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		FileInputStream fis = null;
		fis = new FileInputStream(filename);

		// read from file, convert it to user class
		Simulator sim = mapper.readValue(fis, Simulator.class);
		return sim;
	}
}
