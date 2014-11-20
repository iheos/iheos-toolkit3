package gov.nist.hit.ds.tkapis.validation;

import gov.nist.hit.ds.simSupport.transaction.ValidationStatus;

import java.util.List;

/**
 * Created by bmajur on 8/26/14.
 *
 * This validates a bundle as captured by the proxy.
 */
public interface MessageBundleValidator {
    /**
     * Get validator names
     * @return list displayable names
     */
    List<String> getValidatorNames();

    public class ValidateBundleResponse {
        /**
         * Repository holding the following Events
         */
        String repositoryId;
        /**
         * AssetId of the Event created to hold the validation of the
         * request message
         */
        String requestEventAssetId;
        /**
         * AssetId of the Event created to hold the validation of the
         * response message
         */
        String responseEventAssetId;
        /**
         * Validation summary for the request and response messages.
         */
        ValidationStatus requestMessageStatus;
        ValidationStatus responseMessageStatus;
    }

    /**
     * Perform a validation
     * @param validatorName
     * @param repositoryId - holding the bundle to be validated
     * @param bundleAssetId - the bundle to be validated. The format of the asset
     *                      tree needs to be documented!!!
     * @return results in the form of repository references
     */
    ValidateBundleResponse validateBundle(String validatorName, String repositoryId, String bundleAssetId);
}
