package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "endpoint")
public class Endpoint {

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
