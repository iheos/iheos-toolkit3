package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import gov.nist.toolkit.xdstools3.client.RESTUtils.DSRequest;

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
public class EndpointConfigDSRequest extends DSRequest {

	// Holds all incoming data
	@XmlElementWrapper(name="data")
	@XmlElement(name="EndpointConfigDS") // Must be the class name of the datasource
	List<EndpointConfig> data;
	
	public Collection<EndpointConfig> getMessages() {
		return data;
	}
	
	public void addMessage(EndpointConfig endpoint) {
		if (data == null) {
			data = new ArrayList<EndpointConfig>();
		}
		data.add(endpoint);
	}
}
