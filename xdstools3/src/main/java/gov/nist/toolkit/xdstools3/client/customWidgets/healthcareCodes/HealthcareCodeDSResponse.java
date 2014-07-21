package gov.nist.toolkit.xdstools3.client.customWidgets.healthcareCodes;

import gov.nist.toolkit.xdstools3.client.RESTUtils.DSResponse;

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
public class HealthcareCodeDSResponse extends DSResponse {

	// Holds all incoming data
	@XmlElementWrapper(name="data")
	@XmlElement(name="record")
	List<HealthcareCode> data;
	
	public Collection<HealthcareCode> getMessages() {
		return data;
	}
	
	public void addMessage(HealthcareCode healthcode) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.data.add(healthcode);
    }

}
