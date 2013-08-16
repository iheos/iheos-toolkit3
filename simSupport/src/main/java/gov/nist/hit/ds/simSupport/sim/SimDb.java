package gov.nist.hit.ds.simSupport.sim;

import gov.nist.hit.ds.actorTransaction.ActorType;
import gov.nist.hit.ds.actorTransaction.TransactionType;
import gov.nist.hit.ds.http.environment.Event;
import gov.nist.hit.ds.initialization.ExtendedPropertyManager;
import gov.nist.hit.ds.initialization.Installation;
import gov.nist.hit.ds.simSupport.client.NoSimException;
import gov.nist.hit.ds.simSupport.client.SimId;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.utilities.io.ZipDir;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Each simulator has an on-disk presence that keeps track of its long
 * term status and a log of its input/output messages. This class
 * represents that on-disk presence.
 */
public class SimDb {
	SimId simId = null;    // ip is the simulator id
	File dbRoot = null;  // base of the simulator db
	String event = null;
	File simDir = null;   // directory within simdb that represents this event
	ActorType actorType = null;
	String transaction = null;
	File transactionDir = null;  
	static Logger logger = Logger.getLogger(SimDb.class);


	/**
	 * Create a new ActorSim.  This does not create a new event.  That
	 * comes later when an incoming request is processed.
	 * @param simid
	 * @param actorType
	 * @return
	 * @throws IOException
	 */
	static public SimDb createActorSim(SimId simid, ActorType actorType) throws IOException {

		File simActorDir = new File(getDbRoot().getAbsolutePath() + File.separatorChar + simid + File.separatorChar + actorType.getShortName());
		simActorDir.mkdirs();
		if (!simActorDir.exists()) {
			logger.error("Simulator " + simid + ", " + actorType + " cannot be created");
			throw new IOException("Simulator " + simid + ", " + actorType + " cannot be created");
		}

		return new SimDb(simid);
	}

	public SimDb() {

	}

	public SimDb(SimId simId) {
		this.simId = simId;
		simDir = getSimIdRoot(simId); 
	}

	/**
	 * Create an event for the Simulator defined by simId and the ActorSim defined by actorType.  
	 * The simulator must already exist. The actor and transaction informtion
	 * withing the simulator need not exist ahead of time. It will be created as
	 * part of this call if necessary. As a result of this call, a new simulator event
	 * will be created. So after successful completion of this call, content can
	 * be added to the simulator state.
	 * @param simId - simulator id
	 * @param actor - short name for Actor
	 * @param transaction - short name for Transaction
	 * @throws IOException
	 * @throws NoSimException - Simulator does not exist.
	 */
	public Event createEvent(ActorType actorType, String transaction) throws IOException, NoSimException {
		this.actorType = actorType;
		this.transaction = transaction;
		this.dbRoot = getDbRoot();

		if (!dbRoot.canWrite() || !dbRoot.isDirectory())
			throw new IOException("Simulator database location, " + dbRoot.toString() + " is not a directory or cannot be written to");

		simDir = getSimIdRoot(simId); 
		if (!simDir.exists()) {
			logger.error("Simulator " + simId + " does not exist");
			throw new NoSimException("Simulator " + simId + " does not exist");
		}

		simDir.mkdirs();

		if (!simDir.isDirectory())
			throw new IOException("Cannot create content in Simulator database, creation of " + simDir.toString() + " failed");

		if (actorType != null && transaction != null) {
			String transdir = simDir + File.separator + actorType.getShortName() + File.separator + transaction;
			transactionDir = new File(transdir);
			transactionDir.mkdirs();
			if (!transactionDir.isDirectory())
				throw new IOException("Cannot create content in Simulator database, creation of " + transactionDir + " failed");
		}

		event = nowAsFilenameBase();
		return new Event(getEventDir());
	}

	File getSimIdRoot(SimId id) {
		return new File(getDbRoot().toString()  /*.getAbsolutePath()*/ + File.separatorChar + id);
	}

	static public File getDbRoot() {
		return Installation.installation().simDbFile();
	}

	public void delete() {
		delete(simDir);
	}

	public String getActorForSimulator() {
		File[] files = simDir.listFiles();
		for (File file : files) {
			if (file.isDirectory())
				return file.getName();
		}
		return null;
	}

	static public Date getNewExpiration(@SuppressWarnings("rawtypes") Class controllingClass)   {
		// establish expiration for newly touched cache elements
		Date now = new Date();
		Calendar newExpiration = Calendar.getInstance();
		newExpiration.setTime(now);

		String dayOffset = ExtendedPropertyManager.getProperty(controllingClass, "expiration");
		if (dayOffset == null) {
			logger.info("Extended Property expiration of class " + controllingClass + " is not defined");
			dayOffset = "365";
		}
		newExpiration.add(Calendar.DAY_OF_MONTH, Integer.parseInt(dayOffset));
		return newExpiration.getTime();
	}



	public List<SimId> getAllSimIds() {
		File[] files = dbRoot.listFiles();
		List<SimId> ids = new ArrayList<SimId>();

		for (File sim : files) {
			if (sim.isDirectory())
				ids.add(new SimId(sim.getName()));
		}

		return ids;
	}

	public File getSimulatorControlFile() {
		return new File(simDir.toString() + File.separatorChar + "simctl.ser");
	}

	public File getSimulatorJsonControlFile() {
		return new File(simDir.toString() + File.separatorChar + "simctl.json");
	}

	public static String getTransactionDirName(TransactionType tt)  {
		return tt.getShortName();
	}

	public File getTransactionDir(TransactionType tt) {
		String trans = getTransactionDirName(tt);
		return new File(simDir 
				+ File.separator + actorType.getShortName()
				+ File.separator + trans
				);
	}

	public File getRegistryObjectFile(String id) {
		if (id == null)
			return null;
		if (!id.startsWith("urn:uuid:"))
			return null;

		// version of uuid that could be used as filename
		String x = id.substring(9).replaceAll("-", "_");

		File registryDir = new File(getDBFilePrefix(event) + File.separator + "Registry");
		registryDir.mkdirs();

		return new File(registryDir.toString() + File.separator + x + ".xml");
	}

	public List<String> getTransactionNames(String actorType) {
		File transDir = new File(simDir.toString() + File.separator + actorType);
		List<String> names = new ArrayList<String>();

		try {
			for (File f : transDir.listFiles()) {
				if (f.isDirectory())
					names.add(f.getName());
			}
		} catch (Exception e) {}

		return names;
	}


	public File getRegistryIndexFile() {
		File regDir = new File(simDir.toString() + File.separator + actorType.getShortName());
		regDir.mkdirs();
		return new File(regDir.toString() + File.separator + "reg_db.ser");
	}

	public File getRepositoryIndexFile() {
		File regDir = new File(simDir.toString() + File.separator + actorType.getShortName());
		regDir.mkdirs();
		return new File(regDir.toString() + File.separator + "rep_db.ser");
	}


	//	public void setSimulatorType(String type) throws IOException {
	//		File simType = new File(getDBFilePrefix(fileNameBase) + File.separator + "sim_type.txt");
	//		Io.stringToFile(simType, type);
	//	}

	public ActorType getSimulatorActorType() {
		File aDir = new File(simDir.toString());
		for (File file : aDir.listFiles()) {
			if (file.isDirectory()) {
				String name = file.getName();
				return ActorType.findActor(name);
			}
		}
		return null;
	}

	public List<String> getTransactionsForSimulator() {
		List<String> trans = new ArrayList<String>();

		for (File actor : simDir.listFiles()) {
			if (!actor.isDirectory())
				continue;
			for (File tr : actor.listFiles()) {
				if (!tr.isDirectory())
					continue;
				trans.add(tr.getName());
			}
		}

		return trans;
	}


	// huh? nothing is creating this file
	public String getSimulatorType() throws IOException {
		File simType = new File(getDBFilePrefix(event) + File.separator + "sim_type.txt");
		return Io.stringFromFile(simType).trim();
	}

	public File getRepositoryDocumentFile(String documentId) {
		File repDirFile = new File(getDBFilePrefix(event) + File.separator + "Repository");
		repDirFile.mkdirs();
		File repDocFile = new File(repDirFile.toString() + File.separator + oidToFilename(documentId) + ".bin");
		return repDocFile;
	}

	String oidToFilename(String oid) {
		return oid.replaceAll("\\.", "_");
	}

	String filenameToOid(String filename) {
		return filename.replaceAll("_", ".");
	}

	public String getFileNameBase() {
		return event;
	}

	public void setFileNameBase(String base) {
		event = base;
	}

	public File getSimDir() {
		return getIpDir();
	}

	public File getIpDir() {
		return simDir;
	}

	/**
	 * Get list of transaction instance file names for this transaction in this simulator.
	 * @param trans
	 * @return
	 */
	public List<String> getTransInstances(String trans) {
		List<String> names = new ArrayList<String>();

		for (File actor : simDir.listFiles()) {
			if (!actor.isDirectory())
				continue;
			for (File tr : actor.listFiles()) {
				if (!tr.isDirectory())
					continue;
				String name = tr.getName();
				if (trans != null && !name.equals(trans) && !trans.equals(("All")))
					continue;
				for (File inst : tr.listFiles()) {
					if (!inst.isDirectory())
						continue;
					names.add(inst.getName() + " " + name);
				}
			}
		}

		String[] nameArray = names.toArray(new String[0]);
		java.util.Arrays.sort(nameArray);	


		List<String> returns = new ArrayList<String>();
		for (int i=nameArray.length-1; i>=0; i--)
			returns.add(nameArray[i]);

		return returns;
	}

	public File[] getTransInstanceFiles(String actor, String trans) {
		File dir = new File(simDir 
				+ File.separator + actor
				+ File.separator + trans
				);


		File[] files = dir.listFiles();
		return files;
	}

	File getDBFilePrefix(String event) {
		File f = new File(simDir 
				+ File.separator + actorType.getShortName()
				+ File.separator + transaction
				+ File.separator + event
				);
		f.mkdirs();
		return f;
	}

	public File getEventDir() {
		return getDBFilePrefix(event);
	}

	public File getLogFile() {
		return new File(getDBFilePrefix(event) + File.separator + "log.txt");
	}

	public void getMessageLogZip(OutputStream os, String event) throws IOException {
		new ZipDir().toOutputStream(getDBFilePrefix(event).toString(), os);
	}

	public void delete(String fileNameBase) throws IOException {
		File f = getDBFilePrefix(fileNameBase);
		delete(f);
	}

	public void delete(File f) {
		Io.delete(f);
	}

	public void rename(String fileNameBase, String newFileNameBase) throws IOException {

		File from = getDBFilePrefix(fileNameBase);
		File to = getDBFilePrefix(newFileNameBase);
		boolean stat = from.renameTo(to);

		if (!stat)
			throw new IOException("Rename failed");

	}

	File findEventDir(String trans, String event) {
		for (File actor : simDir.listFiles()) {
			if (!actor.isDirectory())
				continue;
			File eventDir = new File(actor + File.separator + trans + File.separator + event);
			if (eventDir.exists() && eventDir.isDirectory())
				return eventDir;
		}
		return null;
	}

	public File getTransactionEvent(SimId simid, String actor, String trans, String event) {
		File dir = new File(simDir 
				+ File.separator + actor
				+ File.separator + trans
				+ File.separator + event
				);

		return dir;
	}

	public File getLogFile(SimId simid, String trans, String event) {
		File dir = findEventDir(trans, event);
		if (dir == null)
			return null;
		return new File(dir + File.separator + "log.txt");
	}

	public List<String> getRegistryIds(SimId simid, String actor, String trans, String event) {
		List<String> ids = new ArrayList<String>();

		File dir = getTransactionEvent(simid, actor, trans, event);
		File registry = new File(dir.toString() + File.separator + "Registry");

		if (registry.exists()) {
			for (File f : registry.listFiles()) {
				String filename = f.getName();
				int dotI = filename.indexOf('.');
				if (dotI != -1) {
					String name = filename.substring(0, dotI);
					ids.add(name);
				}
			}
		}
		return ids;
	}

	public String nowAsFilenameBase() {
		Date date = new Date();

		Calendar c  = Calendar.getInstance();
		c.setTime(date);

		String year = Integer.toString(c.get(Calendar.YEAR));
		String month = Integer.toString(c.get(Calendar.MONTH) + 1);
		if (month.length() == 1)
			month = "0" + month;
		String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		if (day.length() == 1 )
			day = "0" + day;
		String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
		if (hour.length() == 1)
			hour = "0" + hour;
		String minute = Integer.toString(c.get(Calendar.MINUTE));
		if (minute.length() == 1)
			minute = "0" + minute;
		String second = Integer.toString(c.get(Calendar.SECOND));
		if (second.length() == 1)
			second = "0" + second;
		String mili = Integer.toString(c.get(Calendar.MILLISECOND));
		if (mili.length() == 2)
			mili = "0" + mili;
		else if (mili.length() == 1)
			mili = "00" + mili;

		String dot = "_";

		String val =
				year +
				dot +
				month +
				dot +
				day + 
				dot +
				hour +
				dot +
				minute +
				dot +
				second +
				dot +
				mili
				;
		return val;
	}

}
