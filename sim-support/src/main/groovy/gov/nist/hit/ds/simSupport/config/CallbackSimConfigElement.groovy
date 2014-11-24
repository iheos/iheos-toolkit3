package gov.nist.hit.ds.simSupport.config
/**
 * Created by bmajur on 9/22/14.
 */
public class CallbackSimConfigElement extends AbstractConfigElement {
    String restURL;

    def CallbackSimConfigElement(String restURL) {
        setName('none');
        this.restURL = restURL;
    }

    String toString() { "Callback: ${restURL}"}

}
