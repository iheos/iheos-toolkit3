package gov.nist.toolkit.xdstools3.client.RESTUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Utility class for representing a {@linkRestDataSource} request as an object.
 *
 * @see //http://www.smartclient.com/docs/7.0rc2/a/b/c/go.html#class..RestDataSource
 */
@XmlType
public abstract class DSRequest {

	private String dataSource;
	private OperationType operationType;
	private int startRow;
	private int endRow;
	private String componentId;

    @XmlElement(name = "operationType")
	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

    @XmlElement(name = "startRow")
	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

    @XmlElement(name = "endRow")
	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

    @XmlElement(name = "componentId")
    public String getComponentId() {
		return componentId;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

    @XmlElement(name = "dataSource")
	public String getDataSource() {
		return dataSource;
	}
}
