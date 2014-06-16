package gov.nist.toolkit.xdstools3.client.customWidgets.endpoints;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "endpoint")
public class Endpoint {

    String value;
    int id;

    @XmlElement
    public String getValue(){
        return value;
    }

    public void setValue(String _value){
        value = _value;
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int _id) {
        id = _id;
    }
}
