package gov.nist.hit.ds.simSupport.transaction;

/**
 * Created by bmajur on 8/28/14.
 */
public interface Transaction {
    ValidationStatus validateRequest();
    ValidationStatus validateResponse();
    ValidationStatus run();
}
