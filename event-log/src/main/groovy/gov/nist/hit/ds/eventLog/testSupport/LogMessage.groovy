package gov.nist.hit.ds.eventLog.testSupport;


import java.sql.Timestamp;

public interface LogMessage {
	public void setTimeStamp(Timestamp timestamp)
	public void setSecure(boolean isSecure)
	public void setTestMessage(String testMessage)
	public void setPass(boolean pass)
	public void setIP(String ip)
	public void setCompany(String companyName)
	public void addParam(String tableName, String name, String value)
	public void addHTTPParam(String name, String value)
	public void addSoapParam(String name, String value)
	public void addErrorParam(String name, String value)
	public void addOtherParam(String name, String value)
	public String getMessageID();
	public void writeMessage()
}
