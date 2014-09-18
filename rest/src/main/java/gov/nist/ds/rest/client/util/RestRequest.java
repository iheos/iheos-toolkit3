package gov.nist.ds.rest.client.util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by dazais on 9/11/2014.
 */
@XmlType
public abstract class RestRequest {
    private OperationType operationType;


    @XmlElement(name = "operationType")
    public OperationType getOperationType() {
        return operationType;
    }
}
