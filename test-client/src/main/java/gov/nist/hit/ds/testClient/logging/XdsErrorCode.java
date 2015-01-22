package gov.nist.hit.ds.testClient.logging;

public class XdsErrorCode   {

	static public enum Code  {
		NoCode,
		SoapFault,
		XDSMissingDocument,
		XDSMissingDocumentMetadata,
		XDSRegistryNotAvailable,
		XDSRegistryError,
		XDSRepositoryError,
		XDSRegistryDuplicateUniqueIdInMessage,
		XDSRepositoryDuplicateUniqueIdInMessage,
		XDSDuplicateUniqueIdInRegistry,
		XDSNonIdenticalHash,
		XDSRegistryBusy,
		XDSRepositoryBusy,
		XDSRegistryOutOfResources,
		XDSRepositoryOutOfResources,
		XDSRegistryMetadataError,
		XDSRepositoryMetadataError,
		XDSTooManyResults,
		XDSExtraMetadataNotSaved,
		XDSUnknownPatientId,
		XDSPatientIdDoesNotMatch,
		XDSUnknownStoredQuery,
		XDSStoredQueryMissingParam,
		XDSStoredQueryParamNumber,
		XDSRegistryDeprecatedDocumentError,
		XDSUnknownRepositoryId,
		XDSDocumentUniqueIdError,
		XDSMetadataVersionError,
		XDSMetadataUpdateOperationError,
		XDSMetadataUpdateError,
		XDSMissingHomeCommunityId
	};
	
	
	
}
