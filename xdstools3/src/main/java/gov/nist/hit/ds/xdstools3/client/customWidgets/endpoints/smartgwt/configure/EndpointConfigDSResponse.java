package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;

import gov.nist.hit.ds.xdstools3.client.RESTUtils.DSResponse;

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
@XmlRootElement(name="response")
public class EndpointConfigDSResponse extends DSResponse {
	// Holds all incoming data
	@XmlElementWrapper(name="data")
	@XmlElement(name="record")
	List<EndpointConfig> data;
	
	public Collection<EndpointConfig> getMessages() {
		return data;
	}
	
	public void addMessage(EndpointConfig endpoint) {
        if (data == null) {
            data = new ArrayList<EndpointConfig>();
        }
        this.data.add(endpoint);
    }

}
