package gov.nist.hit.ds.tkapis.validation;

import gov.nist.hit.ds.tkapis.AssetId;

/**
* Created by bmajur on 8/28/14.
*/
public class ValidateMessageResponse {
    AssetId repositoryId;
    AssetId eventAssetId;
    ValidationStatus validationStatus;

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public AssetId getEventAssetId() {
        return eventAssetId;
    }

    public void setEventAssetId(AssetId eventAssetId) {
        this.eventAssetId = eventAssetId;
    }

    public AssetId getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(AssetId repositoryId) {
        this.repositoryId = repositoryId;
    }

}
