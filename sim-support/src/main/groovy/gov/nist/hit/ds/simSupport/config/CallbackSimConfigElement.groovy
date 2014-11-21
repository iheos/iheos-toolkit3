package gov.nist.hit.ds.simSupport.config;

/**
 * Created by bmajur on 9/22/14.
 */
public class CallbackSimConfigElement extends AbstractSimConfigElement {
    String transactionId;
    String restURL;

    public CallbackSimConfigElement(String name, String transactionId, String restURL) {
        setName(name);
        this.transactionId = transactionId;
        this.restURL = restURL;
    }

    public String getTransactionId() { return transactionId; }
    public String getRestURL() { return restURL; }
}
