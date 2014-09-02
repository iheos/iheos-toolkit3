package gov.nist.hit.ds.dsSims.topLevel
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.dsSims.Transaction
import gov.nist.hit.ds.eventLog.Event
import gov.nist.hit.ds.httpSoapValidator.components.parsers.SoapMessageParser
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.tkapis.AssetId
import gov.nist.hit.ds.tkapis.validation.MessageValidator
import gov.nist.hit.ds.tkapis.validation.ValidateMessageResponse
import gov.nist.hit.ds.tkapis.validation.ValidateTransactionResponse
import gov.nist.hit.ds.xdsException.ToolkitRuntimeException

/**
 * Created by bmajur on 8/28/14.
 */
class ValidatorManager implements MessageValidator {

    final String soapAction = 'by SOAP:Action'

    @Override
    List<String> getTransactionValidatorNames() {
        return [soapAction]
    }

    @Override
    List<String> getMessageValidatorNames() {
        return [soapAction]
    }

    @Override
    ValidateMessageResponse validateMessage(String validatorName, String msgHeader, byte[] messageBody) {
        if (soapAction == validatorName) {
            String action = new SoapMessageParser(new String(messageBody)).parse().getSoapAction();

            def (transactionType, isRequest) = getTransactionType(action)
            if (!transactionType) throw new ToolkitRuntimeException("Unknown SOAPAction ${action}")

            // create Event and SimHandle from hdr/body
            Event e = null
            SimHandle simHandle

            // redo this to use TransactionRunner
            def aClass = this.getClass().classLoader.loadClass(transactionType.implementationClassName, true)
            Transaction transaction = aClass.newInstance()
            if (isRequest)
                transaction.validateRequest(simHandle)
            else
                transaction.validateResponse(simHandle)

        }
        return null
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
