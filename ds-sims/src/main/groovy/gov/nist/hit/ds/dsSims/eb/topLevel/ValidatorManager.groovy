package gov.nist.hit.ds.dsSims.eb.topLevel
import gov.nist.hit.ds.actorTransaction.ActorTransactionTypeFactory
import gov.nist.hit.ds.actorTransaction.TransactionType
import gov.nist.hit.ds.eventLog.Fault
import gov.nist.hit.ds.httpSoap.components.parsers.SoapMessageParser
import gov.nist.hit.ds.httpSoap.parsers.HttpSoapParser
import gov.nist.hit.ds.repoSupport.RepoUtils
import gov.nist.hit.ds.repository.shared.ValidationLevel
import gov.nist.hit.ds.repository.shared.id.AssetId
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.TransactionRunner
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.hit.ds.simSupport.utilities.SimUtils
import gov.nist.hit.ds.soapSupport.SoapFaultException
import gov.nist.hit.ds.tkapis.validation.MessageValidator
import gov.nist.hit.ds.tkapis.validation.ValidateMessageResponse
import gov.nist.hit.ds.tkapis.validation.ValidateTransactionResponse
import gov.nist.hit.ds.utilities.html.HttpMessageContent
import gov.nist.hit.ds.xdsExceptions.ExceptionUtil
import gov.nist.hit.ds.xdsExceptions.ToolkitRuntimeException
import groovy.util.logging.Log4j
import org.apache.log4j.Logger

/**
 * Created by bmajur on 8/28/14.
 */

class ValidatorManager implements MessageValidator {
    private static Logger log = Logger.getLogger(ValidatorManager);
    SimHandle simHandle
    String simId  = 'validation'
    String repositoryName  = 'ValidationRepo'

    static final String soapAction = 'by SOAP:Action'

    void setRepositoryName(String _repositoryName) { repositoryName = _repositoryName }
    void setSimId(String _simId) { simId = _simId }

    @Override
    List<String> getTransactionValidatorNames() {
        return [soapAction]
    }

    @Override
    List<String> getMessageValidatorNames() {
        return [soapAction]
    }

    @Override
    ValidateMessageResponse validateMessage(String validationType, String msgHeader, byte[] msgBody) {
        return validateMessage(validationType, msgHeader, msgBody, ValidationLevel.ERROR)
    }

    ValidateMessageResponse validateMessage(String validationType, String msgHeader, byte[] msgBody, ValidationLevel validationLevel) {
        def exceptionMessages = []
        if (soapAction == validationType) {
            def httpMessage
            def hsParser
            byte[] soapEnv
            String action = null
            TransactionType transactionType
            def isRequest

            try {
                httpMessage = new HttpMessageContent(msgHeader, msgBody)
                hsParser = new HttpSoapParser(httpMessage)
                soapEnv = hsParser.getSoapEnvelope()
                action = new SoapMessageParser(new String(soapEnv)).parse().getSoapAction();
            } catch (Exception e) {
                exceptionMessages << ExceptionUtil.exception_details(e)
            }

            if (!action) exceptionMessages << "No WS:Action found in message."
            else
                (transactionType, isRequest) = getTransactionType(action)

            if (!transactionType) {
                exceptionMessages << "Unknown SOAPAction ${action}"
                exceptionMessages << "SOAPActions are configured in actorTransactions.xml."
                exceptionMessages << "Known actions are: " + ActorTransactionTypeFactory.knownRequestActions
                simHandle = SimUtils.open(simId, repositoryName)
            } else {
                simHandle = SimUtils.create(transactionType, repositoryName, simId)
            }

            log.info("exceptionMessage are ${exceptionMessages}")

            assert simHandle.event

            simHandle.event.validationLevel = validationLevel
            simHandle.event.inOut.reqHdr = msgHeader
            simHandle.event.inOut.reqBody = msgBody

            // TODO: need something more specific than Fault for this kind of problem
            // TODO: also, multiple Fault assets generated - huh?
            // This style of fault creation does not trigger error labeling in log browser
            if (!transactionType)
                simHandle.event.fault = new Fault("Validation failed", '','unknown', exceptionMessages.toString())

            ValidateMessageResponse response = new ValidateMessageResponse()

            if (transactionType) {
                TransactionRunner runner = new TransactionRunner(simHandle)
                    if (isRequest)
                        runner.validateRequest()
                    else
                        runner.validateResponse()

                response.setEventAssetId(new AssetId(simHandle.event.eventAsset.id.idString))
                response.setRepositoryId(new AssetId(RepoUtils.getRepository(repositoryName).id.idString))
                response.setValidationStatus((simHandle.event.hasErrors()) ? ValidationStatus.ERROR : ValidationStatus.OK)
            } else {
                response.setEventAssetId(new AssetId(simHandle.event.eventAsset.id.idString))
                response.setRepositoryId(new AssetId(RepoUtils.getRepository(repositoryName).id.idString))
                response.setValidationStatus(ValidationStatus.ERROR)
            }

            SimUtils.close(simHandle)
            return response
        }
        throw new ToolkitRuntimeException("Do not understand validation type ${validationType}")
    }

    private getTransactionType(action) {
        if (!action) return [null, null]
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
