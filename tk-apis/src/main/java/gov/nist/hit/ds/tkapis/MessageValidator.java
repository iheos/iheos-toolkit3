package gov.nist.hit.ds.tkapis;

import java.util.List;

/**
 * Created by bmajur on 8/26/14.
 */
public interface MessageValidator {
    /**
     * Get validator names
     * @return list displayable names
     */
    List<String> getValidatorNames();

    public enum Status { OK, WARNING, ERROR};

    public class ValidateResponse {
        String repositoryId;
        String requestEventAssetId;
        String responseEventAssetId;
        Status status;
    }

    /**
     * Perform a validation
     * @param repositoryId
     * @param bundleAssetId
     * @return results in the form of repository references
     */
    ValidateResponse validate(String repositoryId, String bundleAssetId);
}
