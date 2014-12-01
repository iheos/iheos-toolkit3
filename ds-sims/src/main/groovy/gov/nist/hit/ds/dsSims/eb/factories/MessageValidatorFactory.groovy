package gov.nist.hit.ds.dsSims.eb.factories

import gov.nist.hit.ds.dsSims.eb.topLevel.ValidatorManager
import gov.nist.hit.ds.tkapis.validation.MessageValidator

/**
 * Created by bmajur on 8/28/14.
 */
class MessageValidatorFactory {

    MessageValidator getMessageValidator() { return new ValidatorManager() }
}
