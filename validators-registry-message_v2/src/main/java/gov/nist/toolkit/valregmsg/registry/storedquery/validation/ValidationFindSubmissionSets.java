package gov.nist.toolkit.valregmsg.registry.storedquery.validation;

import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrysupport.logging.LoggerException;
import gov.nist.toolkit.valregmsg.registry.storedquery.generic.FindSubmissionSets;
import gov.nist.toolkit.valregmsg.registry.storedquery.support.StoredQuerySupport;
import gov.nist.hit.ds.xdsExceptions.MetadataException;
import gov.nist.hit.ds.xdsExceptions.MetadataValidationException;
import gov.nist.hit.ds.xdsExceptions.XdsException;

public class ValidationFindSubmissionSets extends FindSubmissionSets {

	public ValidationFindSubmissionSets(StoredQuerySupport sqs)
			throws MetadataValidationException {
		super(sqs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Metadata runImplementation() throws MetadataException,
			XdsException, LoggerException {
		// TODO Auto-generated method stub
		return null;
	}

}
