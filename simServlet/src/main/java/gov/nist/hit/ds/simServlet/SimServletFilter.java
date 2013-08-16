package gov.nist.hit.ds.simServlet;



import gov.nist.hit.ds.http.environment.Event;
import gov.nist.hit.ds.http.parser.HttpMessage;
import gov.nist.hit.ds.http.parser.HttpParseException;
import gov.nist.hit.ds.utilities.io.Io;
import gov.nist.hit.ds.xdsException.ExceptionUtil;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class SimServletFilter implements Filter {
	static Logger logger = Logger.getLogger(SimServletFilter.class);

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		SimServletResponseWrapper wrapper = new SimServletResponseWrapper((HttpServletResponse) response);
		chain.doFilter(request,wrapper);
		
		logger.debug("in doFilter");
		
		Event event = (Event) request.getAttribute("Event");
		if (event == null) {
			logger.error("SimServletFilter - request.getAttribute(\"Event\") failed");
			return;
		}
		
		Map<String, String> hdrs = wrapper.headers;
		String contentType = wrapper.contentType;
		
		HttpMessage hmsg = new HttpMessage();
		hmsg.setHeaderMap(hdrs);
		String messageHeader;
		try {
			messageHeader = hmsg.asMessage();
		} catch (HttpParseException e) {
			Io.stringToFile(event.getResponseHeaderFile(), ExceptionUtil.exception_details(e));
			return;
		}
		
		if (contentType != null) {
			if (messageHeader == null || messageHeader.equals(""))
				messageHeader = "Content-Type: " + contentType + "\r\n\r\n";
			else 
				messageHeader = messageHeader + "Content-Type: " + contentType + "\r\n\r\n";
		}
		
		Io.stringToFile(event.getResponseHeaderFile(), messageHeader);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
