package gov.nist.hit.ds.xdstools3.client.customWidgets.endpoints.smartgwt.configure;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "endpointconfig")
public class EndpointConfig {

    /**
     * The constructor must have no arguments (requirement of RESTful service).
     */

    String name;
    String type;

    @XmlElement
    public String getName(){ return name; }

    public void setName(String _name){
        name = _name;
    }

    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String _type) { type = _type; }
}
