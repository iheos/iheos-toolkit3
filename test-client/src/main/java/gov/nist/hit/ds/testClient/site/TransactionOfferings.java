package gov.nist.hit.ds.testClient.site;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is as close as toolkit comes to managing Actor configurations. ATFactory
 * defines the linkage between Transactions and Actors, this holds the 
 * transaction configurations (actual endpoints).
 * @author bill
 *
 */
public class TransactionOfferings  {
	// regular and tls
	public Map<ATFactory.TransactionType, List<Site>> map = new HashMap<ATFactory.TransactionType, List<Site>>();
	public Map<ATFactory.TransactionType, List<gov.nist.hit.ds.testClient.site.Site>> tmap = new HashMap<ATFactory.TransactionType, List<gov.nist.hit.ds.testClient.site.Site>>();
	
	public boolean hasTransaction(ATFactory.TransactionType tt, boolean isTLS) {
		if (isTLS) {
			return tmap.containsKey(tt);
		} else {
			return map.containsKey(tt);
		}
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("TransactionOfferings:\n");
		buf.append("Non-TLS\n");
		for (ATFactory.TransactionType t : map.keySet()) {
			buf.append("\t").append(t).append("\n");
		}
		buf.append("TLS\n");
		for (ATFactory.TransactionType t : tmap.keySet()) {
			buf.append("\t").append(t).append("\n");
		}
		
		return buf.toString();
	}
	
	public TransactionOfferings() {} // For GWT

}
