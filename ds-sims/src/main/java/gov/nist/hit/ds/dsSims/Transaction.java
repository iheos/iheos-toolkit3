package gov.nist.hit.ds.dsSims;

import gov.nist.hit.ds.tkapis.validation.ValidationStatus;

/**
 * Created by bmajur on 8/28/14.
 */
public interface Transaction {
    ValidationStatus validateRequest();
    ValidationStatus validateResponse();
    ValidationStatus run();
}
