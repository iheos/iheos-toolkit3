package gov.nist.toolkit.simulators.sim.reg;

import gov.nist.toolkit.registrymsgformats.registry.AdhocQueryResponse;

public interface AdhocQueryResponseGeneratingSim extends RegistryResponseGeneratingSim {

	abstract public AdhocQueryResponse getAdhocQueryResponse();
	
}
