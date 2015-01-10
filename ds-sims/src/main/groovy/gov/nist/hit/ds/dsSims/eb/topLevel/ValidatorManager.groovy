package gov.nist.hit.ds.dsSims.eb.topLevel
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.httpSoap.components.parsers.SoapMessageParser
import gov.nist.hit.ds.repoSupport.RepoUtils
import gov.nist.hit.ds.repository.shared.id.AssetId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.tkapis.validation.MessageValidator
import gov.nist.hit.ds.tkapis.validation.ValidateMessageResponse
import gov.nist.hit.ds.tkapis.validation.ValidateTransactionResponse
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException
/**
 * Created by bmajur on 8/28/14.
 */
class ValidatorManager implements MessageValidator {
    SimHandle simHandle

    static final String soapAction = 'by SOAP:Action'

    @Override
    List<String> getTransactionValidatorNames() {
        return [soapAction]
    }

    @Override
    List<String> getMessageValidatorNames() {
        return [soapAction]
    }

    @Override
    ValidateMessageResponse validateMessage(String validationType, String msgHeader, byte[] messageBody) {
        if (soapAction == validationType) {
            String action = new SoapMessageParser(new String(messageBody)).parse().getSoapAction();

            def (transactionType, isRequest) = getTransactionType(action)
            if (!transactionType) throw new ToolkitRuntimeException("Unknown SOAPAction ${action}")

            // create Event and SimHandle from hdr/body
            def simIdName = 'validation'  // horrible approach
            def repoName = 'ValidationRepo'  // another horrible approach
            simHandle = SimUtils.create(transactionType, repoName, simIdName)

            TransactionRunner runner = new TransactionRunner(simHandle)
            if (isRequest)
                runner.validateRequest()
            else
                runner.validateResponse()

            ValidateMessageResponse response = new ValidateMessageResponse()
            response.setEventAssetId(new AssetId(simHandle.event.eventAsset.id.idString))
            response.setRepositoryId(new AssetId(RepoUtils.getRepository(repoName).id.idString))
            response.setValidationStatus((simHandle.event.hasErrors()) ? ValidationStatus.ERROR : ValidationStatus.OK)
            return response
        }
        throw new ToolkitRuntimeException("Do not understand validation type ${validationType}")
    }

    private getTransactionType(action) {
        TransactionType transactionType
        boolean isRequest = true
        transactionType = new ActorTransactionTypeFactory().getTransactionTypeFromRequestAction(action)
        if (!transactionType) {
            transactionType = new ActorTransactionTypeFactory().getTransactionTypeFromResponseAction(action)
            isRequest = false
        }
        return [transactionType, isRequest]
    }

    @Override
    ValidateMessageResponse validateMessage(String validatorName, AssetId repositoryId, AssetId eventId) {
        return null
    }

    @Override
    ValidateTransactionResponse validateTransaction(String validatorName, AssetId repositoryId, AssetId transactionAssetId) {
        return null
    }
}
