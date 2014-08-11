	package gov.nist.toolkit.xdstools3.server;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.FactoryConfigurationError;

import gov.nist.toolkit.xdstools3.client.tabs.preConnectathonTestsTab.ToolkitService;
import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ToolkitServiceImpl extends RemoteServiceServlet implements ToolkitService {

	static Logger logger = Logger.getLogger(ToolkitServiceImpl.class);


	public ToolkitServiceImpl() {
	}

	public Map<String, String> getCollectionNames(String collectionSetName) throws Exception {
//        return session().xdsTestServiceManager().getCollectionNames(collectionSetName);
        return null;
    }
//	public Map<String, String> getCollection(String collectionSetName, String collectionName) throws Exception { return session().xdsTestServiceManager().getCollection(collectionSetName, collectionName); }

	// This exception is passable to the GUI.  The server side exception
	// is NoSessionException
//	public Session session() throws NoServletSessionException {
//		Session s = getSession();
//		if (s == null)
//			throw new NoServletSessionException("");
//		return s;
//	}


//	public Session getSession() {
//		HttpServletRequest request = this.getThreadLocalRequest();
//		return getSession(request);
//	}
//
//	public Session getSession(HttpServletRequest request) {
//		if (request == null && standAloneSession != null) {
//			// not running interactively - maybe part of Dashboard
//			return standAloneSession;
//		}
//
//		Session s = null;
//		HttpSession hsession = null;
//		if (request != null) {
//			hsession = request.getSession();
//			s = (Session) hsession.getAttribute(sessionVarName);
//			if (s != null)
//				return s;
//			servletContext();
//		}
//
//		// Force short session timeout for testing
//		//hsession.setMaxInactiveInterval(60/4);    // one quarter minute
//
//        // more realistic session timeout - 1 hour
//        hsession.setMaxInactiveInterval(1*60*60);
//
//		//******************************************
//		//
//		// New session object to be created
//		//
//		//******************************************
//		File warHome = null;
//		if (s == null) {
//			ServletContext sc = servletContext();
//			warHome = Installation.installation().warHome();
//			if (sc != null && warHome == null) {
//				warHome = new File(sc.getRealPath("/"));
//				Installation.installation().warHome(warHome);
//				System.setProperty("warHome", warHome.toString());
//				System.out.print("warHome [ToolkitServiceImp]: " + warHome);
//				Installation.installation().warHome(warHome);
//			}
//			if (warHome != null)
//				System.setProperty("warHome", warHome.toString());
//
//			if (warHome != null) {
//				s = new Session(warHome, siteServiceManager, getSessionId());
//				if (hsession != null) {
//					s.setSessionId(hsession.getId());
//					s.addSession();
//					hsession.setAttribute(sessionVarName, s);
//				} else
//					s.setSessionId("mysession");
//			}
//		}
//
//		if (request != null) {
//			if (s.getIpAddr() == null) {
//				s.setIpAddr(request.getRemoteHost());
//			}
//
//			s.setServerSpec(request.getLocalName(),
//					String.valueOf(request.getLocalPort()));
//		}
//
//		if (warHome != null) {
//			if (SchemaValidation.toolkitSchemaLocation == null) {
//				SchemaValidation.toolkitSchemaLocation = warHome + File.separator + "toolkitx" + File.separator + "schema";
//			}
//		}
//
//		return s;
//	}

}
