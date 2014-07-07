package gov.nist.toolkit.xdstools3.client.RESTUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public abstract class DSResponse {
    public static int STATUS_FAILURE = -1;
    public static int STATUS_LOGIN_INCORRECT = -5;
    public static int STATUS_LOGIN_REQUIRED = -7;
    public static int STATUS_LOGIN_SUCCESS = -8;
    public static int STATUS_MAX_LOGIN_ATTEMPTS_EXCEEDED = -6;
    public static int STATUS_SERVER_TIMEOUT = -100;
    public static int STATUS_SUCCESS = 0;
    public static int STATUS_TRANSPORT_ERROR = -90;
    public static int STATUS_VALIDATION_ERROR = -4;

	private int status;
	private int startRow;
	private int endRow;
	private int totalRows;

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
	
	public void setStatus(int status) {
		this.status = status;
	}

    @XmlElement(name = "status")
	public int getStatus() {
		return status;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

    @XmlElement(name = "totalRows")
	public int getTotalRows() {
		return totalRows;
	}
}
