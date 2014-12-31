package gov.nist.hit.ds.dsSims.eb.metadataValidator.validator;

public interface RegistryValidationInterface {

	public boolean isDocumentEntry(String uuid);
	public boolean isFolder(String uuid);
	public boolean isSubmissionSet(String uuid);

    // TODO: needs expansion for isDeprecated, isDocumentEntry(uniqueId0, isFolder(uniqueId), isSubmissionSet(uniqueId)
	
}
