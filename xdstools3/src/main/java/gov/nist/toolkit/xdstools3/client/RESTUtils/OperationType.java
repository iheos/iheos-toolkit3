package gov.nist.toolkit.xdstools3.client.RESTUtils;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Simple enumeration representing the different {@link //RestDataSource} scripts.
 */
@XmlType
@XmlEnum(String.class)
public enum OperationType {
	@XmlEnumValue("add") ADD, 
	@XmlEnumValue("fetch") FETCH, 
	@XmlEnumValue("update") UPDATE, 
	@XmlEnumValue("remove") REMOVE
}