package gov.nist.toolkit.simulators.support;

import gov.nist.hit.ds.xdsException.ExceptionUtil;
import gov.nist.toolkit.errorrecording.ErrorRecorder;
import gov.nist.toolkit.errorrecording.client.XdsErrorCode.Code;
import gov.nist.toolkit.registrymsgformats.registry.RegistryResponse;
import gov.nist.toolkit.registrymsgformats.registry.Response;
import gov.nist.toolkit.simulators.sim.reg.RegistryResponseGeneratingSim;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;

public class UnknownSim extends TransactionSimulator implements RegistryResponseGeneratingSim  {
	Response response;
	Exception startUpException = null;

	public UnknownSim(SimCommon common) {
		super(common);

		// build response
		try {
			response = new RegistryResponse(Response.version_3);
		} catch (Exception e) {
			System.out.println(ExceptionUtil.exception_details(e));
			startUpException = e;
			return;
		}
	}

	public void run(ErrorRecorder er, MessageValidatorEngine mve) {
		if (startUpException != null)
			er.err(Code.XDSRegistryError, startUpException);
	}

	public Response getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

}
