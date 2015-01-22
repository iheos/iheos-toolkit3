package gov.nist.hit.ds.session.server;

//import gov.nist.hit.ds.envSetting.EnvSetting;
//import gov.nist.hit.ds.securityCommon.SecurityParams;
//import gov.nist.hit.ds.xdsExceptions.EnvironmentNotSelectedException;
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException;
import gov.nist.toolkit.tk.TkLoader;
import gov.nist.toolkit.tk.client.TkProps;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

//import gov.nist.toolkit.testengine.TransactionSettings;

/**
 * The session object is used in one of four ways depending on the context:
 *
 * 1) GUI - each GUI session is represented by a session object. It is managed
 * through the Servlet session mechanism. It is managed by the class
 * ToolkitServiceImpl. This is a single threaded use of the session instance.
 *
 * 2) Simulators - on session object is used and shared by all simulator
 * instances. It is managed by the class SimServlet. This is a multi-threaded
 * use of the session instance.
 *
 * 3) ValServlet - RESTful service for access to validators. Just like
 * simulators above.
 *
 * 4) Dashboard - coming soon.
 *
 * @author bill
 *
 */
public class Session /*implements SecurityParams*/ {

//	public String repUid;
	// public boolean isTls;
	// public boolean isSaml;
//	public TransactionSettings transactionSettings = new TransactionSettings();
//	public boolean isAdmin = false;
//	public boolean isSoap = true;

    static final String sessionVarName = "NoSession";

	byte[] lastUpload = null;
//	byte[] input2 = null;
	String lastUploadFilename = null;
//	String input2Filename = null;
	byte[] lastUpload2 = null;
	String lastUploadFilename2 = null;
	String password1 = null;
	String password2 = null;

	// File tomcatSessionCache = null; // one for this Tomcat session
	// (corresponds to this Session object)
//	File mesaSessionCache = null; // changes each time new mesaSessionName
									// changes

	public String ipAddr = null; // also used as default sim db id
	String serverIP = null;
	String serverPort = null;
	// List<SimulatorConfig> actorSimulatorConfigs = new
	// ArrayList<SimulatorConfig>();
	String sessionId;

	// File warHome = null;
	// File externalCache = null;
//	File toolkit = null;

	// File currentEnvironmentDir = null;
//	String currentEnvironmentName = null;

//	String mesaSessionName = null;
//	SessionPropertyManager sessionProperties = null;
//	PropertyServiceManager propertyServiceMgr = null;
	static Map<String, Session> sessionMap = new HashMap<String, Session>();
	static final Logger logger = Logger.getLogger(Session.class);

//	public void verifyCurrentEnvironment() throws EnvironmentNotSelectedException {
//		EnvSetting.getEnvSetting(sessionId);
//	}

	public void addSession() {
		sessionMap.put(sessionId, this);
	}

	static public Session getSession(String sessionId) {
		return sessionMap.get(sessionId);
	}

    /**
     * Method that returns the session using servlet request (copied from v2)
     * @param request
     * @return
     */
    static public Session getSession(HttpServletRequest request) {
        if (request == null) throw new ToolkitRuntimeException("Null request object");

        Session s = null;
        HttpSession hsession = null;
        if (request != null) {
            logger.debug("HttpRequest object available");
            hsession = request.getSession();
            logger.debug("SessionId is " + hsession.getId());
            s = (Session) hsession.getAttribute(sessionVarName);
            if (s != null) {
                logger.debug("Using existing Session object");
                return s;
            }
        }

        // Force short session timeout for testing
//		hsession.setMaxInactiveInterval(60/4);    // one quarter minute

        if (s == null) {
            logger.debug("Creating new Session object");
            s = new Session();
            if (hsession != null) {
                s.setSessionId(hsession.getId());
                s.addSession();
                hsession.setAttribute(sessionVarName, s);
                logger.debug("SessionId = " + hsession.getId());
            } else {
                s.setSessionId("mysession");
                logger.debug("SessionId = " + "mysession");
            }
        }

        if (request != null) {
            if (s.getIpAddr() == null) {
                s.setIpAddr(request.getRemoteHost());
            }

            s.setServerSpec(request.getLocalName(),
                    String.valueOf(request.getLocalPort()));
        }

        return s;
    }

    public void setSessionId(String id) {
		sessionId = id;
	}


//	public File getTomcatSessionCache() {
//		return new File(Installation.installation().warHome() + File.separator + "SessionCache" + File.separator
//				+ sessionId);
//	}
//
//	public File getMesaSessionCache() {
//		return mesaSessionCache;
//	}
//
//	public String getServerIP() {
//		return serverIP;
//	}
//
//	public String getServerPort() {
//		return serverPort;
//	}

    public Session() { }

	public Session(File warHome,/* SiteServiceManager siteServiceManager, */String sessionId) {
		this(warHome/*, siteServiceManager*/);
		this.sessionId = sessionId;
	}

	public Session(File warHome/*, SiteServiceManager siteServiceManager*/) {
	}



//	public void setMesaSessionName(String name) {
//		mesaSessionName = name;
//
//		File testLogCache;
//		try {
//			testLogCache = Installation.installation().propertyServiceManager().getTestLogCache();
//		} catch (Exception e) {
//			return;
//		}
//
//		mesaSessionCache = new File(testLogCache + File.separator + mesaSessionName);
//		mesaSessionCache.mkdirs();
//	}

//	boolean mesaSessionExists() {
//		return mesaSessionCache != null;
//	}
//
//	public String getMesaSessionName() {
//		return mesaSessionName;
//	}
//
//	void setSessionProperty(String name, String value) {
//		SessionPropertyManager props = getSessionProperties();
//		if (props == null)
//			return;
//		props.set(name, value);
//		props.save();
//	}
//
//	public void setSessionProperties(Map<String, String> m) {
//		SessionPropertyManager props = getSessionProperties();
//		if (props == null)
//			return;
//		props.clear();
//		props.add(m);
//		props.save();
//	}

	/**
	 * Get id of current Session
	 *
	 * @return
	 */
	public String getId() {
		return sessionId;
	}

	public String id() {
		return sessionId;
	}


	public void setServerSpec(String ip, String port) {
		serverIP = translateIPAddr(ip);
		serverPort = port;

	}

	String translateIPAddr(String ip) {
		if ("0:0:0:0:0:0:0:1%0".equals(ip)) {
			// value returned when in GWT hosted mode
			return "127.0.0.1";
		}
		return ip;

	}

	public void setIpAddr(String ip) {
		ipAddr = translateIPAddr(ip);
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setLastUpload(String filename, byte[] last, String filename2, byte[] last2) {
		lastUploadFilename = filename;
		lastUpload = last;

		lastUploadFilename2 = filename2;
		lastUpload2 = last2;
	}

	public void setLastUpload(String filename, byte[] last, String pass1, String filename2, byte[] last2, String pass2) {
		lastUploadFilename = filename;
		lastUpload = last;

		lastUploadFilename2 = filename2;
		lastUpload2 = last2;

		password1 = pass1;
		password2 = pass2;
		logger.info("lastUpload size=" + ((lastUpload == null) ? "null" : Integer.toString(lastUpload.length)));
		logger.info("lastUpload2 size=" + ((lastUpload2 == null) ? "null" : Integer.toString(lastUpload2.length)));
	}

	public byte[] getlastUpload() {
		return lastUpload;
	}

//	public String getPassword1() {
//		return password1;
//	}
//
//	public String getPassword2() {
//		return password2;
//	}
//
//	public byte[] getInput2() {
//		return input2;
//	}
//
//	public byte[] getlastUpload2() {
//		return lastUpload2;
//	}
//
//	public String getlastUploadFilename() {
//		return lastUploadFilename;
//	}
//
//	public String getInput2Filename() {
//		return input2Filename;
//	}
//
//	public String getlastUploadFilename2() {
//		return lastUploadFilename2;
//	}
//
//	public File getTestkitFile() {
//		return new File(Installation.installation().warHome() + File.separator + "toolkitx" + File.separator
//				+ "testkit");
//	}

	/**
	 * Reset linkage to XdsTest
	 */
//	public void clear() {
//		xt = null;
//		res = null;
		// transactionSettings = null;
//	}

//	boolean lessThan(String a, String b) {
//		return a.compareTo(b) == -1;
//	}

	// public void saveLogMapInSessionCache(LogMap log, XdstestLogId id) throws
	// XdsException {
	// new RawLogCache(getTomcatSessionCache()).logOut(id, log);
	//
	// if (mesaSessionExists()) {
	// new RawLogCache(mesaSessionCache).logOut(id, log);
	// }
	// }

	/*
	 * Manage the environment, choice of keystore and codes.xml
	 */

//	public File getEnvironmentDir() throws EnvironmentNotSelectedException {
//		try {
//			return EnvSetting.getEnvSetting(sessionId).getEnvDir();
//		} catch (Exception e) {
//			throw new EnvironmentNotSelectedException("", e);
//		}
//	}

//	public File getEnvironment() throws EnvironmentNotSelectedException {
//		return getEnvironmentDir();
//	}

//	public File getCodesFile() throws EnvironmentNotSelectedException {
//		if (getEnvironmentDir() == null)
//			return null; // new File(Installation.installation().warHome() +
//							// File.separator + "toolkitx" + File.separator +
//							// "codes" + File.separator + "codes.xml");
//		File f = new File(getEnvironmentDir() + File.separator + "codes.xml");
//		if (f.exists())
//			return f;
//		return null;
//	}

//	public File getKeystoreDir() throws EnvironmentNotSelectedException {
//		File f = new File(getEnvironmentDir() + File.separator + "keystore");
//		if (f.exists() && f.isDirectory())
//			return f;
//		throw new EnvironmentNotSelectedException("");
//	}
//
//	public File getKeystore() throws EnvironmentNotSelectedException {
//		File kd = getKeystoreDir();
//		return new File(kd + File.separator + "keystore");
//	}
//
//	/*
//	 * DSIG_keystore_password=changeit
//	 * DSIG_keystore_alias=1
//	 * keystore_url=keystore
//	 */
//
//	public String getKeystorePassword() throws IOException, EnvironmentNotSelectedException {
//		Properties p = new Properties();
//		File f = new File(getKeystoreDir() + File.separator + "keystore.properties");
//		if (!f.exists())
//			return "";
//		FileInputStream fis = new FileInputStream(f);
//		p.load(fis);
//		return p.getProperty("keyStorePassword");
//	}
//
//	public String getKeystoreAlias() throws IOException, EnvironmentNotSelectedException {
//		Properties p = new Properties();
//		File f = new File(getKeystoreDir() + File.separator + "keystore.properties");
//		if (!f.exists())
//			return "";
//		FileInputStream fis = new FileInputStream(f);
//		p.load(fis);
//		return p.getProperty("DSIG_keystore_alias");
//	}
//


//	public List<String> getEnvironmentNames() {
//		logger.debug(getId() + ": " + "getEnvironmentNames");
//		List<String> names = new ArrayList<String>();
//
//		File k = Installation.installation().environmentFile(); // propertyServiceManager().getPropertyManager().getExternalCache()
//																// +
//																// File.separator
//																// +
//																// "environment");
//		if (!k.exists() || !k.isDirectory())
//			return names;
//		File[] files = k.listFiles();
//		for (File file : files)
//			if (file.isDirectory())
//				names.add(file.getName());
//
//		return names;
//	}

//	public TkProps tkProps() {
//		try {
//			return TkLoader.tkProps(Installation.installation().getTkPropsFile());
//		} catch (Throwable t) {
//			return new TkProps();
//		}
//	}

	/**
	 * Sets name of current environment (for this session)
	 *
	 * @throws
	 */
//	public void setEnvironment(String name) {
//		if (name == null || name.equals(""))
//			return;
//		logger.debug(getId() + ": " + " Environment set to " + name);
//		setEnvironment(name, Installation.installation().propertyServiceManager().getPropertyManager()
//				.getExternalCache());
//	}

//	public void setEnvironment(String name, String externalCache) {
//		File k = Installation.installation().environmentFile(name);
//		if (!k.exists() || !k.isDirectory())
//			k = null;
//		currentEnvironmentName = name;
//		System.setProperty("XDSCodesFile", k.toString() + File.separator + "codes.xml");
//		new EnvSetting(sessionId, name, k);
//		logger.debug(getId() + ": " + "Environment set to " + k);
//	}

//	public String getCurrentEnvironment() {
//		return currentEnvironmentName;
//	}

//	public SessionPropertyManager getSessionProperties() {
//		if (mesaSessionName == null)
//			return null;
//
//		if (sessionProperties == null) {
//			File testLogCache;
//			try {
//				testLogCache = Installation.installation().propertyServiceManager().getTestLogCache();
//			} catch (Exception e) {
//				return null;
//			}
//
//			sessionProperties = new SessionPropertyManager(testLogCache.toString());
//		}
//
//		return sessionProperties;
//	}

//	public Map<String, String> getSessionPropertiesAsMap() {
//		logger.debug(": " + "getSessionProperties()");
//		return getSessionProperties().asMap();
//	}

//	public File getToolkitFile() {
//		if (toolkit == null)
//			toolkit = Installation.installation().toolkitxFile(); // new
//																	// File(Installation.installation().warHome()
//																	// +
//																	// File.separator
//																	// +
//																	// "toolkitx");
//		return toolkit;
//	}

}