package gov.nist.toolkit.xdstools3.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class UploadFileServlet extends HttpServlet {
    private final Logger logger = Logger.getLogger(UploadFileServlet.class
            .getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO (see solution from http://www.mrsondao.com/TopicDetail.aspx?TopicId=2)
    }
}
