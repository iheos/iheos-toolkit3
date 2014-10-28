package gov.nist.hit.ds.simSupport.client.configElementTypes;

/**
 * Created by bmajur on 9/22/14.
 */
public class CallbackSimConfigElement extends SimConfigElement {
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
