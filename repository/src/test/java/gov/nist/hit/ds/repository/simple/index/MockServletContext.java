package gov.nist.hit.ds.repository.simple.index;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;

@SuppressWarnings("NonJREEmulationClassesInClientCode")
public class MockServletContext {
	private static String basePath = "";
	
	
	static public ServletContext getServletContext(String reposPath) {
		basePath = reposPath;
		
		return new ServletContext() {

			
			@Override
			public String getRealPath(String path) {
				return basePath + path;
			}

			@Override
			public ServletContext getContext(String uripath) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getContextPath() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getMajorVersion() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getMinorVersion() {
				// TODO Auto-generated method stub
				return 0;
			}

            @Override
            public int getEffectiveMajorVersion() {
                return 0;
            }

            @Override
            public int getEffectiveMinorVersion() {
                return 0;
            }

            @Override
			public String getMimeType(String file) {
				// TODO Auto-generated method stub
				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Set getResourcePaths(String path) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URL getResource(String path) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public InputStream getResourceAsStream(String path) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public RequestDispatcher getRequestDispatcher(String path) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public RequestDispatcher getNamedDispatcher(String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Servlet getServlet(String name) throws ServletException {
				// TODO Auto-generated method stub
				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Enumeration getServlets() {
				// TODO Auto-generated method stub
				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Enumeration getServletNames() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void log(String msg) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void log(Exception exception, String msg) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void log(String message, Throwable throwable) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getServerInfo() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getInitParameter(String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Enumeration getInitParameterNames() {
				// TODO Auto-generated method stub
				return null;
			}

            @Override
            public boolean setInitParameter(String s, String s2) {
                return false;
            }

            @Override
			public Object getAttribute(String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Enumeration getAttributeNames() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setAttribute(String name, Object object) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void removeAttribute(String name) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String getServletContextName() {
				// TODO Auto-generated method stub
				return null;
			}

            @Override
            public ServletRegistration.Dynamic addServlet(String s, String s2) {
                return null;
            }

            @Override
            public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
                return null;
            }

            @Override
            public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
                return null;
            }

            @Override
            public <T extends Servlet> T createServlet(Class<T> tClass) throws ServletException {
                return null;
            }

            @Override
            public ServletRegistration getServletRegistration(String s) {
                return null;
            }

            @Override
            public Map<String, ? extends ServletRegistration> getServletRegistrations() {
                return null;
            }

            @Override
            public FilterRegistration.Dynamic addFilter(String s, String s2) {
                return null;
            }

            @Override
            public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
                return null;
            }

            @Override
            public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
                return null;
            }

            @Override
            public <T extends Filter> T createFilter(Class<T> tClass) throws ServletException {
                return null;
            }

            @Override
            public FilterRegistration getFilterRegistration(String s) {
                return null;
            }

            @Override
            public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
                return null;
            }

            @Override
            public SessionCookieConfig getSessionCookieConfig() {
                return null;
            }

            @Override
            public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {

            }

            @Override
            public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
                return null;
            }

            @Override
            public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
                return null;
            }

            @Override
            public void addListener(String s) {

            }

            @Override
            public <T extends EventListener> void addListener(T t) {

            }

            @Override
            public void addListener(Class<? extends EventListener> aClass) {

            }

            @Override
            public <T extends EventListener> T createListener(Class<T> tClass) throws ServletException {
                return null;
            }

            @Override
            public JspConfigDescriptor getJspConfigDescriptor() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public void declareRoles(String... strings) {

            }


        };

	}
}
