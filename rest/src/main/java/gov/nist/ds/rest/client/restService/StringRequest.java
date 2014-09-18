package gov.nist.ds.rest.client.restService;

import gov.nist.ds.rest.client.util.RestRequest;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;


/**
 * Utility class for representing a {@link //RestDataSource} request as an object.
 * 
 * @see //http://www.smartclient.com/docs/7.0rc2/a/b/c/go.html#class..RestDataSource
 */
@XmlRootElement(name="request")
public class StringRequest extends RestRequest {

	// Holds all incoming data
	@XmlElementWrapper(name="data")
    ArrayList<String> data;
	
	public ArrayList<String> getMessages() {
		return data;
	}




}
