package gov.nist.toolkit.xdstools3.client.RESTClient.util;

import gov.nist.toolkit.xdstools3.client.RESTClient.Message;

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
public class MessageDSRequest extends DSRequest {
	// Holds all incoming data
	@XmlElementWrapper(name="data")
	@XmlElement(name="MessageDS") // Must be the id of the MessageDS
	List<Message> data;
	
	public Collection<Message> getMessages() {
		return data;
	}
	
	public void addMessage(Message message) {
		if (data == null) {
			data = new ArrayList<Message>();
		}
		data.add(message);
	}
}
