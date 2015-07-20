package gov.nist.hit.ds.soapSupport.core;

// Use EndpointValue
@Deprecated
public class Endpoint {
	String endpoint;

    public Endpoint(String endpoint) {
        this.endpoint = endpoint;
    }
	
	public Endpoint setEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}
	
	public String getEndpoint() {
		return endpoint;
	}

    public String toString() { return endpoint; }
}
