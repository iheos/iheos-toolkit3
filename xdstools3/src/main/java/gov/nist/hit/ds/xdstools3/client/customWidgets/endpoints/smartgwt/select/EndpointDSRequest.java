package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.select;

import gov.nist.hit.ds.xdstools3.client.RESTUtils.DSRequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Utility class for representing a {@link //RestDataSource} request as an object.
 * 
 * @see //http://www.smartclient.com/docs/7.0rc2/a/b/c/go.html#class..RestDataSource
 */
@XmlRootElement(name="request")
public class EndpointDSRequest extends DSRequest {

	// Holds all incoming data
	@XmlElementWrapper(name="data")
	@XmlElement(name="EndpointDS") // Must be the class name of the datasource
	List<Endpoint> data;
	
	public Collection<Endpoint> getMessages() {
		return data;
	}
	
	public void addMessage(Endpoint endpoint) {
		if (data == null) {
			data = new ArrayList<Endpoint>();
		}
		data.add(endpoint);
	}
}
