package gov.nist.toolkit.xdstools3.client.customWidgets.healthcareCodes;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Describes a Healthcare Code element, linked to a Datasource and a Widget through a RESTful interface.
 */
@XmlRootElement(name = "healthcodes")
public class HealthcareCode {
        String description;
        String code;

    /**
     * The constructor must have no arguments (requirement of RESTful service).
     */

        @XmlElement
        public String getDescription(){ return description; }

        public void setDescription(String _description){
            description = _description;
        }

        @XmlAttribute
        public String getCode() {
            return code;
        }

        public void setCode(String _code) { code = _code; }

}
