package gov.nist.toolkit.testengine.transactions;

public class GenericSoap11Transaction extends StoredQueryTransaction {

	public GenericSoap11Transaction() {
		super();
		setXdsVersion(BasicTransaction.xds_a);  // Simple Soap Header
		setParseMetadata(false);
	}

}
