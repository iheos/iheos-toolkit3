package gov.nist.hit.ds.simSupport.serializer;

/**
 * TODO: All ActorSimConfigs in the Simulator should be saved to the same 
 * actor.xml file.
 * TODO: Needs tests.
 * @author bmajur
 *
 */
public class SimulatorSerializer {
//	enum SaveType { JAVA, JSON, REPOSITORY };
//	static final SaveType saveType = SaveType.REPOSITORY;
//	static Logger logger = Logger.getLogger(SimulatorSerializer.class);
//
//	Repository simRepository = null;
//
//	public void setSimRepository(Repository simRepository) {
//		this.simRepository = simRepository;
//	}
//
//	public Asset save(Simulator sim)  {
//		if (sim == null)
//			throw new ToolkitRuntimeException("Cannot save empty Simulator");
//		if (sim.getSimId() == null)
//			throw new ToolkitRuntimeException("Cannot save Simulator: no SimId assigned");
//		if (simRepository == null)
//			throw new ToolkitRuntimeException("Repository not set");
//
//		return repositorySave(sim);
//	}
//
//	private Asset repositorySave(Simulator sim)  {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
//		try {
//			// Find existing asset (must already exist)
////			Asset asset = simRepository.getAsset(new SimpleId(sim.getId().getId()));
////			Asset asset = simRepository.createAsset(sim.getSimId().getId(), "Simulator", new SimpleType("simulator"));
//            Asset asset = simRepository.createNamedAsset(sim.getSimId().getId(), "Simulator", new SimpleType("simulator"), sim.getSimId().getId());
//
//			// translate sim to JSON and save as asset document
//
//            asset.setContent(new SimulatorDAO().toXML(sim), "text/xml");
////			asset.updateContent(mapper.writeValueAsString(sim));
////			// set the mime type on the asset
////			asset.setMimeType("text/json");
//            return asset;
//		} catch (Exception e) {
//			throw new ToolkitRuntimeException("Cannot serialize the Simulator", e);
//		}
//	}
//
//	public Simulator load(SimId simId)   {
//			return repositoryLoad(simId);
//	}
//
//	private Simulator repositoryLoad(SimId simId)  {
//		if (simId == null)
//			throw new ToolkitRuntimeException("Cannot loadFromPropertyBasedResource simulator for null SimId");
//		if (simRepository == null)
//			throw new ToolkitRuntimeException("Repository not set");
//		try {
//			// Find existing asset (must already exist)
//			Asset asset = simRepository.getChildByName(simId.getId());
//            return new SimulatorDAO().toModel(new String(asset.getContent()));
////			ObjectMapper mapper = new ObjectMapper();
////			mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
////
////			// read asset document, convert JSON to Simulator instance
////			return mapper.readValue(asset.getContent(), Simulator.class);
//		} catch (Exception e) {
//			throw new ToolkitRuntimeException("Cannot loadFromPropertyBasedResource Simulator <" + simId + ">", e);
//		}
//	}
//
//	Simulator javaSave(SimDb simdb, Simulator sim) throws IOException {
//
//		FileOutputStream fos = null;
//		ObjectOutputStream out = null;
//
//		fos = new FileOutputStream(simdb.getSimulatorControlFile());
//		out = new ObjectOutputStream(fos);
//		out.writeObject(sim);
//		out.close();
//
//		return sim;
//	}
//
//	Simulator jsonSave(SimDb simdb, Simulator sim) throws IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
//
//		FileOutputStream fos = null;
//		File outFile = simdb.getSimulatorJsonControlFile();
//		logger.info("Write sim file to <" + outFile + ">");
//		try {
//			fos = new FileOutputStream(outFile);
//
//			// convert user object to json string, and save to a file
//			mapper.writeValue(fos, sim);
//		} 
//		finally {
//			if (fos != null)
//				fos.close();
//		}
//		return sim;
//	}
//
//	Simulator javaLoad(File filename) throws IOException, ClassNotFoundException {
//		FileInputStream fis = null;
//		ObjectInputStream in = null;
//		Simulator sim;
//		fis = new FileInputStream(filename);
//		in = new ObjectInputStream(fis);
//		sim = (Simulator)in.readObject();
//		in.close();
//
//		return sim;
//	}
//
//	Simulator jsonLoad(File filename) throws IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
//		FileInputStream fis = null;
//		fis = new FileInputStream(filename);
//
//		// read from file, convert it to user class
//		Simulator sim = mapper.readValue(fis, Simulator.class);
//		return sim;
//	}
}
