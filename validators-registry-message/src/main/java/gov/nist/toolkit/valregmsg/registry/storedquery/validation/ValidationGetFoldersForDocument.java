package gov.nist.toolkit.valregmsg.registry.storedquery.validation;

import gov.nist.hit.ds.xdsException.MetadataException;
import gov.nist.hit.ds.xdsException.XdsException;
import gov.nist.toolkit.registrymetadata.Metadata;
import gov.nist.toolkit.registrysupport.logging.LoggerException;
import gov.nist.toolkit.valregmsg.registry.storedquery.generic.GetFoldersForDocument;
import gov.nist.toolkit.valregmsg.registry.storedquery.support.StoredQuerySupport;

public class ValidationGetFoldersForDocument extends GetFoldersForDocument {

	public ValidationGetFoldersForDocument(StoredQuerySupport sqs) {
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
