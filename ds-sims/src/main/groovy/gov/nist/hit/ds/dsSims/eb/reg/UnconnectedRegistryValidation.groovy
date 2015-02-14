package gov.nist.hit.ds.dsSims.eb.reg

import gov.nist.hit.ds.dsSims.eb.metadataValidator.validator.RegistryValidationInterface

/**
 * Created by bmajur on 2/12/15.
 */
class UnconnectedRegistryValidation implements RegistryValidationInterface {
    @Override
    boolean isConnected() {
        return false
    }

    @Override
    boolean isDocumentEntry(String uuid) {
        return true
    }

    @Override
    boolean isFolder(String uuid) {
        return true
    }

    @Override
    boolean isSubmissionSet(String uuid) {
        return true
    }
}
