package gov.nist.hit.ds.dsSims.factories

import gov.nist.hit.ds.dsSims.topLevel.ValidatorManager
import gov.nist.hit.ds.tkapis.validation.MessageValidator

/**
 * Created by bmajur on 8/28/14.
 */
class MessageValidatorFactory {

    MessageValidator getMessageValidator() { return new ValidatorManager() }
}
