package gov.nist.hit.ds.simSupport.config;

import com.google.gwt.user.client.rpc.IsSerializable

public class TimeSimConfigElement extends AbstractSimConfigElement implements IsSerializable, Serializable {
    String date;

	private static final long serialVersionUID = 2116238641744686908L;

	public TimeSimConfigElement(String name, String date) {
		setName(name);
		this.date = date;
	}
	
	public TimeSimConfigElement() { }
}
