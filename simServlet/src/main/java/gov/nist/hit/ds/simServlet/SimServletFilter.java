package gov.nist.hit.ds.simServlet;



import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

public class SimServletFilter implements Filter {
	static Logger logger = Logger.getLogger(SimServletFilter.class);

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

//		PrintWriter out = response.getWriter();
//		CharResponseWrapper responseWrapper = new CharResponseWrapper(
//				(HttpServletResponse) response);
		

		chain.doFilter(request,response);

//		String responseString = responseWrapper.toString();
//		
//	    out.write(responseString); 
//		
//		Event event = (Event) request.getAttribute("Event");
//		if (event == null) {
//			logger.error("SimServletFilter - request.getAttribute(\"Event\") failed");
//			return;
//		}
//
//		try {
//			event.getInOutMessages().putResponse(responseString);
//		} catch (RepositoryException e) {
//			logger.error("SimServletFilter: " + e.getMessage());
//			throw new ServletException("", e);
//		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
