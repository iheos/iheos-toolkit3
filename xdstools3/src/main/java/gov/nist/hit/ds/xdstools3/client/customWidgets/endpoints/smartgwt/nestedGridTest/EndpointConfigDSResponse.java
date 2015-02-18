package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.nestedGridTest;

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
	List<gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure.EndpointConfig> data;

	public Collection<gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure.EndpointConfig> getMessages() {
		return data;
	}

	public void addMessage(gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure.EndpointConfig endpoint) {
        if (data == null) {
            data = new ArrayList<gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure.EndpointConfig>();
        }
        this.data.add(endpoint);
    }

}
