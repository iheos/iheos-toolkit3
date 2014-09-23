package gov.nist.hit.ds.simSupport.client.configElementTypes;

/**
 * Created by bmajur on 9/22/14.
 */
public class CallbackActorSimConfigElement extends AbstractActorSimConfigElement {
    String transactionId;
    String restURL;

    public CallbackActorSimConfigElement(String transactionId, String restURL) {
        this.transactionId = transactionId;
        this.restURL = restURL;
    }

    public String getTransactionId() { return transactionId; }
    public String getRestURL() { return restURL; }
}
