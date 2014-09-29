package gov.nist.ds.rest.client.restService;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Diane Azais
 * @since 2014
 * This is the XML entity to be passed over REST. It must be annotated with XmlRootElement.
 */
@XmlRootElement
public class StringEntity implements Serializable{
    private String myEntity = "This is the entity being passed over REST";


    public StringEntity(){
    }


    /**
     * This looks like it is not being used but is actually a necessary element. It tells Jersey to convert and send the annotated
     * element.
     */
    @XmlElement
    public String getString(){
        return myEntity;
    }

}
