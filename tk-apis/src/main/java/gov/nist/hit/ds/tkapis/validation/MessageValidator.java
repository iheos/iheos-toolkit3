package gov.nist.hit.ds.tkapis.validation;

import gov.nist.hit.ds.tkapis.AssetId;

import java.util.List;

/**
 * Created by bmajur on 8/28/14.
 */
public interface MessageValidator {

    /**
     * Get names for validators that work on transactions. Most transactions
     * include a request message and a response message.
     * @return list displayable names
     */
    List<String> getTransactionValidatorNames();

    /**
     * Get names for validators that work on messages
     * @return list displayable names
     */
    List<String> getMessageValidatorNames();

    /**
     * Validate a message given its header and body
     * @param validatorName
     * @param msgHeader
     * @param messageBody
     * @return
     */
    ValidateMessageResponse validateMessage(String validatorName, String msgHeader, byte[] messageBody);

    /**
     * Validate a message already stored in an Event.
     * This is a bit troubling since from the data types it is not much different than the above call. Maybe
     * an AssetId datatype should be exposed in this interface.
     * @param validatorName
     * @param repositoryId
     * @param eventId
     * @return
     */
    ValidateMessageResponse validateMessage(String validatorName, AssetId repositoryId, AssetId eventId);

    /**
     * Perform a validation
     * @param validatorName
     * @param repositoryId - holding the transaction to be validated
     * @param transactionAssetId - the transaction to be validated. The format of the asset
     *                      tree needs to be documented!!!
     * @return results in the form of repository references
     */
    ValidateTransactionResponse validateTransaction(String validatorName, AssetId repositoryId, AssetId transactionAssetId);

}
