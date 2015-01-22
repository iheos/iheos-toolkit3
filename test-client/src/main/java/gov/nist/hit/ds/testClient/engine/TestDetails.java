package gov.nist.hit.ds.testClient.engine;

import gov.nist.hit.ds.testClient.logging.LogFileContent;
import gov.nist.hit.ds.testClient.logging.SectionLogMap;
import gov.nist.hit.ds.testClient.logging.SectionTestPlanFileMap;
import gov.nist.hit.ds.utilities.io.LinesOfFile;
import gov.nist.hit.ds.xdsExceptions.XdsInternalException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDetails {

	File testkit;
	File logdir;
	String area;  // examples, tests etc
	String testNum;
//	List<File> testPlanFiles;
	public SectionTestPlanFileMap testPlanFileMap;   // sectionName ==> testplan.xml file
	public SectionLogMap sectionLogMap = new SectionLogMap();
	String[] areas;
	
	static Logger logger = Logger.getLogger(TestDetails.class);

	
	public static final String[] defaultAreas = new String [] { "tests", "testdata", "examples", "internal", "play",
		"selftest", "development", "utilities", "xcpd"};
	static final String testPlanFileName = "testplan.xml";

	public String toString() { return "[TestSpec: testkit=" + testkit + " area=" + area +
		"<br />testnum=" + testNum +
		"<br />sections= " + testPlansToString() +
		"<br />logs= " + sectionLogMap.toString() +
		"]";
	}
	
	public SectionLogMap getSectionLogMap() {
		return sectionLogMap;
	}
	
	public void addTestPlanLog(String section, LogFileContent lf) throws XdsInternalException {
		if (sectionLogMap == null)
			sectionLogMap = new SectionLogMap();		
		sectionLogMap.put(section, lf);
	}
	
	public void resetLogs() {
		sectionLogMap = new SectionLogMap();
	}
	
	public SectionLogMap getTestPlanLogs() {
		return sectionLogMap;
	}
	
	public void setLogDir(File logdir) {
		this.logdir = logdir;
	}

	String testPlansToString() {
		StringBuffer buf = new StringBuffer();

		if (testPlanFileMap == null) {
			buf.append("null");
			return buf.toString();
		}

		buf.append("[");
		boolean first = true;
		for (String section : testPlanFileMap.getSectionNames()) {
			File tp = testPlanFileMap.getPlanFile(section);
			if (!first)
				buf.append(", ");
			first=false;
			String parentdir = tp.getParent();
			int lastSlash = parentdir.lastIndexOf(File.separatorChar);
			if (lastSlash == -1)
				buf.append(parentdir);
			else
				buf.append(parentdir.substring(lastSlash+1));
		}
		buf.append("]");

		return buf.toString();
	}

	/**
	 * Remove from the path of a test, the testkit prefix. Useful for creating same
	 * directory structure in different place for output files.
	 * @param testPath
	 * @param testkit
	 * @return relative path
	 * @throws Exception
	 */
	static public String getLogicalPath(File testPath, File testkit) throws Exception {
		String testkitpath = testkit.toString();
		String testpath = testPath.toString();

		if ( ! testpath.startsWith(testkitpath))
			throw new Exception("Path does not target contents of testkit");

		String diff = testpath.substring(testkitpath.length());
		if (diff.charAt(0) == '/')
			return diff.substring(1);
		return diff;
	}


	File simpleTestDir() throws Exception {
		File file = new File(getTestDir() + File.separator + testPlanFileName);
		if ( !file.exists())
			throw new Exception("Test plan " + file + " does not exist");
		return file;
	}

	public TestDetails(File testkit, String testNum) throws Exception {
		this.testkit = testkit;
		this.testNum = testNum;
		areas = defaultAreas;
		verifyCurrentTestExists(testkit, testNum);
		testPlanFileMap = getTestPlans();
	}

	public TestDetails(File testkit, String testNum, String[] areas) throws Exception {
		this.testkit = testkit;
		this.testNum = testNum;
		this.areas = areas;
		verifyCurrentTestExists(testkit, testNum);
		testPlanFileMap = getTestPlans();
	}
	
	public TestDetails(File testkit) throws Exception {
		if (new File(testkit + File.separator + "tests").exists()) {
			// this is the testkit
			this.testkit = testkit;
			areas = defaultAreas;
		} else  {
			// this is a test directory
			this.testkit = null;
			testPlanFileMap = getTestPlanFromDir(testkit);
		}
	}

	private void verifyCurrentTestExists(File testkit, String testNum)
			throws Exception {
		for (int i=0; i<areas.length; i++) {
			area = areas[i];
			if (exists())
				break;
		}
		if ( ! exists() ) {
			String msg = "TestSpec (testkit=" + testkit + " testNum=" + testNum + ", no " + testPlanFileName + " files found";
			logger.error(msg);
			throw new Exception(msg);
		}
	}

	static public void listTestKitContents(File testkit) throws Exception {
		TestDetails ts = new TestDetails(testkit);

		for (int i=0; i<defaultAreas.length; i++) {
			ts.area = defaultAreas[i];
			File sectionDir = ts.getSectionDir();
			if ( !sectionDir.exists())
				continue;
			System.out.println("======================  " +  ts.area + "  ======================");

			String[] files = sectionDir.list();
			if (files == null)
				continue;
			for (int j=0; j<files.length; j++) {
				if (files[j].startsWith("."))
					continue;
				File file = new File(sectionDir + File.separator + files[j]);
				if ( !file.isDirectory()) 
					continue;
				ts.testNum = file.getName();
				if (ts.isTestDir()) {
					File readme = ts.getReadme();
					String firstline = "";
					if (readme.exists() && readme.isFile())
						firstline = firstLineOfFile(readme);
					System.out.println(ts.testNum + "\t" + firstline.trim());
				}
			}
		}
	}

	static public Map<String, String> getTestKitReadMe(File testkit) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		TestDetails ts = new TestDetails(testkit);

		for (int i=0; i<defaultAreas.length; i++) {
			ts.area = defaultAreas[i];
			File sectionDir = ts.getSectionDir();
			if ( !sectionDir.exists())
				continue;

			String[] files = sectionDir.list();
			if (files == null)
				continue;
			for (int j=0; j<files.length; j++) {
				if (files[j].startsWith("."))
					continue;
				File file = new File(sectionDir + File.separator + files[j]);
				if ( !file.isDirectory()) 
					continue;
				ts.testNum = file.getName();
				if (ts.isTestDir()) {
					File readme = ts.getReadme();
					String firstline = "";
					if (readme.exists() && readme.isFile())
						firstline = firstLineOfFile(readme);
					map.put(ts.testNum, firstline.trim());
				}
			}
		}
		return map;
	}

	static String firstLineOfFile(File file) throws IOException {
		LinesOfFile lof = new LinesOfFile(file);
		return lof.next();
	}

	public File getReadme() {
		return new File(getTestDir() + File.separator + "readme.txt");
	}

	public String getReadmeFirstLine() throws IOException {
		return new LinesOfFile(getReadme()).next();
	}

	File getSectionDir() {
		return new File(testkit + File.separator + area);
	}

	public File getTestDir() {
		return new File(testkit + File.separator + area + File.separatorChar + testNum);
	}

	public File getTestLogDir() {
		if (logdir == null)
			return null;
		return new File(logdir + File.separator + area + File.separatorChar + testNum);
	}

	public boolean exists() {
		return getTestDir().isDirectory();
	}

	public SectionTestPlanFileMap getTestPlans() throws Exception {

		File index = new File(getTestDir() + File.separator + "index.idx");
		if (index.exists()) 
			return getTestPlansFromIndex(index);
		else 
			return getTestPlanFromDir(getTestDir());

	}
	
	public File getIndexFile() {
		return new File(getTestDir() + File.separator + "index.idx");
	}
	
	public List<File> getTestLogs(String upToSection) throws Exception {
		List<File> logfiles = new ArrayList<File>();
		File index = getIndexFile();
		if (index.exists()) 
			return getTestLogsFromIndex(index, upToSection);
		else {
			File testlogdir = getTestLogDir();
			if (testlogdir != null)
				logfiles.add(new File(testlogdir.toString() + File.separatorChar + "log.xml"));
		}
		
		return logfiles;
	}

	SectionTestPlanFileMap getTestPlansFromIndex(File index) throws Exception {
		SectionTestPlanFileMap plans = new SectionTestPlanFileMap();
		File testdir = getTestDir();

		for (LinesOfFile lof = new LinesOfFile(index); lof.hasNext(); ) {
			String dir = lof.next().trim();
			if (dir.length() ==0)
				continue;
			File path = new File(testdir + File.separator + dir + File.separatorChar + testPlanFileName);
			if ( ! path.exists() )
				throw new Exception("TestSpec " + toString() + " references sub-directory " + dir + 
						" which does not exist or does not contain a " + testPlanFileName + " file");
			plans.put(dir, path);
		}
		return plans;
	}

	/**
	 * Read test logs based on index.idx file up to but not including section upToSection. If 
	 * upToSection is null, then read all.
	 * @param index - index.idx file
	 * @param upToSection - first section to not read
	 * @return list of Files representing log files
	 * @throws Exception
	 */
	List<File> getTestLogsFromIndex(File index, String upToSection) throws Exception {
		List<File> logs = new ArrayList<File>();
		if (logdir == null)
			return logs;
		
		if (!index.exists())
			return logs;

		for (LinesOfFile lof = new LinesOfFile(index); lof.hasNext(); ) {
			String dir = lof.next().trim();
			if (dir.length() ==0)
				continue;
			if (upToSection != null && dir.equals(upToSection))
				return logs;
			File path = new File(logdir + File.separator + area + File.separator + testNum + File.separatorChar + dir + File.separatorChar + "log.xml");
			if ( ! path.exists() )
				throw new Exception("TestSpec " + toString() + " references the section " + dir + 
						" - no log file exists ( file " + path.toString() + " does not exist");
			logs.add(path);
		}
		return logs;
	}
	
	public List<String> getSectionsFromTestDef(File testdir) throws IOException {
		List<String> sections = new ArrayList<String>();
		if (logdir == null)
			return sections;
		
		File index = new File(testdir + File.separator + "index.idx");
		
		if (!index.exists())
			return sections;

		for (LinesOfFile lof = new LinesOfFile(index); lof.hasNext(); ) {
			String dir = lof.next().trim();
			if (dir.length() ==0)
				continue;
			File path = new File(logdir + File.separator + area + File.separator + testNum + File.separatorChar + dir + File.separatorChar + "log.xml");
			sections.add(dir);
		}
		
		
		return sections;
	}
	
	public File getTestLog(String testNum, String section) {
		File path;
		if (logdir == null)
			return null;
		
		if (section != null && !section.equals("") && !section.equals("None"))
			path = new File(logdir + File.separator + area + File.separator + testNum + File.separatorChar + section + File.separatorChar + "log.xml");
		else
			path = new File(logdir + File.separator + area + File.separator + testNum + File.separatorChar  + "log.xml");
		return path;
	}

	public void selectSections(List<String> sectionNames) throws Exception {
		loadTestPlansFromSectionList(sectionNames);
		
		// will need to load all previous section logs for referencing
		List<File> previousLogFiles = getTestLogsFromIndex(getIndexFile(), sectionNames.get(0));
				
		for (File f : previousLogFiles) {
			LogFileContent lf = new LogFileContent(f);
			String sectionName = lf.getSection();
			//System.out.println("\tLoading log for section " + sectionName);
			sectionLogMap.put(sectionName, lf);
		}
		
	}
	
	public File getTestplanFile(String section) throws Exception {
		File testdir = getTestDir();
		File path;
		
		if (section == null) {
			path = new File(testdir + File.separator + testPlanFileName);
		} else {
			path = new File(testdir + File.separator + section + File.separator + testPlanFileName);
		}
		if ( ! path.exists() )
			throw new Exception("Test Section " + section + 
					" has been requested but does not exist or does not contain a " + testPlanFileName + " file");
		return path;
	}

	void loadTestPlansFromSectionList(List<String> sections) throws Exception {
		testPlanFileMap = new SectionTestPlanFileMap();
//		File testdir = getTestDir();

		for (String sectionName : sections ) {
			File path = getTestplanFile(sectionName);
//			String dir = sectionName;
//			File path = new File(testdir + File.separator + dir + File.separatorChar + testPlanFileName);
//			if ( ! path.exists() )
//				throw new Exception("Test Section " + dir + 
//						" has been requested but does not exist or does not contain a " + testPlanFileName + " file");
			testPlanFileMap.put(sectionName, path);
		}
	}

	SectionTestPlanFileMap getTestPlanFromDir(File dir) throws Exception {
		SectionTestPlanFileMap plans = new SectionTestPlanFileMap();

		File path = new File(dir + File.separator + testPlanFileName);
		if ( ! path.exists() ) 
			return plans;
//			throw new Exception("TestSpec " + toString() + " does not have index.idx or " + testPlanFileName + " file");
		plans.put(".", path);

		return plans;
	}

	public boolean isTestDir() {
		if ( new File(getTestDir() + File.separator + "index.idx").exists())
			return true;
		if ( new File(getTestDir() + File.separator + testPlanFileName).exists())
			return true;
		return false;
	}

	public void validateTestPlans() throws XdsInternalException {
		for (File tp : testPlanFileMap.values()) {
			if ( !tp.exists())
				throw new XdsInternalException("TestPlan file " + tp + " does not exist");
		}
	}

	public String getTestNum() {
		return testNum;
	}
}

