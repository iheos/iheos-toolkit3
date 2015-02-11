package gov.nist.hit.ds.xdstools3.client.customWidgets.healthcareCodes;

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
public class HealthcareCodeDSRequest extends DSRequest {

	// Holds all incoming data
	@XmlElementWrapper(name="data")
	@XmlElement(name="HealthcareCodeDS") // Must be the class name of the datasource
	List<HealthcareCode> data;
	
	public Collection<HealthcareCode> getMessages() {
		return data;
	}
	
	public void addMessage(HealthcareCode endpoint) {
		if (data == null) {
			data = new ArrayList<HealthcareCode>();
		}
		data.add(endpoint);
	}
}
