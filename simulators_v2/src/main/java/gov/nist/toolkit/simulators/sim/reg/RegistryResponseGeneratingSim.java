package gov.nist.toolkit.simulators.sim.reg;

import gov.nist.toolkit.registrymsgformats.registry.Response;


/**
 * Classes that implement this interface initialize the response
 * variable in the run method
 * @author bill
 *
 */
 public interface RegistryResponseGeneratingSim  {

	public Response getResponse() ;

}
