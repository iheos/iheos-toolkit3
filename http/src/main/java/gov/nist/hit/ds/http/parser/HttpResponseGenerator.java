package gov.nist.hit.ds.http.parser;



/**
 * Generate HTTP response message.
 * TODO: who sets the content type?
 * TODO: needs multipart handling.
 * @author bill
 *
 */
public class HttpResponseGenerator {
	HttpEnvironment httpEnv;

	public HttpResponseGenerator(HttpEnvironment httpEnv) {
		this.httpEnv = httpEnv;
	}

	public void sendResponse(String body) throws Exception {
		try {
			httpEnv.getOutputStream().write(body.getBytes());
		} 
		finally {
			httpEnv.getOutputStream().close();  // only one response to a customer
		}
	}
}
