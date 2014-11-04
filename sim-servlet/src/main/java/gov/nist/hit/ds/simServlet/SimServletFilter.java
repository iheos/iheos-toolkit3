package gov.nist.hit.ds.simServlet;

import gov.nist.hit.ds.xdsException.ExceptionUtil;
import org.apache.log4j.Logger;
import org.mentawai.util.CharResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SimServletFilter implements Filter {
	static Logger logger = Logger.getLogger(SimServletFilter.class);

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

        try {
            PrintWriter out = response.getWriter();

            CharResponseWrapper wrappedResponse = new CharResponseWrapper((HttpServletResponse) response);

            chain.doFilter(request, wrappedResponse);

            String responseString = wrappedResponse.toString();
            System.out.println("Raw response is\n" + responseString);

            String contentType = (String) request.getAttribute("contentType");
            response.setContentType(contentType);
            System.out.println("Content type set to " + contentType);
            out.write(responseString);
        } catch (Throwable t) {
            System.out.println(ExceptionUtil.exception_details(t));
        }
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
