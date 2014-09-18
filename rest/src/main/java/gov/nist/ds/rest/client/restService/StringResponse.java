package gov.nist.ds.rest.client.restService;

import gov.nist.ds.rest.client.util.RestResponse;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Diane Azais
 * @since 2014
 *
 * Utility class for representing a {@link //RestDataSource} request as an object.
 *
 * @see //http://www.smartclient.com/docs/7.0rc2/a/b/c/go.html#class..RestDataSource
 */
@XmlRootElement(name="response")
public class StringResponse extends RestResponse {
    @XmlElementWrapper(name="stringEntity")
    StringEntity stringEntity;


    @XmlElement
    public String getResponseString(){
        stringEntity = new StringEntity();
        return stringEntity.getString();
    }

    }
