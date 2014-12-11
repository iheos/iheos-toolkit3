package gov.nist.toolkit.xdstools3.server.RPCServices;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.hit.ds.utilities.xml.SchemaValidation;
import gov.nist.toolkit.actorfactory.SiteServiceManager;
import gov.nist.toolkit.installation.Installation;
import gov.nist.toolkit.session.server.Session;

import gov.nist.toolkit.xdstools3.client.tabs.mhdRelatedTabs.MHDTabsServices;

import gov.nist.toolkit.xdstools3.server.Caller;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * MHD RPC Services Implementation inspired from v2 ToolkitServicesImpl.
 * TODO Some elements will need to be reviewed when integrating the rest of v2 transactions
 */
@SuppressWarnings("serial")
public class MHDServicesImpl extends RemoteServiceServlet implements MHDTabsServices {
    static Logger logger = Logger.getLogger(PreConnectathonTabServiceImpl.class);

    // Used only for non-servlet use (Dashboard is good example)
    static public final String sessionVarName = "MySession";
    private String sessionID = null;
    private Session standAloneSession = null;  // needed for standalone use not part of servlet

    private ServletContext context = null;

    public SiteServiceManager siteServiceManager;

    /**
     * Default constructor
     */
    public MHDServicesImpl() {
        siteServiceManager = SiteServiceManager.getSiteServiceManager();   // One copy shared between sessions
    }

    /**
     * MHD Message Validation method
     *
     * @param messageType Type of MHD message
     *
     * @return validation result
     */
    public String validateMHDMessage(String messageType){
        return Caller.getInstance().validateMHDMessage(messageType,new String(getSession().getlastUpload()).trim());
    }

    /**
     * Retrieves uploaded MHD file from the servlet and calls the conversion to XDS file.
     * The uploaded file must be an XML file.
     * @return
     */
    @Override
    public String convertMHDToXDS() {
        String rootDirPath = getServletContext().getContextPath();
        String uploadedFileContents = new String(getSession().getlastUpload()).trim();
        return Caller.getInstance().convertMHDtoXDS(rootDirPath, uploadedFileContents);
    }

    /**
     * Method that returns the SessionId (copied from v2)
     * @return Session Id
     */
    public String getSessionId() {
        if (sessionID != null)
            return sessionID;
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession hsession = request.getSession();
        return hsession.getId();
    }

    /**
     * Method that returns the session using rpc servlet
     * @return session
     */
    public Session getSession() {
        HttpServletRequest request = this.getThreadLocalRequest();
        return getSession(request);
    }

    /**
     * Method that returns the session using servlet request (copied from v2)
     * @param request
     * @return
     */
    public Session getSession(HttpServletRequest request) {
        if (request == null && standAloneSession != null) {
            // not running interactively - maybe part of Dashboard
            return standAloneSession;
        }

        Session s = null;
        HttpSession hsession = null;
        if (request != null) {
            hsession = request.getSession();
            s = (Session) hsession.getAttribute(sessionVarName);
            if (s != null)
                return s;
            servletContext();
        }

        // Force short session timeout for testing
//		hsession.setMaxInactiveInterval(60/4);    // one quarter minute

        //******************************************
        //
        // New session object to be created
        //
        //******************************************
        File warHome = null;
        if (s == null) {
            ServletContext sc = servletContext();
            warHome = Installation.installation().warHome();
            if (sc != null && warHome == null) {
                warHome = new File(sc.getRealPath("/"));
                Installation.installation().warHome(warHome);
                System.setProperty("warHome", warHome.toString());
                System.out.print("warHome [ToolkitServiceImp]: " + warHome);
                Installation.installation().warHome(warHome);
            }
            if (warHome != null)
                System.setProperty("warHome", warHome.toString());

            if (warHome != null) {
                s = new Session(warHome, siteServiceManager, getSessionId());
                if (hsession != null) {
                    s.setSessionId(hsession.getId());
                    s.addSession();
                    hsession.setAttribute(sessionVarName, s);
                } else
                    s.setSessionId("mysession");
            }
        }

        if (request != null) {
            if (s.getIpAddr() == null) {
                s.setIpAddr(request.getRemoteHost());
            }

            s.setServerSpec(request.getLocalName(),
                    String.valueOf(request.getLocalPort()));
        }

        if (warHome != null) {
            if (SchemaValidation.toolkitSchemaLocation == null) {
                SchemaValidation.toolkitSchemaLocation = warHome + File.separator + "toolkitx" + File.separator + "schema";
            }
        }

        return s;
    }



    /**
     * Method that sets the servlet context, copied from v2
     * @return
     */
    public ServletContext servletContext() {
        // this gets called from the initialization section of SimServlet
        // for access to properties.  This code is not expected to work correct.
        // Just don't throw exceptions that are not helpful
        try {
            if (context == null)
                context = getServletContext();
        } catch (Exception e) {

        }
        if (context != null && Installation.installation().warHome() == null) {

            File warHome = new File(context.getRealPath("/"));
            System.setProperty("warHome", warHome.toString());
            logger.info("warHome [ToolkitServiceImpl]: " + warHome);
            Installation.installation().warHome(warHome);
        }
        return context;
    }
}
