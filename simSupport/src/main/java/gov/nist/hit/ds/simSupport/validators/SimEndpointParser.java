package gov.nist.hit.ds.simSupport.validators;

import gov.nist.hit.ds.simSupport.datatypes.SimEndPoint;

public class SimEndpointParser {

	public SimEndPoint parse(String endpoint) throws Exception {
		String simid;
		String actor;
		String transaction;
		int simIndex;
		String[] uriParts = endpoint.split("\\/");

		for (simIndex=0; simIndex<uriParts.length; simIndex++) {
			if ("sim".equals(uriParts[simIndex]))
				break;
		}
		if (simIndex >= uriParts.length) {
			throw new Exception("Endpoint <" + endpoint + "> does not parse as a simulator endpoint");
		}
		simid = uriParts[simIndex + 1];
		actor = uriParts[simIndex + 2];
		transaction = uriParts[simIndex + 3];
		return  new SimEndPoint().
				setSimId(simid).
				setActor(actor).
				setTransaction(transaction);

	}
}
