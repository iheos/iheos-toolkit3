package gov.nist.hit.ds.dsSims.eb.metadataValidator;

public interface RegistryValidationInterface {

	public boolean isDocumentEntry(String uuid);
	public boolean isFolder(String uuid);
	public boolean isSubmissionSet(String uuid);
	
}
