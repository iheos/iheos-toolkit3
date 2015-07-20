package gov.nist.hit.ds.v2.validators.transactions

import gov.nist.hit.ds.dsSims.eb.client.ValidationContext
import gov.nist.hit.ds.simSupport.simulator.SimHandle
import gov.nist.hit.ds.simSupport.transaction.Transaction
import gov.nist.hit.ds.simSupport.transaction.ValidationStatus
import gov.nist.toolkit.valregmsg.message.HttpMessageValidator
import gov.nist.toolkit.valsupport.engine.MessageValidatorEngine
import gov.nist.toolkit.valsupport.registry.RegistryValidationInterface

/**
 * Created by bill on 6/29/15.
 */
class Pnr implements Transaction {
    SimHandle simHandle

    def Pnr(SimHandle _simHamdle) { simHandle = _simHamdle}

    @Override
    ValidationStatus validateRequest() {
        // From RepositoryActorSimulator
        ValidationContext vc = new ValidationContext()
        vc.isPnR = true
        vc.isRequest = true
        vc.isDIRECT = simHandle.transactionType.getTransactionProperty('direct') == 'true'


        // from ValidateMessageService#runValidation()
        MessageValidatorEngine mvc = new MessageValidatorEngine();
        RegistryValidationInterface rvi = null
        String httpMsgHdr = simHandle.event.inOutMessages.reqHdr
        byte[] httpMsgBody = simHandle.event.inOutMessages.reqBody

        HttpMessageValidator val = new HttpMessageValidator(vc, httpMsgHdr, httpMsgBody, gerb, mvc, rvi);
        mvc.addMessageValidator("Parse HTTP Message", val, gerb.buildNewErrorRecorder());
        mvc.run();





        common.vc.isPnR = true;
        common.vc.xds_b = true;
        common.vc.isRequest = true;
        common.vc.hasHttp = true;
        common.vc.hasSoap = true;

        if (!common.runInitialValidations())
            return false;

        return null
    }

    @Override
    ValidationStatus validateResponse() {
        return null
    }

    @Override
    ValidationStatus acceptRequest() {
        return null
    }

    @Override
    ValidationStatus sendRequest() {
        return null
    }
}
