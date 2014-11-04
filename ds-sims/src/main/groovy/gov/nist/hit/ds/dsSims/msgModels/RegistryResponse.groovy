package gov.nist.hit.ds.dsSims.msgModels

import gov.nist.hit.ds.actorTransaction.TransactionType

/**
 * Created by bmajur on 10/7/14.
 */
class RegistryResponse {
    def registryErrorList = []
    TransactionType transactionType

    boolean hasErrors() { registryErrorList.find { !it.isWarning }}
    boolean hasWarnings() { registryErrorList.find { it.isWarning }}

}
