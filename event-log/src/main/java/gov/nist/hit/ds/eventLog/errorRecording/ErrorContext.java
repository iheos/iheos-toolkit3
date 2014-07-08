package gov.nist.hit.ds.eventLog.errorRecording;

public class ErrorContext {
	String msg;
	String resource;
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public ErrorContext(String msg, String resource) {
		this.msg = msg;
		this.resource = resource;
	}
	
	public ErrorContext(String context) {
		if (context == null) {
			msg = null;
			resource = null;
			return;
		}
		String[] parts = context.split("\\[", 2);
		if (parts == null || parts.length == 0) {
			msg = null;
			resource = null;
			return;
		}
		if (parts.length == 1) {
			msg = parts[0];
			resource = null;
			return;
		}
		msg = parts[0].trim();
		
		resource = parts[1].trim();
		if (resource.charAt(resource.length()-1) == ']') {
			resource = resource.substring(0, resource.length()-1);
			resource = resource.trim();
		}
		
	}
	
	public String toString() {
		if (resource == null || resource.equals(""))
			return msg;
		if (msg == null)
			msg = "";
		return msg + " [" + resource + "]";
	}
}
