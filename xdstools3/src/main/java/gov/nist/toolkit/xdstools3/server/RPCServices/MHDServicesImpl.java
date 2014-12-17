package gov.nist.toolkit.xdstools3.server.RPCServices;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import gov.nist.toolkit.actorfactory.SiteServiceManager;
import gov.nist.toolkit.session.server.Session;
import gov.nist.toolkit.xdstools3.client.exceptions.ToolkitServerError;
import gov.nist.toolkit.xdstools3.client.tabs.mhdTabs.MHDTabsServices;
import gov.nist.toolkit.xdstools3.server.Caller;
import org.apache.log4j.Logger;

/**
 * MHD RPC Services Implementation inspired from v2 ToolkitServicesImpl.
 * TODO Some elements will need to be reviewed when integrating the rest of v2 transactions
 */
@SuppressWarnings("serial")
public class MHDServicesImpl extends RemoteServiceServlet implements MHDTabsServices {
    static Logger logger = Logger.getLogger(PreConnectathonTabServiceImpl.class);

    // Used only for non-servlet use (Dashboard is good example)
    //    private String sessionID = null;
    private Session standAloneSession = null;  // needed for standalone use not part of servlet

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
    public String validateMHDMessage(String messageType) throws ToolkitServerError {
        return Caller.getInstance().validateMHDMessage(messageType, new String(getSession().getlastUpload()).trim());
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
     * Method that returns the session using rpc servlet
     * @return session
     */
    public Session getSession() { return Session.getSession(this.getThreadLocalRequest()); }


}
