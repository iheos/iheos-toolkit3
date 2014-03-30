package gov.nist.toolkit.messagevalidatorfactory;

import gov.nist.hit.ds.errorRecording.factories.ErrorRecorderBuilder;
import gov.nist.toolkit.valsupport.client.ValidationContext;
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine;
import gov.nist.toolkit.valsupport.registry.RegistryValidationInterface;

public interface MessageValidatorFactory2I {
	public MessageValidatorEngine getValidator(ErrorRecorderBuilder erBuilder, byte[] input, byte[] directCertInput, ValidationContext vc, RegistryValidationInterface rvi);

}
