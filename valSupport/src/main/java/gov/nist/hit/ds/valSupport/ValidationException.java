package gov.nist.hit.ds.valSupport;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {
	List<String> msgs = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException(String msg) {
		super(msg);
		msgs = new ArrayList<String>();
		msgs.add(msg);
	}

	public ValidationException(List<String> msgs) {
		super(join(msgs));
		this.msgs = msgs;
	}
	
	public ValidationException(String msg, Throwable t) {
		super(msg, t);
		msgs = new ArrayList<String>();
	}
	
	public List<String> getMessages() {
		return msgs;
	}
	
	private static String join(List<String> strings) {
		StringBuffer buf = new StringBuffer();
		
		for (String s : strings) {
			if (buf.length() != 0)
				buf.append("\n");
			buf.append(s);
		}
		return buf.toString();
	}
}
