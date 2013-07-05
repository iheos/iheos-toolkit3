package gov.nist.hit.ds.simSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface InitialHttpSim {
	String getState();  //used only to unit test sim loader
	void run();   // start the simulator
	void setHttpRequest(HttpServletRequest request);
	void setHttpResponse(HttpServletResponse response);
}
